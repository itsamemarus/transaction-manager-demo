package org.donis.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.donis.models.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name="transactions")
public class TransactionDetails {


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sending_account", nullable = true)
    private Account sendingAccount;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiving_account", nullable = false)
    private Account receivingAccount;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "created_at")
    private LocalDateTime created_at = LocalDateTime.now();
}
