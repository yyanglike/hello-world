package hello.world.yun;

import org.java_websocket.handshake.ServerHandshake;

public interface YunMessageInterface {
    void register_data(int cmd,String path);

    void onMessage(String s);

    void onClose(int i, String s, boolean b);

    void onError(Exception e);

    void onOpen(ServerHandshake serverHandshake);
}
