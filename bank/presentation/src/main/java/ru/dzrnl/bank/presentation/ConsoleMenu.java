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
                case 1:
                    createUser();
                    break;
                case 2:
                    viewUser();
                    break;
                case 3:
                    addFriend();
                    break;
                case 4:
                    removeFriend();
                    break;
                case 5:
                    viewFriends();
                    break;
                case 6:
                    createAccount();
                    break;
                case 7:
                    viewAccounts();
                    break;
                case 8:
                    depositMoney();
                    break;
                case 9:
                    withdrawMoney();
                    break;
                case 10:
                    transferMoney();
                    break;
                case 0:
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

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

    private void viewUser() {
        System.out.print("Enter login to view: ");
        String login = scanner.nextLine();

        try {
            User user = userService.getUserByLogin(login);
            System.out.println(user);
        } catch (NoSuchElementException e) {
            System.out.println("User not found.");
        }
    }

    private void addFriend() {
        System.out.print("Enter first user login: ");
        String userLogin = scanner.nextLine();

        System.out.print("Enter second user login: ");
        String friendLogin = scanner.nextLine();

        friendshipService.addFriend(userLogin, friendLogin);
        System.out.println("Friend added successfully.");
    }

    private void removeFriend() {
        System.out.print("Enter first user login: ");
        String userLogin = scanner.nextLine();

        System.out.print("Enter second user login: ");
        String friendLogin = scanner.nextLine();

        friendshipService.removeFriend(userLogin, friendLogin);
        System.out.println("Friend removed successfully.");
    }

    private void viewFriends() {
        System.out.print("Enter user login to view friends: ");
        String userLogin = scanner.nextLine();

        Set<String> friends = friendshipService.getFriendLogins(userLogin);

        if (friends.isEmpty()) {
            System.out.println("User has no friends.");
        } else {
            System.out.println("User's friends: " + String.join(", ", friends));
        }
    }

    private void createAccount() {
        System.out.print("Enter user login to create an account: ");
        String userLogin = scanner.nextLine();

        accountService.createAccount(userLogin);
        System.out.println("Account created successfully.");
    }

    private void viewAccounts() {
        System.out.print("Enter user login to view accounts: ");
        String userLogin = scanner.nextLine();

        Set<Account> accounts = accountService.getAllUserAccounts(userLogin);

        if (accounts.isEmpty()) {
            System.out.println("User has no accounts.");
        } else {
            accounts.forEach(account -> System.out.println("Account ID: " + account.getId() + ", Balance: " + account.getBalance()));
        }
    }

    private void depositMoney() {
        System.out.print("Enter account ID to deposit money: ");
        long accountId = scanner.nextLong();

        System.out.print("Enter amount to deposit: ");
        long amount = scanner.nextLong();
        scanner.nextLine();

        accountService.depositMoney(accountId, amount);
        System.out.println("Deposit successful.");
    }

    private void withdrawMoney() {
        System.out.print("Enter account ID to withdraw money: ");
        long accountId = scanner.nextLong();

        System.out.print("Enter amount to withdraw: ");
        long amount = scanner.nextLong();
        scanner.nextLine();

        try {
            accountService.withdrawMoney(accountId, amount);
            System.out.println("Withdrawal successful.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

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
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
