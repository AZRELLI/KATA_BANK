package fr.kataBank.service;


import fr.kataBank.domain.Account;
import fr.kataBank.domain.Operation;
import fr.kataBank.domain.OperationType;
import fr.kataBank.domain.dto.AccountDto;
import fr.kataBank.repository.AccountRepository;
import fr.kataBank.repository.OperationRepository;
import fr.kataBank.utils.NoSuchAccountException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class OperationServiceTest {


    @Mock
    private AccountRepository AccountRepository;

    @Mock
    private OperationRepository operationRepository;

    @Mock
    private AccountDtoMapper accountDtoMapper;

    @InjectMocks
    private OperationService operationService;

    private Account account ;
    private Operation operation;
    @Before
    public void setUp(){
        account = new Account();
        account.setBalance(5000);
        account.setId(12L);
        operation = new Operation(LocalDate.now(), OperationType.DEPOSIT,10000, null);
    }

    @Test(expected = NoSuchAccountException.class)
    public void executeOperation_should_throw_NoSuchAccountException() throws NoSuchAccountException {
        when(AccountRepository.findById(anyLong())).thenReturn(Optional.empty());
        operationService.executeOperation(404L,0,OperationType.WITHDRAWAL);
        Assert.fail("should have thrown NoSuchAccountException ");

    }

    @Test
    public void executeOperation_should_perform_deposit() throws NoSuchAccountException {
        when(AccountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        long currentAccountBalance = account.getBalance();
        Operation operation = operationService.executeOperation(12L,1000,OperationType.DEPOSIT);
        assertThat(operation.getAmount()).isEqualTo(1000);
        assertThat(operation.getType()).isEqualTo(OperationType.DEPOSIT);
        assertThat(operation.getAccount()).isNotNull();
        assertThat(operation.getAccount().getBalance()).isEqualTo(currentAccountBalance+1000);
    }

    @Test
    public void executeOperation_should_perform_withdrawal() throws NoSuchAccountException {
        when(AccountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        long currentAccountBalance = account.getBalance();
        Operation operation = operationService.executeOperation(12L,5000,OperationType.WITHDRAWAL);
        assertThat(operation.getAmount()).isEqualTo(-5000);
        assertThat(operation.getType()).isEqualTo(OperationType.WITHDRAWAL);
        assertThat(operation.getAccount()).isNotNull();
        assertThat(operation.getAccount().getBalance()).isEqualTo(currentAccountBalance-5000);
    }

    @Test(expected = NoSuchAccountException.class)
    public void doDeposit_should_throw_NoSuchAccountException() throws NoSuchAccountException {
        when(AccountRepository.findById(anyLong())).thenReturn(Optional.empty());
        operationService.doDeposit(12L,1200);
        Assert.fail("should have thrown NoSuchAccountException ");
    }


    @Test
    public void doDeposit_should_perform_deposit_and_save_op() throws NoSuchAccountException {
        when(AccountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        when(accountDtoMapper.mapEntityToDto(any(Account.class))).thenCallRealMethod();
        when(operationRepository.save(any(Operation.class))).thenReturn(operation);
        long currentAccountBalance = account.getBalance();
        AccountDto dto = operationService.doDeposit(12L,1200);
        assertThat(dto.getOperationsList().size()).isEqualTo(1);
        assertThat(dto.getOperationsList().get(0).getType()).isEqualTo(OperationType.DEPOSIT);
        assertThat(dto.getOperationsList().get(0).getAmount()).isEqualTo(1200);
        assertThat(dto.getBalance()).isEqualTo(currentAccountBalance+1200);
    }

    @Test(expected = NoSuchAccountException.class)
    public void doWithdrawal_should_throw_NoSuchAccountException() throws NoSuchAccountException {
        when(AccountRepository.findById(anyLong())).thenReturn(Optional.empty());
        operationService.doWithdrawal(12L,1200);
        Assert.fail("should have thrown NoSuchAccountException ");
    }

    @Test
    public void doWithdrawal_should_perform_withdrawal_and_save_op() throws NoSuchAccountException {
        when(AccountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        when(accountDtoMapper.mapEntityToDto(any(Account.class))).thenCallRealMethod();
        when(operationRepository.save(any(Operation.class))).thenReturn(operation);
        long currentAccountBalance = account.getBalance();
        AccountDto dto = operationService.doWithdrawal(12L,1200);
        assertThat(dto.getOperationsList().size()).isEqualTo(1);
        assertThat(dto.getOperationsList().get(0).getType()).isEqualTo(OperationType.WITHDRAWAL);
        assertThat(dto.getOperationsList().get(0).getAmount()).isEqualTo(-1200);
        assertThat(dto.getBalance()).isEqualTo(currentAccountBalance-1200);
    }

}
