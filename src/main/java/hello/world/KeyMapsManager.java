package hello.world;

import java.util.HashMap;
import java.util.Map;

import jakarta.inject.Singleton;

@Singleton
public class KeyMapsManager{

    private Map<String,String> keyMaps ;

    public KeyMapsManager() {
        keyMaps = new HashMap<>();
    }

    public void putKeyMap(String url,String url2){
        keyMaps.put(url, url2);
    }

    public String getKeyMap(String url){
        return keyMaps.get(url);
    }

    public Map<String,String> getAllMaps(){
        return keyMaps;
    }

}