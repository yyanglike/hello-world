package hello.world;

import hello.world.datamanager.DataManager;
import hello.world.jdbc.dto.YunRecordRepository;
import hello.world.jdbc.entity.YunRecord;
import hello.world.util.RingBuffer;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.serde.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.h2.tools.Csv;
import org.h2.tools.SimpleResultSet;

import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


@Controller("/helloController")
public class HelloControllerController {
    @Inject
    DataService dataService;

    @Inject
    YunRecordRepository yunRecordRepository;
    @Inject
    DataManager dataManager;
    @Inject
    ObjectMapper objectMapper;

    RingBuffer<Long> ringBuffer = new RingBuffer<Long>(40);

    @Get(uri="/", produces="text/plain")
    public String index() {

//        return dataService.getAll() + dataService.getAllMultimap();

         return "Example Response";
    }

    @Operation(summary = "Greets a person",
            description = "A friendly greeting is returned"
    )
    @ApiResponse(
            content = @Content(mediaType = "text/plain",
                    schema = @Schema(type="string"))
    )
    @ApiResponse(responseCode = "400", description = "Invalid Name Supplied")
    @ApiResponse(responseCode = "404", description = "Person not found")
    @Tag(name = "greeting")
    @Get(uri="/mul", produces="text/plain")
    public String mul() {


        ThreadLocalRandom random = ThreadLocalRandom.current();


        String s = "Hello:" + Long.toString(random.nextLong(1,1000000));
        dataService.insertIntoEhcaches(s,s);

        //String multimap = dataService.getMultimap();
//        natsMessage.send("multimap".getBytes(StandardCharsets.UTF_8));

//        YunRecord yunRecord = new YunRecord("/cem/test","time","{'a':'b'} ");
//        yunRecordRepository.save(yunRecord);

        return "Hello";

//        List<YunRecord> byTitle = yunRecordRepository.findByTitle("/test/cem");


//        List<String> urls = yunRecordRepository.queryDistinctKeyByUrl();

//        try {
//            return objectMapper.writeValueAsString(urls);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "Hello";
//        }
//        String str = natsMessage.sendMsg("hello");
//        return str;

        // return "Example Response";
    }

//
//    @Post(value = "/echo", consumes = MediaType.TEXT_PLAIN, produces = MediaType.TEXT_PLAIN) //
//    public String test11(@Body String text) { //
//        return text; //
//    }
//
//    @Post(value = "/save")
//    @SingleResult
//    public Publisher<HttpResponse<Person>> save(@Body Publisher<Person> person) { //
//        return Mono.from(person).map(p -> {
//                    dataService.saveRedisPerson(p.getName(),p.getAge());
//                    return HttpResponse.created(p); //
//                }
//        );
//    }

    @Get(uri="/getSize", produces="text/plain")
    public String getBuffSize() {

        return Long.toString(20) ;
//        return Integer.toString(dataService.getAllBuff()) ;

        // return "Example Response";
    }

    @Get(uri="/find", produces="text/plain")
    public String findRecords() throws IOException {
        ThreadLocalRandom random = ThreadLocalRandom.current();


//        String s = objectMapper.writeValueAsString(yunRecordRepository.findById(random.nextLong(1,1000)).get());
        return dataService.findByKey("Hello:" + Long.toString(random.nextLong(1,100000)));
//        return Long.toString(dataManager.getSize()) ;
//        return Integer.toString(dataService.getAllBuff()) ;

        // return "Example Response";
    }

    @Get(uri="/getTitle", produces="text/plain")
    public String getTitle() {
        List<YunRecord> byTitle = yunRecordRepository.findByUrl("/test/cem",Pageable.from(0,50));
        try {
            return objectMapper.writeValueAsString(byTitle);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Hello";
    }
    @Get(uri="/getDist", produces="text/plain")
    public String getDist() {
        List<String> dist = yunRecordRepository.getDistinctUrl();
//        try {
//            return objectMapper.writeValueAsString(dist);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        Date date = new Date(System.currentTimeMillis() - 24*60*60*1000L);
//        yunRecordRepository.deleteByDateInBefore(new Date(System.currentTimeMillis()));

//        List<YunRecord> quote_provider_yun = yunRecordRepository.findByUrl("quote_provider_yun");
//        try {
//            return objectMapper.writeValueAsString(quote_provider_yun);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        Page<YunRecord> quote_provider_yun = yunRecordRepository.findByUrlEqual("quote_provider_yun", Pageable.from(1, 25));

//        quote_provider_yun.getTotalPages();
        try {
            return objectMapper.writeValueAsString(quote_provider_yun.getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }


        return "Hello";
    }
    @Get(uri="/getDistMem", produces="text/plain")
    public String getDistMem() {
        try {
            return objectMapper.writeValueAsString( dataService.getByUrl("quote_provider_yun") );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Hello";
    }
    @Get(uri="/export", produces="text/plain")
    public String export() {

        SimpleResultSet rs = new SimpleResultSet();
        rs.addColumn("ID", Types.BIGINT ,0,0);
        rs.addColumn("URL", Types.VARCHAR,512,0);
        rs.addColumn("KEY", Types.VARCHAR,255,0);
        rs.addColumn("VALUE", Types.VARCHAR,4096,0);
        rs.addColumn("DATAEIN", Types.DATE,0,0);
        rs.addColumn("DATEUP", Types.DATE,0,0);


        Page<YunRecord> yunRecords = yunRecordRepository.find(Pageable.from(0, 50));

        for (int i = 0; i < yunRecords.getTotalPages(); i++) {

            for (YunRecord v:yunRecords.getContent() ) {
                rs.addRow(v.getId(),v.getUrl(),v.getKey(),v.getValue(),v.getDateIn(),v.getDateUp());
            }

            Pageable nextpageable = yunRecords.nextPageable();
            yunRecords = yunRecordRepository.find(nextpageable);
        }
        try {
            new Csv().write("./test.csv",rs,null);
            System.out.println("Export ./test.csv OK!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "OK";
    }

    @Get(uri="/import", produces="text/plain")
    public String importData() {
        try {
            ResultSet rs = new Csv().read("./test.csv",null,null);
            while(rs.next()){
                YunRecord yunRecord = new YunRecord(rs.getString("URL"),rs.getString("KEY"),rs.getString("VALUE"));
                dataService.insertOrUpdateData(yunRecord.getUrl(), yunRecord.getKey(), yunRecord.getValue(), true);
//                yunRecordRepository.save(yunRecord);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Hello";

    }

}