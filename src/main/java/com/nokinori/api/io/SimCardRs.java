package com.nokinori.api.io;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@Data
@Builder
public class SimCardRs {

    private Long simCardId;

    @JsonInclude(Include.NON_NULL)
    private List<MinutesRs> minutesPacks;

    @JsonInclude(Include.NON_NULL)
    private List<GigabytesRs> gigabytesPacks;
}
