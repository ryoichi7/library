package ru.ryoichi.service;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import ru.ryoichi.dao.DaoConfiguration;

@SpringBootApplication
@Import(value = {DaoConfiguration.class})
public class ServiceConfiguration {
}
