package qlik.assignment.application.web;

import org.springframework.http.HttpStatus;

import qlik.assignment.domain.DomainException;

/**
 * Response object returned from all web request that result in an error <br>
 * Provides a consistent error response structure.
 */
public class ErrorResponse {

	private String docs = "http://todo-provide-url.com/";;
	private String message;
	private int errorCode;
	private String name;
	private HttpStatus httpStatusCode;

	public ErrorResponse(DomainException ex, HttpStatus httpStatusCode) {
		this.message = ex.getFriendlyMsg();
		this.errorCode = ex.getErrorCode();
		this.name = ex.getName();
		this.httpStatusCode = httpStatusCode;
	}

	public ErrorResponse(Exception ex, HttpStatus httpStatusCode) {
		this.message = ex.getMessage(); // TODO: Address leaking sensitive information and producing a safe friendlier
										// message
		this.name = ex.getClass().getSimpleName();
		this.httpStatusCode = httpStatusCode;
	}

	public String getDocs() {
		return docs;
	}

	public String getMessage() {
		return message;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public String getName() {
		return name;
	}

	public HttpStatus getHttpStatusCode() {
		return httpStatusCode;
	}
}
