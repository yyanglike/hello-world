package hello.world;



import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.validation.constraints.Size;

import org.reactivestreams.Publisher;

import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.validation.Validated;
import jakarta.inject.Inject;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller("/helloController")
@Validated
public class HelloControllerController {

    @Inject
    DataService dataService;

    Map<String, Person> inMemoryDatastore = new ConcurrentHashMap<>();

    @Get(uri="/", produces="text/plain")
    public String index() {

        return dataService.getAll() + dataService.getAllMultimap();

        // return "Example Response";
    }

    @Get(uri="/mul", produces="text/plain")
    public String mul() {

        return dataService.getMultimap();

        // return "Example Response";
    }


    // @Post(value = "/echo", consumes = MediaType.APPLICATION_JSON, produces = MediaType.TEXT_PLAIN) // 
    // public String echo(@Size(max = 1024) @Body String text) { // 
    //     return text; // 
    // }

    @Post(value = "/saveReactive", consumes = MediaType.APPLICATION_JSON)
    @SingleResult
    public Publisher<HttpResponse<Person>> save(@Body Publisher<Person> person) { // 
        return Mono.from(person).map(p -> {
                    inMemoryDatastore.put(p.getFirstName(), p); // 
                    return HttpResponse.created(p); // 
                }
        );
    }

}