package com.nokinori.api.endpoints;

import com.nokinori.api.io.SimCardRs;
import com.nokinori.services.api.BillingService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;

import static com.nokinori.api.endpoints.mappings.PathMappings.GIGABYTES_PATH;
import static com.nokinori.api.endpoints.mappings.PathMappings.SIM_CARD_PATH_WITH_ID_PARAM;

/**
 * Rest controller that process all requests to {@link SIM_CARD_PATH_WITH_ID_PARAM} + {@link GIGABYTES_PATH}.
 *
 * @see com.nokinori.api.endpoints.mappings.PathMappings
 */
@RestController
@RequestMapping(SIM_CARD_PATH_WITH_ID_PARAM + GIGABYTES_PATH)
@Validated
public class GigabytesEndpoints {

    /**
     * Service with logic for minutes connected with sim-card processing.
     */
    private final BillingService<SimCardRs> billingService;

    public GigabytesEndpoints(@Qualifier("gigabytesService") BillingService<SimCardRs> billingService) {
        this.billingService = billingService;
    }

    /**
     * Endpoint for getting gigabytes for sim-card.
     * Includes validation of path variable id.
     *
     * @param id of sim-card.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimCardRs> getGigabytes(@PathVariable @Positive Long id) {
        SimCardRs rs = billingService.get(id);
        return ResponseEntity.ok(rs);
    }

    /**
     * Endpoint for adding gigabytes for sim-card.
     * Includes validation of path variable id.
     *
     * @param id of sim-card.
     */
    @PostMapping(params = "amount")
    @ResponseStatus(HttpStatus.CREATED)
    public void addGigabytes(@PathVariable @Positive Long id, @RequestParam @Positive Integer amount) {
        billingService.add(id, amount);
    }

    /**
     * Endpoint for subtracting minutes for sim-card.
     * Includes validation of path variable id.
     *
     * @param id of sim-card.
     */
    @DeleteMapping(params = "amount")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void subtractGigabytes(@PathVariable @Positive Long id, @RequestParam @Positive Integer amount) {
        billingService.subtract(id, amount);
    }
}
