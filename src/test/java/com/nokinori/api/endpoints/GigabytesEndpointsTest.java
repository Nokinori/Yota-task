package com.nokinori.api.endpoints;

import com.nokinori.api.handlers.ErrorCode;
import com.nokinori.api.io.GigabytesRs;
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

import java.util.Arrays;
import java.util.function.Supplier;

import static com.nokinori.api.endpoints.mappings.PathMappings.CONTEXT_PATH;
import static com.nokinori.api.endpoints.mappings.PathMappings.GIGABYTES_PATH;
import static com.nokinori.api.endpoints.mappings.PathMappings.SIM_CARD_PATH;
import static java.time.LocalDateTime.now;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(GigabytesEndpoints.class)
public class GigabytesEndpointsTest {

    private final Long simCardId = 1001L;

    private final Long wrongId = -1001L;

    private final String contextPath = CONTEXT_PATH;

    private final String uri = contextPath + SIM_CARD_PATH + "/" + simCardId;

    private final String uriConstrainViolation = contextPath + SIM_CARD_PATH + "/" + wrongId;

    private final String gigabytesPath = GIGABYTES_PATH;

    private final Integer amount = 100;

    private final Integer wrongAmount = 100;

    private final Supplier<GigabytesRs> gigabytesRsSupplier = () ->
            GigabytesRs.builder()
                    .amount(amount)
                    .expiresAt(now()
                            .plusMinutes(10))
                    .build();

    @Autowired
    private MockMvc mvc;

    @MockBean
    @Qualifier("gigabytesService")
    private BillingService<SimCardRs> service;

    @Before
    public void setUp() {
        SimCardRs simCardRs = SimCardRs.builder()
                .simCardId(simCardId)
                .gigabytesPacks(Arrays.asList(gigabytesRsSupplier.get(), gigabytesRsSupplier.get()))
                .build();

        willReturn(simCardRs).given(service)
                .get(simCardId);
    }

    @Test
    public void getGigabytesForSimCard() throws Exception {
        mvc.perform(get(uri + gigabytesPath)
                .contextPath(contextPath))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.simCardId").value(simCardId))
                .andExpect(jsonPath("$.gigabytesPacks[0].amount").value(amount))
                .andExpect(jsonPath("$.gigabytesPacks[1].amount").value(amount))
                .andExpect(jsonPath("$.minutesPacks").doesNotExist());

        verify(service).get(simCardId);
    }

    @Test
    public void addGigabytesForSimCard() throws Exception {
        mvc.perform(post(uri + gigabytesPath).param("amount", amount.toString())
                .contextPath(contextPath))
                .andExpect(status().isCreated());

        verify(service).add(simCardId, amount);
    }

    @Test
    public void deleteGigabytesForSimCard() throws Exception {
        mvc.perform(delete(uri + gigabytesPath).param("amount", amount.toString())
                .contextPath(contextPath))
                .andExpect(status().isNoContent());

        verify(service).subtract(simCardId, amount);
    }

    @Test
    public void notFound() throws Exception {
        willThrow(NotFoundException.class).given(service)
                .get(simCardId);

        mvc.perform(get(uri + gigabytesPath)
                .contextPath(contextPath))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.NOT_FOUND.value()));

        verify(service).get(simCardId);
    }

    @Test
    public void checkValidationOnGet() throws Exception {
        mvc.perform(get(uriConstrainViolation + gigabytesPath)
                .contextPath(contextPath))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.VALIDATION_EXCEPTION.value()))
                .andExpect(jsonPath("$.errorText").value("getGigabytes.id: must be greater than 0"));

        verify(service, never()).get(wrongId);
    }

    @Test
    public void checkValidationOnAdd() throws Exception {
        mvc.perform(post(uriConstrainViolation + gigabytesPath)
                .param("amount", wrongAmount.toString())
                .contextPath(contextPath))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.VALIDATION_EXCEPTION.value()))
                .andExpect(jsonPath("$.errorText").value("addGigabytes.id: must be greater than 0"));

        verify(service, never()).add(wrongId, wrongAmount);
    }

    @Test
    public void checkValidationOnSubtract() throws Exception {
        mvc.perform(delete(uriConstrainViolation + gigabytesPath)
                .param("amount", wrongAmount.toString())
                .contextPath(contextPath))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.VALIDATION_EXCEPTION.value()))
                .andExpect(jsonPath("$.errorText").value("subtractGigabytes.id: must be greater than 0"));

        verify(service, never()).subtract(wrongId, wrongAmount);
    }
}