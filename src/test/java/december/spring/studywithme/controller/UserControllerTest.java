package december.spring.studywithme.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import december.spring.studywithme.config.SecurityConfig;
import december.spring.studywithme.dto.EditPasswordRequestDTO;
import december.spring.studywithme.dto.PasswordRequestDTO;
import december.spring.studywithme.dto.UserProfileUpdateRequestDTO;
import december.spring.studywithme.dto.UserRequestDTO;
import december.spring.studywithme.entity.User;
import december.spring.studywithme.entity.UserType;
import december.spring.studywithme.filter.MockSpringSecurityFilter;
import december.spring.studywithme.jwt.JwtUtil;
import december.spring.studywithme.monkeyUtils.MonkeyUtils;
import december.spring.studywithme.security.UserDetailsImpl;
import december.spring.studywithme.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
        controllers = {UserController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = SecurityConfig.class
                )
        }
)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil;



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
    @DisplayName("회원가입 요청 테스트 성공")
    public void 회원가입요청성공() throws Exception {
        //given
        UserRequestDTO requestDTO = new UserRequestDTO();
        requestDTO.setUserId("test12345678");
        requestDTO.setPassword("Test123456789!@");
        requestDTO.setName("test");
        requestDTO.setEmail("test@test.com");
        requestDTO.setIntroduce("test");

        String jsonRequestDto = objectMapper.writeValueAsString(requestDTO);

        //when, then
        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestDto))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 요청 테스트 실패")
    public void 회원가입요청실패() throws Exception {
        //given
        UserRequestDTO requestDTO = MonkeyUtils.monkey().giveMeOne(UserRequestDTO.class);

        String jsonRequestDto = objectMapper.writeValueAsString(requestDTO);

        //when, then
        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestDto))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    @DisplayName("회원탈퇴 요청 테스트 성공")
    public void 회원탈퇴요청성공() throws Exception {
        //given
        this.mockUserSetup();
        PasswordRequestDTO requestDTO = new PasswordRequestDTO();
        requestDTO.setPassword("Test123456789!@");

        String jsonRequestDto = objectMapper.writeValueAsString(requestDTO);

        //when, then
        mockMvc.perform(put("/api/users/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestDto)
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("회원탈퇴 요청 테스트 실패")
    public void 회원탈퇴요청실패() throws Exception {
        //given
        this.mockUserSetup();
        PasswordRequestDTO requestDTO = new PasswordRequestDTO();
        requestDTO.setPassword("");

        String jsonRequestDto = objectMapper.writeValueAsString(requestDTO);

        //when, then
        mockMvc.perform(put("/api/users/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestDto)
                        .principal(mockPrincipal))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    @DisplayName("로그아웃 요청 테스트")
    public void 로그아웃() throws Exception {
        //given
        this.mockUserSetup();

        //when, then
        mockMvc.perform(get("/api/users/logout")
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("id를 이용한 회원정보 조회 테스트")
    public void 회원정보() throws Exception {
        //given
        Long id = 1L;

        //then, when
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인한 사용자 회원정보 조회 테스트")
    public void 사용자회원정보() throws Exception {
        //given
        mockUserSetup();

        //then, when
        mockMvc.perform(get("/api/users/mypage")
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인한 사용자 프로필 수정 테스트")
    public void 프로필수정() throws Exception {
        //given
        mockUserSetup();
        UserProfileUpdateRequestDTO requestDTO = new UserProfileUpdateRequestDTO();
        requestDTO.setUserId("test789456");
        requestDTO.setName("트스테");
        requestDTO.setIntroduce("테스트테스트");
        requestDTO.setCurrentPassword("Test78946456!@");

        String jsonRequestDto = objectMapper.writeValueAsString(requestDTO);

        //then, when
        mockMvc.perform(put("/api/users/mypage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestDto)
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("비밀번호 변경 테스트")
    public void 비밀번호변경() throws Exception{
        //given
        mockUserSetup();
        EditPasswordRequestDTO requestDTO = new EditPasswordRequestDTO();
        requestDTO.setCurrentPassword("Test123456789!@");
        requestDTO.setNewPassword("Test123456789!@!@");

        String jsonRequestDtd = objectMapper.writeValueAsString(requestDTO);

        //when, then
        mockMvc.perform(put("/api/users/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequestDtd)
                .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
