package hello.world;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

import com.github.benmanes.caffeine.cache.Cache;
import com.google.gson.JsonElement;
import hello.world.jdbc.dto.YunRecordRepository;
import hello.world.jdbc.entity.YunRecord;
import hello.world.util.Util;
import hello.world.yun.HttpQueryObject;
import hello.world.yun.YunManager;
import io.micronaut.cache.ehcache.EhcacheSyncCache;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpVersion;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.scheduling.annotation.Scheduled;
import io.micronaut.serde.ObjectMapper;
import org.ehcache.CacheManager;

import io.micronaut.cache.SyncCache;
import io.micronaut.cache.caffeine.DefaultDynamicCacheManager;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;


import hello.world.util.MultiMap;
import reactor.core.publisher.Flux;

import static com.google.gson.JsonParser.parseString;
import static io.micronaut.http.HttpRequest.GET;
import static io.micronaut.http.HttpRequest.POST;

@Singleton
public class DataService {
//    @Inject
//    private RedissonClient redisson;

    private static final int HttpRequest = 0;

    @Inject
    private YunRecordRepository yunRecordRepository;

    @Inject
    YunManager yunManager;

//    @Inject
//    @Named("my-Cache")
//    SyncCache<Cache<String,String>> syncCache;

    @Inject
    @Named("mulkeys")
    SyncCache<Cache<String,Collection<String>>> syncCache1;

//    private final CacheManager cacheManager;

    @Inject
    @Named("a-cache")
    EhcacheSyncCache ehcacheSyncCache;

    @Inject
    ObjectMapper objectMapper;

    @Inject
    DefaultDynamicCacheManager defaultDynamicCacheManager;

    @Client("https://hqyun.ydtg.com.cn") @Inject HttpClient httpClient;

    // String url = "https://hqyun.ydtg.com.cn/node/sdkapi?token=sgdsgdsikhuewikdnjlkgh";

//    RScoredSortedSet<String> scoredSortedSet;
//    LocalCachedMapOptions<String,String> localCachedMapOptions;
//    RLocalCachedMap<String, String> localCachedMap;
//    RLocalCachedMap<String,String> localYunRecordCachedMap;

    MultiMap<String,String> multiMapCache;
    Map<String,String> map_keys;

//    RRingBuffer<String> buffer;

    //ehcache
    org.ehcache.Cache<String, String> cache;

