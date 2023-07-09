package fr.kataBank.controller;


import fr.kataBank.domain.Operation;
import fr.kataBank.domain.dto.AccountDto;
import fr.kataBank.domain.dto.OperationDto;
import fr.kataBank.service.AccountService;
import fr.kataBank.service.OperationService;
import fr.kataBank.utils.NoSuchAccountException;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts/")
public class AccountResources {

    private final AccountService accountService;
    private final OperationService operationService;

    public AccountResources(AccountService accountService, OperationService operationService) {
        this.accountService = accountService;
        this.operationService = operationService;
    }

    @ApiOperation(value = "showOperationsList",notes = "lists all given account operations")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success",responseContainer = "list",response = Operation.class),
            @ApiResponse(code = 404, message = "Bad request"),
    })
    @GetMapping("{accountId}/history")
    public List<Operation> showOperationsList(@PathVariable long accountId) throws NoSuchAccountException {
        return accountService.listAllOperations(accountId);
    }


    @ApiOperation(value = "deposit",notes = "perfom a deposit on the given account")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success",response = AccountDto.class),
            @ApiResponse(code = 404, message = "Bad request"),
    })
    @PutMapping(value = "{accountId}/deposit")
    public AccountDto deposit(@PathVariable long accountId,
                        @RequestBody OperationDto operationDto) throws NoSuchAccountException {
       return operationService.doDeposit(accountId,operationDto.getAmount());
    }


    @ApiOperation(value = "withdrawall",notes = "perfom a withdrawal on the given account")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success",response = AccountDto.class),
            @ApiResponse(code = 404, message = "Bad request"),
    })
    @PutMapping(value = "{accountId}/withdrawal")
    public AccountDto withdrawal(@PathVariable long accountId,
                           @RequestBody OperationDto operationDto) throws NoSuchAccountException {
        return operationService.doWithdrawal(accountId,operationDto.getAmount());
    }
}
