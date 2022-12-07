package hello.world.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

public class Util {
    private static Gson gson = new Gson();

    public static JsonElement combinJson(JsonElement e, JsonElement eUpdate){
        if(e.isJsonArray()){
            JsonArray jsonArray = e.getAsJsonArray();
            JsonArray eUpdateArray = eUpdate.getAsJsonArray();
            if(eUpdateArray == null){
                return jsonArray;
            }
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonElement element = jsonArray.get(i);
                JsonElement jsonElement = combinJson(jsonArray.get(i), eUpdateArray.get(i));
                jsonArray.set(i,jsonElement);
            }
        }
        if(e.isJsonPrimitive()){
            eUpdate = e;
            return e;
        }
        if(e.isJsonObject()){
            final JsonObject jsonObject = gson.toJsonTree(e).getAsJsonObject();
            for(Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                JsonElement eUpdate1 = eUpdate.getAsJsonObject().get(entry.getKey());
                if(!(eUpdate1 == null)){
                    eUpdate1 = combinJson(entry.getValue(),eUpdate1);
                    eUpdate.getAsJsonObject().asMap().putIfAbsent(entry.getKey(),eUpdate1);
                    eUpdate.getAsJsonObject().add(entry.getKey(),eUpdate1);
                }
                else {
                    eUpdate.getAsJsonObject().add(entry.getKey(), entry.getValue());
                }
//                System.out.println("Key = " + entry.getKey() + " Value = " + entry.getValue() );
            }
        }
        return e;
    }

    public static String converJsonToString(JsonElement json){
        return gson.toJson(json);
    }
}