    public DataService( CacheManager cacheManager) {
//        this.redisson = redisson;
//        this.scoredSortedSet = redisson.getScoredSortedSet("hello");
//        this.localCachedMapOptions = LocalCachedMapOptions.<String,String>defaults()
//        .cacheSize(10000)
//        // .cacheProvider(CacheProvider.CAFFEINE)
//        .storeMode(StoreMode.LOCALCACHE_REDIS)
//        .writeBehindBatchSize(5000)
//        .writeBehindDelay(100)
//        .storeCacheMiss(true)
//        .writeMode(WriteMode.WRITE_BEHIND)
//        // .writerAsync(new MapWriterAsync<String,String>() {
//
//        //     @Override
//        //     public CompletionStage<Void> write(Map<String, String> map) {
//        //         try {
//        //             System.out.println("MapWrite" + Integer.toString(map.size()));
//        //             mySQL.putAll(map);
//        //             return CompletableFuture.completedFuture(null);
//        //         } catch (SQLException e) {
//        //             e.printStackTrace();
//        //         };
//        //         return CompletableFuture.completedFuture(null);
//        //     }
//
//        //     @Override
//        //     public CompletionStage<Void> delete(Collection<String> keys) {
//        //         return null;
//        //     }
//
//        // })
//        // .writer(new MapWriter<String,String>() {
//
//        //     @Override
//        //     public void write(Map<String, String> map) {
//        //         System.out.println("MapWrite" + Integer.toString(map.size()));
//        //         // for (String string : map.keySet()) {
//        //         //     natsMessage.send(string.getBytes());
//        //         // }
//
//        //         // try {
//        //         //     mySQL.putAll(map);
//        //         // } catch (SQLException e) {
//        //         //     e.printStackTrace();
//        //         // };
//
//        //     }
//
//        //     @Override
//        //     public void delete(Collection<String> keys) {
//
//        //     }
//
//        // })
//        // .loader(new MapLoader<String,String>() {
//
//        //     @Override
//        //     public String load(String key) {
//        //         return null;
//        //     }
//
//        //     @Override
//        //     public Iterable<String> loadAllKeys() {
//        //         return null;
//        //     }
//
//        // })
//        .reconnectionStrategy(ReconnectionStrategy.LOAD)
//        .evictionPolicy(EvictionPolicy.LRU)
//        .timeToLive(10000, TimeUnit.SECONDS);
//
//        this.localCachedMap = redisson.getLocalCachedMap("testMap", new org.redisson.codec.JsonJacksonCodec(),this.localCachedMapOptions);
//        this.localYunRecordCachedMap = redisson.getLocalCachedMap("testMap", new org.redisson.codec.JsonJacksonCodec(),this.localCachedMapOptions);
        multiMapCache = new MultiMap<>();

        // try {
        //     System.out.println(redisson.getConfig().toYAML());
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
        // usersService = new UsersService();

//        buffer = redisson.getRingBuffer("buff");
//        buffer.setCapacity(10000);

//        this.cacheManager = cacheManager;
//
//        this.cache= cacheManager.createCache("yun-cache", CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class
//                , ResourcePoolsBuilder.heap(100).offheap(10, MemoryUnit.MB)));

        map_keys = new HashMap<>();
        map_keys.put("/quote_provider_yun","/quote_provider_yun");
        map_keys.put("/fund_flow_yun/fund-data","/quote_provider_yun");
    }

//    public String getAll(){
//
//        scoredSortedSet.add(10, "hello");
//        scoredSortedSet.add(9, "world9");
//        scoredSortedSet.add(8, "world8");
//        scoredSortedSet.add(7, "world7");
//        scoredSortedSet.add(6, "world6");
//        scoredSortedSet.add(5, "world5");
//
//        // Collection<String> readAll = scoredSortedSet.readAll();
//
//        // for (String string : readAll) {
//        //     // System.out.println(string);
//        // }
//
//        String str = "example";
//
//        Collection<String> valueRange2 = scoredSortedSet.valueRange(0, 5);
//        for (String string : valueRange2) {
//            str += string;
//            str += "  ";
//        }
//
//
//        Collection<String> valueRange = scoredSortedSet.valueRangeReversed(0, 5);
//
//        for (String string : valueRange) {
//            str += string;
//            str += "  ";
//        }
//
//        scoredSortedSet.clear();
//
//        str += Integer.toString(scoredSortedSet.size());
//
//        return str;
//    }

//    public String getAllMultimap(){
//
//        RSetMultimapCache<String, String> setMultimapCache = redisson.getSetMultimapCache("myMultiMap");
//
//        setMultimapCache.put("1", "a");
//        setMultimapCache.put("1", "b");
//        setMultimapCache.put("1", "c");
//        setMultimapCache.put("1", "d");
//
//        setMultimapCache.expireKey("1", 10, TimeUnit.SECONDS);
//
//        Set<String> all = setMultimapCache.getAll("1");
//
//        return all.toString();
//
//
//    }

//    public String getMultimap(){
//        RSetMultimapCache<String, String> setMultimapCache = redisson.getSetMultimapCache("myMultiMap");
//
//
//        Set<String> all = setMultimapCache.getAll("1");
//
//
//        this.localCachedMap.put("hello", "World");
//
//        return all.toString();
//    }

//    public int getAllBuff(){
//
//        return buffer.size();
//    }
//    public String saveRedisPerson(String name,int age){
//        String key = "hello" + Long.toString(System.nanoTime());
//        this.localCachedMap.put( key, "World" + name + Integer.toString(age));
//        // buffer.add(key);
//
//        return "OK";
//    }
//
//    public void saveYunRecord( String key, String value){
//        this.localYunRecordCachedMap.fastPutAsync(key,value);
//    }


