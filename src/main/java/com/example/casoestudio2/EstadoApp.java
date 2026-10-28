package com.example.casoestudio2;

public enum EstadoApp {
    LISTO("Listo"),
    EN_EJECUCION("En ejecución"),
    CANCELANDO("Cancelando..."),
    CANCELADO("Cancelado"),
    COMPLETADO("Completado"),
    ERROR("Error");

    private final String etiqueta;

    EstadoApp(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }
}