package com.project.contractflow.util;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;

@Component
public class DocumentParserUtil {
    public String extractText(File file) throws IOException {
        String fileName = file.getName().toLowerCase();

        if (!fileName.endsWith(".pdf")) {
            throw new InvalidParameterException("only pdf files are supported");
        }

        return extractTextFromPdf(file);
    }

    private String extractTextFromPdf(File file) throws IOException {
        try (PDDocument document = Loader.loadPDF(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }
}