    //yuandayun

    private void extracted() {
        yunManager.register_data("wss://hqyun.ydtg.com.cn?username=abc&password=123","/quote_provider_yun",this);
        yunManager.register_data("wss://hqyun.ydtg.com.cn?username=abc&password=123","/fund_flow_yun/fund-data",this);
//        System.out.println("================================================================");
//        try {
//            String url = "wss://hqyun.ydtg.com.cn?username=abc&password=123";
//            cc = new YunWebSocket(new URI(url), new Draft_6455()) ;
//            YunMessageListener yunMessageListener = new YunMessageListener(cc,this);
//            cc.setListener(yunMessageListener);
//            cc.connect();
//            yunManager.put(url,yunMessageListener,cc);
//        } catch (URISyntaxException ex){
//            System.out.println(ex);
//        }
    }

//    public String findByKey(String key) {
//        return this.localYunRecordCachedMap.get(key);
//    }
//
//    public String getByUrlAdnKey(String urlKey){
//        return localYunRecordCachedMap.get(urlKey);
//    }
    public Map<String,String> getByUrl(String url){
        Collection<String> keySets = this.multiMapCache.get(url);
        Map<String,String> maps = new HashMap<>();

        for (String s: keySets) {
            Optional<String> value = ehcacheSyncCache.get(url + "/" + s, String.class);
            if(value.isPresent()){
                maps.put( s , value.get());
                if(maps.size() > 25 )
                    break;
            }
        }
        return maps;
    }

//    @Scheduled(fixedDelay = "10s")
//    public void check_connection(){
//        JsonObject user = new JsonObject();
//        user.addProperty("cmd",13);
//        user.addProperty("hbbyte",-127);
//        if( cc != null &&  !cc.isClosed() && cc.isOpen()){
//            cc.sendFragmentedFrame(Opcode.BINARY, ByteBuffer.wrap(user.toString().getBytes()),true);
//        }
//        else{
//            if( !bWebsocketInit.get())
//                extracted();
//        }
//    }

    @Scheduled(fixedDelay = "30s")
    public void saveDbToRedis(){
        Page<YunRecord> yunRecords = yunRecordRepository.find(Pageable.from(0, 1000));
        for (int i = 0; i < yunRecords.getTotalPages(); i++) {
            Map<String,String> map = new HashMap<>();
            for (YunRecord v:yunRecords.getContent() ) {
                map.put(v.getUrl()+"/"+v.getKey(),v.getValue());
            }
//            this.localYunRecordCachedMap.putAll(map);
            Pageable nextpageable = yunRecords.nextPageable();
            yunRecords = yunRecordRepository.find(nextpageable);
        }
    }

