package com.iwor.mapper;

import com.iwor.dto.CreateUserDto;
import com.iwor.entity.Gender;
import com.iwor.entity.Role;
import com.iwor.entity.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class CreateUserMapperTest {
    private final CreateUserMapper mapper = CreateUserMapper.getInstance();

    @Test
    void map() {
        CreateUserDto dto = CreateUserDto.builder()
                .name("Ivan")
                .birthday("1990-05-23")
                .email("1@1")
                .password("123")
                .role(Role.USER.name())
                .gender(Gender.MALE.name())
                .build();

        User actualResult = mapper.map(dto);
        User expectedResult = User.builder()
                .name("Ivan")
                .birthday(LocalDate.of(1990, 5, 23))
                .email("1@1")
                .password("123")
                .role(Role.USER)
                .gender(Gender.MALE)
                .build();

        assertThat(actualResult).isEqualTo(expectedResult);
    }
}