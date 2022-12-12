package hello.world;

import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;


@OpenAPIDefinition(
        info = @Info(
                title = "Hello World",
                version = "0.0",
                description = "My API",
                license = @License(name = "Apache 2.0", url = "https://foo.bar"),
                contact = @Contact(url = "https://gigantic-server.com", name = "Fred", email = "Fred@gigagantic-server.com")
        )
)
public class Application {
    public static void main(String[] args) {
        try{
            ApplicationContext context = Micronaut.run(Application.class, args);
//            NATSMessage productClient =  context.getBean(NATSMessage.class);
//            productClient.send("Hello".getBytes());

        }
        catch(Exception e){
            System.out.println(e.toString());
        }
    }
}
