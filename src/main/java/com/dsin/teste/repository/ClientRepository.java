package com.dsin.teste.repository;

import com.dsin.teste.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.dsin.teste.model.Client;


import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query("SELECT c FROM Client c WHERE c.email = :email")
    Optional<Client> findClientByEmail(@Param("email") String email);

    @Query("SELECT c FROM Client c WHERE c.phoneNumber = :phoneNumber")
    Optional<Client> findClientByPhone(@Param("phoneNumber") String phoneNumber);


}
