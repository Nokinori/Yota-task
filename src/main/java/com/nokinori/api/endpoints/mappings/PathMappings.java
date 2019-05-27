package com.nokinori.api.endpoints.mappings;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Class holder for all mapping paths.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PathMappings {

    /**
     * Sim-card path.
     */
    public static final String SIM_CARD_PATH = "/sim-cards";

    /**
     * Id param in path.
     */
    public static final String ID_PARAM = "/{id}";

    /**
     * Sim-card path with id param.
     */
    public static final String SIM_CARD_PATH_WITH_ID_PARAM = SIM_CARD_PATH + ID_PARAM;

    /**
     * Activate path.
     */
    public static final String SIM_CARD_ACTIVATE_PATH = "/activate";

    /**
     * Block path.
     */
    public static final String SIM_CARD_BLOCK_PATH = "/block";

    /**
     * Minutes path.
     */
    public static final String MINUTES_PATH = "/minutes";

    /**
     * Gigabytes path.
     */
    public static final String GIGABYTES_PATH = "/gigabytes";

    /**
     * Context path.
     */
    public static final String CONTEXT_PATH = "/services/billing";
}
