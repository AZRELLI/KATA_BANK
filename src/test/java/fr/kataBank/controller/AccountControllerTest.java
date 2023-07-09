package fr.kataBank.controller;


import fr.kataBank.KataBankApp;
import fr.kataBank.domain.Account;
import fr.kataBank.domain.Operation;
import fr.kataBank.domain.OperationType;
import fr.kataBank.domain.dto.OperationDto;
import fr.kataBank.repository.AccountRepository;
import fr.kataBank.repository.OperationRepository;
import fr.kataBank.service.AccountService;
import fr.kataBank.service.OperationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.util.ArrayList;

import static fr.kataBank.helper.TestHelper.convertObjectToJsonBytes;
import static org.hamcrest.Matchers.hasItems;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = KataBankApp.class)
public class AccountControllerTest {
    @MockBean
    private OperationService operationService;

    @MockBean
    private OperationRepository operationRepository;

    @MockBean
    private AccountRepository accountRepository;
    @MockBean
    private  AccountService accountService;

    @Autowired
    private  WebApplicationContext context;


    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc =
                MockMvcBuilders.webAppContextSetup(context)
                        .apply(SecurityMockMvcConfigurers.springSecurity())
                        .build();
    }

    @Test
    public void deposit_should_return_error_message_and_404_code_status() throws Exception {

        // Given
        String url = "/api/accounts/404/deposit";
        // When // Then
        mockMvc.perform(get(url).contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }

    @Test
    @Transactional
    public void deposit_should_perform_a_deposit_operation() throws Exception {
        Account account = new Account();
        account.setBalance(0);
        account.setOperations(new ArrayList<>());
        accountRepository.saveAndFlush(account);
        mockMvc.perform(put("/api/accounts/{accountId}/deposit", account.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(new OperationDto(10000))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.latestOperations").isNotEmpty())
                .andExpect(jsonPath("$.balance").value(10000));

    }

    @Test
    public void withdrawal_should_return_error_message_and_404_code_status() throws Exception {

        mockMvc.perform(put("/api/accounts/{accountId}/withdrawal", 404L)
                .accept(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(new OperationDto(2000))))
                .andExpect(status().is4xxClientError());

    }

    @Test
    @Transactional
    public void withdrawal_should_perform_a_withdrawal_operation() throws Exception {
        Account account = new Account();
        account.setBalance(0);
        account.setOperations(new ArrayList<>());
        accountRepository.saveAndFlush(account);
        mockMvc.perform(put("/api/accounts/{accountId}/withdrawal", account.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(new OperationDto(500))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.latestOperations").isNotEmpty())
                .andExpect(jsonPath("$.balance").value(-500));
    }

    @Test
    @Transactional
    public void showOperationsList_should_list_all_previous_operations() throws Exception {
        Account account = new Account();
        account.setBalance(0);
        account.setOperations(new ArrayList<>());
        accountRepository.saveAndFlush(account);
        Operation operation = new Operation();
        operation.setAccount(account);
        operation.setType(OperationType.WITHDRAWAL);
        operation.setAmount(1000L);
        operationRepository.saveAndFlush(operation);
        Operation operation2 = new Operation();
        operation2.setAccount(account);
        operation2.setType(OperationType.DEPOSIT);
        operation2.setAmount(4000L);
        operationRepository.saveAndFlush(operation2);
        account.getOperations().add(operation);
        account.getOperations().add(operation2);
        mockMvc.perform(get("/api/accounts/{id}/history", account.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*]").isNotEmpty())
                .andExpect(jsonPath("$.[*].id").value(hasItems(operation.getId().intValue(),operation2.getId().intValue())))
                .andExpect(jsonPath("$.[*].amount").value(hasItems(operation.getAmount().intValue(),operation2.getAmount().intValue())))
                .andExpect(jsonPath("$.[*].type").value(hasItems(operation.getType().toString(),operation2.getType().toString())));
    }

    @Test
    public void showOperationsList_should_return_error_message_and_404_code_status() throws Exception {

        mockMvc.perform(get("/api/accounts/{id}/history", 5555555)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}