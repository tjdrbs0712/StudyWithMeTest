package december.spring.studywithme.dto;

import december.spring.studywithme.entity.Post;
import december.spring.studywithme.entity.User;
import december.spring.studywithme.monkeyUtils.MonkeyUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PostResponseDTOTest {

    @Test
    @DisplayName("PostResponseDTO 객체 생성 테스트")
    public void DTO생성(){
        //given
        User user = MonkeyUtils.monkey().giveMeBuilder(User.class)
                .set("userId", "testUserId")
                .sample();
//        Post post = MonkeyUtils.monkey().giveMeBuilder(Post.class)
//                .set("user", user)
//                .sample();

        System.out.println(user.getUserId());

//        //then
//        PostResponseDTO responseDTO = new PostResponseDTO(post);
//
//        //when
//        assertThat(responseDTO.getUserId()).isEqualTo(post.getUser().getUserId());
//        assertThat(responseDTO.getTitle()).isEqualTo(post.getTitle());
//        assertThat(responseDTO.getContents()).isEqualTo(post.getContents());
//        assertThat(responseDTO.getLikes()).isEqualTo(post.getLikes());
//        assertThat(responseDTO.getCreatedAt()).isEqualTo(post.getCreatedAt());
//        assertThat(responseDTO.getModifiedAt()).isEqualTo(post.getModifiedAt());
    }
}
