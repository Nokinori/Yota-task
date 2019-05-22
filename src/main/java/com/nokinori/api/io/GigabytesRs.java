package com.nokinori.api.io;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GigabytesRs {

    private Long userId;

    private Long amount;

    private LocalDateTime expiresAt;
}
