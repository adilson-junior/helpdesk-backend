package br.com.facilitysoft.helpdesk.util.exception;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//Classe de controler de exceções
@ControllerAdvice
public class ResourceExceptionHandler {
	
	//Exceção para controlar erros com retornos null.
	@ExceptionHandler(ObjectNotFoundException.class)
	public ResponseEntity<StandardError> objectNotFoundException(ObjectNotFoundException ex,
			HttpServletRequest request) {
		StandardError error = new StandardError(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(),
				"Objeto não encontrado", ex.getMessage(), request.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	
	//Exceção para controlar erros de integridades dos dados, não permitindo duplicidades.
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<StandardError> dataIntegrityViolationException(DataIntegrityViolationException ex,
			HttpServletRequest request) {
		StandardError error = new StandardError(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(),
				"Violação de dados", ex.getMessage(), request.getRequestURI());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
	
	//Exceção para controlar erros de integridades dos dados, não permitindo duplicidades.
		@ExceptionHandler(MethodArgumentNotValidException.class)
		public ResponseEntity<StandardError> validationErrors(MethodArgumentNotValidException ex,
				HttpServletRequest request) {
			ValidationError errors = new ValidationError(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
					"Validação de erros", "Erro na validação dos dados", request.getRequestURI());
			for(FieldError x : ex.getBindingResult().getFieldErrors()) {
				errors.addError(x.getField(), x.getDefaultMessage());
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
		}


}
