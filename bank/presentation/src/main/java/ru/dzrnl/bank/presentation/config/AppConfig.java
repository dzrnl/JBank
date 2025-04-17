package ru.dzrnl.bank.presentation.config;

import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(basePackages = {
        "ru.dzrnl.bank.presentation",
        "ru.dzrnl.bank.business"
})
@Import({ru.dzrnl.bank.data.config.DataConfig.class})
public class AppConfig {
}