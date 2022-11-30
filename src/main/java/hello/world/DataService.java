package hello.world;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

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
import org.redisson.api.map.MapLoader;
import org.redisson.api.map.MapWriter;
import org.redisson.api.map.MapWriterAsync;

import hello.world.MySQL.MySQL;
import hello.world.nats.NATSMessage;
import io.micronaut.context.annotation.Context;
import io.micronaut.context.annotation.Parallel;
import jakarta.inject.Inject;

@Context
@Parallel
public class DataService {

    @Inject
    private RedissonClient redisson;

    @Inject
    NATSMessage natsMessage;

    RScoredSortedSet<String> scoredSortedSet;
    LocalCachedMapOptions<String, String> localCachedMapOptions;
    RLocalCachedMap<String, String> localCachedMap;
    RRingBuffer<String> buffer;

    private MySQL mySQL = null;

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
        mySQL = new MySQL("jdbc:mysql://127.0.0.1:3306/test", "root", "jackson");

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

}
