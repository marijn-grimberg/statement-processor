package com.metafactory.statementprocessor.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.metafactory.statementprocessor.exceptions.StatementServiceException;
import com.metafactory.statementprocessor.models.Record;
import com.metafactory.statementprocessor.models.Records;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.metafactory.statementprocessor.services.FileService.CONTENT_TYPE_XML;

@Service
@RequiredArgsConstructor
public class StatementService {
    public byte[] processStatement(InputStream inputStream, String contentType) throws StatementServiceException {
        val records = readRecords(inputStream, contentType);
        val failedRecords = getFailedRecords(records);
        return writeRecordsToByteArray(failedRecords);
    }

    private List<Record> readRecords(InputStream inputStream, String contentType) {
        val objectReader = CONTENT_TYPE_XML.equals(contentType) ? createXmlObjectReader() : createCsvObjectReader();
        try {
            return objectReader.<Record>readValues(new InputStreamReader(inputStream)).readAll();
        } catch (IOException e) {
            throw new StatementServiceException("An error occurred while trying to read records", e);
        }
    }

    private ObjectReader createXmlObjectReader() {
        val xmlMapper = new XmlMapper();
        return xmlMapper.readerFor(Record.class);
    }

    private ObjectReader createCsvObjectReader() {
        val csvMapper = new CsvMapper();
        val schema = csvMapper.schemaFor(Record.class).withSkipFirstDataRow(true);
        return csvMapper.readerFor(Record.class).with(schema);
    }

    private Records getFailedRecords(List<Record> records) {
        val failedRecords = records.stream()
                .filter(record -> Collections.frequency(records, record) > 1 || !record.isValid())
                .map(record -> Record.builder().reference(record.getReference()).description(record.getDescription()).build())
                .collect(Collectors.toList());

        return Records.builder().records(failedRecords).build();
    }

    private byte[] writeRecordsToByteArray(Records records) {
        ObjectMapper objectMapper = new XmlMapper();
        try {
            return objectMapper.writeValueAsBytes(records);
        } catch (JsonProcessingException e) {
            throw new StatementServiceException("An error occurred while trying to write failed records", e);
        }
    }
}
