package org.donis.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {

    //NOTE: A custom generator can be used here in order to comply with banking and provide unicity
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID accountNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private Currency currency;

    @Column(name = "balance")
    private BigDecimal balance = BigDecimal.valueOf(0);
}
