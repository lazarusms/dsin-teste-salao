package com.dsin.teste.service;

import com.dsin.teste.model.Appointment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentService {
    Appointment create(Appointment appointment);
    List<Appointment> getAllAppointments();
    Optional<Appointment> getAppointmentById(Long id);
    Appointment clientUpdateAppoint(Long appointmentId, Appointment updatedAppointment);
    Appointment managerUpdate(Long appointmentId, Appointment updatedAppointment);
    void deleteAppointmentById(Long id);
    List<Appointment> findClientAppointmentsByIdDate(LocalDateTime startDate, LocalDateTime endDate, Long appointmentId);
    List<Appointment> findAllClientAppointmentsByDate(LocalDateTime startDate, LocalDateTime endDate);


}
