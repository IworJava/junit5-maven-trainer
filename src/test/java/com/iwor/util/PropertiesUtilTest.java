package com.iwor.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PropertiesUtilTest {

    @ParameterizedTest
    @MethodSource("getPropertiesArguments")
    void get(String key, String expectedValue) {
        String actualValue = PropertiesUtil.get(key);

        assertEquals(expectedValue, actualValue);
    }

    static Stream<Arguments> getPropertiesArguments() {
        return Stream.of(
                Arguments.of("db.url", "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"),
                Arguments.of("db.user", "sa"),
                Arguments.of("db.password", ""),
                Arguments.of("dummy", null)
        );
    }
}