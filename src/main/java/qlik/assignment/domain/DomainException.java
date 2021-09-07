package qlik.assignment.domain;

/**
 * Base exception for any domain exceptions
 */
public abstract class DomainException extends Exception{
	private static final long serialVersionUID = 1L;
	
	public abstract String getName();
	public abstract String getFriendlyMsg();
	public abstract int getErrorCode();
	
	public DomainException() {
		super();
	}
}
