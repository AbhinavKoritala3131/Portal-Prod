package org.example.requestLogs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Mapper {

    public static String MapToString(Object obj){

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(obj);
        }
        catch(JsonProcessingException e){
             e.printStackTrace();
        }
        return null;
    }
}
