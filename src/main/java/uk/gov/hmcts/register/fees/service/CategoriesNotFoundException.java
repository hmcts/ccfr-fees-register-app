package uk.gov.hmcts.register.fees.service;

public class CategoriesNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public CategoriesNotFoundException() {
	}

	public CategoriesNotFoundException(String message) {
		super(message);
	}

	public CategoriesNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public CategoriesNotFoundException(Throwable cause) {
		super(cause);
	}

}
