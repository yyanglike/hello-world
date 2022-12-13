package hello.world.yun;

import com.google.gson.JsonObject;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.enums.Opcode;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;

public class YunWebSocket extends WebSocketClient {

    private  YunMessageInterface listener;

    public void setListener(YunMessageInterface listener){
        this.listener = listener;
    }

    public YunWebSocket(URI serverUri) {
        super(serverUri);
    }

    public YunWebSocket(URI serverUri, Draft protocolDraft) {
        super(serverUri, protocolDraft);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        listener.onOpen(serverHandshake);
    }

    @Override
    public void onMessage(String s) {
        listener.onMessage(s);
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        listener.onClose(i,s,b);
    }

    @Override
    public void onError(Exception e) {
        listener.onError(e);
    }

    public boolean check_connection() {
        JsonObject user = new JsonObject();
        user.addProperty("cmd", 13);
        user.addProperty("hbbyte", -127);
        if (this != null && !this.isClosed() && this.isOpen()) {
            this.sendFragmentedFrame(Opcode.BINARY, ByteBuffer.wrap(user.toString().getBytes()), true);
            return true;
        }
        else {
            return false;
        }
    }
}
