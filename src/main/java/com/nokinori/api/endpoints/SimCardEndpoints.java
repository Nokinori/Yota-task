package com.nokinori.api.endpoints;

import com.nokinori.services.api.SimCardService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;

import static com.nokinori.api.endpoints.mappings.PathMappings.SIM_CARD_ACTIVATE_PATH;
import static com.nokinori.api.endpoints.mappings.PathMappings.SIM_CARD_BLOCK_PATH;
import static com.nokinori.api.endpoints.mappings.PathMappings.SIM_CARD_PATH_WITH_ID_PARAM;

/**
 * Rest controller that process all requests to {@link SIM_CARD_PATH_WITH_ID_PARAM}.
 *
 * @see com.nokinori.api.endpoints.mappings.PathMappings
 */
@RestController
@RequestMapping(SIM_CARD_PATH_WITH_ID_PARAM)
@Validated
public class SimCardEndpoints {

    /**
     * Service with logic for sim-card processing.
     */
    private final SimCardService simCardService;

    public SimCardEndpoints(SimCardService simCardService) {
        this.simCardService = simCardService;
    }

    /**
     * Endpoint for activation sim-card.
     * Includes validation of path variable id.
     *
     * @param id of sim-card.
     */
    @PutMapping(SIM_CARD_ACTIVATE_PATH)
    @ResponseStatus(HttpStatus.OK)
    public void activateSimCard(@PathVariable @Positive Long id) {
        simCardService.activate(id);
    }

    /**
     * Endpoint for block sim-card.
     * Includes validation of path variable id.
     *
     * @param id of sim-card.
     */
    @PutMapping(SIM_CARD_BLOCK_PATH)
    @ResponseStatus(HttpStatus.OK)
    public void blockSimCard(@PathVariable @Positive Long id) {
        simCardService.block(id);
    }
}
