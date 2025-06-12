package ru.dzrnl.apigateway.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferDto {
    private long fromAccountId;
    private long toAccountId;
    private long amount;
}
