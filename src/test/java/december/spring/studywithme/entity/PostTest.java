package december.spring.studywithme.entity;

import december.spring.studywithme.dto.PostRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PostTest {

    Post post;
    User user;

    @BeforeEach
    void setUp() {
        //given
        user = new User();
        post = Post.builder()
                .user(user)
                .title("제목")
                .contents("내용")
                .build();

    }

    @Test
    @DisplayName("post 객체 생성")
    public void testAllArgsConstructor(){
        //then
        assertEquals(post.getUser(), user);
        assertEquals(post.getTitle(), "제목");
        assertEquals(post.getContents(), "내용");
    }

    @Test
    @DisplayName("post 내용 수정")
    public void testUpdate(){
        //given
        PostRequestDTO requestDTO = new PostRequestDTO();

        //when
        requestDTO.setTitle("뉴제목");
        requestDTO.setContents("뉴내용");
        post.update(requestDTO);

        //then
        assertEquals(post.getTitle(), "뉴제목");
        assertEquals(post.getContents(), "뉴내용");
    }

    @Test
    @DisplayName("post 좋아요 수 업데이트")
    public void testUpdatePostLike(){
        //when
        post.updatePostLikes(10L);

        //then
        assertEquals(post.getLikes(), 10L);
    }


}
