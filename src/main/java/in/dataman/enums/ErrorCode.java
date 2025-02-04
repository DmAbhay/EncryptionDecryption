package in.dataman.enums;

public enum ErrorCode {
    MISSING_FIELDS(400, "Missing required fields"),
    INVALID_INPUT(422, "Invalid input data"),
    INTERNAL_SERVER_ERROR(500, "Internal server error");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
