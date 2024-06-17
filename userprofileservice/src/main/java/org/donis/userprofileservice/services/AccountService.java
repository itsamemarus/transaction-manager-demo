package org.donis.userprofileservice.services;

import jakarta.transaction.Transactional;
import org.donis.models.dto.AccountDTO;
import org.donis.models.entities.Account;
import org.donis.models.entities.User;
import org.donis.models.mappers.AccountMapper;
import org.donis.repositories.AccountRepository;
import org.donis.userprofileservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;


    @Transactional
    public AccountDTO createAccount(String username, AccountDTO accountDTO){
        User user = userRepository.findByUsername(username);

        if (user == null){
            throw new UsernameNotFoundException("User not found: " + username);
        }

        Account newAccount = new Account();

        newAccount.setCurrency(accountDTO.getCurrency());
        newAccount.setBalance(accountDTO.getBalance());

        newAccount.setUser(user);

        return AccountMapper.INSTANCE.toDTO(accountRepository.save(newAccount));

    }
}
