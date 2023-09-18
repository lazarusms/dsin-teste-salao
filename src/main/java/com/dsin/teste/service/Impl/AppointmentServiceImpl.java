package com.dsin.teste.service.Impl;

import com.dsin.teste.Controller.AppointmentClientController;
import com.dsin.teste.model.Appointment;
import com.dsin.teste.model.enums.ServiceType;
import com.dsin.teste.model.enums.StatusAppoint;
import com.dsin.teste.repository.AppointmentRepository;
import com.dsin.teste.service.AppointmentService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    private final Logger logger = LoggerFactory.getLogger(AppointmentClientController.class);
    private final AppointmentRepository appointmentRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public List<Appointment> findClientAppointmentsByIdDate(LocalDateTime startDate, LocalDateTime endDate, Long appointmentId) {
        return appointmentRepository.findClientAppointmentsByIdDate(startDate, endDate, appointmentId);
    }

    @Override
    public List<Appointment> findAllClientAppointmentsByDate(LocalDateTime startDate, LocalDateTime endDate) {
        return appointmentRepository.findAllClientAppointmentsByDate(startDate, endDate);
    }


    @Override
    public Appointment create(Appointment appointment) {
        logger.info("Agendamento cadastrado com sucesso no banco de dados");
        return appointmentRepository.save(appointment);
    }


    @Override
    public List<Appointment> getAllAppointments() {
        logger.info("Listando todos os agendamentos");
        return appointmentRepository.findAll();
    }

    @Override
    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }

    @Override
    @Transactional
    public Appointment clientUpdateAppoint(Long appointmentId, Appointment updatedAppointment) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalStateException("O agendamento com o id " + appointmentId + " não existe"));
            updateStatus(appointment, updatedAppointment.getStatus());
            updateDateTime(appointment, updatedAppointment.getDateTimeAppointment());
            updateAmount(appointment, updatedAppointment.getAmount());
            updateServiceType(appointment, updatedAppointment.getServiceType());
            appointmentRepository.save(appointment);
            logger.info("Update realizado");
            return appointment;
        }


    @Override
    @Transactional
    public Appointment managerUpdate(Long appointmentId, Appointment updatedAppointment) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalStateException("O agendamento com o id " + appointmentId + " não existe"));
        updateStatus(appointment, updatedAppointment.getStatus());
        updateDateTime(appointment, updatedAppointment.getDateTimeAppointment());
        updateAmount(appointment, updatedAppointment.getAmount());
        updateServiceType(appointment, updatedAppointment.getServiceType());
        appointmentRepository.save(appointment);
        logger.info("Update realizado");
        return appointment;
    }

    @Override
    public void deleteAppointmentById(Long id) {
        boolean appointmentExists = appointmentRepository.existsById(id);
        if (!appointmentExists){
            throw new IllegalStateException("O agendamento com id " + id + " não existe");
        }
        logger.info("Agendamento" + id + "deletado com sucesso");
        appointmentRepository.deleteById(id);
    }
    public Appointment updateAndVerifyDate(Long appointmentId, Appointment updatedAppointment) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalStateException("O agendamento com o ID " + appointmentId + " não existe"));
        if (updatedAppointment.getDateTimeAppointment() != null) {
            LocalDateTime actualDateTime = LocalDateTime.now();
            long differenceDays = Math.abs(ChronoUnit.DAYS.between(appointment.getDateTimeAppointment(), actualDateTime));

            if (differenceDays <= 2) {
                logger.error("Não é possível remarcar porque a data está muito próxima. Ligue para (11) 999999999.");
                return null;
            }
        }

        return updatedAppointment;
    }


    public List<Appointment> findAllByClient(Long clientId) {
        List<Appointment> appointments = appointmentRepository.findAll();
        return appointments.stream()
                .filter(existing -> existing.getClient().getId().equals(clientId))
                .collect(Collectors.toList());
    }
    public List<Appointment> findNextAppointments() {
        LocalDateTime now = LocalDateTime.now();
        List<Appointment> appointmentList = appointmentRepository.findAll();

        return appointmentList.stream()
                .filter(appointment -> appointment.getDateTimeAppointment().isAfter(now))
                .sorted(Comparator.comparing(appointment ->  appointment.getDateTimeAppointment()))
                .collect(Collectors.toList());
    }

    public boolean verifyAppointmentWeek(Appointment appointment, List<Appointment> appointments) {
        LocalDateTime newAppointmentDateTime = appointment.getDateTimeAppointment();

        List<Appointment> filteredAppointments = appointments.stream()
                .filter(existing -> existing.getClient().getId().equals(appointment.getClient().getId()))
                .toList();


        return filteredAppointments.stream()
                .anyMatch(existing -> {
                    LocalDateTime existingDateTime = existing.getDateTimeAppointment();
                    long daysWeek = ChronoUnit.DAYS.between(existingDateTime, newAppointmentDateTime);
                    return daysWeek >= 0 && daysWeek <= 6;
                });
    }


    public void updateStatus(Appointment appointment, StatusAppoint newStatus) {
        if (newStatus != null && !Objects.equals(appointment.getStatus(), newStatus) && StatusAppoint.isValidStatus(newStatus)) {
            appointment.setStatus(newStatus);
            logger.info("Status atualizado com sucesso");
        }
    }
   public void updateDateTime(Appointment appointment, LocalDateTime dateTime) {
        if (dateTime != null) {
            appointment.setDateTimeAppointment(dateTime);
        }
    }

    public void updateServiceType(Appointment appointment, List<ServiceType> serviceType) {
        if (serviceType != null && serviceType != appointment.getServiceType()) {
            appointment.setServiceType(serviceType);
            logger.info("Tipo do agendamento alterado");
        }
    }

    public void updateAmount(Appointment appointment, BigDecimal amount) {
        if (amount != null) {
            appointment.setAmount(amount);
            logger.info("Valor do agendamento alterado");
        }
    }

}
