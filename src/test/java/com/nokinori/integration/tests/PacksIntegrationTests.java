package com.nokinori.integration.tests;

import com.nokinori.api.handlers.ErrorCode;
import com.nokinori.utils.JsonExpressions;
import com.nokinori.utils.Operations;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.nokinori.utils.TestDataHolder.amount;
import static com.nokinori.utils.TestDataHolder.contextPath;
import static com.nokinori.utils.TestDataHolder.gigabytesPath;
import static com.nokinori.utils.TestDataHolder.minutesPath;
import static com.nokinori.utils.TestDataHolder.notExistId;
import static com.nokinori.utils.TestDataHolder.simCardPath;
import static com.nokinori.utils.TestDataHolder.wrongAmount;
import static com.nokinori.utils.TestDataHolder.wrongId;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PacksIntegrationTests {

    private final Operations op = new Operations();
    @Autowired
    private MockMvc mvc;

    @Before
    public void setUp() {
        op.setMvc(mvc);
    }

    @Test
    public void addMinAndGigThenSubtract() throws Exception {
        Integer id = op.createSimCard();
        op.getMinutesPack(id);
        op.getGigabytesPack(id);

        op.addMinutesPack(id, amount);
        op.addGigabytesPack(id, amount);

        op.getMinutesPack(id, amount);
        op.getGigabytesPack(id, amount);

        op.deleteMinutesPack(id, amount);
        op.deleteGigabytesPack(id, amount);

        op.getMinutesPack(id);
        op.getGigabytesPack(id);
    }

    @Test
    public void subtractMoreThanOne() throws Exception {
        Integer id = op.createSimCard();
        op.getMinutesPack(id);
        op.getGigabytesPack(id);

        op.addMinutesPack(id, amount);
        op.addMinutesPack(id, amount);

        op.addGigabytesPack(id, amount);
        op.addGigabytesPack(id, amount);

        op.deleteMinutesPack(id, amount + 50);
        op.deleteGigabytesPack(id, amount + 50);

        op.getMinutesPack(id, 50);
        op.getGigabytesPack(id, 50);
    }

    @Test
    public void subtractOnBlockedGig() throws Exception {
        Integer id = op.createSimCard();
        op.blockSimCard(id);

        mvc.perform(delete(simCardPath + id + gigabytesPath).param(JsonExpressions.AMOUNT_PARAMETER, amount.toString())
                .contextPath(contextPath))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(JsonExpressions.ERROR_CODE).value(ErrorCode.BLOCKAGE_EXCEPTION.value()))
                .andExpect(jsonPath(JsonExpressions.ERROR_TEXT).value("Sim-card with id: " + id + " is blocked! Must be activated for this operation."));
    }

    @Test
    public void addOnBlockedGig() throws Exception {
        Integer id = op.createSimCard();
        op.blockSimCard(id);

        mvc.perform(post(simCardPath + id + gigabytesPath).param(JsonExpressions.AMOUNT_PARAMETER, amount.toString())
                .contextPath(contextPath))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(JsonExpressions.ERROR_CODE).value(ErrorCode.BLOCKAGE_EXCEPTION.value()))
                .andExpect(jsonPath(JsonExpressions.ERROR_TEXT).value("Sim-card with id: " + id + " is blocked! Must be activated for this operation."));
    }

    @Test
    public void subtractOnBlockedMin() throws Exception {
        Integer id = op.createSimCard();
        op.blockSimCard(id);

        mvc.perform(delete(simCardPath + id + minutesPath).param(JsonExpressions.AMOUNT_PARAMETER, amount.toString())
                .contextPath(contextPath))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(JsonExpressions.ERROR_CODE).value(ErrorCode.BLOCKAGE_EXCEPTION.value()))
                .andExpect(jsonPath(JsonExpressions.ERROR_TEXT).value("Sim-card with id: " + id + " is blocked! Must be activated for this operation."));
    }

    @Test
    public void addOnBlockedMin() throws Exception {
        Integer id = op.createSimCard();
        op.blockSimCard(id);

        mvc.perform(post(simCardPath + id + minutesPath).param(JsonExpressions.AMOUNT_PARAMETER, amount.toString())
                .contextPath(contextPath))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(JsonExpressions.ERROR_CODE).value(ErrorCode.BLOCKAGE_EXCEPTION.value()))
                .andExpect(jsonPath(JsonExpressions.ERROR_TEXT).value("Sim-card with id: " + id + " is blocked! Must be activated for this operation."));
    }

    @Test
    public void notFoundGig() throws Exception {
        mvc.perform(get(simCardPath + notExistId + gigabytesPath)
                .contextPath(contextPath))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(JsonExpressions.ERROR_CODE).value(ErrorCode.NOT_FOUND.value()));

    }

    @Test
    public void notFoundMin() throws Exception {
        mvc.perform(get(simCardPath + notExistId + minutesPath)
                .contextPath(contextPath))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(JsonExpressions.ERROR_CODE).value(ErrorCode.NOT_FOUND.value()));

    }

    @Test
    public void checkValidationOnGetMin() throws Exception {
        mvc.perform(get(simCardPath + wrongId + minutesPath)
                .contextPath(contextPath))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath(JsonExpressions.ERROR_CODE).value(ErrorCode.VALIDATION_EXCEPTION.value()))
                .andExpect(jsonPath(JsonExpressions.ERROR_TEXT).value("getMinutes.id: must be greater than 0"));
    }

    @Test
    public void checkValidationOnAddMin() throws Exception {
        mvc.perform(post(simCardPath + wrongId + minutesPath)
                .param(JsonExpressions.AMOUNT_PARAMETER, wrongAmount.toString())
                .contextPath(contextPath))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath(JsonExpressions.ERROR_CODE).value(ErrorCode.VALIDATION_EXCEPTION.value()))
                .andExpect(jsonPath(JsonExpressions.ERROR_TEXT, containsString("must be greater than 0")))
                .andExpect(jsonPath(JsonExpressions.ERROR_TEXT, containsString("addMinutes.id")))
                .andExpect(jsonPath(JsonExpressions.ERROR_TEXT, containsString("addMinutes.amount")));

    }

    @Test
    public void checkValidationOnSubtractMin() throws Exception {
        mvc.perform(delete(simCardPath + wrongId + minutesPath)
                .param(JsonExpressions.AMOUNT_PARAMETER, wrongAmount.toString())
                .contextPath(contextPath))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath(JsonExpressions.ERROR_CODE).value(ErrorCode.VALIDATION_EXCEPTION.value()))
                .andExpect(jsonPath(JsonExpressions.ERROR_TEXT, containsString("must be greater than 0")))
                .andExpect(jsonPath(JsonExpressions.ERROR_TEXT, containsString("subtractMinutes.id")))
                .andExpect(jsonPath(JsonExpressions.ERROR_TEXT, containsString("subtractMinutes.amount")));

    }

    @Test
    public void checkValidationOnGetGig() throws Exception {
        mvc.perform(get(simCardPath + wrongId + gigabytesPath)
                .contextPath(contextPath))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath(JsonExpressions.ERROR_CODE).value(ErrorCode.VALIDATION_EXCEPTION.value()))
                .andExpect(jsonPath(JsonExpressions.ERROR_TEXT).value("getGigabytes.id: must be greater than 0"));
    }

    @Test
    public void checkValidationOnAddGig() throws Exception {
        mvc.perform(post(simCardPath + wrongId + gigabytesPath)
                .param(JsonExpressions.AMOUNT_PARAMETER, wrongAmount.toString())
                .contextPath(contextPath))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath(JsonExpressions.ERROR_CODE).value(ErrorCode.VALIDATION_EXCEPTION.value()))
                .andExpect(jsonPath(JsonExpressions.ERROR_TEXT, containsString("must be greater than 0")))
                .andExpect(jsonPath(JsonExpressions.ERROR_TEXT, containsString("addGigabytes.id")))
                .andExpect(jsonPath(JsonExpressions.ERROR_TEXT, containsString("addGigabytes.amount")));

    }

    @Test
    public void checkValidationOnSubtractGig() throws Exception {
        mvc.perform(delete(simCardPath + wrongId + gigabytesPath)
                .param(JsonExpressions.AMOUNT_PARAMETER, wrongAmount.toString())
                .contextPath(contextPath))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath(JsonExpressions.ERROR_CODE).value(ErrorCode.VALIDATION_EXCEPTION.value()))
                .andExpect(jsonPath(JsonExpressions.ERROR_TEXT, containsString("must be greater than 0")))
                .andExpect(jsonPath(JsonExpressions.ERROR_TEXT, containsString("subtractGigabytes.id")))
                .andExpect(jsonPath(JsonExpressions.ERROR_TEXT, containsString("subtractGigabytes.amount")));

    }
}
