package december.spring.studywithme.dto;

import december.spring.studywithme.entity.User;
import december.spring.studywithme.monkeyUtils.MonkeyUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class UserResponseDtoTest {

    @Test
    @DisplayName("UserResponseDto 객체 생성 테스트")
    public void DTO생성(){
        //given
        User user = MonkeyUtils.monkey().giveMeOne(User.class);

        System.out.println(user.getPassword());

        //when
        UserResponseDTO responseDTO = new UserResponseDTO(user);

        //then
        assertThat(responseDTO.getId()).isEqualTo(user.getId());
        assertThat(responseDTO.getUserId()).isEqualTo(user.getUserId());
        assertThat(responseDTO.getName()).isEqualTo(user.getName());
        assertThat(responseDTO.getEmail()).isEqualTo(user.getEmail());
        assertThat(responseDTO.getIntroduce()).isEqualTo(user.getIntroduce());
        assertThat(responseDTO.getCreatedAt()).isEqualTo(user.getCreatedAt());
        assertThat(responseDTO.getModifiedAt()).isEqualTo(user.getModifiedAt());
    }
}
