package hello.world;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.gson.JsonElement;
import hello.world.jdbc.dto.YunRecordRepository;
import hello.world.jdbc.entity.YunRecord;
import hello.world.util.Util;
import io.micronaut.scheduling.annotation.Scheduled;
import org.java_websocket.enums.Opcode;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RRingBuffer;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RSetMultimapCache;
import org.redisson.api.RedissonClient;
import org.redisson.api.LocalCachedMapOptions.EvictionPolicy;
import org.redisson.api.LocalCachedMapOptions.ReconnectionStrategy;
import org.redisson.api.LocalCachedMapOptions.StoreMode;
import org.redisson.api.MapOptions.WriteMode;

import hello.world.nats.NATSMessage;

import io.micronaut.context.annotation.Context;
import io.micronaut.context.annotation.Parallel;
import jakarta.inject.Inject;


import com.google.gson.JsonObject;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import static com.google.gson.JsonParser.parseString;


@Context
@Parallel
public class DataService {

    private AtomicBoolean bWebsocketInit = new AtomicBoolean(false);
    private String _userID;
    private WebSocketClient cc;

    @Inject
    private RedissonClient redisson;

    @Inject
    private YunRecordRepository yunRecordRepository;

    @Inject
    NATSMessage natsMessage;

    RScoredSortedSet<String> scoredSortedSet;
    LocalCachedMapOptions<String, String> localCachedMapOptions;
    RLocalCachedMap<String, String> localCachedMap;
    RRingBuffer<String> buffer;

    // UsersService usersService ;

    public DataService(RedissonClient redisson) {
        this.redisson = redisson;
        this.scoredSortedSet = redisson.getScoredSortedSet("hello");
        this.localCachedMapOptions = LocalCachedMapOptions.<String,String>defaults()
        .cacheSize(1000)
        // .cacheProvider(CacheProvider.CAFFEINE)
        .storeMode(StoreMode.LOCALCACHE_REDIS)
        .writeBehindBatchSize(10000)
        .writeBehindDelay(100)
        .storeCacheMiss(true)
        .writeMode(WriteMode.WRITE_BEHIND)
        // .writerAsync(new MapWriterAsync<String,String>() {

        //     @Override
        //     public CompletionStage<Void> write(Map<String, String> map) {
        //         // TODO Auto-generated method stub

        //         try {
        //             System.out.println("MapWrite" + Integer.toString(map.size()));
        //             mySQL.putAll(map);
        //             return CompletableFuture.completedFuture(null);
        //         } catch (SQLException e) {
        //             // TODO Auto-generated catch block
        //             e.printStackTrace();
        //         };
        //         return CompletableFuture.completedFuture(null);
        //     }

        //     @Override
        //     public CompletionStage<Void> delete(Collection<String> keys) {
        //         // TODO Auto-generated method stub
        //         return null;
        //     }
            
        // })
        // .writer(new MapWriter<String,String>() {

        //     @Override
        //     public void write(Map<String, String> map) {
        //         // TODO Auto-generated method stub
        //         System.out.println("MapWrite" + Integer.toString(map.size()));
        //         // for (String string : map.keySet()) {
        //         //     natsMessage.send(string.getBytes());
        //         // }

        //         // try {
        //         //     mySQL.putAll(map);
        //         // } catch (SQLException e) {
        //         //     // TODO Auto-generated catch block
        //         //     e.printStackTrace();
        //         // };

        //     }

        //     @Override
        //     public void delete(Collection<String> keys) {
        //         // TODO Auto-generated method stub
                
        //     }
            
        // })
        // .loader(new MapLoader<String,String>() {

        //     @Override
        //     public String load(String key) {
        //         // TODO Auto-generated method stub
        //         return null;
        //     }

        //     @Override
        //     public Iterable<String> loadAllKeys() {
        //         // TODO Auto-generated method stub
        //         return null;
        //     }
            
        // })
        .reconnectionStrategy(ReconnectionStrategy.LOAD)
        .evictionPolicy(EvictionPolicy.LRU)
        .timeToLive(10000, TimeUnit.SECONDS);

        this.localCachedMap = redisson.getLocalCachedMap("testMap", new org.redisson.codec.JsonJacksonCodec(),this.localCachedMapOptions);

        // try {
        //     System.out.println(redisson.getConfig().toYAML());
        // } catch (IOException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
        // usersService = new UsersService();

        buffer = redisson.getRingBuffer("buff");
        buffer.setCapacity(10000);
    }

