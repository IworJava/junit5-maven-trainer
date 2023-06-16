package com.iwor.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LocalDateFormatterTest {

    @Test
    void format() {
        String date = "2023-06-16";

        LocalDate actualResult = LocalDateFormatter.format(date);

        assertThat(actualResult).isEqualTo(LocalDate.of(2023, 6, 16));
    }

    @Test
    void shouldThrowExceptionIfDateInvalid() {
        String date = "2023-06-16 16:58";

        assertThrows(DateTimeParseException.class, () -> LocalDateFormatter.format(date));
    }

    @ParameterizedTest
    @MethodSource("getValidationArguments")
    void isValid(String date, boolean expectedResult) {
        boolean actualResult = LocalDateFormatter.isValid(date);

        assertEquals(expectedResult, actualResult);
    }

    static Stream<Arguments> getValidationArguments() {
        return Stream.of(
                Arguments.of("2023-06-16", true),
                Arguments.of("2023-06-16 16:58", false),
                Arguments.of("01-01-2001", false),
                Arguments.of(null, false)
        );
    }
}