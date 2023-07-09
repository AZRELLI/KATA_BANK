package fr.kataBank.service;


import fr.kataBank.domain.Account;
import fr.kataBank.domain.Operation;
import fr.kataBank.domain.dto.AccountDto;
import fr.kataBank.repository.AccountRepository;
import fr.kataBank.utils.NoSuchAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AccountService {

    private final AccountRepository bankAccountRepository;

    private final AccountDtoMapper accountDtoMapper;

    public AccountService(AccountRepository bankAccountRepository, AccountDtoMapper accountDtoMapper) {
        this.bankAccountRepository = bankAccountRepository;
        this.accountDtoMapper = accountDtoMapper;
    }

    /**
     *
     * @param accountId account identifier
     * @return operations list of a given account
     * @throws NoSuchAccountException
     */
    public List<Operation> listAllOperations(long accountId) throws NoSuchAccountException {
        Optional<Account> optionalBankAccount = bankAccountRepository.findById(accountId);
        if(!optionalBankAccount.isPresent()){
            throw new NoSuchAccountException(": "+accountId);
        }
        return optionalBankAccount.get().operations;
    }
}
