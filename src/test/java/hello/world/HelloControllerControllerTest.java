package hello.world;
import hello.world.jdbc.dto.BookRepository;
import hello.world.jdbc.entity.Book;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import io.micronaut.http.client.annotation.*;
import jakarta.inject.Inject;
import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class HelloControllerControllerTest {

    @Inject
    @Client("/")
    HttpClient client;


    @Inject
    BookRepository bookRepository;

    @Test
    public void testIndex() throws Exception {
        assertEquals(HttpStatus.OK, client.toBlocking().exchange("/helloController").status());
    }

    @Test
    void testBook1() {
        Book book = new Book("The Stand", 100);

        bookRepository.save(book);

    }

}
