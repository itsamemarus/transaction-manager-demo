package org.donis.tms.models;

import lombok.Data;
import org.donis.models.TransactionType;
import org.donis.models.entities.Currency;

import java.math.BigDecimal;

@Data
public class TransactionRequestDTO {
    private BigDecimal amount;
    private Currency currency;
    private TransactionType transactionType;
    private String accountNumber;
}
