package fr.kataBank.domain.dto;

import fr.kataBank.domain.Operation;

import java.util.List;

public class AccountDto {

    private long balance;

    private List<Operation> operationsList;

    public AccountDto() {
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public List<Operation> getOperationsList() {
        return operationsList;
    }

    public void setOperationsList(List<Operation> operationsList) {
        this.operationsList = operationsList;
    }
}
