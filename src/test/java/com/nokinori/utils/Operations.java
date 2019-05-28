package com.nokinori.utils;

import com.jayway.jsonpath.JsonPath;
import lombok.Setter;
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

@Setter
public class Operations {


    private final String contextPath = CONTEXT_PATH;

    private final String uri = contextPath + SIM_CARD_PATH + "/";

    private final String activatePath = SIM_CARD_ACTIVATE_PATH;

    private final String blockPath = SIM_CARD_BLOCK_PATH;

    private final String minutesPath = MINUTES_PATH;

    private final String gigabytesPath = GIGABYTES_PATH;

    private MockMvc mvc;

    public Integer createSimCard() throws Exception {
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

    public void blockSimCard(int id) throws Exception {
        mvc.perform(put(uri + id + blockPath)
                .contextPath(contextPath))
                .andExpect(status().isOk());
    }

    public void activateSimCard(int id) throws Exception {
        mvc.perform(put(uri + id + activatePath)
                .contextPath(contextPath))
                .andExpect(status().isOk());
    }

    public void getMinutesPack(int id, Integer amountToValidate) throws Exception {
        mvc.perform(get(uri + id + minutesPath)
                .contextPath(contextPath))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.simCardId").value(id))
                .andExpect(jsonPath("$.minutesPacks[0].amount").value(amountToValidate))
                .andExpect(jsonPath("$.gigabytesPacks").doesNotExist());
    }

    public void getMinutesPack(int id) throws Exception {
        mvc.perform(get(uri + id + minutesPath)
                .contextPath(contextPath))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.simCardId").value(id))
                .andExpect(jsonPath("$.minutesPacks").isEmpty())
                .andExpect(jsonPath("$.gigabytesPacks").doesNotExist());
    }

    public void getGigabytesPack(int id, Integer amountToValidate) throws Exception {
        mvc.perform(get(uri + id + gigabytesPath)
                .contextPath(contextPath))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.simCardId").value(id))
                .andExpect(jsonPath("$.gigabytesPacks[0].amount").value(amountToValidate))
                .andExpect(jsonPath("$.minutesPacks").doesNotExist());
    }

    public void getGigabytesPack(int id) throws Exception {
        mvc.perform(get(uri + id + gigabytesPath)
                .contextPath(contextPath))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.simCardId").value(id))
                .andExpect(jsonPath("$.gigabytesPacks").isEmpty())
                .andExpect(jsonPath("$.minutesPacks").doesNotExist());
    }

    public void addMinutesPack(int id, Integer amount) throws Exception {
        mvc.perform(post(uri + id + minutesPath).param("amount", amount.toString())
                .contextPath(contextPath))
                .andExpect(status().isCreated());
    }

    public void addGigabytesPack(int id, Integer amount) throws Exception {
        mvc.perform(post(uri + id + gigabytesPath).param("amount", amount.toString())
                .contextPath(contextPath))
                .andExpect(status().isCreated());
    }

    public void deleteMinutesPack(int id, Integer amount) throws Exception {
        mvc.perform(delete(uri + id + minutesPath).param("amount", amount.toString())
                .contextPath(contextPath))
                .andExpect(status().isNoContent());
    }

    public void deleteGigabytesPack(int id, Integer amount) throws Exception {
        mvc.perform(delete(uri + id + gigabytesPath).param("amount", amount.toString())
                .contextPath(contextPath))
                .andExpect(status().isNoContent());
    }
}
