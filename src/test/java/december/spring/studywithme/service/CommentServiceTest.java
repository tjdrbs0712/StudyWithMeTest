package december.spring.studywithme.service;

import december.spring.studywithme.dto.CommentRequestDTO;
import december.spring.studywithme.dto.CommentResponseDTO;
import december.spring.studywithme.entity.Comment;
import december.spring.studywithme.entity.Post;
import december.spring.studywithme.entity.User;
import december.spring.studywithme.entity.UserType;
import december.spring.studywithme.exception.NoContentException;
import december.spring.studywithme.repository.CommentRepository;
import december.spring.studywithme.security.UserDetailsImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostService postService;

    @InjectMocks
    private CommentService commentService;

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
    @DisplayName("댓글 생성 테스트")
    public void 댓글생성() {
        userCreate();
        postCreate();
        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        CommentRequestDTO requestDTO = new CommentRequestDTO();
        requestDTO.setContents("내용");

        when(postService.getValidatePost(anyLong())).thenReturn(post);
        when(commentRepository.save(any(Comment.class))).thenReturn(new Comment());

        // When
        CommentResponseDTO responseDTO = commentService.createComment(userDetails, 1L, requestDTO);

        // Then
        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.getContents()).isEqualTo(requestDTO.getContents());
    }

    @Test
    @DisplayName("전체 댓글 조회 테스트")
    public void 전체댓글조회() {
        userCreate();
        postCreate();

        Comment comment1 = Comment.builder()
                .post(post)
                .contents("내용1")
                .user(user)
                .build();
        Comment comment2 = Comment.builder()
                .post(post)
                .contents("내용2")
                .user(user)
                .build();

        post.setCommentList(List.of(comment1, comment2));

        when(postService.getValidatePost(anyLong())).thenReturn(post);

        // When
        List<CommentResponseDTO> responseDTOList = commentService.getAllComments(1L);

        // Then
        assertThat(responseDTOList).hasSize(2);
        assertThat(responseDTOList.get(0).getContents()).isEqualTo("내용1");
        assertThat(responseDTOList.get(1).getContents()).isEqualTo("내용2");
    }

    @Test
    @DisplayName("단일 댓글 조회 테스트")
    public void 단일댓글조회() {
        //given
        userCreate();
        postCreate();
        post.setId(1L);
        Comment comment = Comment.builder()
                .post(post)
                .contents("내용")
                .user(user)
                .build();

        when(postService.getValidatePost(anyLong())).thenReturn(post);
        when(commentRepository.findByPostIdAndId(anyLong(), anyLong())).thenReturn(Optional.of(comment));

        // When
        CommentResponseDTO responseDTO = commentService.getComment(1L, 1L);

        // Then
        assertThat(responseDTO.getContents()).isEqualTo("내용");
    }

    @Test
    @DisplayName("댓글 수정 테스트")
    public void 댓글수정(){
        //given
        userCreate();
        postCreate();
        post.setId(1L);
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        Comment comment = Comment.builder()
                .post(post)
                .contents("내용")
                .user(user)
                .build();

        CommentRequestDTO requestDTO = new CommentRequestDTO();
        requestDTO.setContents("내용 수정");

        when(postService.getValidatePost(anyLong())).thenReturn(post);
        when(commentRepository.findByPostIdAndId(anyLong(), anyLong())).thenReturn(Optional.of(comment));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        // When
        CommentResponseDTO responseDTO = commentService.updateComment(userDetails, 1L, 1L, requestDTO);

        // Then
        assertThat(responseDTO.getContents()).isEqualTo("내용 수정");

    }

    @Test
    @DisplayName("댓글 삭제 테스트")
    public void 댓글삭제(){
        //given
        userCreate();
        postCreate();
        post.setId(1L);
        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        Comment comment = Comment.builder().user(user).build();

        when(postService.getValidatePost(anyLong())).thenReturn(post);
        when(commentRepository.findByPostIdAndId(anyLong(), anyLong())).thenReturn(Optional.of(comment));

        // When
        commentService.deleteComment(userDetails, 1L, 1L);

        // Then
        verify(commentRepository, times(1)).delete(any(Comment.class));

    }
}
