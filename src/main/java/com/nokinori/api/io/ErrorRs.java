package com.nokinori.api.io;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorRs {

    private Integer errorCode;

    private String errorText;

    private LocalDateTime timeStamp;
}
