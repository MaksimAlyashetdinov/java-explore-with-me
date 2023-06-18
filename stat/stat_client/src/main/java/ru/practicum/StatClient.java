package ru.practicum;

import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.beans.factory.annotation.Value;

@Service
public class StatClient extends BaseClient {

    public StatClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addStats(EndpointHitDto endpointHitDto) {
        return post("/hit", endpointHitDto);
    }

    public ResponseEntity<Object> getStats(String start, String end, String[] uris, Boolean unique) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("start", start);
        parameters.put("end", end);
        if (uris != null && uris.length != 0) {
            String allUri = new String();
            for (String uri : uris) {
                allUri = String.join("&uris=", uri);
            }
            parameters.put("uris", allUri);
        }
        parameters.put("unique", unique);

        if (parameters.containsKey("uris")) {
            return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);

        } else {
            return get("/stats?start={start}&end={end}&unique={unique}", parameters);
        }
    }
}