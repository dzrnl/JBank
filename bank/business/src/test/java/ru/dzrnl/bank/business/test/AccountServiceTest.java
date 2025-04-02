package ru.dzrnl.bank.business.test;

import org.junit.jupiter.api.Test;
import ru.dzrnl.bank.business.contracts.AccountService;
import ru.dzrnl.bank.business.contracts.FriendshipService;
import ru.dzrnl.bank.business.models.account.Account;
import ru.dzrnl.bank.business.models.account.Transaction;
import ru.dzrnl.bank.business.models.account.TransactionType;
import ru.dzrnl.bank.business.repositories.AccountRepository;
import ru.dzrnl.bank.business.repositories.TransactionRepository;
import ru.dzrnl.bank.business.services.AccountServiceImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AccountServiceTest {
    private final String defaultUserLogin = "ivanov";
    private final String secondDefaultUserLogin = "petrov";

    @Test
    public void shouldCreateAccount() {
        int accountId = 0;

        AccountRepository mockAccountRepo = mock(AccountRepository.class);
        TransactionRepository mockTransactionRepo = mock(TransactionRepository.class);
        FriendshipService mockFriendshipService = mock(FriendshipService.class);

        when(mockAccountRepo.createAccount(defaultUserLogin)).thenReturn(new Account(accountId, defaultUserLogin));

        AccountService accountService = new AccountServiceImpl(mockAccountRepo, mockTransactionRepo, mockFriendshipService);

        assertEquals(accountService.createAccount(defaultUserLogin).getOwnerLogin(), defaultUserLogin);

        verify(mockAccountRepo, times(1)).createAccount(defaultUserLogin);
    }

    @Test
    public void shouldGetAccount() {
        int accountId = 0;

        AccountRepository mockAccountRepo = mock(AccountRepository.class);
        TransactionRepository mockTransactionRepo = mock(TransactionRepository.class);
        FriendshipService mockFriendshipService = mock(FriendshipService.class);

        when(mockAccountRepo.findAccountById(accountId)).thenReturn(Optional.of(new Account(accountId, defaultUserLogin)));

        AccountService accountService = new AccountServiceImpl(mockAccountRepo, mockTransactionRepo, mockFriendshipService);

        assertEquals(accountService.getAccount(accountId).getOwnerLogin(), defaultUserLogin);

        verify(mockAccountRepo, times(1)).findAccountById(accountId);
    }

    @Test
    public void shouldThrowExceptionWhenNoAccount() {
        int accountId = 0;

        AccountRepository mockAccountRepo = mock(AccountRepository.class);
        TransactionRepository mockTransactionRepo = mock(TransactionRepository.class);
        FriendshipService mockFriendshipService = mock(FriendshipService.class);

        when(mockAccountRepo.findAccountById(accountId)).thenReturn(Optional.empty());

        AccountService accountService = new AccountServiceImpl(mockAccountRepo, mockTransactionRepo, mockFriendshipService);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> accountService.getAccount(accountId));

        assertEquals("Account with id '" + accountId + "' not found", exception.getMessage());

        verify(mockAccountRepo, times(1)).findAccountById(accountId);
    }

    @Test
    public void shouldGetAllUserAccounts() {
        var userAccounts = List.of(new Account(1, defaultUserLogin), new Account(2, defaultUserLogin));

        AccountRepository mockAccountRepo = mock(AccountRepository.class);
        TransactionRepository mockTransactionRepo = mock(TransactionRepository.class);
        FriendshipService mockFriendshipService = mock(FriendshipService.class);

        when(mockAccountRepo.findAllUserAccounts(defaultUserLogin)).thenReturn(userAccounts);

        AccountService accountService = new AccountServiceImpl(mockAccountRepo, mockTransactionRepo, mockFriendshipService);

        assertEquals(accountService.getAllUserAccounts(defaultUserLogin), new HashSet<>(userAccounts));

        verify(mockAccountRepo, times(1)).findAllUserAccounts(defaultUserLogin);
    }

    @Test
    public void shouldDepositMoney() {
        var account = new Account(0, defaultUserLogin);
        long amount = 1000;

        AccountRepository mockAccountRepo = mock(AccountRepository.class);
        TransactionRepository mockTransactionRepo = mock(TransactionRepository.class);
        FriendshipService mockFriendshipService = mock(FriendshipService.class);

        when(mockAccountRepo.findAccountById(account.getId())).thenReturn(Optional.of(account));

        when(mockTransactionRepo.createTransaction(eq(account.getId()), eq(amount), eq(TransactionType.DEPOSIT)))
                .thenReturn(new Transaction(0, account.getId(), amount, TransactionType.DEPOSIT));

        AccountService accountService = new AccountServiceImpl(mockAccountRepo, mockTransactionRepo, mockFriendshipService);

        accountService.depositMoney(account.getId(), amount);

        assertEquals(amount, account.getBalance());
    }

    @Test
    public void shouldWithdrawMoney() {
        long balance = 1000;
        var account = createAccountWithBalance(0, defaultUserLogin, balance);
        long amount = 800;

        AccountRepository mockAccountRepo = mock(AccountRepository.class);
        TransactionRepository mockTransactionRepo = mock(TransactionRepository.class);
        FriendshipService mockFriendshipService = mock(FriendshipService.class);

        when(mockAccountRepo.findAccountById(account.getId())).thenReturn(Optional.of(account));

        when(mockTransactionRepo.createTransaction(eq(account.getId()), eq(amount), eq(TransactionType.WITHDRAW)))
                .thenReturn(new Transaction(0, account.getId(), amount, TransactionType.WITHDRAW));

        AccountService accountService = new AccountServiceImpl(mockAccountRepo, mockTransactionRepo, mockFriendshipService);

        accountService.withdrawMoney(account.getId(), amount);

        assertEquals(balance - amount, account.getBalance());
    }

    @Test
    public void shouldThrowExceptionWhenNotEnoughMoney() {
        long balance = 1000;
        var account = createAccountWithBalance(0, defaultUserLogin, balance);
        long amount = 2000;

        AccountRepository mockAccountRepo = mock(AccountRepository.class);
        TransactionRepository mockTransactionRepo = mock(TransactionRepository.class);
        FriendshipService mockFriendshipService = mock(FriendshipService.class);

        when(mockAccountRepo.findAccountById(account.getId())).thenReturn(Optional.of(account));

        when(mockTransactionRepo.createTransaction(eq(account.getId()), eq(amount), eq(TransactionType.WITHDRAW)))
                .thenReturn(new Transaction(0, account.getId(), amount, TransactionType.WITHDRAW));

        AccountService accountService = new AccountServiceImpl(mockAccountRepo, mockTransactionRepo, mockFriendshipService);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> accountService.withdrawMoney(account.getId(), amount));

        assertEquals("Insufficient funds for account " + account.getId(), exception.getMessage());

        assertEquals(balance, account.getBalance());
    }

    @Test
    public void shouldTransferMoneyBetweenAccountsOfSameUser() {
        long firstBalance = 2000;
        var firstAccount = createAccountWithBalance(0, defaultUserLogin, firstBalance);
        long secondBalance = 1000;
        var secondAccount = createAccountWithBalance(1, defaultUserLogin, secondBalance);
        long amount = 200;

        AccountRepository mockAccountRepo = mock(AccountRepository.class);
        TransactionRepository mockTransactionRepo = mock(TransactionRepository.class);
        FriendshipService mockFriendshipService = mock(FriendshipService.class);

        when(mockAccountRepo.findAccountById(firstAccount.getId())).thenReturn(Optional.of(firstAccount));
        when(mockAccountRepo.findAccountById(secondAccount.getId())).thenReturn(Optional.of(secondAccount));

        when(mockTransactionRepo.createTransaction(eq(firstAccount.getId()), eq(amount), eq(TransactionType.WITHDRAW)))
                .thenReturn(new Transaction(0, firstAccount.getId(), amount, TransactionType.WITHDRAW));

        when(mockTransactionRepo.createTransaction(eq(secondAccount.getId()), eq(amount), eq(TransactionType.DEPOSIT)))
                .thenReturn(new Transaction(1, secondAccount.getId(), amount, TransactionType.DEPOSIT));

        AccountService accountService = new AccountServiceImpl(mockAccountRepo, mockTransactionRepo, mockFriendshipService);

        accountService.transferMoney(firstAccount.getId(), secondAccount.getId(), amount);

        assertEquals(firstBalance - amount, firstAccount.getBalance());
        assertEquals(secondBalance + amount, secondAccount.getBalance());
    }

    @Test
    public void shouldTransferMoneyBetweenFriendsAccounts() {
        long firstBalance = 2000;
        var firstAccount = createAccountWithBalance(0, defaultUserLogin, firstBalance);
        long secondBalance = 1000;
        var secondAccount = createAccountWithBalance(1, secondDefaultUserLogin, secondBalance);
        long amount = 200;
        long expectedTransferAmount = amount - amount * 3 / 100;

        AccountRepository mockAccountRepo = mock(AccountRepository.class);
        TransactionRepository mockTransactionRepo = mock(TransactionRepository.class);
        FriendshipService mockFriendshipService = mock(FriendshipService.class);

        when(mockAccountRepo.findAccountById(firstAccount.getId())).thenReturn(Optional.of(firstAccount));
        when(mockAccountRepo.findAccountById(secondAccount.getId())).thenReturn(Optional.of(secondAccount));

        when(mockFriendshipService.areFriends(defaultUserLogin, secondDefaultUserLogin)).thenReturn(true);

        when(mockTransactionRepo.createTransaction(eq(firstAccount.getId()), eq(amount), eq(TransactionType.WITHDRAW)))
                .thenReturn(new Transaction(0, firstAccount.getId(), amount, TransactionType.WITHDRAW));

        when(mockTransactionRepo.createTransaction(eq(secondAccount.getId()), eq(expectedTransferAmount), eq(TransactionType.DEPOSIT)))
                .thenReturn(new Transaction(1, secondAccount.getId(), expectedTransferAmount, TransactionType.DEPOSIT));

        AccountService accountService = new AccountServiceImpl(mockAccountRepo, mockTransactionRepo, mockFriendshipService);

        accountService.transferMoney(firstAccount.getId(), secondAccount.getId(), amount);

        assertEquals(firstBalance - amount, firstAccount.getBalance());
        assertEquals(secondBalance + expectedTransferAmount, secondAccount.getBalance());
    }

    @Test
    public void shouldTransferMoney() {
        long firstBalance = 2000;
        var firstAccount = createAccountWithBalance(0, defaultUserLogin, firstBalance);
        long secondBalance = 1000;
        var secondAccount = createAccountWithBalance(1, secondDefaultUserLogin, secondBalance);
        long amount = 200;
        long expectedTransferAmount = amount - amount * 10 / 100;

        AccountRepository mockAccountRepo = mock(AccountRepository.class);
        TransactionRepository mockTransactionRepo = mock(TransactionRepository.class);
        FriendshipService mockFriendshipService = mock(FriendshipService.class);

        when(mockAccountRepo.findAccountById(firstAccount.getId())).thenReturn(Optional.of(firstAccount));
        when(mockAccountRepo.findAccountById(secondAccount.getId())).thenReturn(Optional.of(secondAccount));

        when(mockFriendshipService.areFriends(defaultUserLogin, secondDefaultUserLogin)).thenReturn(false);

        when(mockTransactionRepo.createTransaction(eq(firstAccount.getId()), eq(amount), eq(TransactionType.WITHDRAW)))
                .thenReturn(new Transaction(0, firstAccount.getId(), amount, TransactionType.WITHDRAW));

        when(mockTransactionRepo.createTransaction(eq(secondAccount.getId()), eq(expectedTransferAmount), eq(TransactionType.DEPOSIT)))
                .thenReturn(new Transaction(1, secondAccount.getId(), expectedTransferAmount, TransactionType.DEPOSIT));

        AccountService accountService = new AccountServiceImpl(mockAccountRepo, mockTransactionRepo, mockFriendshipService);

        accountService.transferMoney(firstAccount.getId(), secondAccount.getId(), amount);

        assertEquals(firstBalance - amount, firstAccount.getBalance());
        assertEquals(secondBalance + expectedTransferAmount, secondAccount.getBalance());
    }

    private static Account createAccountWithBalance(long accountId, String userLogin, long balance) {
        var account = new Account(accountId, userLogin);

        AccountRepository mockAccountRepo = mock(AccountRepository.class);
        TransactionRepository mockTransactionRepo = mock(TransactionRepository.class);
        FriendshipService mockFriendshipService = mock(FriendshipService.class);

        when(mockAccountRepo.findAccountById(accountId)).thenReturn(Optional.of(account));

        when(mockTransactionRepo.createTransaction(eq(accountId), eq(balance), eq(TransactionType.DEPOSIT)))
                .thenReturn(new Transaction(0, accountId, balance, TransactionType.DEPOSIT));

        AccountService accountService = new AccountServiceImpl(mockAccountRepo, mockTransactionRepo, mockFriendshipService);

        accountService.depositMoney(accountId, balance);

        return account;
    }
}
