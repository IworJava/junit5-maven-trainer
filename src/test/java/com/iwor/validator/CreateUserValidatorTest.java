package com.iwor.validator;

import com.iwor.dto.CreateUserDto;
import com.iwor.entity.Gender;
import com.iwor.entity.Role;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;

class CreateUserValidatorTest {
    private final CreateUserValidator createUserValidator = CreateUserValidator.getInstance();

    @Test
    void validate() {
        CreateUserDto createUserDto = CreateUserDto.builder()
                .name("Ivan")
                .birthday("1990-05-23")
                .email("1@1")
                .password("123")
                .role(Role.USER.name())
                .gender(Gender.MALE.name())
                .build();

        ValidationResult actualResult = createUserValidator.validate(createUserDto);

        assertFalse(actualResult.hasErrors());
    }

    @Test
    void invalidBirthday() {
        CreateUserDto createUserDto = CreateUserDto.builder()
                .name("Ivan")
                .birthday("1990-05-23 12:23")
                .email("1@1")
                .password("123")
                .role(Role.USER.name())
                .gender(Gender.MALE.name())
                .build();

        ValidationResult actualResult = createUserValidator.validate(createUserDto);

        assertAll(
                () -> assertThat(actualResult.getErrors()).hasSize(1),
                () -> assertThat(actualResult.getErrors().get(0).getCode()).contains("birthday")
        );
    }

    @Test
    void invalidRole() {
        CreateUserDto createUserDto = CreateUserDto.builder()
                .name("Ivan")
                .birthday("1990-05-23")
                .email("1@1")
                .password("123")
                .role("dummy")
                .gender(Gender.MALE.name())
                .build();

        ValidationResult actualResult = createUserValidator.validate(createUserDto);

        assertAll(
                () -> assertThat(actualResult.getErrors()).hasSize(1),
                () -> assertThat(actualResult.getErrors().get(0).getMessage()).containsIgnoringCase("role")
        );
    }

    @Test
    void invalidGenre() {
        CreateUserDto createUserDto = CreateUserDto.builder()
                .name("Ivan")
                .birthday("1990-05-23")
                .email("1@1")
                .password("123")
                .role(Role.USER.name())
                .gender("dummy")
                .build();

        ValidationResult actualResult = createUserValidator.validate(createUserDto);

        assertAll(
                () -> assertThat(actualResult.getErrors()).hasSize(1),
                () -> assertThat(actualResult.getErrors().get(0).getMessage()).containsIgnoringCase("gender")
        );
    }

    @Test
    void invalidBirthdayRoleGenre() {
        CreateUserDto createUserDto = CreateUserDto.builder()
                .name("Ivan")
                .birthday("1990-05-23 12:23")
                .email("1@1")
                .password("123")
                .role("dummy")
                .gender("dummy")
                .build();

        ValidationResult actualResult = createUserValidator.validate(createUserDto);

        List<Error> errors = actualResult.getErrors();
        assertAll(
                () -> assertThat(errors).hasSize(3),
                () -> assertThat(errors.stream().map(Error::getCode).toList())
                        .contains("invalid.birthday", "invalid.gender", "invalid.role")
        );
    }
}