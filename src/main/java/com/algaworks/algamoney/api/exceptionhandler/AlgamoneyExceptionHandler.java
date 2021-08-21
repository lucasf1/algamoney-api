package com.algaworks.algamoney.api.exceptionhandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Getter;

@ControllerAdvice
@AllArgsConstructor
public class AlgamoneyExceptionHandler extends ResponseEntityExceptionHandler{

	private MessageSource messageSource;
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		String mensagemUser = messageSource.getMessage("mensagem.invalida", null, LocaleContextHolder.getLocale());
		String mensagemDesenv = ex.getCause().toString();
		
		List<Erro> erros = Arrays.asList(new Erro(mensagemUser, mensagemDesenv));
		
		return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		List<Erro> erros = criarListaErros(ex.getBindingResult());
		
		return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	private List<Erro> criarListaErros(BindingResult bindingResult){
		
		List<Erro> erros = new ArrayList<>();
		
		for (FieldError fieldError: bindingResult.getFieldErrors()) {
			
			String mensagemUser = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
			String mensagemDesenv = fieldError.toString();
			erros.add(new Erro(mensagemUser, mensagemDesenv));
		}
		
		return erros;
	}
	
	@AllArgsConstructor
	@Getter
	public static class Erro {
		
		private String mensagemUser;
		private String mensagemDesenv;
	}
}