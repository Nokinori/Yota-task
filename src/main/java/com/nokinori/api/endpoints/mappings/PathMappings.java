package com.nokinori.api.endpoints.mappings;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PathMappings {

    public static final String SIM_CARD_PATH = "/sim-cards";

    public static final String ID_PARAM = "/{id}";

    public static final String SIM_CARD_PATH_WITH_ID_PARAM = SIM_CARD_PATH + ID_PARAM;

    public static final String SIM_CARD_ACTIVATE_PATH = "/activate";

    public static final String SIM_CARD_BLOCK_PATH = "/block";

    public static final String MINUTES_PATH = "/minutes";

    public static final String GIGABYTES_PATH = "/gigabytes";

    public static final String CONTEXT_PATH = "/services/billing";
}
