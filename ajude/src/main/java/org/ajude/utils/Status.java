package org.ajude.utils;

public enum Status {

    A("ACTIVE"),
    C("CLOSED"),
    E("EXPIRED"),
    F("FINISHED");


    private  String valor;

    Status(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

}
