package com.dsin.teste.model.enums;

public enum ServiceType {
    CORTE("CORTE"),
    PINTURA("PINTURA"),
    HIDRATAÇÃO("HIDRATAÇÃO"),
    ESCOVA("ESCOVA");

    private final String serviceName;


    ServiceType(String serviceName) {
        this.serviceName = serviceName;
    }
    public boolean isValidService(ServiceType serviceType) {
        for (ServiceType validService : values()) {
            if (validService.equals(serviceType)) {
                return true;
            }
        }
        return false;
    }
    public String getServiceName() {
        return serviceName;
    }
}
