package android.octopu.wifi.utils;


import com.flyzebra.utils.FlyLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsonTools {

    public static String MapToJsonString(Map<?, ?> map) throws JSONException {
        JSONObject jsonObject = new JSONObject(map);
        return jsonObject.toString();
    }

    /**
     * 将转换成的List添加到list中
     *
     * @param list
     * @param jsonObject
     * @param jsonKey
     */
    public static void addList(List<Map<String, Object>> list, JSONObject jsonObject, String jsonKey) {
        try {
            JSONArray jsonArray = null;
            if (jsonKey == null) {
                jsonArray = new JSONArray(jsonObject.toString());
            } else {
                jsonArray = jsonObject.getJSONArray(jsonKey);
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                Map<String, Object> map = new HashMap<String, Object>();
                Iterator<String> iterator = jsonObject2.keys();
                while (iterator.hasNext()) {
                    String json_key = iterator.next();
                    Object json_value = jsonObject2.get(json_key);
                    if (json_value == null) {
                        json_value = "";
                    }
                    map.put(json_key, json_value);
                }
                list.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回jsonObject转换成的list
     *
     * @param jsonObject
     * @param jsonKey
     * @return
     */
    public static List<Map<String, Object>> addList(JSONObject jsonObject, String jsonKey) {
        List list = new ArrayList();
        JSONArray jsonArray;
        try {
            if (jsonKey == null) {
                jsonArray = new JSONArray(jsonObject.toString());
            } else {
                jsonArray = jsonObject.getJSONArray(jsonKey);
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                Map<String, Object> map = new HashMap<String, Object>();
                Iterator<String> iterator = jsonObject2.keys();
                while (iterator.hasNext()) {
                    String json_key = iterator.next();
                    Object json_value = jsonObject2.get(json_key);
                    if (json_value == null) {
                        json_value = "";
                    }
                    map.put(json_key, json_value);
                }
                list.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<Map<String, Object>> addList(JSONArray jsonArray) {
        List list = new ArrayList();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                Map<String, Object> map = new HashMap<String, Object>();
                Iterator<String> iterator = jsonObject2.keys();
                while (iterator.hasNext()) {
                    String json_key = iterator.next();
                    Object json_value = jsonObject2.get(json_key);
                    if (json_value == null) {
                        json_value = "";
                    }
                    map.put(json_key, json_value);
                }
                list.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static Map<String, Object> getMap(String jsonList, String jsonKey) {
        Map<String, Object> map = null;
        try {
            JSONObject jsonObject = null;
            if (jsonKey == null) {
                jsonObject = new JSONObject(jsonList);
            } else {
                jsonObject = new JSONObject(jsonList).getJSONObject(jsonKey);
            }
            Iterator<String> iterator = jsonObject.keys();
            map = new HashMap<String, Object>();
            while (iterator.hasNext()) {
                String json_key = iterator.next();
                Object json_value = jsonObject.getInt(json_key);
                if (json_value == null) {
                    json_value = "";
                }
                map.put(json_key, json_value);
            }
        } catch (JSONException e) {
            FlyLog.i("JsonTools->getMap-->JSONException:" + e.toString());
            e.printStackTrace();
        }
        return map;
    }


    public static Object jsonArrayToObject(Class<?> cls, String jsonString) {
        Object obj = null;
        try {
            obj = cls.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Iterator<String> iterator = jsonObject.keys();
                while (iterator.hasNext()) {
                    String json_key = iterator.next();
                    Object json_value = jsonObject.get(json_key);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
