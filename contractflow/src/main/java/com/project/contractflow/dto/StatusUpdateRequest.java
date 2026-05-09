package com.project.contractflow.dto;

import com.project.contractflow.entity.ContractStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class StatusUpdateRequest {
    @NotNull
    private ContractStatus status;
}
