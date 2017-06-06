package uk.gov.hmcts.fees.register.service;

public class EntityNotFoundException extends RuntimeException {
	

	private static final long serialVersionUID = 1L;

	public EntityNotFoundException() {
	}

	public EntityNotFoundException(String message) {
		super(message);
	}

	public EntityNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public EntityNotFoundException(Throwable cause) {
		super(cause);
	}

}
