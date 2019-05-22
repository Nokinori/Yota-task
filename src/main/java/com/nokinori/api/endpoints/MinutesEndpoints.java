package com.nokinori.api.endpoints;

import com.nokinori.api.io.MinutesRs;
import com.nokinori.services.api.BillingService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/users/{id}/minutes", produces = MediaType.APPLICATION_JSON_VALUE)
public class MinutesEndpoints {

    private final BillingService<MinutesRs> billingService;

    public MinutesEndpoints(@Qualifier("minutesService") BillingService<MinutesRs> billingService) {
        this.billingService = billingService;
    }

    @GetMapping
    public ResponseEntity<MinutesRs> getMinutes(@PathVariable Long id) {
        MinutesRs rs = billingService.get(id);
        return ResponseEntity.ok(rs);
    }

    @PostMapping(params = "amount")
    public ResponseEntity<MinutesRs> addMinutes(@PathVariable Long id, @RequestParam Long amount) {
        MinutesRs rs = billingService.add(id, amount);
        return ResponseEntity.ok(rs);
    }

    @PutMapping(params = "amount")
    public ResponseEntity<MinutesRs> subtractMinutes(@PathVariable Long id, @RequestParam Long amount) {
        MinutesRs rs = billingService.subtract(id, amount);
        return ResponseEntity.ok(rs);
    }
}
