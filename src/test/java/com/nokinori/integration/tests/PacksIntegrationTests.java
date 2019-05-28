package com.nokinori.integration.tests;

import com.nokinori.api.handlers.ErrorCode;
import com.nokinori.utils.Operations;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.nokinori.utils.JsonExpressions.amountParameter;
import static com.nokinori.utils.JsonExpressions.errorCode;
import static com.nokinori.utils.JsonExpressions.errorText;
import static com.nokinori.utils.TestDataHolder.amount;
import static com.nokinori.utils.TestDataHolder.contextPath;
import static com.nokinori.utils.TestDataHolder.gigabytesPath;
import static com.nokinori.utils.TestDataHolder.minutesPath;
import static com.nokinori.utils.TestDataHolder.notExistId;
import static com.nokinori.utils.TestDataHolder.simCardPath;
import static com.nokinori.utils.TestDataHolder.wrongAmount;
import static com.nokinori.utils.TestDataHolder.wrongId;
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

        mvc.perform(delete(simCardPath + id + gigabytesPath).param(amountParameter, amount.toString())
                .contextPath(contextPath))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(errorCode).value(ErrorCode.BLOCKAGE_EXCEPTION.value()))
                .andExpect(jsonPath(errorText).value("Sim-card with id: " + id + " is blocked! Must be activated for this operation."));
    }

    @Test
    public void addOnBlockedGig() throws Exception {
        Integer id = op.createSimCard();
        op.blockSimCard(id);

        mvc.perform(post(simCardPath + id + gigabytesPath).param(amountParameter, amount.toString())
                .contextPath(contextPath))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(errorCode).value(ErrorCode.BLOCKAGE_EXCEPTION.value()))
                .andExpect(jsonPath(errorText).value("Sim-card with id: " + id + " is blocked! Must be activated for this operation."));
    }

    @Test
    public void subtractOnBlockedMin() throws Exception {
        Integer id = op.createSimCard();
        op.blockSimCard(id);

        mvc.perform(delete(simCardPath + id + minutesPath).param(amountParameter, amount.toString())
                .contextPath(contextPath))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(errorCode).value(ErrorCode.BLOCKAGE_EXCEPTION.value()))
                .andExpect(jsonPath(errorText).value("Sim-card with id: " + id + " is blocked! Must be activated for this operation."));
    }

    @Test
    public void addOnBlockedMin() throws Exception {
        Integer id = op.createSimCard();
        op.blockSimCard(id);

        mvc.perform(post(simCardPath + id + minutesPath).param(amountParameter, amount.toString())
                .contextPath(contextPath))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(errorCode).value(ErrorCode.BLOCKAGE_EXCEPTION.value()))
                .andExpect(jsonPath(errorText).value("Sim-card with id: " + id + " is blocked! Must be activated for this operation."));
    }

    @Test
    public void notFoundGig() throws Exception {
        mvc.perform(get(simCardPath + notExistId + gigabytesPath)
                .contextPath(contextPath))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(errorCode).value(ErrorCode.NOT_FOUND.value()));

    }

    @Test
    public void notFoundMin() throws Exception {
        mvc.perform(get(simCardPath + notExistId + minutesPath)
                .contextPath(contextPath))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(errorCode).value(ErrorCode.NOT_FOUND.value()));

    }

    @Test
    public void checkValidationOnGetMin() throws Exception {
        mvc.perform(get(simCardPath + wrongId + minutesPath)
                .contextPath(contextPath))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath(errorCode).value(ErrorCode.VALIDATION_EXCEPTION.value()))
                .andExpect(jsonPath(errorText).value("getMinutes.id: must be greater than 0"));
    }

    @Test
    public void checkValidationOnAddMin() throws Exception {
        mvc.perform(post(simCardPath + wrongId + minutesPath)
                .param(amountParameter, wrongAmount.toString())
                .contextPath(contextPath))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath(errorCode).value(ErrorCode.VALIDATION_EXCEPTION.value()))
                .andExpect(jsonPath(errorText).value("addMinutes.id: must be greater than 0"));

    }

    @Test
    public void checkValidationOnSubtractMin() throws Exception {
        mvc.perform(delete(simCardPath + wrongId + minutesPath)
                .param(amountParameter, wrongAmount.toString())
                .contextPath(contextPath))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath(errorCode).value(ErrorCode.VALIDATION_EXCEPTION.value()))
                .andExpect(jsonPath(errorText).value("subtractMinutes.id: must be greater than 0"));

    }

    @Test
    public void checkValidationOnGetGig() throws Exception {
        mvc.perform(get(simCardPath + wrongId + gigabytesPath)
                .contextPath(contextPath))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath(errorCode).value(ErrorCode.VALIDATION_EXCEPTION.value()))
                .andExpect(jsonPath(errorText).value("getMinutes.id: must be greater than 0"));
    }

    @Test
    public void checkValidationOnAddGig() throws Exception {
        mvc.perform(post(simCardPath + wrongId + gigabytesPath)
                .param(amountParameter, wrongAmount.toString())
                .contextPath(contextPath))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath(errorCode).value(ErrorCode.VALIDATION_EXCEPTION.value()))
                .andExpect(jsonPath(errorText).value("addMinutes.id: must be greater than 0"));

    }

    @Test
    public void checkValidationOnSubtractGig() throws Exception {
        mvc.perform(delete(simCardPath + wrongId + gigabytesPath)
                .param(amountParameter, wrongAmount.toString())
                .contextPath(contextPath))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath(errorCode).value(ErrorCode.VALIDATION_EXCEPTION.value()))
                .andExpect(jsonPath(errorText).value("subtractMinutes.id: must be greater than 0"));

    }
}
