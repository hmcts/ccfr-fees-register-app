package uk.gov.hmcts.fees.register.repository;

public class FeesRegisterNotLoadedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public FeesRegisterNotLoadedException() {
	}

	public FeesRegisterNotLoadedException(String message) {
		super(message);
	}

	public FeesRegisterNotLoadedException(String message, Throwable cause) {
		super(message, cause);
	}

	public FeesRegisterNotLoadedException(Throwable cause) {
		super(cause);
	}

}
