package ru.dzrnl.bank.presentation;

import ru.dzrnl.bank.business.contracts.AccountService;
import ru.dzrnl.bank.business.contracts.FriendshipService;
import ru.dzrnl.bank.business.contracts.UserService;
import ru.dzrnl.bank.business.repositories.AccountRepository;
import ru.dzrnl.bank.business.repositories.FriendshipRepository;
import ru.dzrnl.bank.business.repositories.TransactionRepository;
import ru.dzrnl.bank.business.repositories.UserRepository;
import ru.dzrnl.bank.business.services.AccountServiceImpl;
import ru.dzrnl.bank.business.services.FriendshipServiceImpl;
import ru.dzrnl.bank.business.services.UserServiceImpl;
import ru.dzrnl.bank.data.repositories.AccountRepositoryImpl;
import ru.dzrnl.bank.data.repositories.FriendshipRepositoryImpl;
import ru.dzrnl.bank.data.repositories.TransactionRepositoryImpl;
import ru.dzrnl.bank.data.repositories.UserRepositoryImpl;

/**
 * Main class to initiate the program.
 * Initializes services and repositories, then runs the console menu for the user interaction.
 */
public class Program {

    /**
     * Default constructor for Program class.
     */
    public Program() {
    }

    /**
     * Main method that sets up the application by initializing necessary repositories,
     * services, and then running the console menu.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        UserRepository userRepository = new UserRepositoryImpl();
        FriendshipRepository friendshipRepository = new FriendshipRepositoryImpl();
        TransactionRepository transactionRepository = new TransactionRepositoryImpl();
        AccountRepository accountRepository = new AccountRepositoryImpl();

        UserService userService = new UserServiceImpl(userRepository);
        FriendshipService friendshipService = new FriendshipServiceImpl(friendshipRepository);
        AccountService accountService = new AccountServiceImpl(accountRepository, transactionRepository, friendshipService);

        var console = new ConsoleMenu(userService, friendshipService, accountService);
        console.run();
    }
}
