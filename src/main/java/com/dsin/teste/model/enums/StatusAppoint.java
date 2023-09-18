package com.dsin.teste.model.enums;

public enum StatusAppoint {
    CONFIRMADO,
    PENDENTE,
    CONCLUIDO,
    CANCELADO;

    public static boolean isValidStatus(StatusAppoint status) {
        for (StatusAppoint validStatus : values()) {
            if (validStatus.equals(status)) {
                return true;
            }
        }
        return false;
    }
}
