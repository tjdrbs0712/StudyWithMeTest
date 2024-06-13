package december.spring.studywithme.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import december.spring.studywithme.dto.ErrorMessage;
import december.spring.studywithme.dto.ResponseMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		Map<String, String> errors = new HashMap<>();
		e.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});

		ErrorMessage errorMessage = ErrorMessage.builder()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.message(errors.toString())
				.build();

		return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler({UserException.class, PostException.class, EmailException.class,
			LikeException.class, CommentException.class, PageException.class})
	public ResponseEntity<ErrorMessage> handleNormalException(Exception e) {

		ErrorMessage errorMessage = ErrorMessage.builder()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.message(e.getMessage())
				.build();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
	}
	
	@ExceptionHandler(NoContentException.class)
	public ResponseEntity<ResponseMessage<Void>> handleNoContentException(NoContentException e) {
		ResponseMessage<Void> responseMessage = ResponseMessage.<Void>builder()
				.statusCode(HttpStatus.OK.value())
				.message(e.getMessage())
				.build();

		return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
	}
	
}
