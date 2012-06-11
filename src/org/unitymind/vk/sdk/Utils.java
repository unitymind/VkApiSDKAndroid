package org.unitymind.vk.sdk;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.RequestParams;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Utils {
    static Gson gson = new Gson();

    public static HashMap<String, Object> parse(String json) {
        JsonParser parser = new JsonParser();
        JsonObject object = (JsonObject) parser.parse(json);
        Set<Map.Entry<String, JsonElement>> set = object.entrySet();
        Iterator<Map.Entry<String, JsonElement>> iterator = set.iterator();
        HashMap<String, Object> map = new HashMap<String, Object>();
        while (iterator.hasNext()) {
            Map.Entry<String, JsonElement> entry = iterator.next();
            String key = entry.getKey();
            JsonElement value = entry.getValue();
            if (!value.isJsonPrimitive()) {
                map.put(key, parse(value.toString()));
            } else {
                map.put(key, value.getAsString());
            }
        }
        return map;
    }

    public static RequestParams paramsFromJson(String json) {
        RequestParams params = new RequestParams();

        Map<String, Object> api_params = Utils.parse(json);

        for (Map.Entry<String, Object> entry : api_params.entrySet()) {
            params.put(entry.getKey(), entry.getValue().toString());
        }

        return params;
    }

    public static String toJson(Map<String, String> map) {
        return gson.toJson(map);
    }
}
