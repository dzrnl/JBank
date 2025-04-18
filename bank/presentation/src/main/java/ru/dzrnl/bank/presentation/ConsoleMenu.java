package ru.dzrnl.bank.presentation;

import ru.dzrnl.bank.business.contracts.AccountService;
import ru.dzrnl.bank.business.contracts.FriendshipService;
import ru.dzrnl.bank.business.contracts.UserService;
import ru.dzrnl.bank.business.models.account.Account;
import ru.dzrnl.bank.business.models.user.Gender;
import ru.dzrnl.bank.business.models.user.HairColor;
import ru.dzrnl.bank.business.models.user.User;

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

public class ConsoleMenu {
    private final Scanner scanner;
    private final UserService userService;
    private final FriendshipService friendshipService;
    private final AccountService accountService;

    public ConsoleMenu(UserService userService, FriendshipService friendshipService, AccountService accountService) {
        this.scanner = new Scanner(System.in);

        this.userService = userService;
        this.friendshipService = friendshipService;
        this.accountService = accountService;
    }

    /**
     * Runs the console menu, allowing the user to select and execute actions.
     */
    public void run() {
        while (true) {
            System.out.println("\nSelect action");
            System.out.println("1. Create User");
            System.out.println("2. View User");
            System.out.println("3. Add Friend");
            System.out.println("4. Remove Friend");
            System.out.println("5. View Friends");
            System.out.println("6. Create Account");
            System.out.println("7. View Accounts");
            System.out.println("8. Deposit Money");
            System.out.println("9. Withdraw Money");
            System.out.println("10. Transfer Money");
            System.out.println("0. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> createUser();
                case 2 -> viewUser();
                case 3 -> addFriend();
                case 4 -> removeFriend();
                case 5 -> viewFriends();
                case 6 -> createAccount();
                case 7 -> viewAccounts();
                case 8 -> depositMoney();
                case 9 -> withdrawMoney();
                case 10 -> transferMoney();
                case 0 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Prompts the user to enter details and creates a new user.
     */
    private void createUser() {
        System.out.print("Enter login: ");
        String login = scanner.nextLine();

        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        System.out.print("Enter age: ");
        int age = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter gender (MALE (M)/FEMALE (F)): ");
        String genderInput = scanner.nextLine().toUpperCase();
        Gender gender;
        try {
            if (genderInput.equals("M")) {
                gender = Gender.MALE;
            } else if (genderInput.equals("F")) {
                gender = Gender.FEMALE;
            } else {
                gender = Gender.valueOf(genderInput.toUpperCase());
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid gender. Please try again.");
            return;
        }

        System.out.print("Enter hair color (BLONDE/BROWN/BLACK/OTHER) (default OTHER): ");
        String hairColorInput = scanner.nextLine();
        HairColor hairColor = HairColor.OTHER;
        try {
            hairColor = HairColor.valueOf(hairColorInput.toUpperCase());
        } catch (IllegalArgumentException ignored) {}

        try {
            userService.createUser(login, name, age, gender, hairColor);
            System.out.println("User created successfully.");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Displays details of a user by login.
     */
    private void viewUser() {
        System.out.print("Enter login to view: ");
        String login = scanner.nextLine();

        try {
            User user = userService.getUserByLogin(login);
            System.out.println(user);
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Adds a friend connection between two users.
     */
    private void addFriend() {
        System.out.print("Enter first user login: ");
        String userLogin = scanner.nextLine();

        System.out.print("Enter second user login: ");
        String friendLogin = scanner.nextLine();

        try {
            userService.getUserByLogin(userLogin);
            userService.getUserByLogin(friendLogin);

            friendshipService.addFriend(userLogin, friendLogin);
            System.out.println("Friend added successfully.");
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Removes a friend connection between two users.
     */
    private void removeFriend() {
        System.out.print("Enter first user login: ");
        String userLogin = scanner.nextLine();

        System.out.print("Enter second user login: ");
        String friendLogin = scanner.nextLine();

        try {
            userService.getUserByLogin(userLogin);
            userService.getUserByLogin(friendLogin);

            friendshipService.removeFriend(userLogin, friendLogin);
            System.out.println("Friend removed successfully.");
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Displays the friends of a user.
     */
    private void viewFriends() {
        System.out.print("Enter user login to view friends: ");
        String userLogin = scanner.nextLine();

        try {
            userService.getUserByLogin(userLogin);
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
            return;
        }

        Set<String> friends = friendshipService.getFriendLogins(userLogin);

        if (friends.isEmpty()) {
            System.out.println("User has no friends.");
        } else {
            System.out.println("User's friends: " + String.join(", ", friends));
        }
    }

    /**
     * Creates a new bank account for a user.
     */
    private void createAccount() {
        System.out.print("Enter user login to create an account: ");
        String userLogin = scanner.nextLine();

        try {
            userService.getUserByLogin(userLogin);

            accountService.createAccount(userLogin);
            System.out.println("Account created successfully.");
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Displays all accounts of a user.
     */
    private void viewAccounts() {
        System.out.print("Enter user login to view accounts: ");
        String userLogin = scanner.nextLine();

        try {
            userService.getUserByLogin(userLogin);
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
            return;
        }

        Set<Account> accounts = accountService.getAllUserAccounts(userLogin);

        if (accounts.isEmpty()) {
            System.out.println("User has no accounts.");
        } else {
            accounts.forEach(account -> System.out.println("Account ID: " + account.getId() + ", Balance: " + account.getBalance()));
        }
    }

    /**
     * Deposits money into a specified account.
     */
    private void depositMoney() {
        System.out.print("Enter account ID to deposit money: ");
        long accountId = scanner.nextLong();

        System.out.print("Enter amount to deposit: ");
        long amount = scanner.nextLong();
        scanner.nextLine();

        try {
            accountService.depositMoney(accountId, amount);
            System.out.println("Deposit successful.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Withdraws money from a specified account.
     */
    private void withdrawMoney() {
        System.out.print("Enter account ID to withdraw money: ");
        long accountId = scanner.nextLong();

        System.out.print("Enter amount to withdraw: ");
        long amount = scanner.nextLong();
        scanner.nextLine();

        try {
            accountService.withdrawMoney(accountId, amount);
            System.out.println("Withdrawal successful.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Transfers money between two accounts.
     */
    private void transferMoney() {
        System.out.print("Enter user account ID: ");
        long fromAccountId = scanner.nextLong();

        System.out.print("Enter recipient account ID: ");
        long toAccountId = scanner.nextLong();

        System.out.print("Enter amount to transfer: ");
        long amount = scanner.nextLong();
        scanner.nextLine();

        try {
            accountService.transferMoney(fromAccountId, toAccountId, amount);
            System.out.println("Transfer successful.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }
}
