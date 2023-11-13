package ru.practicum.shareit.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    public CommentResponseDto toCommentDto(Comment comment, ItemResponseDto itemResponseDto) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .item(itemResponseDto)
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }

    public Comment toComment(CommentRequestDto commentDto, User author, Item item, LocalDateTime dateTimeCreation) {
        return Comment.builder()
                .text(commentDto.getText())
                .item(item)
                .author(author)
                .created(dateTimeCreation)
                .build();
    }
}
