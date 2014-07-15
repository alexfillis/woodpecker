package woodpecker.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class WoodpeckerApiTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void unmapped_path_should_respond_with_404() throws IOException {
        // given
        URI uri = URI.create("http://localhost:9996/unmapped/path");

        // when
        Response response = Request.Get(uri).execute();

        // then
        assertEquals(404, response.returnResponse().getStatusLine().getStatusCode());
    }

    @Test
    public void mapped_path_should_return_with_200_and_something_in_the_body() throws IOException {
        // given
        URI uri = URI.create("http://localhost:9996/quote?symbol=APPL");

        // when
        Response response = Request.Get(uri).execute();

        // then
        HttpResponse httpResponse = response.returnResponse();
        assertEquals(200, httpResponse.getStatusLine().getStatusCode());
        Map<String, Object> result = objectMapper.readValue(httpResponse.getEntity().getContent(), Map.class);
        assertFalse(result.isEmpty());
    }
}
