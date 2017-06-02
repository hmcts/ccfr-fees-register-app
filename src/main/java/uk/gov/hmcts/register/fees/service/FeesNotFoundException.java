package uk.gov.hmcts.register.fees.service;

public class FeesNotFoundException extends RuntimeException {
	

	private static final long serialVersionUID = 1L;

	public FeesNotFoundException() {
	}

	public FeesNotFoundException(String message) {
		super(message);
	}

	public FeesNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public FeesNotFoundException(Throwable cause) {
		super(cause);
	}

}
