package com.metafactory.statementprocessor.controllers;

import com.metafactory.statementprocessor.services.FileService;
import com.metafactory.statementprocessor.services.StatementService;
import com.metafactory.statementprocessor.validations.ValidFile;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Validated
public class StatementController {
    private final FileService fileService;
    private final StatementService statementService;

    @PostMapping
    public ResponseEntity<Void> processStatement(@ValidFile @RequestPart("statement") MultipartFile statement) {
        val inputStream = fileService.getInputStreamFromFile(statement);
        val output = statementService.processStatement(inputStream, statement.getContentType());
        fileService.saveToFile(output);

        return ResponseEntity.ok().build();
    }
}
