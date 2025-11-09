package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableCaching
public class App 
{
    static Logger log=LogManager.getLogger(App.class);
    public static void main( String[] args )
    {
        SpringApplication.run(App.class, args);
        System.out.println( "New Push !" );
        log.info("Vectrolla Started logging !");
    }
}