    public void insertOrUpdateData(String url1,String key, String m,boolean bCache) {

        String url = map_keys.get(url1);
        if(url == null)
            return;
        Collection<String> setKeys = multiMapCache.get(url);
        if(!setKeys.contains(key)){
            setKeys.add(key);
        }

        // Add this to caffeine cache and h2 database;
        String cacheKey = url + "/" + key;
        Optional<String> yunRecord = ehcacheSyncCache.get(cacheKey, String.class);
        if(yunRecord.isPresent()){
            try {
                YunRecord record = objectMapper.readValue(yunRecord.get(), YunRecord.class);
                JsonElement jsonElement = parseString(m);
                JsonElement jsonUpdate = parseString(record.getValue());
                Util.combinJson(jsonElement, jsonUpdate );
                String s1 = Util.converJsonToString(jsonUpdate);
                record.setValue(s1);
                YunRecord update = yunRecordRepository.update(record);
                String s = objectMapper.writeValueAsString(update);
                ehcacheSyncCache.put(cacheKey,s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            YunRecord save = yunRecordRepository.save(new YunRecord(url, key, m));
            if(bCache){
                try {
                    String s = objectMapper.writeValueAsString(save);
                    ehcacheSyncCache.put(url+"/"+key,s);      //redis should be url + key
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
//        String yunRecord = this.findByKey(key);
//        if(yunRecord != null){
//            JsonElement jsonElement = parseString(m);
//            JsonElement jsonUpdate = parseString(yunRecord);
//            Util.combinJson(jsonElement, jsonUpdate );
//            String s1 = Util.converJsonToString(jsonUpdate);
//            yunRecordRepository.updateByKeyAndUrl(url, key,s1);
//        }
//        else {
//            yunRecordRepository.save(new YunRecord(url, key, m));
//            if(bRedis)
//                this.saveYunRecord(url+"/"+key,m);      //redis should be url + key
//        }

    }

    public void putCacheFromDB(){
        Page<YunRecord> yunRecords = yunRecordRepository.find(Pageable.from(0, 2000));

        for (int i = 0; i < yunRecords.getTotalPages(); i++) {

            for (YunRecord v:yunRecords.getContent() ) {
                Collection<String> setKeys = multiMapCache.get(v.getUrl());
                if(!setKeys.contains(v.getKey())){
                    setKeys.add(v.getKey());
                }
                String cacheKey = v.getUrl() + "/" +v.getKey();
                try {
                    String sValue = objectMapper.writeValueAsString(v);
                    ehcacheSyncCache.put(cacheKey,sValue);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Pageable nextpageable = yunRecords.nextPageable();
            yunRecords = yunRecordRepository.find(nextpageable);
        }
    }

    public void insertIntoEhcaches(String key,String value){
        ehcacheSyncCache.put(key,value);
    }
    public String findByKey(String key){
        Optional<String> s = ehcacheSyncCache.get(key, String.class);
        if(s.isPresent()){
            return s.get();
        }
        return "Hello";
    }

    @PostConstruct
    void Init(){
        // String str = "/fund_flow_yun/fund-data/SH600000";
        // int indexOf = str.lastIndexOf('/');
        // String substring = str.substring(indexOf+1);
        // String substring1 = str.substring(0, indexOf);


        {
            HttpQueryObject queryObject = new HttpQueryObject("/quote_provider_yun/SH000010", 10, true);
            Flux<Map> from = null;
            from = Flux.from(httpClient.retrieve(POST("/node/sdkapi", queryObject), Map.class));

            from.subscribe(new Consumer<Map>() {
                @Override
                public void accept(Map map) {
                    System.out.println(map);
                }
            });
        }
        {
            String path = UriBuilder.of("/node/getTree").queryParam("path", "/cem").queryParam("token","sgdsgdsikhuewikdnjlkgh").queryParam("limitToFirst",20).toString();
            Flux<Map> from = null;
            from = Flux.from(httpClient.retrieve(GET(path), Map.class));


            from.subscribe(new Consumer<Map>() {
                @Override
                public void accept(Map map) {
                    System.out.println(map);
                }
            });
        }

        putCacheFromDB();
        extracted();
//        ehcacheSyncCache.put("Hello","World");
//        Optional<String> hello = ehcacheSyncCache.get(new String("Hello"), String.class);
//        System.out.println(hello.get());

//         syncCache.put("bWebsocketInit", "_userID");
//         Optional<String> optional = syncCache.get("bWebsocketInit", String.class);
//         System.out.println(optional.get());


//         var cache = defaultDynamicCacheManager.getCache("hello");
//         cache.put("optional", "cache");
//         Optional<String> optional2 = cache.get("optional", String.class);
//         System.out.println(optional2.get());


//        cache.putIfAbsent("optional", "20L");
//        String optional3 = cache.get("optional");
        // System.out.println(optional3.get());

//        Set<String> keySet = localYunRecordCachedMap.keySet();
//        Map<String, String> all = localYunRecordCachedMap.getAll(keySet);
//        for (String string : keySet) {
//            String[] split = string.split("/");
//
//            this.insertOrUpdateData(split[0],split[1],all.get(string),false);
//            cache.put(split[0]+"/"+split[1],all.get(string));
//        }
    }
}
