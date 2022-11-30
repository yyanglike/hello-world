package hello.world.nats;

import io.micronaut.nats.annotation.NatsClient;
import io.micronaut.nats.annotation.Subject;

import java.util.concurrent.Flow;

@NatsClient // 
public interface NATSMessage {

    @Subject("product") // 
    void send(byte[] data); //


    //request
    @Subject("msg")
    String sendMsg(String data);

    @Subject("msg")
    Flow.Publisher<String> sendReactive(String data);
}