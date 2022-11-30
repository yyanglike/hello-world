package hello.world;

import hello.world.nats.NATSMessage;
import org.reactivestreams.Publisher;

import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Controller("/helloController")
public class HelloControllerController {

    @Inject
    DataService dataService;

    @Inject
    NATSMessage natsMessage;

    @Get(uri="/", produces="text/plain")
    public String index() {

        return dataService.getAll() + dataService.getAllMultimap();

        // return "Example Response";
    }

    @Get(uri="/mul", produces="text/plain")
    public String mul() {


        //String multimap = dataService.getMultimap();
        natsMessage.send("multimap".getBytes(StandardCharsets.UTF_8));
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
    public Publisher<HttpResponse<Person1>> save(@Body Publisher<Person1> person) { // 
        return Mono.from(person).map(p -> {
                    dataService.saveRedisPerson(p.getName(),p.getAge());
                    return HttpResponse.created(p); // 
                }
        );
    }

    @Get(uri="/getSize", produces="text/plain")
    public String getBuffSize() {

        return Integer.toString(dataService.getAllBuff()) ;

        // return "Example Response";
    }

}