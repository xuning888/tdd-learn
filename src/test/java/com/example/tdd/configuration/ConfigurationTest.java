package com.example.tdd.configuration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author xuning
 * @date 2023/10/4 22:43
 */
public class ConfigurationTest {

    /**
     * 注解创建了一个临时目录
     */
    @TempDir
    Path tempDir;


    @Test
    void get_string_without_defaultValue_happy() throws Exception {
        Path file = tempDir.resolve("conf.properties");
        Files.write(file, Arrays.asList("birthday=2002-05-11", "size=15",
                "closed=true", "locked=false", "salary=12.5", "name=张三", "noneValue="));
        Configuration instance = Configuration.builder()
                .fromFile(file.toFile())
                .dateFormat("yyyy-MM-dd")
                .build();
        assertThat(instance.getString("name")).isEqualTo("张三");
    }

    @Test
    void get_int_with_defaultValue_and_with_value() throws Exception {
        Path file = tempDir.resolve("conf.properties");
        Files.write(file, Arrays.asList("birthday=2002-05-11", "size=15",
                "closed=true", "locked=false", "salary=12.5", "name=张三", "noneValue="));
        Configuration instance = Configuration.builder()
                .fromFile(file.toFile())
                .dateFormat("yyyy-MM-dd")
                .build();
        assertThat(instance.getInt("size", 1000)).isEqualTo(15);
    }

    @Test
    void get_int_with_defaultValue_and_without_value() throws Exception {
        Path file = tempDir.resolve("conf.properties");
        Files.write(file, Arrays.asList("birthday=2002-05-11",
                "closed=true", "locked=false", "salary=12.5", "name=张三", "noneValue="));
        Configuration instance = Configuration.builder()
                .fromFile(file.toFile())
                .dateFormat("yyyy-MM-dd")
                .build();
        assertThat(instance.getInt("size", 1000)).isEqualTo(1000);
    }

    @Test
    void get_date() throws IOException, ParseException {
        Path file = tempDir.resolve("conf.properties");
        Files.write(file, Arrays.asList("birthday=2002-05-11", "size=15",
                "closed=true", "locked=false", "salary=12.5", "name=张三", "noneValue="));
        Configuration instance = Configuration.builder()
                .fromFile(file.toFile())
                .dateFormat("yyyy-MM-dd")
                .build();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = dateFormat.parse("2002-05-11");
        assertThat(instance.getBirthday()).isEqualTo(parse);
    }

    @Test
    void get_date_with_black() throws IOException {
        Path file = tempDir.resolve("conf.properties");
        Files.write(file, Arrays.asList("birthday= ", "size=15",
                "closed=true", "locked=false", "salary=12.5", "name=张三", "noneValue="));
        Configuration instance = Configuration.builder()
                .fromFile(file.toFile())
                .dateFormat("yyyy-MM-dd")
                .build();
        assertThrows(PropertyNotFoundException.class, instance::getBirthday);
    }

    @Test
    void get_date_throws_ParseException() throws IOException {
        Path file = tempDir.resolve("conf.properties");
        Files.write(file, Arrays.asList("birthday=2002-05-11", "size=15",
                "closed=true", "locked=false", "salary=12.5", "name=张三", "noneValue="));
        Configuration instance = Configuration.builder()
                .fromFile(file.toFile())
                .dateFormat("yyyy/MM/dd")
                .build();
        assertThrows(ParseException.class, instance::getBirthday);
    }
}
