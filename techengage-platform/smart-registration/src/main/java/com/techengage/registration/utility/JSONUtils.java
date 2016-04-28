package com.techengage.registration.utility;

import com.cts.iot.internal.transfer.objects.Dummy;
import com.cts.iot.internal.transfer.objects.FileUploadResponse;
import com.cts.iot.internal.transfer.objects.ResultsForFaces;
import com.cts.iot.internal.transfer.objects.ResultsForPerson;
import java.io.IOException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class JSONUtils {
    @JsonCreator
    public static FileUploadResponse convertValue(String jsonString) throws JsonParseException, JsonMappingException, IOException {
	ObjectMapper mapper = new ObjectMapper();
	return mapper.readValue(jsonString, FileUploadResponse.class);
    }

    @JsonCreator
    public static ResultsForFaces convertToResultsForFaces(String jsonString) throws JsonParseException, JsonMappingException, IOException {
	ObjectMapper mapper = new ObjectMapper();
	return mapper.readValue(jsonString, ResultsForFaces.class);
    }

    @JsonCreator
    public static ResultsForPerson convertToResultsForPerson(String jsonString) throws JsonParseException, JsonMappingException, IOException {
	ObjectMapper mapper = new ObjectMapper();
	return mapper.readValue(jsonString, ResultsForPerson.class);
    }

    @JsonCreator
    public static Dummy convertToDummy(String jsonString) throws Exception {
	ObjectMapper mapper = new ObjectMapper();
	return mapper.readValue(jsonString, Dummy.class);
    }
}
