package ru.practicum.shareit.it;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.it.dto.ItCreateDto;
import ru.practicum.shareit.it.dto.ItResponseDto;

@Service
public class ItClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getOwnerItems(Long userId, Integer from, Integer size) {

        String path = "?from=" + from;
        if (size != null) {
            path += "&size=" + size;
        }
        return get(path, userId, null);
        /*
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("?from={from}&size={size}", userId, parameters);

         */
    }

    public ResponseEntity<Object> getItemById(Long userId, Long itemId) {
        String path = "/" + itemId;
        return get(path, userId);
    }

    public ResponseEntity<Object> createItem(Long userId, ItCreateDto newItem) {
        return post("", userId, newItem);
    }

    public ResponseEntity<Object> updateItem(ItResponseDto itemData, Long itemId, Long userId) {
        return patch("/" + itemId, userId, itemData);
    }

    public ResponseEntity<Object> searchItemsByText(String text, Integer from, Integer size) {
        String path = "/search?text=" + text + "&from=" + from;
        if (size != null) {
            path += "&size=" + size;
        }
        return get(path);
    }

    public ResponseEntity<Object> createComment(Long userId, Long itemId, CommentRequestDto newComment) {
        return post("/" + itemId + "/comment", userId, newComment);
    }
}
