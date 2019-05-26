package com.nokinori.api.endpoints;

import com.nokinori.api.handlers.ErrorCode;
import com.nokinori.services.api.SimCardService;
import com.nokinori.services.exceptions.NotFoundException;
import com.nokinori.services.exceptions.SimCardActivationException;
import com.nokinori.services.exceptions.SimCardBlockageException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.nokinori.api.endpoints.mappings.PathMappings.CONTEXT_PATH;
import static com.nokinori.api.endpoints.mappings.PathMappings.SIM_CARD_ACTIVATE_PATH;
import static com.nokinori.api.endpoints.mappings.PathMappings.SIM_CARD_BLOCK_PATH;
import static com.nokinori.api.endpoints.mappings.PathMappings.SIM_CARD_PATH;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(SimCardEndpoints.class)
public class SimCardEndpointsTest {

    private final Long simCardId = 1001L;

    private final Long wrongId = -1001L;

    private final String contextPath = CONTEXT_PATH;

    private final String uri = contextPath + SIM_CARD_PATH + "/" + simCardId;

    private final String uriConstrainViolation = contextPath + SIM_CARD_PATH + "/" + wrongId;

    private final String activatePath = SIM_CARD_ACTIVATE_PATH;

    private final String blockPath = SIM_CARD_BLOCK_PATH;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SimCardService service;

    @Before
    public void setUp() {
        willDoNothing().given(service)
                .activate(simCardId);

        willDoNothing().given(service)
                .block(simCardId);
    }

    @Test
    public void activateSimCard() throws Exception {
        mvc.perform(put(uri + activatePath)
                .contextPath(contextPath))
                .andExpect(status().isOk());

        verify(service).activate(simCardId);
    }

    @Test
    public void blockSimCard() throws Exception {
        mvc.perform(put(uri + blockPath)
                .contextPath(contextPath))
                .andExpect(status().isOk());

        verify(service).block(simCardId);
    }

    @Test
    public void notFoundExForActivateUri() throws Exception {
        willThrow(NotFoundException.class).given(service)
                .activate(simCardId);

        mvc.perform(put(uri + activatePath)
                .contextPath(contextPath))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.NOT_FOUND.value()));

        verify(service).activate(simCardId);
    }

    @Test
    public void notFoundExForBlockUri() throws Exception {
        willThrow(NotFoundException.class).given(service)
                .block(simCardId);

        mvc.perform(put(uri + blockPath)
                .contextPath(contextPath))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.NOT_FOUND.value()));

        verify(service).block(simCardId);
    }

    @Test
    public void alreadyActivated() throws Exception {
        willThrow(SimCardActivationException.class).given(service)
                .activate(simCardId);

        mvc.perform(put(uri + activatePath)
                .contextPath(contextPath))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode")
                        .value(ErrorCode.ACTIVATION_EXCEPTION.value()))
                .andExpect(jsonPath("$.errorText").value("Activation exception"));

        verify(service).activate(simCardId);
    }

    @Test
    public void alreadyBlocked() throws Exception {
        willThrow(SimCardBlockageException.class).given(service)
                .block(simCardId);

        mvc.perform(put(uri + blockPath)
                .contextPath(contextPath))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.BLOCKAGE_EXCEPTION.value()))
                .andExpect(jsonPath("$.errorText").value("Blockage exception"));

        verify(service).block(simCardId);
    }

    @Test
    public void checkValidationOnActivation() throws Exception {
        mvc.perform(put(uriConstrainViolation + activatePath)
                .contextPath(contextPath))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.VALIDATION_EXCEPTION.value()))
                .andExpect(jsonPath("$.errorText").value("activateSimCard.id: must be greater than 0"));

        verify(service, never()).block(simCardId);
    }

    @Test
    public void checkValidationOnBlockage() throws Exception {
        mvc.perform(put(uriConstrainViolation + blockPath)
                .contextPath(contextPath))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.VALIDATION_EXCEPTION.value()))
                .andExpect(jsonPath("$.errorText").value("blockSimCard.id: must be greater than 0"));

        verify(service, never()).block(simCardId);
    }

}