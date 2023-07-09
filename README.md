# kata-Bank

### A SpringBoot application offering Rest Endpoints in order to manage a bank account

## Overview:

- The application offers the possibility to have multiple accounts. For each account multiple operations.
- A bank account has a unique identifier. It's represented by a balance and a list of operations which could be a deposit or a withdrawal.
- An operation is categorised(deposit or a withdrawal), have an amount, occurence date and a unique ID. Each operation is linked to a specific account.

- BANK ACCOUNT KATA
Requirements :



•            Deposit and Withdrawal

•            Account statement (date, amount, balance)

•            Statement printing



The expected result is a domain implementation that meets the expressed needs.

Don’t build an entire app, especially no UI, no persistence.



User Stories



US 1:

In order to save money as a bank client I want to make a deposit in my account

US 2:

In order to retrieve some or all of my savings as a bank client I want to make a withdrawal from my account

US 3:

In order to check my operations as a bank client I want to see the history (operation, date, amount, balance) of my operations