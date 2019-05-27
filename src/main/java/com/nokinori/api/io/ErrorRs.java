package com.nokinori.api.io;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Interface object to be used in responses.
 */
@Data
@Builder
public class ErrorRs {

    private Integer errorCode;

    private String errorText;

    private LocalDateTime timeStamp;
}
