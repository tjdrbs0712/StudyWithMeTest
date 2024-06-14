package december.spring.studywithme.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CommentRequestDTOTest {

    @Test
    @DisplayName("댓글 DTO 생성 객체")
    public void DTO생성(){
        //given
        CommentRequestDTO requestDTO = new CommentRequestDTO();
        String contents = "내용";

        //when
        requestDTO.setContents(contents);

        //then
        assertThat(requestDTO.getContents()).isEqualTo(contents);

    }

}