package com.nokinori.api.endpoints;

import com.nokinori.api.handlers.ErrorCode;
import com.nokinori.api.io.GigabytesRs;
import com.nokinori.api.io.SimCardRs;
import com.nokinori.services.api.BillingService;
import com.nokinori.services.exceptions.NotFoundException;
import com.nokinori.utils.JsonExpressions;
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

import static com.nokinori.utils.TestDataHolder.amount;
import static com.nokinori.utils.TestDataHolder.contextPath;
import static com.nokinori.utils.TestDataHolder.gigabytesPath;
import static com.nokinori.utils.TestDataHolder.simCardId;
import static com.nokinori.utils.TestDataHolder.simCardPath;
import static com.nokinori.utils.TestDataHolder.wrongAmount;
import static com.nokinori.utils.TestDataHolder.wrongId;
import static java.time.LocalDateTime.now;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//import static org.mockito.ArgumentMatchers.contains;

@RunWith(SpringRunner.class)
@WebMvcTest(GigabytesEndpoints.class)
public class GigabytesEndpointsTest {

    private final String uri = simCardPath + simCardId;

    private final String uriConstrainViolation = simCardPath + wrongId;

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
                .andExpect(jsonPath(JsonExpressions.SIM_CARD_ID).value(simCardId))
                .andExpect(jsonPath(JsonExpressions.GIGABYTES_PACKS + "[0].amount").value(amount))
                .andExpect(jsonPath(JsonExpressions.GIGABYTES_PACKS + "[1].amount").value(amount))
                .andExpect(jsonPath(JsonExpressions.MINUTES_PACKS).doesNotExist());

        verify(service).get(simCardId);
    }

    @Test
    public void addGigabytesForSimCard() throws Exception {
        mvc.perform(post(uri + gigabytesPath).param(JsonExpressions.AMOUNT_PARAMETER, amount.toString())
                .contextPath(contextPath))
                .andExpect(status().isCreated());

        verify(service).add(simCardId, amount);
    }

    @Test
    public void deleteGigabytesForSimCard() throws Exception {
        mvc.perform(delete(uri + gigabytesPath).param(JsonExpressions.AMOUNT_PARAMETER, amount.toString())
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
                .andExpect(jsonPath(JsonExpressions.ERROR_CODE).value(ErrorCode.NOT_FOUND.value()));

        verify(service).get(simCardId);
    }

    @Test
    public void checkValidationOnGet() throws Exception {
        mvc.perform(get(uriConstrainViolation + gigabytesPath)
                .contextPath(contextPath))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath(JsonExpressions.ERROR_CODE).value(ErrorCode.VALIDATION_EXCEPTION.value()))
                .andExpect(jsonPath(JsonExpressions.ERROR_TEXT).value("getGigabytes.id: must be greater than 0"));

        verify(service, never()).get(wrongId);
    }

    @Test
    public void checkValidationOnAdd() throws Exception {
        mvc.perform(post(uriConstrainViolation + gigabytesPath)
                .param(JsonExpressions.AMOUNT_PARAMETER, wrongAmount.toString())
                .contextPath(contextPath))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath(JsonExpressions.ERROR_CODE).value(ErrorCode.VALIDATION_EXCEPTION.value()))
                .andExpect(jsonPath(JsonExpressions.ERROR_TEXT, containsString("must be greater than 0")))
                .andExpect(jsonPath(JsonExpressions.ERROR_TEXT, containsString("addGigabytes.id")))
                .andExpect(jsonPath(JsonExpressions.ERROR_TEXT, containsString("addGigabytes.amount")));

        verify(service, never()).add(wrongId, wrongAmount);
    }

    @Test
    public void checkValidationOnSubtract() throws Exception {
        mvc.perform(delete(uriConstrainViolation + gigabytesPath)
                .param(JsonExpressions.AMOUNT_PARAMETER, wrongAmount.toString())
                .contextPath(contextPath))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath(JsonExpressions.ERROR_CODE).value(ErrorCode.VALIDATION_EXCEPTION.value()))
                .andExpect(jsonPath(JsonExpressions.ERROR_TEXT, containsString("must be greater than 0")))
                .andExpect(jsonPath(JsonExpressions.ERROR_TEXT, containsString("subtractGigabytes.id")))
                .andExpect(jsonPath(JsonExpressions.ERROR_TEXT, containsString("subtractGigabytes.amount")));

        verify(service, never()).subtract(wrongId, wrongAmount);
    }
}