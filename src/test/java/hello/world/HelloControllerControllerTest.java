package hello.world;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import hello.world.jdbc.dto.YunRecordRepository;
import hello.world.util.Util;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import io.micronaut.http.client.annotation.*;
import jakarta.inject.Inject;

import java.util.Map;

import static com.google.gson.JsonParser.parseString;
import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class HelloControllerControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Inject
    YunRecordRepository yunRecordRepository;
    private Gson gson = new Gson();



    @Test
    public void testIndex() throws Exception {
        assertEquals(HttpStatus.OK, client.toBlocking().exchange("/helloController").status());
    }


    public JsonElement combinJson(JsonElement e, JsonElement eUpdate){
        if(e.isJsonArray()){
            JsonArray jsonArray = e.getAsJsonArray();
            JsonArray eUpdateArray = eUpdate.getAsJsonArray();
            if(eUpdateArray == null){
                return jsonArray;
            }
            for (int i = 0; i < jsonArray.size(); i++) {
                combinJson(jsonArray.get(i),eUpdateArray.get(i));
            }
        }
        if(e.isJsonPrimitive()){
            eUpdate = e.deepCopy() ;
            return e;
        }
        if(e.isJsonObject()){
            final JsonObject jsonObject = gson.toJsonTree(e).getAsJsonObject();
            for(Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                JsonElement eUpdate1 = eUpdate.getAsJsonObject().get(entry.getKey());
                if(!(eUpdate1 == null)){
                    eUpdate1 = combinJson(entry.getValue(),eUpdate1);
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

    @Test
    public void testGson(){

//        /*
//        {"width":"50%","dataElement":"name","isPresent":true,"day":56,"header":"rum.table.column.txnname","results":{"totalCT":393,"name":"/api/uptime_monitor/*","avgRT":467.35,"throughput":6.66,"totalRT":183668}}
//         */
//        Optional<YunRecord> record = yunRecordRepository.findById(3L);
//        if(record.isPresent()){
//            String str = record.get().getValue();
//            String msg = str.replace('<','{');
//            msg = msg.replace('>','}');
//            msg = msg.replace('$',':');
//
//            JsonElement jsonElement = parseString(msg);
//            JsonElement jsonUpdate = jsonElement.deepCopy();
//            jsonUpdate.getAsJsonObject().remove("header");
//            jsonUpdate.getAsJsonObject().remove("results");
//            testJson(jsonElement,jsonUpdate);
//
//            System.out.println(jsonElement);
//            System.out.println("JsonUpdate:"+jsonUpdate);
//
//
//        }


        JsonElement jsonUpdate = parseString("[{'b':2,'c':5}]");
        JsonElement jsonElement = parseString("[{'b':5}]");

//        Util.combinJson(jsonElement,jsonUpdate);
        combinJson(jsonElement,jsonUpdate);

        System.out.println(jsonElement);
        System.out.println("JsonUpdate:"+jsonUpdate);

    }

}
