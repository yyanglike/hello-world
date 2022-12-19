package hello.world.yun;

import hello.world.DataService;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import org.java_websocket.drafts.Draft_6455;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class YunManager {
    private Map<String,YunMessageListener> addressMapListener;
    private Map<String,YunWebSocket> addressMapWebsocket;
    public YunManager() {
        addressMapListener = new HashMap<>();
        addressMapWebsocket = new HashMap<>();
    }

    public void put(String address,YunMessageListener listener, YunWebSocket webSocket){
        addressMapListener.put(address,listener);
        addressMapWebsocket.put(address,webSocket);
    }

    public void register_data(String url,String node,DataService dataService){
        if (addressMapWebsocket.get(url) == null){
            try {
                YunWebSocket cc = new YunWebSocket(new URI(url), new Draft_6455()) ;
                YunMessageListener yunMessageListener = new YunMessageListener(cc,dataService);
                cc.setListener(yunMessageListener);
                cc.connect();
                yunMessageListener.register_data(7,node);
                put(url,yunMessageListener,cc);
            } catch (URISyntaxException ex){
                System.out.println(ex);
            }
        }
        else{
            addressMapListener.get(url).register_data(7,node);
        }
    }

    @Scheduled(fixedDelay = "10s")
    void timedCheckConnections(){
        for (String s : addressMapWebsocket.keySet()) {
            YunWebSocket ws = addressMapWebsocket.get(s);
            if( !ws.check_connection() ){
                try {
                    addressMapWebsocket.remove(s);
                    YunWebSocket cc = new YunWebSocket(new URI(s), new Draft_6455()) ;
                    YunMessageListener yunMessageInterface = addressMapListener.get(s);
                    yunMessageInterface.setWebSocketClient(cc);
                    cc.setListener(yunMessageInterface);
                    cc.connect();
                    addressMapWebsocket.putIfAbsent(s,cc);
                } catch (URISyntaxException ex){
                    System.out.println(ex);
                }
            }
        }
    }

}
