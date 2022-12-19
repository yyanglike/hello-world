package hello.world.datamanager;


import io.micronaut.context.annotation.Context;
import org.h2.tools.Server;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.sql.SQLException;

@Context
public class DataManager {

    // private static final Logger LOG = LoggerFactory.getLogger(DataManager.class);

    private Server server;


    @PostConstruct
    void init(){
        try {
            server = Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9090");
            server.start();
            // LOG.debug("H2 database startup at port 9090.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    void destroy(){
        server.shutdown();
    }


}
