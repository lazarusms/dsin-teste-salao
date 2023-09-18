package com.dsin.teste.repository;

import com.dsin.teste.model.Appointment;
import com.dsin.teste.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @Query("SELECT a FROM Appointment a WHERE a.dateTimeAppointment BETWEEN :startDate AND :endDate AND a.client.id = :clientId")
    List<Appointment> findClientAppointmentsByIdDate(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("clientId") Long clientId
    );
    @Query("SELECT a FROM Appointment a WHERE a.dateTimeAppointment BETWEEN :startDate AND :endDate")
    List<Appointment> findAllClientAppointmentsByDate(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );


}
