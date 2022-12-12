package hello.world.yun;

import com.google.gson.JsonObject;
import hello.world.DataService;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;


import static com.google.gson.JsonParser.parseString;

public class YunMessageListener implements YunMessageInterface{

    private final DataService dataService;

    private String _userID;
    private WebSocketClient webSocketClient;

    public YunMessageListener(WebSocketClient ws,DataService dataService){
        this.webSocketClient = ws;
        this.dataService = dataService;
    }

    private void register_data(int cmd,String path){
        JsonObject user = new JsonObject();
        user.addProperty("id", _userID);
        JsonObject reg_obj = new JsonObject();
        reg_obj.add("user",user);
        reg_obj.addProperty("cmd",cmd);
        reg_obj.addProperty("path","yuanda/node"+ path);
        if( webSocketClient != null) {
//            cc.sendFragmentedFrame(Opcode.BINARY, ByteBuffer.wrap(reg_obj.toString().getBytes(StandardCharsets.UTF_8)),true);
            webSocketClient.send(reg_obj.toString());
        }
    }

    @Override
    public void onMessage(String s) {
        JsonObject msg = parseString(s).getAsJsonObject();

        if( msg != null){
            int iCommand = msg.get("command").getAsInt();
            if( iCommand != 14){
                int iCode = msg.get("code").getAsInt();

                if( iCode == 10007  && iCommand == 6){
                    JsonObject obj = msg.getAsJsonObject("data");
                    JsonObject _userObj = obj.getAsJsonObject("user");
                    System.out.println(_userObj);
                    if(!_userObj.isJsonNull()){
                        _userID = _userObj.get("id").getAsString();
                        System.out.println(_userID);
                        register_data(7,"/quote_provider_yun");
                    }

                }
                else if(iCode == 1101 && iCommand == 12){
                    JsonObject obj = msg.getAsJsonObject("data");
                    String value = obj.get("nodeContent").getAsString();
                    String m = value.replace('<','{');
                    m = m.replace('$',':');
                    m = m.replace('>','}');

                    String key = obj.get("nodePath").getAsString();

                    String[] split = key.split("/");

                    dataService.insertOrUpdateData(split[1],split[2],m,true);

//                    String yunRecord = dataService.findByKey(key);
//                    if(yunRecord != null){
//                        JsonElement jsonElement = parseString(m);
//                        JsonElement jsonUpdate = parseString(yunRecord);
//                        Util.combinJson(jsonElement, jsonUpdate );
//                        String s1 = Util.converJsonToString(jsonUpdate);
//                        yunRecordRepository.updateByKeyAndUrl(split[1], split[2],s1);
//                    }
//                    else {
//                        YunRecord save = yunRecordRepository.save(new YunRecord(split[1], split[2], m));
//                        dataService.saveYunRecord(key,m);
//                    }

//                                Optional<YunRecord> byUrlEqualAndKeyEqual = yunRecordRepository.findByUrlEqualAndKeyEqual(split[1], split[2]);
//                                if(byUrlEqualAndKeyEqual.isPresent()){
//                                    JsonElement jsonElement = parseString(m);
//                                    JsonElement jsonUpdate = parseString(byUrlEqualAndKeyEqual.get().getValue());
//                                    Util.combinJson(jsonElement, jsonUpdate );
//                                    yunRecordRepository.update(byUrlEqualAndKeyEqual.get().getId(),Util.converJsonToString(jsonUpdate));
//                                }
//                                else {
//                                    YunRecord save = yunRecordRepository.save(new YunRecord(split[1], split[2], m));
//                                    service.saveYunRecord(key,save);
//                                }

                }
            }
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        webSocketClient = null;
    }

    @Override
    public void onError(Exception e) {
        webSocketClient = null;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {

    }
}
