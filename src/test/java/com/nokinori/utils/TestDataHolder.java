package com.nokinori.utils;

import com.nokinori.api.endpoints.mappings.PathMappings;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class TestDataHolder {
    public static Long notExistId = 1001L;

    public static Long wrongId = -1001L;

    public static Integer amount = 100;

    public static Integer wrongAmount = -100;

    public static String contextPath = PathMappings.CONTEXT_PATH;

    public static String simCardPath = contextPath + PathMappings.SIM_CARD_PATH + "/";

    public static String activatePath = PathMappings.SIM_CARD_ACTIVATE_PATH;

    public static String blockPath = PathMappings.SIM_CARD_BLOCK_PATH;

    public static String minutesPath = PathMappings.MINUTES_PATH;

    public static String gigabytesPath = PathMappings.GIGABYTES_PATH;

}