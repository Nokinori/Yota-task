package com.nokinori.integration.tests;

import com.jayway.jsonpath.JsonPath;
import com.nokinori.api.handlers.ErrorCode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.nokinori.api.endpoints.mappings.PathMappings.CONTEXT_PATH;
import static com.nokinori.api.endpoints.mappings.PathMappings.GIGABYTES_PATH;
import static com.nokinori.api.endpoints.mappings.PathMappings.MINUTES_PATH;
import static com.nokinori.api.endpoints.mappings.PathMappings.SIM_CARD_ACTIVATE_PATH;
import static com.nokinori.api.endpoints.mappings.PathMappings.SIM_CARD_BLOCK_PATH;
import static com.nokinori.api.endpoints.mappings.PathMappings.SIM_CARD_PATH;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SimCardIntegrationTest {


    private final Long notExistId = 1001L;

    private final Long wrongId = -1001L;

    private final String contextPath = CONTEXT_PATH;

    private final String uri = contextPath + SIM_CARD_PATH + "/";

    private final String activatePath = SIM_CARD_ACTIVATE_PATH;

    private final String blockPath = SIM_CARD_BLOCK_PATH;

    private final String minutesPath = MINUTES_PATH;

    private final String gigabytesPath = GIGABYTES_PATH;

    private final Integer amount = 100;

    @Autowired
    private MockMvc mvc;

    @Test
    public void createThenBlockSimCard() throws Exception {
        Integer id = createSimCard();
        blockSimCard(id);
    }

    @Test
    public void createThenActivateSimCard() throws Exception {
        Integer id = createSimCard();

        mvc.perform(put(uri + id + activatePath)
                .contextPath(contextPath))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.ACTIVATION_EXCEPTION.value()))
                .andExpect(jsonPath("$.errorText").value("Sim-card with id: " + id + "already activated"));
    }

    @Test
    public void createThenBlockThenBlockSimCard() throws Exception {
        Integer id = createSimCard();
        blockSimCard(id);

        mvc.perform(put(uri + id + blockPath)
                .contextPath(contextPath))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.BLOCKAGE_EXCEPTION.value()))
                .andExpect(jsonPath("$.errorText").value("Sim-card with id: " + id + " already blocked"));
    }

    @Test
    public void createThenBlockThenActivateSimCard() throws Exception {
        Integer id = createSimCard();
        blockSimCard(id);
        activateSimCard(id);
    }

    @Test
    public void addMinAndGigPacksThenGetThenDeletePack() throws Exception {
        Integer id = createSimCard();
        getMinutesPack(id);
        getGigabytesPack(id);

        addMinutesPack(id, amount);
        addGigabytesPack(id, amount);

        getMinutesPack(id, amount);
        getGigabytesPack(id, amount);

        deleteMinutesPack(id, amount);
        deleteGigabytesPack(id, amount);

        getMinutesPack(id);
        getGigabytesPack(id);
    }

    @Test
    public void simCardNotFound() throws Exception {
        mvc.perform(get(uri + notExistId + minutesPath)
                .contextPath(contextPath))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.NOT_FOUND.value()));
    }

    @Test
    public void checkValidation() throws Exception {
        mvc.perform(get(uri + wrongId + minutesPath)
                .contextPath(contextPath))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.VALIDATION_EXCEPTION.value()))
                .andExpect(jsonPath("$.errorText").value("getMinutes.id: must be greater than 0"));
    }


    private Integer createSimCard() throws Exception {
        MvcResult mvcResult = mvc.perform(post(uri).contextPath(contextPath))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.simCardId").isNotEmpty())
                .andExpect(jsonPath("$.minutesPacks").isEmpty())
                .andExpect(jsonPath("$.gigabytesPacks").isEmpty())
                .andReturn();

        String body = mvcResult.getResponse()
                .getContentAsString();

        return JsonPath.parse(body)
                .read("$.simCardId");
    }

    private void blockSimCard(int id) throws Exception {
        mvc.perform(put(uri + id + blockPath)
                .contextPath(contextPath))
                .andExpect(status().isOk());
    }

    private void activateSimCard(int id) throws Exception {
        mvc.perform(put(uri + id + activatePath)
                .contextPath(contextPath))
                .andExpect(status().isOk());
    }

    private void getMinutesPack(int id, Integer amount) throws Exception {
        mvc.perform(get(uri + id + minutesPath)
                .contextPath(contextPath))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.simCardId").value(id))
                .andExpect(jsonPath("$.minutesPacks[0].amount").value(amount))
                .andExpect(jsonPath("$.gigabytesPacks").doesNotExist());
    }

    private void getMinutesPack(int id) throws Exception {
        mvc.perform(get(uri + id + minutesPath)
                .contextPath(contextPath))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.simCardId").value(id))
                .andExpect(jsonPath("$.minutesPacks").isEmpty())
                .andExpect(jsonPath("$.gigabytesPacks").doesNotExist());
    }

    private void getGigabytesPack(int id, Integer amount) throws Exception {
        mvc.perform(get(uri + id + gigabytesPath)
                .contextPath(contextPath))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.simCardId").value(id))
                .andExpect(jsonPath("$.gigabytesPacks[0].amount").value(amount))
                .andExpect(jsonPath("$.minutesPacks").doesNotExist());
    }

    private void getGigabytesPack(int id) throws Exception {
        mvc.perform(get(uri + id + gigabytesPath)
                .contextPath(contextPath))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.simCardId").value(id))
                .andExpect(jsonPath("$.gigabytesPacks").isEmpty())
                .andExpect(jsonPath("$.minutesPacks").doesNotExist());
    }

    private void addMinutesPack(int id, Integer amount) throws Exception {
        mvc.perform(post(uri + id + minutesPath).param("amount", amount.toString())
                .contextPath(contextPath))
                .andExpect(status().isCreated());
    }

    private void addGigabytesPack(int id, Integer amount) throws Exception {
        mvc.perform(post(uri + id + gigabytesPath).param("amount", amount.toString())
                .contextPath(contextPath))
                .andExpect(status().isCreated());
    }

    private void deleteMinutesPack(int id, Integer amount) throws Exception {
        mvc.perform(delete(uri + id + minutesPath).param("amount", amount.toString())
                .contextPath(contextPath))
                .andExpect(status().isNoContent());
    }

    private void deleteGigabytesPack(int id, Integer amount) throws Exception {
        mvc.perform(delete(uri + id + gigabytesPath).param("amount", amount.toString())
                .contextPath(contextPath))
                .andExpect(status().isNoContent());
    }

}
