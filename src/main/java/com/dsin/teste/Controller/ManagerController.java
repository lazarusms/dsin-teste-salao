package com.dsin.teste.Controller;

import com.dsin.teste.model.Appointment;
import com.dsin.teste.service.Impl.AppointmentServiceImpl;
import com.dsin.teste.service.Impl.ManagerServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/manager")
public class ManagerController {
    private final Logger logger = LoggerFactory.getLogger(ManagerController.class);
    private final ManagerServiceImpl managerService;
    private final AppointmentServiceImpl appointmentService;

    public ManagerController(ManagerServiceImpl managerService, AppointmentServiceImpl appointmentService) {
        this.managerService = managerService;
        this.appointmentService = appointmentService;
    }

    @GetMapping(path = "/list")
    public ResponseEntity<List<Appointment>> getAppointments() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointments);
    }
    @GetMapping(path = "/{appointmentId}")
    public ResponseEntity<Appointment> findAppointmentById(@PathVariable Long appointmentId) {
        Appointment appointment = appointmentService.getAppointmentById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado " + appointmentId));
        return ResponseEntity.status(HttpStatus.OK).body(appointment);
    }
    @GetMapping(path = "/upcoming-list")
    public ResponseEntity<List<Appointment>> nextAppointments() {
        List<Appointment> appointments = appointmentService.findNextAppointments();
        return ResponseEntity.ok(appointments);
    }

    @GetMapping(path = "/salon-revenue")
    public ResponseEntity<String> getSalonRevenue () {
        List<Appointment> totalAppointments = appointmentService.getAllAppointments();
        BigDecimal revenue = managerService.salonRevenue(totalAppointments);
        logger.info("Acessando o total de lucro da semana");
        return ResponseEntity.ok("O total de lucro dessa semana foi de: R$" + revenue.toString());
    }

    @GetMapping(path = "/list/ordered")
    public ResponseEntity<List<Appointment>> getAppointmentsBetweenDates(
            @RequestParam("startDate") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate endDate) {

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        List<Appointment> appointments = appointmentService.findAllClientAppointmentsByDate(startDateTime, endDateTime);
        appointments.sort((a1, a2) -> a2.getDateTimeAppointment().compareTo(a1.getDateTimeAppointment()));

        logger.info("Resposta com todas os agendamentos ordenados pelo período solicitado");
        return ResponseEntity.ok(appointments);

    }

    @PostMapping(path = "/register-appointment")
    public ResponseEntity<String> registerAppointment(@RequestBody Appointment appointment) {
        appointmentService.create(appointment);
        return ResponseEntity.status(HttpStatus.OK).body("Agendamento criado!");

    }
    @DeleteMapping(path = "/{appointmentId}")
    public ResponseEntity<String> deleteAppointment(@PathVariable("appointmentId") Long appointmentId) {
        appointmentService.deleteAppointmentById(appointmentId);
        return ResponseEntity.ok("Você deleteu o agendamento de id: " + appointmentId.toString());
    }
    @PutMapping(path = "/{appointmentId}")
    public ResponseEntity<Appointment> updateAppointment(
            @PathVariable("appointmentId") Long appointmentId,
            @RequestBody Appointment updatedAppointment) {
        Appointment updated = appointmentService.managerUpdate(appointmentId, updatedAppointment);
        return ResponseEntity.ok(updated);
    }



}



