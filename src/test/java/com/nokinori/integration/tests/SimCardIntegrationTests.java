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

import static com.nokinori.utils.TestDataHolder.activatePath;
import static com.nokinori.utils.TestDataHolder.blockPath;
import static com.nokinori.utils.TestDataHolder.contextPath;
import static com.nokinori.utils.TestDataHolder.minutesPath;
import static com.nokinori.utils.TestDataHolder.notExistId;
import static com.nokinori.utils.TestDataHolder.simCardPath;
import static com.nokinori.utils.TestDataHolder.wrongId;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SimCardIntegrationTests {

    private final Operations op = new Operations();
    @Autowired
    private MockMvc mvc;

    @Before
    public void setUp() {
        op.setMvc(mvc);
    }

    @Test
    public void createThenBlockSimCard() throws Exception {
        Integer id = op.createSimCard();
        op.blockSimCard(id);
    }

    @Test
    public void createThenActivateSimCard() throws Exception {
        Integer id = op.createSimCard();

        mvc.perform(put(simCardPath + id + activatePath)
                .contextPath(contextPath))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(JsonExpressions.ERROR_CODE).value(ErrorCode.ACTIVATION_EXCEPTION.value()))
                .andExpect(jsonPath(JsonExpressions.ERROR_TEXT).value("Sim-card with id: " + id + " already activated"));
    }

    @Test
    public void createThenBlockThenBlockSimCard() throws Exception {
        Integer id = op.createSimCard();
        op.blockSimCard(id);

        mvc.perform(put(simCardPath + id + blockPath)
                .contextPath(contextPath))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(JsonExpressions.ERROR_CODE).value(ErrorCode.BLOCKAGE_EXCEPTION.value()))
                .andExpect(jsonPath(JsonExpressions.ERROR_TEXT).value("Sim-card with id: " + id + " already blocked"));
    }

    @Test
    public void createThenBlockThenActivateSimCard() throws Exception {
        Integer id = op.createSimCard();
        op.blockSimCard(id);
        op.activateSimCard(id);
    }

    @Test
    public void simCardNotFound() throws Exception {
        mvc.perform(get(simCardPath + notExistId + minutesPath)
                .contextPath(contextPath))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(JsonExpressions.ERROR_CODE).value(ErrorCode.NOT_FOUND.value()));
    }

    @Test
    public void checkValidationOnBlockage() throws Exception {
        mvc.perform(put(simCardPath + wrongId + blockPath)
                .contextPath(contextPath))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath(JsonExpressions.ERROR_CODE).value(ErrorCode.VALIDATION_EXCEPTION.value()))
                .andExpect(jsonPath(JsonExpressions.ERROR_TEXT).value("blockSimCard.id: must be greater than 0"));
    }

    @Test
    public void checkValidationOnActivation() throws Exception {
        mvc.perform(put(simCardPath + wrongId + activatePath)
                .contextPath(contextPath))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath(JsonExpressions.ERROR_CODE).value(ErrorCode.VALIDATION_EXCEPTION.value()))
                .andExpect(jsonPath(JsonExpressions.ERROR_TEXT).value("activateSimCard.id: must be greater than 0"));

    }
}
