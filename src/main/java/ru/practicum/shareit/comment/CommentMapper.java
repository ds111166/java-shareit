package ru.practicum.shareit.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final ItemMapper itemMapper;
    private final UserMapper userMapper;

    public CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .item(itemMapper.toItemDto(comment.getItem()))
                .author(userMapper.toUserDto(comment.getAuthor()))
                .build();
    }

    public Comment toComment(CommentDto commentDto) {
        return Comment.builder()
                .id(commentDto.getId())
                .text(commentDto.getText())
                .item(itemMapper.toItem(commentDto.getItem()))
                .author(userMapper.toUser(commentDto.getAuthor()))
                .build();
    }

    public Comment toComment(CommentDto commentDto, UserDto author, ItemDto item) {
        return Comment.builder()
                .id(commentDto.getId())
                .text(commentDto.getText())
                .item(itemMapper.toItem(item))
                .author(userMapper.toUser(author))
                .build();
    }
}
