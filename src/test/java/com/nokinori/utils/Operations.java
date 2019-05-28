package com.nokinori.utils;

import com.jayway.jsonpath.JsonPath;
import lombok.Setter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.nokinori.utils.JsonExpressions.AMOUNT_PARAMETER;
import static com.nokinori.utils.JsonExpressions.SIM_CARD_ID;
import static com.nokinori.utils.TestDataHolder.activatePath;
import static com.nokinori.utils.TestDataHolder.blockPath;
import static com.nokinori.utils.TestDataHolder.contextPath;
import static com.nokinori.utils.TestDataHolder.gigabytesPath;
import static com.nokinori.utils.TestDataHolder.minutesPath;
import static com.nokinori.utils.TestDataHolder.simCardPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class Operations {

    @Setter
    private MockMvc mvc;

    public Integer createSimCard() throws Exception {
        MvcResult mvcResult = mvc.perform(post(simCardPath).contextPath(contextPath))
                .andExpect(status().isCreated())
                .andExpect(jsonPath(SIM_CARD_ID).isNotEmpty())
                .andExpect(jsonPath(JsonExpressions.MINUTES_PACKS).isEmpty())
                .andExpect(jsonPath(JsonExpressions.GIGABYTES_PACKS).isEmpty())
                .andReturn();

        String body = mvcResult.getResponse()
                .getContentAsString();

        return JsonPath.parse(body)
                .read(SIM_CARD_ID);
    }

    public void blockSimCard(int id) throws Exception {
        mvc.perform(put(simCardPath + id + blockPath)
                .contextPath(contextPath))
                .andExpect(status().isOk());
    }

    public void activateSimCard(int id) throws Exception {
        mvc.perform(put(simCardPath + id + activatePath)
                .contextPath(contextPath))
                .andExpect(status().isOk());
    }

    public void getMinutesPack(int id, Integer amountToValidate) throws Exception {
        mvc.perform(get(simCardPath + id + minutesPath)
                .contextPath(contextPath))
                .andExpect(status().isOk())
                .andExpect(jsonPath(SIM_CARD_ID).value(id))
                .andExpect(jsonPath(JsonExpressions.MINUTES_PACKS + "[0].amount").value(amountToValidate))
                .andExpect(jsonPath(JsonExpressions.GIGABYTES_PACKS).doesNotExist());
    }

    public void getMinutesPack(int id) throws Exception {
        mvc.perform(get(simCardPath + id + minutesPath)
                .contextPath(contextPath))
                .andExpect(status().isOk())
                .andExpect(jsonPath(SIM_CARD_ID).value(id))
                .andExpect(jsonPath(JsonExpressions.MINUTES_PACKS).isEmpty())
                .andExpect(jsonPath(JsonExpressions.GIGABYTES_PACKS).doesNotExist());
    }

    public void getGigabytesPack(int id, Integer amountToValidate) throws Exception {
        mvc.perform(get(simCardPath + id + gigabytesPath)
                .contextPath(contextPath))
                .andExpect(status().isOk())
                .andExpect(jsonPath(SIM_CARD_ID).value(id))
                .andExpect(jsonPath(JsonExpressions.GIGABYTES_PACKS + "[0].amount").value(amountToValidate))
                .andExpect(jsonPath(JsonExpressions.MINUTES_PACKS).doesNotExist());
    }

    public void getGigabytesPack(int id) throws Exception {
        mvc.perform(get(simCardPath + id + gigabytesPath)
                .contextPath(contextPath))
                .andExpect(status().isOk())
                .andExpect(jsonPath(SIM_CARD_ID).value(id))
                .andExpect(jsonPath(JsonExpressions.GIGABYTES_PACKS).isEmpty())
                .andExpect(jsonPath(JsonExpressions.MINUTES_PACKS).doesNotExist());
    }

    public void addMinutesPack(int id, Integer amount) throws Exception {
        mvc.perform(post(simCardPath + id + minutesPath).param(AMOUNT_PARAMETER, amount.toString())
                .contextPath(contextPath))
                .andExpect(status().isCreated());
    }

    public void addGigabytesPack(int id, Integer amount) throws Exception {
        mvc.perform(post(simCardPath + id + gigabytesPath).param(AMOUNT_PARAMETER, amount.toString())
                .contextPath(contextPath))
                .andExpect(status().isCreated());
    }

    public void deleteMinutesPack(int id, Integer amount) throws Exception {
        mvc.perform(delete(simCardPath + id + minutesPath).param(AMOUNT_PARAMETER, amount.toString())
                .contextPath(contextPath))
                .andExpect(status().isNoContent());
    }

    public void deleteGigabytesPack(int id, Integer amount) throws Exception {
        mvc.perform(delete(simCardPath + id + gigabytesPath).param(AMOUNT_PARAMETER, amount.toString())
                .contextPath(contextPath))
                .andExpect(status().isNoContent());
    }
}
