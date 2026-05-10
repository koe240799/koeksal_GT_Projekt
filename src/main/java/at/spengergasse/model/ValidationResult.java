package at.spengergasse.model;

public class ValidationResult<T> {

    private final T data;
    private final boolean valid;
    private final String message;

    public ValidationResult(T data, boolean valid, String message) {
        this.data = data;
        this.valid = valid;
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public boolean isValid() {
        return valid;
    }

    public String getMessage() {
        return message;
    }
}