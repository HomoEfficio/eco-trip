package io.homo_efficio.ecotrip.global.error;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;

import java.util.List;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-03-30
 */
@Getter
@NoArgsConstructor
public class ErrorResponse {

    private int statusCode;
    private String errorCode;
    private String message;
    private List<FieldErr> errors;


    public ErrorResponse(ErrorValue errorValue, String message, List<FieldErr> errors) {
        this.statusCode = errorValue.getStatusCode();
        this.errorCode = errorValue.getErrorCode();
        this.errors = errors;
        this.message = message;
    }

    public static ErrorResponse of(ErrorValue errorValue, String message, BindingResult bindingResult) {
        return new ErrorResponse(errorValue, message, FieldErr.from(bindingResult));
    }
}
