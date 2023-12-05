package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;

@Service
public class ItemRequestClient extends BaseClient {

    private static final String API_PREFIX = "/requests";

    @Autowired
    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createItemRequest(Long requestorId, ItemRequestCreateDto newItemRequest) {
        return post("", requestorId, newItemRequest);
    }

    public ResponseEntity<Object> getItemRequestById(Long userId, Long requestId) {
        String path = "/" + requestId;
        return get(path, userId);
    }

    public ResponseEntity<Object> getItemRequests(Long requestorId) {
        return get("", requestorId);
    }

    public ResponseEntity<Object> getItemRequestsAll(Long userId, Integer from, Integer size) {
        String path = "/all?from=" + from;
        if (size != null) {
            path += "&size=" + size;
        }
        return get(path, userId);
    }
}
