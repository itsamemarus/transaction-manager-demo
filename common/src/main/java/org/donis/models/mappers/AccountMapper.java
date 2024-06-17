package org.donis.models.mappers;

import org.donis.models.dto.AccountDTO;
import org.donis.models.entities.Account;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    Account toEntity(AccountDTO accountDTO);
    AccountDTO toDTO(Account account);
}
