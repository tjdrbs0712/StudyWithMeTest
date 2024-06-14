package december.spring.studywithme.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserRequestDtoTest {

    @Test
    @DisplayName("UserRequestDto 객체 생성 테스트")
    void DTO생성(){
        //given
        UserRequestDTO requestDTO = new UserRequestDTO();

        String userId = "test1234";
        String password = "password1234";
        String name = "testName";
        String email = "test@test.com";
        String introduce = "test";

        //when
        requestDTO.setUserId(userId);
        requestDTO.setPassword(password);
        requestDTO.setName(name);
        requestDTO.setEmail(email);
        requestDTO.setIntroduce(introduce);

        //then
        assertThat(requestDTO.getUserId()).isEqualTo(userId);
        assertThat(requestDTO.getPassword()).isEqualTo(password);
        assertThat(requestDTO.getName()).isEqualTo(name);
        assertThat(requestDTO.getEmail()).isEqualTo(email);
        assertThat(requestDTO.getIntroduce()).isEqualTo(introduce);
    }


}
