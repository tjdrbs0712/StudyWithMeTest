package december.spring.studywithme.service;


import december.spring.studywithme.dto.PostPageResponseDTO;
import december.spring.studywithme.dto.PostRequestDTO;
import december.spring.studywithme.dto.PostResponseDTO;
import december.spring.studywithme.entity.Post;
import december.spring.studywithme.entity.User;
import december.spring.studywithme.entity.UserType;
import december.spring.studywithme.exception.PostException;
import december.spring.studywithme.repository.PostRepository;
import december.spring.studywithme.security.UserDetailsImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    private User user;

    private Post post;

    public void userCreate() {
        user = User.builder()
                .userId("test123456789")
                .password("encodedPassword")
                .name("test")
                .email("test@test.com")
                .userType(UserType.ACTIVE)
                .introduce("test")
                .build();
    }

    public void postCreate() {
        post = Post.builder()
                .title("제목")
                .contents("내용")
                .user(user)
                .build();
    }

    @Test
    @DisplayName("게시글 생성 테스트")
    public void 게시글생성(){
        //given
        userCreate();
        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        PostRequestDTO requestDTO = new PostRequestDTO();
        requestDTO.setTitle("제목");
        requestDTO.setContents("내용");

        Post post = Post.builder()
                .title(requestDTO.getTitle())
                .contents(requestDTO.getContents())
                .user(user)
                .build();
        when(postRepository.save(any(Post.class))).thenReturn(post);

        //when
        PostResponseDTO responseDTO = postService.createPost(userDetails, requestDTO);

        //then
        assertThat(responseDTO.getTitle()).isEqualTo("제목");
        assertThat(responseDTO.getContents()).isEqualTo("내용");
    }

    @Test
    @DisplayName("게시글 단일 조회 테스트")
    public void 게시글단일조회(){
        //given
        Long id = 1L;
        userCreate();
        postCreate();

        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));

        //when
        PostResponseDTO responseDTO = postService.getPost(id);

        //then
        assertThat(responseDTO.getTitle()).isEqualTo("제목");
        assertThat(responseDTO.getContents()).isEqualTo("내용");
    }

    @Test
    @DisplayName("게시글 단일 조회 테스트 실패")
    public void 게시글단일조회실패(){
        //given
        Long id = 1L;
        userCreate();
        postCreate();

        when(postRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when
        PostException exception = assertThrows(PostException.class, () ->{
            postService.getPost(id);
        });

        //then
        assertThat(exception.getMessage()).isEqualTo("게시글이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("게시글 수정 테스트")
    public void 게시글수정(){
        //given
        userCreate();
        postCreate();
        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        PostRequestDTO requestDTO = new PostRequestDTO();
        requestDTO.setTitle("제목 수정");
        requestDTO.setContents("내용 수정");

        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        // When
        PostResponseDTO responseDTO = postService.updatePost(1L, userDetails, requestDTO);

        // Then
        assertThat(responseDTO.getTitle()).isEqualTo("제목 수정");
        assertThat(responseDTO.getContents()).isEqualTo("내용 수정");
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    public void 게시글삭제(){
        //given
        userCreate();
        postCreate();
        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));

        // When
        postService.deletePost(1L, userDetails);

        // Then
        verify(postRepository, times(1)).delete(any(Post.class));
    }

}
