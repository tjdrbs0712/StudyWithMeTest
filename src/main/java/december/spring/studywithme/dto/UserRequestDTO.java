package december.spring.studywithme.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDTO {
	
	@NotBlank(message = "아이디를 입력해 주세요")
	@Size(min = 10, max = 20, message = "ID는 10글자 이상, 20글자 이하여야 합니다.")
	@Pattern(regexp = "^[a-zA-Z0-9]*$", message = "영문 대소문자와 숫자만 입력 가능합니다.")
	private String userId;
	
	@NotBlank(message = "비밀번호를 입력해 주세요")
	@Size(min = 10, message = "비밀번호는 최소 10글자 이상이어야 합니다.")
	@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*]).+$", message = "영어 대소문자와 특수문자를 최소 1글자씩 포함해야 합니다.")
	private String password;
	
	@NotBlank(message = "이름을 입력해 주세요.")
	private String name;
	
	@NotBlank(message = "이메일을 입력해 주세요.")
	@Email(message = "이메일 형식을 입력해 주세요.")
	private String email;

	private String introduce;
}
