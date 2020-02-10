package com.ucsmy.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gaokx on 2016/9/12.
 */
public class JsonUtils {
    public static final ObjectMapper mapper = new ObjectMapper();

    private static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    public static List<Map> jsonToList(String jsonStr) throws IOException {
        return mapper.readValue(jsonStr, new TypeReference<List>() {
        });
    }

    public static List jsonToList(String jsonStr, Class clazz) throws IOException {
        JavaType javaType = getCollectionType(ArrayList.class, clazz);
        return mapper.readValue(jsonStr, javaType);
    }

    public static <E> E jsonToObject(String jsonStr, Class<E> clazz) throws IOException {
        return mapper.readValue(jsonStr, clazz);
    }

    public static JsonNode jsonToJsonNode(String jsonStr) throws IOException {
        return mapper.readTree(jsonStr);
    }

    public static JsonNode objectToJsonNode(Object jsonStr) {
        return mapper.valueToTree(jsonStr);
    }

    public static String formatObjectToJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }

    public static JsonNode formatObjectToJsonNode(Object object) {
        return mapper.valueToTree(object);
    }

    /**
     * * @Title: strJson2Map * @Description: json串转map
     * @param json
     * @return 入参 * @return Map<String,Object> 返回类型 * @author （作者） * @throws *
     * @date 2017-3-9 上午11:49:16 * @version V1.0
     */
    public static Map<String, Object> strJson2Map(String json) {
        try {
            return mapper.readValue(json, new TypeReference<Map<String, Object>>(){});
        } catch (IOException e) {
            return new HashMap<>();
        }
    }

    private static List<Map<String, Object>> json2List(String json) {
        try {
            return mapper.readValue(json, new TypeReference<List<Map<String, Object>>>(){});
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
}
