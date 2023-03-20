package com.metafactory.statementprocessor.validations;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.metafactory.statementprocessor.services.FileService.CONTENT_TYPE_CSV;
import static com.metafactory.statementprocessor.services.FileService.CONTENT_TYPE_XML;

public class FileValidator implements ConstraintValidator<ValidFile, MultipartFile> {
    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {
        String contentType = multipartFile.getContentType();

        return contentType != null && isSupportedContentType(contentType);
    }

    private boolean isSupportedContentType(String contentType) {
        return contentType.equals(CONTENT_TYPE_XML)
                || contentType.equals(CONTENT_TYPE_CSV);
    }
}
