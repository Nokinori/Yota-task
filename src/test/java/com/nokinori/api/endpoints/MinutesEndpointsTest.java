package com.nokinori.api.endpoints;

import com.nokinori.api.handlers.ErrorCode;
import com.nokinori.api.io.MinutesRs;
import com.nokinori.api.io.SimCardRs;
import com.nokinori.services.api.BillingService;
import com.nokinori.services.exceptions.NotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.function.Supplier;

import static com.nokinori.api.endpoints.mappings.PathMappings.CONTEXT_PATH;
import static com.nokinori.api.endpoints.mappings.PathMappings.MINUTES_PATH;
import static com.nokinori.api.endpoints.mappings.PathMappings.SIM_CARD_PATH;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(MinutesEndpoints.class)
public class MinutesEndpointsTest {

    private final Long simCardId = 1001L;

    private final String contextPath = CONTEXT_PATH;

    private final String uri = contextPath + SIM_CARD_PATH + "/" + simCardId;

    private final String minutesPath = MINUTES_PATH;

    private final Integer amount = 100;

    private final Supplier<MinutesRs> minutesRsSupplier = () ->
            MinutesRs.builder()
                    .amount(amount)
                    .expiresAt(LocalDateTime.now()
                            .plusMinutes(10))
                    .build();

    @Autowired
    private MockMvc mvc;

    @MockBean
    @Qualifier("minutesService")
    private BillingService<SimCardRs> service;

    @Before
    public void setUp() {
        SimCardRs simCardRs = SimCardRs.builder()
                .simCardId(simCardId)
                .minutesPacks(Arrays.asList(minutesRsSupplier.get(), minutesRsSupplier.get()))
                .build();

        willReturn(simCardRs).given(service)
                .get(simCardId);
    }

    @Test
    public void getMinutesForSimCard() throws Exception {
        mvc.perform(get(uri + minutesPath)
                .contextPath(contextPath))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.simCardId").value(simCardId))
                .andExpect(jsonPath("$.minutesPacks[0].amount").value(amount))
                .andExpect(jsonPath("$.minutesPacks[1].amount").value(amount))
                .andExpect(jsonPath("$.gigabytesPacks").doesNotExist());

        verify(service).get(simCardId);
    }

    @Test
    public void addMinutesForSimCard() throws Exception {
        mvc.perform(post(uri + minutesPath).param("amount", amount.toString())
                .contextPath(contextPath))
                .andExpect(status().isCreated());

        verify(service).add(simCardId, amount);
    }

    @Test
    public void deleteMinutesForSimCard() throws Exception {
        mvc.perform(delete(uri + minutesPath).param("amount", amount.toString())
                .contextPath(contextPath))
                .andExpect(status().isNoContent());

        verify(service).subtract(simCardId, amount);
    }

    @Test
    public void notFound() throws Exception {
        willThrow(NotFoundException.class).given(service)
                .get(simCardId);

        mvc.perform(get(uri + minutesPath)
                .contextPath(contextPath))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.NOT_FOUND.value()));

        verify(service).get(simCardId);
    }


}