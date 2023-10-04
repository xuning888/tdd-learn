package com.example.tdd.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * @author xuning
 * @date 2023/10/4 22:48
 */
public class Configuration {
    private final File file;
    private final String datePattern;
    private final Properties properties = new Properties();

    public Configuration(File file, String datePattern) {
        this.file = file;
        this.datePattern = datePattern;
        loadPropFromFile();
    }

    public static ConfigurationBuilder builder() {
        return new ConfigurationBuilder();
    }

    private void loadPropFromFile() {
        try (FileInputStream fileInputStream = new FileInputStream(this.file);
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8)) {
            this.properties.load(inputStreamReader);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public String getString(String key) {
        return this.properties.getProperty(key);
    }

    public Integer getInt(String key, Integer defaultValue) {
        Object aInt = this.properties.getOrDefault(key, defaultValue);
        return Integer.parseInt(aInt.toString());
    }

    public Date getBirthday() throws ParseException {
        String property = this.properties.getProperty("birthday");
        if (property == null || "".equals(property.trim())) {
            throw new PropertyNotFoundException();
        }
        SimpleDateFormat format = new SimpleDateFormat(datePattern);
        return format.parse(property);
    }

    static class ConfigurationBuilder {

        private File file;

        private String datePattern;

        public ConfigurationBuilder fromFile(File file) {
            this.file = file;
            return this;
        }

        public ConfigurationBuilder dateFormat(String datePattern) {
            this.datePattern = datePattern;
            return this;
        }

        public Configuration build() {
            return new Configuration(file, datePattern);
        }
    }
}
