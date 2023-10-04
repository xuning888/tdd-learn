package com.example.tdd.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author xuning
 * @date 2023/10/4 23:51
 */
public class ConfigurationTest2 {

    @TempDir
    Path tempDir;

    private Configuration instance;

    @BeforeEach
    void setUp() throws IOException {
        Path file = tempDir.resolve("conf.properties");
        Files.write(file, Arrays.asList("birthday=2002-05-11", "size=15",
                "closed=true", "locked=false", "salary=12.5", "name=张三", "noneValue="));
        instance = Configuration.builder()
                .fromFile(file.toFile())
                .dateFormat("yyyy-MM-dd")
                .build();
    }

    @Test
    void get_string_without_defaultValue_happy() {
        assertThat(instance.getString("name")).isEqualTo("张三");
    }

    @Test
    void get_int_with_defaultValue_and_with_value() {
        assertThat(instance.getInt("size", 1000)).isEqualTo(15);
    }
}
