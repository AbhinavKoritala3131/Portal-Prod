package org.example.requestLogs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Mapper {

    private final static Logger logger=LogManager.getLogger(Mapper.class);
    public static String MapToString(Object obj){

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(obj);
        }
        catch(JsonProcessingException e){
            logger.error("Failed to serialize object: {}", obj, e);
        }
        return null;
    }
}
