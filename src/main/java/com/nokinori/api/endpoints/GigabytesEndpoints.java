package com.nokinori.api.endpoints;

import com.nokinori.api.io.GigabytesRs;
import com.nokinori.services.api.BillingService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users/{id}/gigabytes")
public class GigabytesEndpoints {

    private final BillingService<GigabytesRs> billingService;

    public GigabytesEndpoints(@Qualifier("gigabyteService") BillingService<GigabytesRs> billingService) {
        this.billingService = billingService;
    }

    @GetMapping
    public ResponseEntity<GigabytesRs> getGigabytes(@PathVariable Long id) {
        GigabytesRs rs = billingService.get(id);
        return ResponseEntity.ok(rs);
    }

    @PostMapping(params = "amount")
    public ResponseEntity<GigabytesRs> addGigabytes(@PathVariable Long id, @RequestParam Long amount) {
        GigabytesRs rs = billingService.add(id, amount);
        return ResponseEntity.ok(rs);
    }

    @PutMapping(params = "amount")
    public ResponseEntity<GigabytesRs> subtractGigabytes(@PathVariable Long id, @RequestParam Long amount) {
        GigabytesRs rs = billingService.subtract(id, amount);
        return ResponseEntity.ok(rs);
    }
}
