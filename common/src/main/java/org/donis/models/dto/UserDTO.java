package org.donis.models.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserDTO {

    private Long id;
    private String username;
    private String password;
    private String email;
    private String defaultAccountCurrency;
    private List<AccountDTO> accounts = new ArrayList<>();
}
