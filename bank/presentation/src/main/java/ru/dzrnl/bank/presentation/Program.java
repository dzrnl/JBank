package ru.dzrnl.bank.presentation;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.dzrnl.bank.presentation.config.AppConfig;


public class Program {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        ConsoleMenu console = context.getBean(ConsoleMenu.class);
        console.run();
    }
}
