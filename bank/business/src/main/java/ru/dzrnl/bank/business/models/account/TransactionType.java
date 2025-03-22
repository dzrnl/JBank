package ru.dzrnl.bank.business.models.account;

/**
 * Represents types of account transactions.
 */
public enum TransactionType {

    /**
     * Deposit transaction, where money is added to the account.
     */
    DEPOSIT,

    /**
     * Withdrawal transaction, where money is taken out of the account.
     */
    WITHDRAW
}
