package io.makai.validator;

import io.makai.exception.ApiException;
import io.makai.payload.dto.UserDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserDtoValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return cls.getClass().equals(UserDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {

        UserDto user = (UserDto) target;

        if (user.getUsername() == null && user.getPassword() == null)
            throw new ApiException("No fields were sent to change");

        if (user.getUsername() != null) {
            if (user.getUsername().length() < 3 || user.getUsername().length() > 12)
                errors.rejectValue("username", "Length", "size must be between 6 and 24");
        }

        if (user.getPassword() != null) {
            // TODO: Add regex for password validation:
            // String regex = "^(?=.*[0-9])"
            //        + "(?=.*[a-z])(?=.*[A-Z])"
            //        + "(?=.*[@#$%^&+=])"
            //        + "(?=\\S+$).{8,20}$";

            if (user.getPassword().length() < 6 || user.getPassword().length() > 24)
                errors.rejectValue("password", "Length", "size must be between 3 and 12");
        }

    }
}
