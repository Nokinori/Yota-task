package com.nokinori.api.endpoints;

import com.nokinori.services.api.SimCardService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.nokinori.api.endpoints.mappings.PathMappings.SIM_CARD_ACTIVATE_PATH;
import static com.nokinori.api.endpoints.mappings.PathMappings.SIM_CARD_BLOCK_PATH;
import static com.nokinori.api.endpoints.mappings.PathMappings.SIM_CARD_PATH_WITH_ID_PARAM;

@RestController
@RequestMapping(SIM_CARD_PATH_WITH_ID_PARAM)
public class SimCardEndpoints {

    private final SimCardService simCardService;

    public SimCardEndpoints(SimCardService simCardService) {
        this.simCardService = simCardService;
    }

    @PutMapping(SIM_CARD_ACTIVATE_PATH)
    @ResponseStatus(HttpStatus.OK)
    public void activateSimCard(@PathVariable Long id) {
        simCardService.activate(id);
    }

    @PutMapping(SIM_CARD_BLOCK_PATH)
    @ResponseStatus(HttpStatus.OK)
    public void blockSimCard(@PathVariable Long id) {
        simCardService.block(id);
    }
}
