package qlik.assignment.application.web;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import qlik.assignment.domain.factory.MaxMessageIdReachedException;
import qlik.assignment.domain.repository.OptimisticLockException;
import qlik.assignment.domain.repository.MessageNotFoundException;

/**
 * Transforms exceptions thrown by controllers and Spring into
 * {@linkplain ErrorResponse}s providing a consistent error response structure
 * 
 */
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(MessageNotFoundException.class)
	public final ResponseEntity<ErrorResponse> handleMessageNotFound(MessageNotFoundException ex) {
		final ErrorResponse response = new ErrorResponse(ex, HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(response, response.getHttpStatusCode());
	}

	@ExceptionHandler(OptimisticLockException.class)
	public final ResponseEntity<ErrorResponse> handleDirtyUpdate(OptimisticLockException ex) {
		final ErrorResponse response = new ErrorResponse(ex, HttpStatus.CONFLICT);
		return new ResponseEntity<>(response, response.getHttpStatusCode());
	}

	@ExceptionHandler({ Exception.class, MaxMessageIdReachedException.class })
	public final ResponseEntity<ErrorResponse> handleUnknownError(Exception ex) {
		final ErrorResponse response = new ErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(response, response.getHttpStatusCode());
	}

	// This allows some spring managed exceptions such as input validation to be
	// wrapped in ErrorResponse
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception exception, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
		final ErrorResponse errorResponse = new ErrorResponse(exception, status);
		return super.handleExceptionInternal(exception, errorResponse, headers, status, request);
	}
}
