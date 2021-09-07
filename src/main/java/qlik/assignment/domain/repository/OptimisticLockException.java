package qlik.assignment.domain.repository;

import qlik.assignment.domain.DomainException;

/**
 * Exception thrown when a message attempted to be saved has been modified by another request/session
 *
 */
public class OptimisticLockException extends DomainException {
	private static final long serialVersionUID = 1L;
	private Integer msgId;

	public OptimisticLockException(Integer id) {
		super();
		this.msgId = id;
	}

	@Override
	public String getName() {
		return "Message Modified";
	}

	@Override
	public String getFriendlyMsg() {
		return  String.format("Message %d has been modified by another user", msgId);
	}

	@Override
	public int getErrorCode() {
		return 100;
	}
}