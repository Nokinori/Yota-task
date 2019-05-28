package com.nokinori.api.endpoints;

import com.nokinori.api.handlers.ErrorCode;
import com.nokinori.services.api.SimCardService;
import com.nokinori.services.exceptions.NotFoundException;
import com.nokinori.services.exceptions.SimCardActivationException;
import com.nokinori.services.exceptions.SimCardBlockageException;
import com.nokinori.utils.JsonExpressions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.nokinori.utils.TestDataHolder.activatePath;
import static com.nokinori.utils.TestDataHolder.blockPath;
import static com.nokinori.utils.TestDataHolder.contextPath;
import static com.nokinori.utils.TestDataHolder.simCardId;
import static com.nokinori.utils.TestDataHolder.simCardPath;
import static com.nokinori.utils.TestDataHolder.wrongId;
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

    private final String uri = simCardPath + simCardId;

    private final String uriConstrainViolation = simCardPath + wrongId;

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
                .andExpect(jsonPath(JsonExpressions.ERROR_CODE).value(ErrorCode.NOT_FOUND.value()));

        verify(service).activate(simCardId);
    }

    @Test
    public void notFoundExForBlockUri() throws Exception {
        willThrow(NotFoundException.class).given(service)
                .block(simCardId);

        mvc.perform(put(uri + blockPath)
                .contextPath(contextPath))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(JsonExpressions.ERROR_CODE).value(ErrorCode.NOT_FOUND.value()));

        verify(service).block(simCardId);
    }

    @Test
    public void alreadyActivated() throws Exception {
        willThrow(SimCardActivationException.class).given(service)
                .activate(simCardId);

        mvc.perform(put(uri + activatePath)
                .contextPath(contextPath))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(JsonExpressions.ERROR_CODE)
                        .value(ErrorCode.ACTIVATION_EXCEPTION.value()))
                .andExpect(jsonPath(JsonExpressions.ERROR_TEXT).value("Activation exception"));

        verify(service).activate(simCardId);
    }

    @Test
    public void alreadyBlocked() throws Exception {
        willThrow(SimCardBlockageException.class).given(service)
                .block(simCardId);

        mvc.perform(put(uri + blockPath)
                .contextPath(contextPath))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(JsonExpressions.ERROR_CODE).value(ErrorCode.BLOCKAGE_EXCEPTION.value()))
                .andExpect(jsonPath(JsonExpressions.ERROR_TEXT).value("Blockage exception"));

        verify(service).block(simCardId);
    }

    @Test
    public void checkValidationOnActivation() throws Exception {
        mvc.perform(put(uriConstrainViolation + activatePath)
                .contextPath(contextPath))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath(JsonExpressions.ERROR_CODE).value(ErrorCode.VALIDATION_EXCEPTION.value()))
                .andExpect(jsonPath(JsonExpressions.ERROR_TEXT).value("activateSimCard.id: must be greater than 0"));

        verify(service, never()).block(simCardId);
    }

    @Test
    public void checkValidationOnBlockage() throws Exception {
        mvc.perform(put(uriConstrainViolation + blockPath)
                .contextPath(contextPath))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath(JsonExpressions.ERROR_CODE).value(ErrorCode.VALIDATION_EXCEPTION.value()))
                .andExpect(jsonPath(JsonExpressions.ERROR_TEXT).value("blockSimCard.id: must be greater than 0"));

        verify(service, never()).block(simCardId);
    }

}