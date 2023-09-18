package com.dsin.teste.service.Impl;

import com.dsin.teste.model.Appointment;
import com.dsin.teste.model.Manager;
import com.dsin.teste.model.enums.Role;
import com.dsin.teste.model.enums.StatusAppoint;
import com.dsin.teste.repository.ManagerRepository;
import com.dsin.teste.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ManagerServiceImpl implements ManagerService {
    private final ManagerRepository managerRepository;

    @Autowired
    public ManagerServiceImpl(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    public BigDecimal salonRevenue(List<Appointment> appointmentList) {
        LocalDateTime timeNow = LocalDateTime.now();
        LocalDateTime oneWeekAmount = timeNow.minus(7, ChronoUnit.DAYS);
        BigDecimal totalRevenue = BigDecimal.ZERO;

        for (Appointment appointment : appointmentList) {
            LocalDateTime appointmentDate = appointment.getDateTimeAppointment();

            if (appointmentDate != null && appointment.getStatus() == StatusAppoint.CONCLUIDO &&
                    appointmentDate.isBefore(timeNow) && appointmentDate.isAfter(oneWeekAmount)) {

                BigDecimal amount = appointment.getAmount();
                totalRevenue = totalRevenue.add(amount);
            }
        }

        return totalRevenue;
    }


    @Override
    public Manager create(Manager employee) {
        return null;
    }

}

