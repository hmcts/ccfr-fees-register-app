package uk.gov.hmcts.register.fees.service;

public class ClaimCategoriesNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public ClaimCategoriesNotFoundException() {
	}

	public ClaimCategoriesNotFoundException(String message) {
		super(message);
	}

	public ClaimCategoriesNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ClaimCategoriesNotFoundException(Throwable cause) {
		super(cause);
	}

}
