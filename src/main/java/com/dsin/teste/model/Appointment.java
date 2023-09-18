package com.dsin.teste.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.dsin.teste.model.enums.ServiceType;
import com.dsin.teste.model.enums.StatusAppoint;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Entity
@SequenceGenerator(name = "appointment_sequence", sequenceName = "SQ_APPOINT", allocationSize = 1)
@JsonPropertyOrder({"idAppointment", "client", "status", "dateTimeAppointment", "amount", "serviceTypes"})
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "appointment_sequence")
    @JsonProperty("idAppointment")
    private Long id;
    @ManyToOne(optional = false)
    private Client client;
    @NotBlank(message = "É necessário passar um status")
    @Enumerated(EnumType.STRING)
    private StatusAppoint status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime dateTimeAppointment;
    @Min(value = 0, message = "O valor precisa ser maior que 0")
    private BigDecimal amount;

    @NotBlank(message = "É necessário informar o tipo do serviço")
    @Enumerated(EnumType.STRING)
    private List<ServiceType> serviceTypes;

    public Appointment(Client client, StatusAppoint status, LocalDateTime dateTimeAppointment, BigDecimal amount, List<ServiceType> serviceTypes) {
        this.client = client;
        this.status = status;
        this.dateTimeAppointment = dateTimeAppointment;
        this.amount = amount;
        this.serviceTypes = serviceTypes;
    }


    public Appointment() {
    }
    public Appointment(Long id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", client=" + client +
                ", status=" + status +
                ", dateTimeAppointment=" + dateTimeAppointment +
                ", amount=" + amount +
                ", serviceTypes=" + serviceTypes +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public void setServiceTypes(List<ServiceType> serviceTypes) {
        this.serviceTypes = serviceTypes;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public StatusAppoint getStatus() {
        return status;
    }

    public void setStatus(StatusAppoint status) {
        this.status = status;
    }

    public LocalDateTime getDateTimeAppointment() {
        return dateTimeAppointment;
    }

    public void setDateTimeAppointment(LocalDateTime dateTimeAppointment) {
        this.dateTimeAppointment = dateTimeAppointment;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public List<ServiceType> getServiceType() {
        return serviceTypes;
    }

    public void setServiceType(List<ServiceType> serviceTypes) {
        this.serviceTypes = serviceTypes;
    }

}

