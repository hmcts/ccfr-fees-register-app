package uk.gov.hmcts.fees2.register.data.model;
public enum IdamUser {

    USER_NOT_FOUND("The user's details are unavailable");


    private final String message; // String value associated with each enum

    // Constructor
    IdamUser(String message) {
        this.message = message;
    }

    // Getter method to retrieve the string value
    public String getMessage() {
        return message;
    }

}
