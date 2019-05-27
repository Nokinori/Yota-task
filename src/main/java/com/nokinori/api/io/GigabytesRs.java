package com.nokinori.api.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


/**
 * Interface object to be used in responses.
 * {@link NoArgsConstructor}
 * {@link AllArgsConstructor}
 * is used for mapping by {@link com.nokinori.mappers.GenericMapper}.
 *
 * @see com.nokinori.mappers.GenericMapper
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GigabytesRs {

    private Integer amount;

    private LocalDateTime expiresAt;
}
