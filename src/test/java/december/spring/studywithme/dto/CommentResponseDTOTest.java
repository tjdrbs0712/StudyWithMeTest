package december.spring.studywithme.dto;

import december.spring.studywithme.entity.Comment;
import december.spring.studywithme.entity.Post;
import december.spring.studywithme.entity.User;
import december.spring.studywithme.monkeyUtils.MonkeyUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CommentResponseDTOTest {

    @Test
    @DisplayName("CommentResponseDto 생성 테스트")
    void DTO생성() {
        //given
        User user = MonkeyUtils.monkey().giveMeOne(User.class);
        Post post = MonkeyUtils.monkey().giveMeOne(Post.class);

        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .contents("내용")
                .build();

        //when
        CommentResponseDTO responseDTO = new CommentResponseDTO(comment);

        //then
        assertThat(responseDTO.getUserId()).isEqualTo(comment.getUser().getUserId());
        assertThat(responseDTO.getPostId()).isEqualTo(comment.getPost().getId());
        assertThat(responseDTO.getContents()).isEqualTo(comment.getContents());

    }
}