package com.metafactory.statementprocessor.controllers;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import java.io.File;

import static com.metafactory.statementprocessor.services.FileService.CONTENT_TYPE_CSV;
import static com.metafactory.statementprocessor.services.FileService.CONTENT_TYPE_XML;
import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = StatementControllerTest.Initializer.class)
public class StatementControllerTest {
    @TempDir
    static File tempDir;

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "/statement";
    }

    @Test
    public void processStatement_withXmlFile_returnsOk() {
        given()
                .multiPart("statement", new File("src/test/resources/input/records.xml"), CONTENT_TYPE_XML)
        .when()
                .post("/")
        .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void processStatement_withCsvFile_returnsOk() {
        given()
                .multiPart("statement", new File("src/test/resources/input/records.csv"), CONTENT_TYPE_CSV)
        .when()
                .post("/")
        .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void processStatement_withTxtFile_returnsBadRequest() {
        given()
                .multiPart("statement", new File("src/test/resources/input/records.txt"), MediaType.TEXT_PLAIN_VALUE)
        .when()
                .post("/")
        .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void processStatement_witInvalidXmlFile_returnsBadRequest() {
        given()
                .multiPart("statement", new File("src/test/resources/input/invalid_file.xml"), CONTENT_TYPE_XML)
        .when()
                .post("/")
        .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext context) {
            TestPropertyValues.of("output.path=" + tempDir).applyTo(context);
        }
    }
}
