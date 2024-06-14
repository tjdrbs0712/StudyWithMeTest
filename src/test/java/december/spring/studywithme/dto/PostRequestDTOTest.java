package december.spring.studywithme.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PostRequestDTOTest {

    @Test
    @DisplayName("PostRequestDTO 객체 생성 테스트")
    public void DTO생성(){
        //given
        PostRequestDTO requestDTO = new PostRequestDTO();
        String title = "제목";
        String contents = "내용";

        //when
        requestDTO.setTitle(title);
        requestDTO.setContents(contents);

        //then
        assertThat(requestDTO.getTitle()).isEqualTo(title);
        assertThat(requestDTO.getContents()).isEqualTo(contents);
    }
}