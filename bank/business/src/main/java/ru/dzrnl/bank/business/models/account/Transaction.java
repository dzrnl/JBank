package ru.dzrnl.bank.business.models.account;

/**
 * Represents a financial transaction for an account.
 *
 * @param id        unique transaction identifier
 * @param accountId target account identifier
 * @param amount    transaction amount
 * @param type      type of transaction (DEPOSIT or WITHDRAW)
 */
public record Transaction(long id, long accountId, long amount, TransactionType type) {

    /**
     * Executes the transaction on the provided account.
     *
     * @param account the account to apply the transaction to
     * @throws IllegalArgumentException if the account id does not match
     */
    public void execute(Account account) {
        if (account.getId() != accountId) {
            throw new IllegalArgumentException("Account id mismatch");
        }
        account.applyTransaction(this);
    }
}
