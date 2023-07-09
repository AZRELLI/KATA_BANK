package fr.kataBank.service;

import com.google.common.annotations.VisibleForTesting;
import fr.kataBank.domain.Account;
import fr.kataBank.domain.Operation;
import fr.kataBank.domain.OperationType;
import fr.kataBank.domain.dto.AccountDto;
import fr.kataBank.repository.AccountRepository;
import fr.kataBank.repository.OperationRepository;
import fr.kataBank.utils.NoSuchAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional
public class OperationService {

    private final OperationRepository operationRepository;
    private final AccountRepository accountRepository;
    private AccountDtoMapper accountDtoMapper;

    public OperationService(OperationRepository operationRepository, AccountRepository accountRepository, AccountDtoMapper dtoMapper) {
        this.operationRepository = operationRepository;
        this.accountRepository = accountRepository;
        this.accountDtoMapper = dtoMapper;
    }

    /**
     * make a withdrawal from a given account
     * @param accountId the account identifier
     * @param amount the amount of the transaction
     * @throws NoSuchAccountException
     */
    public AccountDto doWithdrawal(long accountId, long amount) throws NoSuchAccountException {
        Operation operation = executeOperation(accountId,amount,OperationType.WITHDRAWAL);
        Account bankAccount = accountRepository.findById(accountId).get();
        bankAccount.getOperations().add(operation);
        return accountDtoMapper.mapEntityToDto(bankAccount);
    }


    /**
     * deposit the specified amount into the specified account
     * @param accountId the account identifier
     * @param amount the amount of the transaction
     * @throws NoSuchAccountException
     */
    public AccountDto doDeposit(long accountId, long amount) throws NoSuchAccountException {
        Operation operation = executeOperation(accountId,amount,OperationType.DEPOSIT);
        Optional<Account> accountReturned = accountRepository.findById(accountId);
        if(!accountReturned.isPresent()){
            throw new NoSuchAccountException("The account= " +accountId + "does not  exists");
        }
        accountReturned.get().getOperations().add(operation);
        return accountDtoMapper.mapEntityToDto(accountReturned.get());
    }


    /**
     * executeOperation create a new deposit or withdrawal operation
     * @param accountId
     * @param amount
     * @param operationType
     * @return
     * @throws NoSuchAccountException
     */
      public Operation executeOperation(long accountId, long amount, OperationType operationType) throws NoSuchAccountException {
        Optional<Account> accountReturned = accountRepository.findById(accountId);
        if(!accountReturned.isPresent()){
            throw new NoSuchAccountException("The account= " +accountId + "does not  exists");
        }
        Account account = accountReturned.get();
        int minusOrPlusOperation = operationType.equals(OperationType.DEPOSIT) ? 1 : -1;
        Operation operation = new Operation();
        operation.setAmount(minusOrPlusOperation*amount);
        operation.setDate(LocalDate.now());
        operation.setAccount(account);
        operation.setType(operationType);
        account.balance+=minusOrPlusOperation*amount;
        operationRepository.save(operation);
        return operation;
    }
}
