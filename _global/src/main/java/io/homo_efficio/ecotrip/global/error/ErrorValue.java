package io.homo_efficio.ecotrip.global.error;

import lombok.Getter;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-03-30
 */
@Getter
public enum ErrorValue {

    INVALID_PARAMETER(400, "COMMON_001"),
    ENTITY_NOT_FOUND(404, "COMMON_002"),
    ;

    private int statusCode;
    private String errorCode;

    ErrorValue(int statusCode, String errorCode) {
        this.statusCode = statusCode;
        this.errorCode = errorCode;
    }
}
