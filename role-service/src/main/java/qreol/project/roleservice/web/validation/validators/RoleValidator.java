package qreol.project.roleservice.web.validation.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import qreol.project.roleservice.model.Role;
import qreol.project.roleservice.model.exception.ResourceNotValidException;
import qreol.project.roleservice.repository.RoleRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RoleValidator implements Validator {

    private final RoleRepository roleRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Role.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Role role = (Role) target;

        validateName(role, errors);

        checkError(errors);
    }

    private void validateName(Role role, Errors errors) {
        Optional<Role> foundUserByName = roleRepository
                .findRoleByName(role.getName());

        if (foundUserByName.isPresent()) {
            errors.rejectValue(
                    "name",
                    "",
                    "This name is already taken"
            );
        }
    }

    private void checkError(Errors errors) {
        if (errors.hasErrors()) {
            throw new ResourceNotValidException(errors.getFieldErrors());
        }
    }

}
