package hello.world;

import hello.world.nats.NATSMessage;
import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.Micronaut;

public class Application {
    public static void main(String[] args) {
        try{
            ApplicationContext context = Micronaut.run(Application.class, args);
            NATSMessage productClient =  context.getBean(NATSMessage.class);
            productClient.send("Hello".getBytes());

        }
        catch(Exception e){
            System.out.println(e.toString());
        }
    }
}
