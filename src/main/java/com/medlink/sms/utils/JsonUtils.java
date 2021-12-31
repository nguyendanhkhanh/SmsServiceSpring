package com.medlink.sms.utils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class JsonUtils {
    private static final Logger log = LogManager.getLogger(JsonUtils.class);

    @SuppressWarnings("unchecked")
    public static <T> T convertJsonToObject(String value,final TypeReference<T> typeReference) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return (T)mapper.readValue(value, typeReference);
        } catch (JsonParseException e) {
            log.error(e.getMessage(),e);
        } catch (JsonMappingException e) {
            log.error(e.getMessage(),e);
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
        return null;
    }

    public static String convertObjectToJson(Object strJsons){
        String jsons;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            jsons = objectMapper.writeValueAsString(strJsons);
            return jsons;
        } catch (JsonGenerationException e) {
            log.error(e.getMessage(),e);
        } catch (JsonMappingException e) {
            log.error(e.getMessage(),e);
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
        return null;
    }
}
