package qlik.assignment.domain.factory;

import qlik.assignment.domain.DomainException;

public class MaxMessageIdReachedException extends DomainException {
	private static final long serialVersionUID = 1L;

	@Override
	public String getName() {
		return "Max Messages Reached";
	}

	@Override
	public String getFriendlyMsg() {
		return "Maximum number of messsages has been reached";
	}

	@Override
	public int getErrorCode() {
		return 102;
	}
}