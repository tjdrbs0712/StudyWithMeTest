package december.spring.studywithme.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;
    private LocalDateTime now;

    @BeforeEach
    public void setUp() {
        //given
        now = LocalDateTime.now();
        user = User.builder()
                .userId("user1")
                .password("password")
                .name("박성균")
                .email("asdf@naver.com")
                .introduce("박성균입니다.")
                .userType(UserType.ACTIVE)
                .statusChangedAt(now)
                .build();
    }

    @Test
    @DisplayName("User 객체 생성")
    public void testAllArgsConstructor(){
        //then
        assertEquals(user.getUserId(), "user1");
        assertEquals(user.getPassword(), "password");
        assertEquals(user.getName(), "박성균");
        assertEquals(user.getEmail(), "asdf@naver.com");
        assertEquals(user.getIntroduce(), "박성균입니다.");
        assertEquals(user.getUserType(), UserType.ACTIVE);
        assertEquals(user.getStatusChangedAt(), now);
    }

    @Test
    @DisplayName("회원 탈퇴 테스트")
    public void testWithdrawUser(){
        //when
        user.withdrawUser();

        //then
        assertEquals(user.getUserType(), UserType.DEACTIVATED);
    }

    @Test
    @DisplayName("이메일 인증 회원 상태 변경")
    public void testActiveUser() {
        //when
        user.ActiveUser();

        //then
        assertEquals(user.getUserType(), UserType.ACTIVE);
    }

    @Test
    @DisplayName("회원이름, 자기소개 변경")
    public void testEditProfile(){
        //when
        user.editProfile("김성균", "김성균입니다.");

        //then
        assertEquals(user.getName(), "김성균");
        assertEquals(user.getIntroduce(), "김성균입니다.");
    }

    @Test
    @DisplayName("비밀번호 변경")
    public void testChangePassword(){
        //when
        user.changePassword("newPassword");

        //then
        assertEquals(user.getPassword(), "newPassword");
    }

}