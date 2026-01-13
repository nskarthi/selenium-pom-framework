package org.selenium.pom.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.selenium.pom.model.BillingModel;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonUtils {
	public static <T> T deserializeJson(String jsonFilename, Class<T> T) throws IOException {
		InputStream is = JacksonUtils.class.getClassLoader().getResourceAsStream(jsonFilename);
		ObjectMapper objectMapper = new ObjectMapper();
        //objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return objectMapper.readValue(is, T);
	}

    public static Map<String, BillingModel> deserializeJson_map(InputStream is) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(is, new TypeReference<Map<String, BillingModel>>() {});
    }
}
