package com.nokinori.api.endpoints;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nokinori.api.io.SimCardRs;
import com.nokinori.services.api.BillingService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;
import static com.nokinori.api.endpoints.mappings.PathMappings.GIGABYTES_PATH;
import static com.nokinori.api.endpoints.mappings.PathMappings.SIM_CARD_PATH_WITH_ID_PARAM;

@RestController
@RequestMapping(SIM_CARD_PATH_WITH_ID_PARAM + GIGABYTES_PATH)
public class GigabytesEndpoints {

    private final BillingService<SimCardRs> billingService;

    public GigabytesEndpoints(@Qualifier("gigabytesService") BillingService<SimCardRs> billingService) {
        this.billingService = billingService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonInclude(Include.NON_NULL)
    public ResponseEntity<SimCardRs> getGigabytes(@PathVariable Long id) {
        SimCardRs rs = billingService.get(id);
        return ResponseEntity.ok(rs);
    }

    @PostMapping(params = "amount")
    @ResponseStatus(HttpStatus.CREATED)
    public void addGigabytes(@PathVariable Long id, @RequestParam Integer amount) {
        billingService.add(id, amount);
    }

    @DeleteMapping(params = "amount")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void subtractGigabytes(@PathVariable Long id, @RequestParam Integer amount) {
        billingService.subtract(id, amount);
    }
}