    public String getAll(){

        scoredSortedSet.add(10, "hello");
        scoredSortedSet.add(9, "world9");
        scoredSortedSet.add(8, "world8");
        scoredSortedSet.add(7, "world7");
        scoredSortedSet.add(6, "world6");
        scoredSortedSet.add(5, "world5");
  
        // Collection<String> readAll = scoredSortedSet.readAll();

        // for (String string : readAll) {
        //     // System.out.println(string);
        // }

        String str = "example";

        Collection<String> valueRange2 = scoredSortedSet.valueRange(0, 5);
        for (String string : valueRange2) {
            str += string;
            str += "  ";            
        }


        Collection<String> valueRange = scoredSortedSet.valueRangeReversed(0, 5);

        for (String string : valueRange) {
            str += string;
            str += "  ";  
        }

        scoredSortedSet.clear();

        str += Integer.toString(scoredSortedSet.size());

        return str;        
    }

    public String getAllMultimap(){

        RSetMultimapCache<String, String> setMultimapCache = redisson.getSetMultimapCache("myMultiMap");

        setMultimapCache.put("1", "a");
        setMultimapCache.put("1", "b");
        setMultimapCache.put("1", "c");
        setMultimapCache.put("1", "d");

        setMultimapCache.expireKey("1", 10, TimeUnit.SECONDS);

        Set<String> all = setMultimapCache.getAll("1");
        
        return all.toString();


    }

    public String getMultimap(){
        RSetMultimapCache<String, String> setMultimapCache = redisson.getSetMultimapCache("myMultiMap");

           
        Set<String> all = setMultimapCache.getAll("1");

        
        this.localCachedMap.put("hello", "World");
        
        return all.toString();
    }

    public int getAllBuff(){

        return buffer.size();
    }
    public String saveRedisPerson(String name,int age){        
        String key = "hello" + Long.toString(System.nanoTime());
        this.localCachedMap.put( key, "World" + name + Integer.toString(age));
        // buffer.add(key);
        
        return "OK";
    }


    //yuandayun

    private void extracted() {
        System.out.println("================================================================");
        bWebsocketInit.set(false);
        try {
            cc = new WebSocketClient(new URI("wss://hqyun.ydtg.com.cn?username=abc&password=123"), new Draft_6455()) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    System.out.println("onOPen=======================================");
                }

                @Override
                public void onMessage(String s) {
//                    System.out.println(s);
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

//                                System.out.println( key + " : "  + value);
                                String[] split = key.split("/");
//                                System.out.println(split[0] + "::::"+split[1]);
                                Optional<YunRecord> byUrlEqualAndKeyEqual = yunRecordRepository.findByUrlEqualAndKeyEqual(split[1], split[2]);
                                if(byUrlEqualAndKeyEqual.isPresent()){
                                    JsonElement jsonElement = parseString(m);
                                    JsonElement jsonUpdate = parseString(byUrlEqualAndKeyEqual.get().getValue());
                                    Util.combinJson(jsonElement, jsonUpdate );
                                    yunRecordRepository.updateById(byUrlEqualAndKeyEqual.get().getId(),Util.converJsonToString(jsonUpdate));
                                }
                                else {
                                    yunRecordRepository.save(new YunRecord(split[1],split[2],m));
                                }

                            }
                        }
                    }

                }
//                @Override
//                public void onMessage(ByteBuffer bytes) {
//                    System.out.println("ByteBuffer:" + bytes);
//                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    System.out.println(s);
                    bWebsocketInit.set(false);
                }

                @Override
                public void onError(Exception e) {
//                        System.out.println(e);
                }
            };

            cc.connect();
        } catch (URISyntaxException ex){
            System.out.println(ex);
        }
        bWebsocketInit.set(true);
    }

    private void register_data(int cmd,String path){
        JsonObject user = new JsonObject();
        user.addProperty("id", _userID);
        JsonObject reg_obj = new JsonObject();
        reg_obj.add("user",user);
        reg_obj.addProperty("cmd",cmd);
        reg_obj.addProperty("path","yuanda/node"+ path);
        if( cc != null) {
//            cc.sendFragmentedFrame(Opcode.BINARY, ByteBuffer.wrap(reg_obj.toString().getBytes(StandardCharsets.UTF_8)),true);
            cc.send(reg_obj.toString());
        }
    }

//    @Scheduled(fixedDelay = "10s")
    public void check_connection(){
        JsonObject user = new JsonObject();
        user.addProperty("cmd",13);
        user.addProperty("hbbyte",-127);
        if( cc != null &&  !cc.isClosed() && cc.isOpen()){
            cc.sendFragmentedFrame(Opcode.BINARY, ByteBuffer.wrap(user.toString().getBytes()),true);
        }
        else{
            if( !bWebsocketInit.get())
                extracted();
        }
    }
}
