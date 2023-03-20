package com.metafactory.statementprocessor.services;

import com.metafactory.statementprocessor.exceptions.StatementServiceException;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Files;
import java.nio.file.Paths;

import static com.metafactory.statementprocessor.services.FileService.CONTENT_TYPE_CSV;
import static com.metafactory.statementprocessor.services.FileService.CONTENT_TYPE_XML;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class StatementServiceTest {
    @InjectMocks
    private StatementService statementService;

    @Test
    @SneakyThrows
    void processStatement_withXmlInputContainingInvalidBalances_returnsFailedRecords() {
        // Arrange
        byte[] actualOutput;

        // Act
        try (val inputStream = Files.newInputStream(Paths.get("src/test/resources/input/records.xml"))) {
            actualOutput = statementService.processStatement(inputStream, CONTENT_TYPE_XML);
        }

        // Assert
        assertArrayEquals(Files.readAllBytes(Paths.get("src/test/resources/output/1679237103312.xml")), actualOutput);
    }

    @Test
    @SneakyThrows
    void processStatement_withCsvInputContainingDuplicates_returnsFailedRecords() {
        // Arrange
        byte[] actualOutput;

        // Act
        try (val inputStream = Files.newInputStream(Paths.get("src/test/resources/input/records.csv"))) {
            actualOutput = statementService.processStatement(inputStream, CONTENT_TYPE_CSV);
        }

        // Assert
        assertArrayEquals(Files.readAllBytes(Paths.get("src/test/resources/output/1679237061408.xml")), actualOutput);
    }

    @Test
    @SneakyThrows
    void processStatement_withInvalidFile_throwsException() {
        // Arrange
        Executable executable;

        // Act
        try (val inputStream = Files.newInputStream(Paths.get("src/test/resources/input/invalid_file.xml"))) {
            executable = () -> statementService.processStatement(inputStream, CONTENT_TYPE_XML);
        }

        // Assert
        assertThrows(StatementServiceException.class, executable);
    }
}
