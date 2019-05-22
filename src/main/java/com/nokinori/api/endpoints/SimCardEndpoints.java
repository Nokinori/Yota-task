package com.nokinori.api.endpoints;

import com.nokinori.services.api.SimCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/users/{id}/sim-cards", produces = MediaType.APPLICATION_JSON_VALUE)
public class SimCardEndpoints {

    private final SimCardService simCardService;

    @Autowired
    public SimCardEndpoints(SimCardService simCardService) {
        this.simCardService = simCardService;
    }

    @PutMapping(path = "/activate")
    @ResponseStatus(HttpStatus.OK)
    public void activateSimCard(@PathVariable Long id) {
        simCardService.activate(id);
    }

    @PutMapping(path = "/block")
    @ResponseStatus(HttpStatus.OK)
    public void blockSimCard(@PathVariable Long id) {
        simCardService.block(id);
    }
}
