package spring.security.init.global.result.error;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum ErrorCode {
    // sign in error
    NOT_FOUND_USER(400, "S001", "This account does not exist."),
    NOT_MATCH_PASSWORD(400, "S002", "Passwords do not match."),
    ;


    private int status;
    private String code;
    private String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

}
