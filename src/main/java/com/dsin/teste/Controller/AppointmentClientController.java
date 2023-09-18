package com.dsin.teste.Controller;

import com.dsin.teste.model.Appointment;
import com.dsin.teste.model.enums.StatusAppoint;
import com.dsin.teste.service.Impl.AppointmentServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/client")
public class AppointmentClientController {
    private final Logger logger = LoggerFactory.getLogger(AppointmentClientController.class);
    private final AppointmentServiceImpl appointmentService;

    public AppointmentClientController(AppointmentServiceImpl appointmentService) {
        this.appointmentService = appointmentService;
    }
    @PutMapping("/confirm/{appointmentId}")
    public ResponseEntity<String> confirmAppointment(@PathVariable("appointmentId") Long appointmentId) {
        try {
            Appointment appointment = appointmentService.getAppointmentById(appointmentId)
                    .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado " + appointmentId));

            if (appointment.getStatus() == StatusAppoint.CONFIRMADO || appointment.getStatus() == StatusAppoint.CONCLUIDO) {
                return ResponseEntity.badRequest().body("Este agendamento já está confirmado.");
            }
            appointment.setStatus(StatusAppoint.CONFIRMADO);
            appointmentService.clientUpdateAppoint(appointmentId, appointment);
            return ResponseEntity.ok("Agendamento confirmado com sucesso.");

            } catch (EntityNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/info/{clientId}")
    public ResponseEntity<List<Appointment>> findAllClientAppointments(@PathVariable("clientId") Long clientId) {
        List<Appointment> clientAppointments = appointmentService.findAllByClient(clientId);
        logger.info("Resposta com todos os agendamentos do usuário");
        return ResponseEntity.ok(clientAppointments);
    }

    @GetMapping(path = "/appointment/{appointmentId}")
    public ResponseEntity<Appointment> findAppointmentById(@PathVariable Long appointmentId) {
        Appointment appointment = appointmentService.getAppointmentById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado " + appointmentId));
        logger.info("Resposta com todas os agendamentos ordenados pelo id do cliente");
        return ResponseEntity.status(HttpStatus.OK).body(appointment);
    }

    @GetMapping("/appointment/ordered")
    public ResponseEntity<List<Appointment>> findClientAppointmentsByIdDate(
            @RequestParam("startDate") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate endDate,
            @RequestParam("clientId") Long clientId) {

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        List<Appointment> appointments = appointmentService.findAllClientAppointmentsByDate(startDateTime, endDateTime);
        appointments.sort((a1, a2) -> a2.getDateTimeAppointment().compareTo(a1.getDateTimeAppointment()));
        List<Appointment> appointmentsOrded = appointmentService.findAllByClient(clientId);
        logger.info("Resposta com todas os agendamentos ordenados pelo período solicitado");
        return ResponseEntity.ok(appointmentsOrded);

    }

    @PostMapping(path = "/appointment/register")
    public ResponseEntity<String> registerAppointment(@RequestBody Appointment appointment) {
        Long clientId = appointment.getClient().getId();
        List<Appointment> clientAppointments = appointmentService.findAllByClient(clientId);
        boolean verify = appointmentService.verifyAppointmentWeek(appointment, clientAppointments);
        appointmentService.create(appointment);
        if(!verify) {
            return ResponseEntity.status(HttpStatus.OK).body("Agendamento criado!");
        }
        else {
            logger.warn("Agendamento criado! Porém, já existe um agendamento marcado para essa semana. Sugerimos que tente remarcar para o mesmo dia.");
            return ResponseEntity.status(HttpStatus.OK).body("Agendamento criado!. No entanto, já existe um agendamento marcado para essa semana. Sugerimos que tente remarcar para o mesmo dia");
        }
    }
    @PutMapping(path = "/appointment/{appointmentId}")
    public ResponseEntity<String> updateAppointment(
            @PathVariable("appointmentId") Long appointmentId,
            @RequestBody Appointment updatedAppointment) {
        try {
            Appointment updated = appointmentService.updateAndVerifyDate(appointmentId, updatedAppointment);
            if (updated != null) {
                appointmentService.clientUpdateAppoint(appointmentId, updated);
                logger.info("Agendamento atualizado");
                return ResponseEntity.ok("Agendamento atualizado com sucesso!");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Não é possível remarcar porque a nova data está muito próxima da data atual. Ligue para (11) 999999999");
            }
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("O agendamento com o ID " + appointmentId + " não existe.");
        }
    }

    @DeleteMapping(path = "/appointment/{appointmentId}")
    public ResponseEntity<String> deleteAppointment(@PathVariable("appointmentId") Long appointmentId) {
        logger.info("Deletando agendamento pelo id do client");
        appointmentService.deleteAppointmentById(appointmentId);
        return ResponseEntity.ok("Você deleteu o agendamento de id: " + appointmentId.toString());
    }


}
