package december.spring.studywithme.entity;

import december.spring.studywithme.dto.CommentRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommentTest {

    User user;
    Post post;
    Comment comment;

    @BeforeEach
    void setUp() {
        //given
        user = new User();
        post = new Post();
        comment = Comment.builder()
                .post(post)
                .user(user)
                .contents("내용")
                .build();
    }

    @Test
    @DisplayName("comment 객체 생성")
    public void testAllArgsConstructor() {
        //then
        assertEquals(comment.getPost(), post);
        assertEquals(comment.getUser(), user);
        assertEquals(comment.getContents(), "내용");
    }

    @Test
    @DisplayName("comment 내용 수정")
    public void testCommentUpdate() {
        //given
        CommentRequestDTO requestDTO = new CommentRequestDTO();

        //when
        requestDTO.setContents("뉴내용");
        comment.update(requestDTO);

        //then
        assertEquals(comment.getContents(), "뉴내용");
    }

    @Test
    @DisplayName("comment 좋아요 수 업데이트")
    public void testCommentLikeUpdate(){
        //when
        comment.updateCommentLikes(10L);

        //then
        assertEquals(comment.getLikes(), 10L);
    }
}
