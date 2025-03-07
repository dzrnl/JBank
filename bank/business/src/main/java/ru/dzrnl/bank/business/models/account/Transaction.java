package ru.dzrnl.bank.business.models.account;

public record Transaction(long id, long accountId, long amount, TransactionType type) {
    public void execute(Account account) {
        if (account.getId() != accountId) {
            throw new IllegalArgumentException("Account id mismatch");
        }
        account.applyTransaction(this);
    }
}
