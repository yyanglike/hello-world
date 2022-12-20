package hello.world.logs;

import io.micronaut.context.annotation.Context;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@Context
public class MyLogger {
    
    static Logger logger = Logger.getLogger(MyLogger.class.getName());

    public MyLogger(){
        try {
            String path = MyLogger.class.getClassLoader().getResource("logging.properties").getFile();
            LogManager.getLogManager().readConfiguration(new FileInputStream(path));
        } catch (SecurityException | IOException e1) {
            e1.printStackTrace();
        }
        logger.setLevel(Level.FINE);
        logger.addHandler(new ConsoleHandler());
        //adding custom handler
        logger.addHandler(new MyHandler());
        try {
            //FileHandler file name with max size and number of log files limit
            String property = System.getProperty("user.dir");
            Handler fileHandler = new FileHandler( property + "/logs/logger.log", 2000, 1);
            fileHandler.setFormatter(new MyFormatter());
            //setting custom filter for FileHandler
            fileHandler.setFilter(new MyFilter());
            logger.addHandler(fileHandler);
            
//             for(int i=0; i<1000; i++){
//                 //logging messages
//                 logger.log(Level.INFO, "Msg"+i);
//             }
            logger.log(Level.CONFIG, "Config data");
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void logs(Level l , String s){
        logger.log(l, s);
    }
}
