package hello.world;

import hello.world.datamanager.DataManager;
import hello.world.jdbc.dto.BookRepository;
import hello.world.jdbc.dto.YunRecordRepository;
import hello.world.jdbc.entity.Book;
import hello.world.jdbc.entity.YunRecord;
import hello.world.nats.NATSMessage;
import hello.world.util.RingBuffer;
import io.micronaut.serde.ObjectMapper;
import org.reactivestreams.Publisher;

import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;


@Controller("/helloController")
public class HelloControllerController {
    @Inject
    DataService dataService;
    @Inject
    NATSMessage natsMessage;
    @Inject
    BookRepository bookRepository;
    @Inject
    YunRecordRepository yunRecordRepository;
    @Inject
    DataManager dataManager;
    @Inject
    ObjectMapper objectMapper;

    RingBuffer<Long> ringBuffer = new RingBuffer(40);

    @Get(uri="/", produces="text/plain")
    public String index() {

        return dataService.getAll() + dataService.getAllMultimap();

        // return "Example Response";
    }

    @Get(uri="/mul", produces="text/plain")
    public String mul() {


        //String multimap = dataService.getMultimap();
//        natsMessage.send("multimap".getBytes(StandardCharsets.UTF_8));

        Book book = new Book("The Stand", 100);

        book = bookRepository.save(book);
        ringBuffer.offer(book.getId());


        YunRecord yunRecord = new YunRecord("/cem/test","time","{'a':'b'} ");
        yunRecordRepository.save(yunRecord);


        return "hello";
//        String str = natsMessage.sendMsg("hello");
//        return str;

        // return "Example Response";
    }


    @Post(value = "/echo", consumes = MediaType.TEXT_PLAIN, produces = MediaType.TEXT_PLAIN) // 
    public String test11(@Body String text) { // 
        return text; // 
    }

    @Post(value = "/save")
    @SingleResult
    public Publisher<HttpResponse<Person>> save(@Body Publisher<Person> person) { //
        return Mono.from(person).map(p -> {
                    dataService.saveRedisPerson(p.getName(),p.getAge());
                    return HttpResponse.created(p); // 
                }
        );
    }

    @Get(uri="/getSize", produces="text/plain")
    public String getBuffSize() {

        return Long.toString(dataManager.getSize()) ;
//        return Integer.toString(dataService.getAllBuff()) ;

        // return "Example Response";
    }

    @Get(uri="/find", produces="text/plain")
    public String findRecords() throws IOException {
        ThreadLocalRandom random = ThreadLocalRandom.current();


        Optional<Book> book = bookRepository.findById(random.nextLong(1,1000000));
        String s = objectMapper.writeValueAsString(book.get());
        s += objectMapper.writeValueAsString(yunRecordRepository.findById(random.nextLong(1,1000000)).get());
        return s;
//        return Long.toString(dataManager.getSize()) ;
//        return Integer.toString(dataService.getAllBuff()) ;

        // return "Example Response";
    }

}