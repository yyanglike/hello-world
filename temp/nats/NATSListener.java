package hello.world.nats;

import hello.world.DataService;
import io.micronaut.nats.annotation.NatsListener;
import io.micronaut.nats.annotation.Subject;
import jakarta.inject.Inject;

@NatsListener
public class NATSListener {

    @Inject
    private DataService dataService;


    public NATSListener(){

    }

    @Subject("product")
    public void receive(byte[] data){
//        System.out.println(new String(data));
//        dataService.saveRedisPerson(Long.toString(System.nanoTime()) + new String(data),20);
    }
    @Subject("msg")
    public String toUpperCase(String data){
        return data.toUpperCase();
    }
}
