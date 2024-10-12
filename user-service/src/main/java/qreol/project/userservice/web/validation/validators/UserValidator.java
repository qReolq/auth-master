package qreol.project.userservice.web.validation.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import qreol.project.userservice.model.User;
import qreol.project.userservice.model.exception.ResourceNotValidException;
import qreol.project.userservice.repository.UserRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserValidator implements Validator {

    private final UserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        validateEmail(user, errors);
        validateUsername(user, errors);

        checkError(errors);
    }

    private void validateUsername(User user, Errors errors) {
        Optional<User> foundUserByUsername = userRepository
                .findUserByUsername(user.getUsername());

        if (foundUserByUsername.isPresent()) {
            errors.rejectValue(
                    "username",
                    "",
                    "This username is already taken"
            );
        }
    }

    private void validateEmail(User user, Errors errors) {
        Optional<User> foundUserByUsername = userRepository
                .findUserByEmail(user.getEmail());

        if (foundUserByUsername.isPresent()) {
            errors.rejectValue(
                    "email",
                    "",
                    "This email is already taken"
            );
        }
    }

    private void checkError(Errors errors) {
        if (errors.hasErrors()) {
            throw new ResourceNotValidException(errors.getFieldErrors());
        }
    }

}
