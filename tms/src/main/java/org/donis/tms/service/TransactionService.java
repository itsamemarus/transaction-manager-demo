package org.donis.tms.service;

import jakarta.transaction.Transactional;
import org.donis.models.dto.AccountDTO;
import org.donis.models.dto.UserDTO;
import org.donis.models.entities.Account;
import org.donis.repositories.AccountRepository;
import org.donis.repositories.TransactionDetailsRepository;
import org.donis.tms.feign.UserProfileClient;
import org.donis.models.entities.TransactionDetails;
import org.donis.tms.models.TransactionRequestDTO;
import org.donis.models.TransactionType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


//TODO: Test me
@Service
public class TransactionService {

    private final UserProfileClient userProfileClient;
    private final AccountRepository accountRepository;
    private final TransactionDetailsRepository transactionDetailsRepository;

    public TransactionService(UserProfileClient userProfileClient,
                              AccountRepository accountRepository,
                              TransactionDetailsRepository transactionDetailsRepository) {
        this.userProfileClient = userProfileClient;
        this.accountRepository = accountRepository;
        this.transactionDetailsRepository = transactionDetailsRepository;
    }

    @Transactional
    public String processTransactionRequest(String token, TransactionRequestDTO transactionRequestDTO) {

        //NOTE: Validation should be done at the DTO level before processing. For example the amount given by the user should be positive.
        //Probably some request level validation on the DTO.

        //NOTE: The call to external should always be wrapped into try/catch or some sort of mechanism to handle a potential error
        UserDTO user = userProfileClient.getUserInfo(token);

        List<AccountDTO> authUserAccounts = user.getAccounts();
        List<String> authUserAccountsNumbers = authUserAccounts.stream().map(AccountDTO::getAccountNumber).toList();

        TransactionType transactionType = transactionRequestDTO.getTransactionType();
        switch (transactionType) {
            case TOPUP -> {
                if (authUserAccountsNumbers.contains(transactionRequestDTO.getAccountNumber())) {
                    Optional<Account> accountOpt = accountRepository.findById(UUID.fromString(transactionRequestDTO.getAccountNumber()));

                    if (accountOpt.isPresent()) {
                        Account userAccount = accountOpt.get();
                        //NOTE: Probably in a proper implementation the currency could be taken into consideration and a conversion should be done

                        handleAccountBalanceUpdate(null, userAccount, transactionRequestDTO.getAmount());
                        handleNewTransaction(transactionRequestDTO, null, userAccount);
                        return "Topup successful";
                    }
                }
            }

            case TRANSFER -> {
                AccountDTO sendingAccountDTO = authUserAccounts.stream()
                        .filter(account -> account.getCurrency() == transactionRequestDTO.getCurrency())
                        .findFirst()
                        .orElse(authUserAccounts.get(0));

                //Note: Custom exception should be created for proper handling of errors
                Account sendingAccount = accountRepository.findById(UUID.fromString(sendingAccountDTO.getAccountNumber()))
                        .orElseThrow(() -> new RuntimeException("Sending account not found"));

                Account receivingAccount = accountRepository.findById(UUID.fromString(transactionRequestDTO.getAccountNumber()))
                        .orElseThrow(() -> new RuntimeException("Receiving account not found"));


                BigDecimal transferAmount = transactionRequestDTO.getAmount();
                //NOTE: A validation chain or validation bean should be implemented to properly handle the validation.
                if (sendingAccount.getBalance().compareTo(transferAmount) < 0) {
                    throw new RuntimeException("Insufficient funds");
                }

                handleAccountBalanceUpdate(sendingAccount, receivingAccount, transferAmount);
                handleNewTransaction(transactionRequestDTO, sendingAccount, receivingAccount);

                return String.format("'%s' successful", transactionRequestDTO.getTransactionType());
            }

        }

        return String.format("'%s' unsuccessful.", transactionRequestDTO.getTransactionType());
    }

    private void handleAccountBalanceUpdate(Account sendingAccount, Account receivingAccount, BigDecimal transferAmount) {
        if (Optional.ofNullable(sendingAccount).isPresent()) {
            sendingAccount.setBalance(sendingAccount.getBalance().subtract(transferAmount));
        }

        if (Optional.ofNullable(receivingAccount).isPresent()) {
            receivingAccount.setBalance(receivingAccount.getBalance().add(transferAmount));
        }

        List<Account> possibleModifiedAccounts = Arrays.asList(sendingAccount, receivingAccount);
        accountRepository.saveAll(possibleModifiedAccounts.stream().filter(Objects::nonNull).collect(Collectors.toSet()));
    }

    private void handleNewTransaction(TransactionRequestDTO transactionRequestDTO,
                                      Account sendingAccount,
                                      Account receivingAccount) {
        TransactionDetails transactionDetails = new TransactionDetails();
        transactionDetails.setTransactionType(transactionRequestDTO.getTransactionType());
        transactionDetails.setSendingAccount(sendingAccount);
        transactionDetails.setReceivingAccount(receivingAccount);
        transactionDetails.setAmount(transactionRequestDTO.getAmount());

        transactionDetailsRepository.save(transactionDetails);
    }
}
