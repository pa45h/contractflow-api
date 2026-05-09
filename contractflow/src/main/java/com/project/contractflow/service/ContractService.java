package com.project.contractflow.service;

import com.project.contractflow.dto.QuestionRequest;
import com.project.contractflow.dto.StatusUpdateRequest;
import com.project.contractflow.entity.Contract;
import com.project.contractflow.entity.ContractStatus;
import com.project.contractflow.repository.ContractRepository;
import com.project.contractflow.util.DocumentParserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;
    private final DocumentParserUtil documentParserUtil;

    @Value("${file.upload-dir}")
    private String uploadDirectory;

    public Contract createContract(String title, MultipartFile file) throws IOException {

        if (!file.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
            throw new RuntimeException("Only PDF files are supported");
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        File destination = new File(uploadDirectory, fileName);
        destination.getParentFile().mkdirs();
        file.transferTo(destination);

        String extractedText = documentParserUtil.extractText(destination);

        Contract contract = Contract.builder()
                .title(title)
                .fileName(fileName)
                .filePath(destination.getAbsolutePath())
                .extractedText(extractedText)
                .status(ContractStatus.DRAFT)
                .createdAt(LocalDateTime.now())
                .build();

        return contractRepository.save(contract);
    }

    public Contract getContract(Long id) {
        return contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contract not found"));
    }

    public Contract updateStatus(Long id, StatusUpdateRequest statusUpdateRequest) {

        Contract contract = getContract(id);

        validateStatusTransition(contract.getStatus(), statusUpdateRequest.getStatus());

        contract.setStatus(statusUpdateRequest.getStatus());

        return contractRepository.save(contract);
    }

    private void validateStatusTransition(ContractStatus currentStatus, ContractStatus newStatus) {

        if (currentStatus == newStatus) {
            return;
        }

        if (currentStatus == ContractStatus.DRAFT && newStatus == ContractStatus.REVIEW) {
            return;
        }

        if (currentStatus == ContractStatus.REVIEW && newStatus == ContractStatus.APPROVED) {
            return;
        }

        throw new RuntimeException("Invalid status transition from " + currentStatus + " to " + newStatus);
    }

    public String askQuestion(Long id, QuestionRequest questionRequest) {

        Contract contract = getContract(id);

        String extractedText = contract.getExtractedText();

        String question = questionRequest.getQuestion().toLowerCase();

        List<String> keywords = Arrays.asList(question.split(" "));

        String[] paragraphs = extractedText.split("\\n");

        for (String paragraph : paragraphs) {
            String lowerParagraph = paragraph.toLowerCase();

            for (String keyword : keywords) {
                if (lowerParagraph.contains(keyword)) {
                    return paragraph;
                }
            }

        }

        return "No relevant information found in the contract.";
    }

}
