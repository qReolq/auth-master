package qreol.project.userservice.model.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.FieldError;

import java.util.List;

@AllArgsConstructor
@Getter
public class ResourceNotValidException extends RuntimeException {

    private List<FieldError> errors;

}