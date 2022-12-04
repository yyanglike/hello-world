package hello.world.datamanager;

import hello.world.jdbc.dto.BookRepository;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class DataManager {

    private static final Logger LOG = LoggerFactory.getLogger(DataManager.class);

    @Inject
    BookRepository bookRepository;


//    @Scheduled(initialDelay = "10s",fixedRate = "20s")
//    void checkRecords(){
//        long lSize = bookRepository.count();
//        LOG.debug("Start Clear Records." + lSize);
//        if(lSize > 10000){
//            bookRepository.deleteAll();
//            LOG.debug("Delete Count size:" ,lSize);
//        }
//    }
    public long getSize(){
        return bookRepository.count();
    }
}
