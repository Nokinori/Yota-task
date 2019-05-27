package com.nokinori.api.io;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

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
public class SimCardRs {

    private Long simCardId;

    @JsonInclude(Include.NON_NULL)
    private List<MinutesRs> minutesPacks;

    @JsonInclude(Include.NON_NULL)
    private List<GigabytesRs> gigabytesPacks;
}
