package com.dsin.teste;


import com.dsin.teste.model.Client;
import com.dsin.teste.model.Manager;
import com.dsin.teste.model.enums.Role;
import com.dsin.teste.repository.ClientRepository;
import com.dsin.teste.repository.ManagerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.LocalDate;


import static java.util.Calendar.*;

@Configuration
public class InsertConfiguration {

    @Bean
    CommandLineRunner commandLineRunner(ClientRepository clientRepository, ManagerRepository managerRepository) {
        return args -> {
            Client newClient = new Client(
                    "Mathaus",
                    "Lazarus",
                    LocalDate.of(2000, MARCH, 5),
                    Role.CLIENTE,
                    "mathaus@email.com",
                    "119895119232");
            clientRepository.save(newClient);
            Client newClientNew = new Client(
                    "Victoria",
                    "Lazarus",
                    LocalDate.of(2000, AUGUST, 15),
                    Role.CLIENTE,
                    "victoria@email.com",
                    "119895119232");
            clientRepository.save(newClientNew);
            Manager manager = new Manager(
                    Role.ADMIN,
                    "Leila");
            managerRepository.save(manager);
        };

    }
}
