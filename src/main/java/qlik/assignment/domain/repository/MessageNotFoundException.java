package qlik.assignment.domain.repository;

import qlik.assignment.domain.DomainException;

public class MessageNotFoundException extends DomainException {
	private static final long serialVersionUID = 1L;
	
	private Integer msgId;
	
	public MessageNotFoundException(Integer id) {
		super();
		this.msgId = id;
	}

	@Override
	public String getName() {
		return "Message Not Found";
	}

	@Override
	public String getFriendlyMsg() {
		return String.format("Message %d does not exist", msgId);
	}

	@Override
	public int getErrorCode() {
		return 101;
	}
}
