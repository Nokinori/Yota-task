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
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(SimCardEndpoints.class)
public class SimCardEndpointsTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    ServerProperties properties;

    @MockBean
    private SimCardService service;

    private Long userId = 1001L;

    private String contextPath = "/services/billing";

    private String uri = contextPath + "/users/" + userId + "/sim-cards";

    private String activate = "/activate";

    private String block = "/block";

    @Before
    public void setUp() {
        willDoNothing().given(service)
                .activate(userId);

        willDoNothing().given(service)
                .block(userId);


    }

    @Test
    public void activateSimCard() throws Exception {
        mvc.perform(put(uri + activate)
                .contextPath(contextPath))
                .andExpect(status().isOk());

        verify(service, times(1)).activate(userId);
    }

    @Test
    public void blockSimCard() throws Exception {
        mvc.perform(put(uri + block)
                .contextPath(contextPath))
                .andExpect(status().isOk());

        verify(service, times(1)).block(userId);
    }

    @Test
    public void notFoundExForActivateUri() throws Exception {
        willThrow(NotFoundException.class).given(service)
                .activate(userId);

        mvc.perform(put(uri + activate)
                .contextPath(contextPath))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.NOT_FOUND.value()));

        verify(service, times(1)).activate(userId);
    }

    @Test
    public void notFoundExForBlockUri() throws Exception {
        willThrow(NotFoundException.class).given(service)
                .block(userId);

        mvc.perform(put(uri + block)
                .contextPath(contextPath))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.NOT_FOUND.value()));

        verify(service, times(1)).block(userId);
    }

    @Test
    public void alreadyActivated() throws Exception {
        willThrow(SimCardActivationException.class).given(service)
                .activate(userId);

        mvc.perform(put(uri + activate)
                .contextPath(contextPath))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode")
                        .value(ErrorCode.ACTIVATION_EXCEPTION.value()))
                .andExpect(jsonPath("$.errorText").value("Activation exception"));

        verify(service, times(1)).activate(userId);
    }

    @Test
    public void alreadyBlocked() throws Exception {
        willThrow(SimCardBlockageException.class).given(service)
                .block(userId);

        mvc.perform(put(uri + block)
                .contextPath(contextPath))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.BLOCKAGE_EXCEPTION.value()))
                .andExpect(jsonPath("$.errorText").value("Blockage exception"));

        verify(service, times(1)).block(userId);
    }

}