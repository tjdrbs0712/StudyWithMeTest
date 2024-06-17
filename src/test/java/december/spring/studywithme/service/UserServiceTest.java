package december.spring.studywithme.service;

import december.spring.studywithme.dto.*;
import december.spring.studywithme.entity.User;
import december.spring.studywithme.entity.UserType;
import december.spring.studywithme.exception.UserException;
import december.spring.studywithme.jwt.JwtUtil;
import december.spring.studywithme.repository.UserRepository;
import december.spring.studywithme.security.UserDetailsImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private JwtUtil jwtUtil;

	@InjectMocks
	private UserService userService;

	private User user;

	public void userCreate() {

	}


	@Test
	@DisplayName("회원 가입 테스트 성공")
	public void 회원가입테스트() {
		// Given
		UserRequestDTO requestDTO = new UserRequestDTO();
		requestDTO.setUserId("test12345678");
		requestDTO.setPassword("Test123456789!@");
		requestDTO.setName("test");
		requestDTO.setEmail("test@test.com");
		requestDTO.setIntroduce("test");


		User user = User.builder()
				.userId(requestDTO.getUserId())
				.password("encodedPassword")
				.name(requestDTO.getName())
				.email(requestDTO.getEmail())
				.userType(UserType.UNVERIFIED)
				.introduce(requestDTO.getIntroduce())
				.statusChangedAt(LocalDateTime.now())
				.build();

		when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
		when(userRepository.findByUserId(anyString())).thenReturn(Optional.empty());
		when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
		when(userRepository.save(any(User.class))).thenReturn(user);

		// When
		UserResponseDTO responseDTO = userService.createUser(requestDTO);


		// Then
		assertThat(responseDTO.getUserId()).isEqualTo("test12345678");
		assertThat(responseDTO.getName()).isEqualTo("test");
		assertThat(responseDTO.getEmail()).isEqualTo("test@test.com");
	}

	@Test
	@DisplayName("회원 가입 테스트 실패(동일 id)")
	public void 회원가입테스트실패() {
		// Given
		UserRequestDTO requestDTO = new UserRequestDTO();
		requestDTO.setUserId("test12345678");
		requestDTO.setPassword("Test123456789!@");
		requestDTO.setName("test");
		requestDTO.setEmail("test@test.com");
		requestDTO.setIntroduce("test");

		User user = User.builder()
				.userId(requestDTO.getUserId())
				.password("encodedPassword")
				.name(requestDTO.getName())
				.email(requestDTO.getEmail())
				.userType(UserType.UNVERIFIED)
				.introduce(requestDTO.getIntroduce())
				.statusChangedAt(LocalDateTime.now())
				.build();

		when(userRepository.findByUserId(anyString())).thenReturn(Optional.of(user));

		// When & Then
		UserException exception = assertThrows(UserException.class, () -> {
			userService.createUser(requestDTO);
		});

		assertThat(exception.getMessage()).isEqualTo("중복된 id 입니다.");

	}

	@Test
	@DisplayName("회원 탈퇴 테스트 성공")
	public void 회원탈퇴성공() {
		//given
		User user = User.builder()
				.userId("test12345678")
				.password("encodedPassword")
				.userType(UserType.ACTIVE)
				.build();

		PasswordRequestDTO requestDTO = new PasswordRequestDTO();
		requestDTO.setPassword("Test123456798!!");

		when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
		when(userRepository.save(any(User.class))).thenReturn(user);

		//when
		String userId = userService.withdrawUser(requestDTO, user);

		//then
		assertThat(userId).isEqualTo(user.getUserId());
		assertThat(user.getUserType()).isEqualTo(UserType.DEACTIVATED);
	}

	@Test
	@DisplayName("회원 탈퇴 테스트 실패")
	public void 회원탈퇴실패() {
		//given
		User user = User.builder()
				.userId("test12345678")
				.password("encodedPassword")
				.userType(UserType.UNVERIFIED)
				.build();

		PasswordRequestDTO requestDTO = new PasswordRequestDTO();
		requestDTO.setPassword("Test123456798!!");

		when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

		// When & Then
		UserException exception = assertThrows(UserException.class, () -> {
			userService.withdrawUser(requestDTO, user);
		});

		assertThat(exception.getMessage()).isEqualTo("비밀번호가 일치하지 않습니다.");
	}

	@Test
	@DisplayName("로그아웃 테스트")
	public void 로그아웃() {
		// Given
		User user = User.builder()
				.userId("test12345678")
				.password("encodedPassword")
				.userType(UserType.ACTIVE)
				.build();

		when(userRepository.findByUserId(anyString())).thenReturn(Optional.of(user));

		// When
		userService.logout(user, "accessToken", "refreshToken");

		// Then
		verify(userRepository, times(1)).save(any(User.class));
		verify(jwtUtil, times(1)).invalidateToken("accessToken");
		verify(jwtUtil, times(1)).invalidateToken("refreshToken");
	}

	@Test
	@DisplayName("회원 조회 테스트")
	public void 회원조회(){
		// Given
		User user = User.builder()
				.userId("test12345678")
				.email("test@test.com")
				.build();

		when(userRepository.findByUserId(anyString())).thenReturn(Optional.of(user));

		// When
		UserProfileResponseDTO responseDTO = userService.inquiryUser("test12345678");

		// Then
		assertThat(responseDTO.getUserId()).isEqualTo("test12345678");
		assertThat(responseDTO.getEmail()).isEqualTo("test@test.com");
	}

	@Test
	@DisplayName("회원 프로필 수정 테스트")
	public void testEditProfile() {
		// Given
		User user = User.builder()
				.userId("test12345678")
				.password("encodedPassword")
				.name("test")
				.introduce("test")
				.build();

		UserProfileUpdateRequestDTO requestDTO = new UserProfileUpdateRequestDTO();
		requestDTO.setName("이름수정");
		requestDTO.setIntroduce("내용수정");
		requestDTO.setCurrentPassword("encodedPassword");

		when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
		when(userRepository.save(any(User.class))).thenReturn(user);

		// When
		UserResponseDTO responseDTO = userService.editProfile(requestDTO, user);

		// Then
		assertThat(responseDTO.getName()).isEqualTo("이름수정");
		assertThat(responseDTO.getIntroduce()).isEqualTo("내용수정");
	}

	@Test
	@DisplayName("비밀번호 변경 테스트")
	public void testEditPassword() {
		// Given
		User user = User.builder()
				.userId("test12345678")
				.password("encodedPassword")
				.build();

		UserDetailsImpl userDetails = new UserDetailsImpl(user);

		EditPasswordRequestDTO requestDTO = new EditPasswordRequestDTO();
		requestDTO.setCurrentPassword("Test12345789!!11");
		requestDTO.setNewPassword("Test12345789!@");

		// 현재 비밀번호와 비교할 때는 true를 반환
		when(passwordEncoder.matches(requestDTO.getCurrentPassword(), user.getPassword())).thenReturn(true);
		// 새로운 비밀번호와 기존 비밀번호를 비교할 때는 false를 반환
		when(passwordEncoder.matches(requestDTO.getNewPassword(), user.getPassword())).thenReturn(false);
		when(passwordEncoder.encode(requestDTO.getNewPassword())).thenReturn("newEncodedPassword");
		when(userRepository.save(any(User.class))).thenReturn(user);

		// When
		UserResponseDTO responseDTO = userService.editPassword(requestDTO, userDetails);

		// Then
		assertThat(user.getPassword()).isEqualTo("newEncodedPassword");
	}

}