package org.donis.models.dto;

import lombok.Data;
import org.donis.models.entities.Currency;

import java.math.BigDecimal;

@Data
public class AccountDTO {

    // Will treat this as some sort of IBAN
    private String accountNumber;
    private Currency currency;
    private BigDecimal balance;
}
