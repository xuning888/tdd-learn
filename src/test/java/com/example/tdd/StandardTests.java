package com.example.tdd;


import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * @author xuning
 * @date 2023/10/5 12:43
 */
public class StandardTests {

    private static final Logger logger = LoggerFactory.getLogger(StandardTests.class);

    @BeforeAll
    static void initAll() {
        logger.info("Before All");
    }

    @AfterAll
    static void tearDownAll() {
        logger.info("After All");
    }

    @BeforeEach
    void init() {
        logger.info("Before Each");
    }

    @AfterEach
    void tearDown() {
        logger.info("After Each");
    }

    @Test
    void succeedingTest() {
        logger.info("succeeding test");
    }

    @Test
    void failingTest() {
        logger.info("a failing test");
    }

    @Test
    @Disabled("for demonstration purposes")
    void skippedTest() {
    }

    @Test
    void abortedTest() {
        assumeTrue("abc".contains("Z"));
        fail("test should have been aborted");
    }
}
