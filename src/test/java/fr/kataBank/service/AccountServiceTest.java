package fr.kataBank.service;


import fr.kataBank.domain.Account;
import fr.kataBank.domain.Operation;
import fr.kataBank.domain.OperationType;
import fr.kataBank.repository.AccountRepository;
import fr.kataBank.utils.NoSuchAccountException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository AccountRepository;

    @Mock
    private AccountDtoMapper accountDtoMapper;

    @InjectMocks
    private AccountService accountService;

    private List<Operation> operations;
    private Account account ;
    @Before
    public void setUp(){
        account = new Account();
        account.setBalance(5000);
        account.setId(2L);
        operations = new ArrayList<>();
        operations.add(new Operation(LocalDate.now(), OperationType.DEPOSIT,10000,account));
        account.setOperations(operations);
    }

    @Test(expected = NoSuchAccountException.class)
    public void listAllOperations_should_throw_exception_for_no_such_account() throws Exception {
        when(AccountRepository.findById(anyLong())).thenReturn(Optional.empty());
        accountService.listAllOperations(10L);
        Assert.fail("should have thrown NoSuchAccountException ");
    }


    @Test
    public void listAllOperations_should_successfully_return_all_account_operations() throws NoSuchAccountException {
        when(AccountRepository.findById(2L)).thenReturn(Optional.of(account));
        when(accountDtoMapper.mapEntityToDto(any(Account.class))).thenCallRealMethod();
        List<Operation> operations = accountService.listAllOperations(2L);
        assertThat(operations).isNotEmpty();
        assertThat(operations).hasSize(1);
    }

}
