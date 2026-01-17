package com.example.mvrkblog.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CouldNotResolveTokenException extends RuntimeException {
    public CouldNotResolveTokenException(String message) {
        super(message);
    }
}
