package com.metafactory.statementprocessor.services;

import com.metafactory.statementprocessor.exceptions.FileServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Service
@RequiredArgsConstructor
public class FileService {
    public static final String CONTENT_TYPE_XML = "text/xml";
    public static final String CONTENT_TYPE_CSV = "text/csv";

    @Value("${output.path}")
    private String outputPath;

    public InputStream getInputStreamFromFile(MultipartFile statement) throws FileServiceException {
        try {
            return statement.getInputStream();
        } catch (IOException e) {
            throw new FileServiceException("An error occurred while trying to read statement file", e);
        }
    }

    public void saveToFile(byte[] output) {
        try (OutputStream out = new FileOutputStream(outputPath + "/" + System.currentTimeMillis() + ".xml")) {
            out.write(output);
        } catch (IOException e) {
            throw new FileServiceException("An error occurred while trying to save statement file to directory", e);
        }
    }
}
