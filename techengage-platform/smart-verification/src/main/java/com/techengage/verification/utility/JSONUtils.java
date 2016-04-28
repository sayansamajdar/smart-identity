package com.techengage.verification.utility;

import com.cts.iot.internal.transfer.objects.ResultsForPerson;
import java.io.IOException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class JSONUtils {
    private static ObjectMapper mapper;
    static {
	mapper = new ObjectMapper();
    }

    @JsonCreator
    public static ResultsForPerson convertToResultsForPerson(String jsonString) throws JsonParseException, JsonMappingException, IOException {
	return mapper.readValue(jsonString, ResultsForPerson.class);
    }
}
