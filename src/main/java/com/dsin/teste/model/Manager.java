package com.dsin.teste.model;

import com.dsin.teste.model.enums.Role;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Entity
@SequenceGenerator(name = "manager_sequence", sequenceName = "SQ_MANAGER", allocationSize = 1)
public class Manager {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "manager_sequence")
    private  Long id;
    private Role role;
    private String name;
    @OneToMany
    private List<Appointment> appointmentList;

    public Manager() {

    }
    public Manager(Role role, String name, List<Appointment> appointmentList) {
        this.role = role;
        this.name = name;
        this.appointmentList = appointmentList;
    }
    public Manager(Role role, String name) {
        this.role = role;
        this.name = name;
    }
    public int salonClientsQuantity(List<Appointment> appointmentList){
        return 0;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Appointment> getAppointmentList() {
        return appointmentList;
    }

    public void setAppointmentList(List<Appointment> appointmentList) {
        this.appointmentList = appointmentList;
    }
}
