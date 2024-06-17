package december.spring.studywithme.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import december.spring.studywithme.config.SecurityConfig;
import december.spring.studywithme.dto.PostRequestDTO;
import december.spring.studywithme.entity.User;
import december.spring.studywithme.entity.UserType;
import december.spring.studywithme.filter.MockSpringSecurityFilter;
import december.spring.studywithme.security.UserDetailsImpl;
import december.spring.studywithme.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(locations = "classpath:properties/env.properties")
@WebMvcTest(
        controllers = {PostController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = SecurityConfig.class
                )
        }
)
public class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostService postService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
    }

    private void mockUserSetup() {
        User user = User.builder()
                .userId("test123456789")
                .password("Test123456789!@")
                .name("test")
                .email("test@naver.com")
                .introduce("test")
                .userType(UserType.DEACTIVATED)
                .build();

        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        mockPrincipal = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    @Test
    @DisplayName("게시글 등록 테스트")
    public void 게시글등록() throws Exception {
        //given
        mockUserSetup();
        PostRequestDTO requestDTO = new PostRequestDTO();
        requestDTO.setTitle("제목");
        requestDTO.setContents("내용");

        String jsonRequestDto = objectMapper.writeValueAsString(requestDTO);

        //when, then
        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestDto)
                        .principal(mockPrincipal))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @DisplayName("단일 게시글 조회 테스트")
    public void 단일게시글조회() throws Exception {
        //given
        Long id = 1L;

        //when, then
        mockMvc.perform(get("/api/posts/{id}", id))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("전체 게시글 조회 테스트")
    public void 전체게시글조회() throws Exception {
        //when, then
        mockMvc.perform(get("/api/posts")
                        .param("page", "1")
                        .param("sortBy", "createAt")
                        .param("from", "2024-06-16")
                        .param("to", "2024-06-16")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 수정 테스트")
    public void 게시글수정() throws Exception {
        //given
        Long id = 1L;
        PostRequestDTO requestDTO = new PostRequestDTO();
        requestDTO.setTitle("제목수정");
        requestDTO.setContents("내용 수정");
        mockUserSetup();

        String jsonRequestDto = objectMapper.writeValueAsString(requestDTO);

        //when, then
        mockMvc.perform(put("/api/posts/{id}" ,id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestDto)
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    public void 게시글삭제() throws Exception{
        //given
        mockUserSetup();
        Long id = 1L;

        //when, then
        mockMvc.perform(delete("/api/posts/{id}", id)
                .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
