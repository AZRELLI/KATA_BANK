package fr.kataBank.service;

import fr.kataBank.domain.Account;
import fr.kataBank.domain.Operation;
import fr.kataBank.domain.dto.AccountDto;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AccountDtoMapper {
    public AccountDtoMapper() {
    }

    public AccountDto mapEntityToDto(Account account){
        AccountDto accountDto = new AccountDto();
        accountDto.setBalance(account.getBalance());
        List<Operation> recentOps = account.getOperations()
                .stream()
                .sorted(Comparator.comparing(Operation::getDate).reversed())
                .collect(Collectors.toList());
        accountDto.setOperationsList(recentOps);
        return accountDto;
    }
}
