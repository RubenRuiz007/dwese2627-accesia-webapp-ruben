package org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.exceptions;

import lombok.Getter;

/**
 * Excepción que se lanza cuando se intenta borrar un dato que todavía se está usando
 * (por ejemplo, un establecimiento que aún tiene tipos de entrada).
 */
@Getter
public class ResourceInUseException extends RuntimeException {

    /**
     * Nombre del recurso afectado (por ejemplo "establecimiento" o "tipoEntrada").
     */
    private final String resource;

    /**
     * Campo por el que se ha detectado el problema (por ejemplo "nombre" o "id").
     */
    private final String field;

    /**
     * Valor que ha provocado el problema.
     */
    private final Object value;

    /**
     * Crea la excepción con los datos del problema.
     *
     * @param resource nombre del recurso afectado
     * @param field    campo que da el problema
     * @param value    valor que da el problema
     */
    public ResourceInUseException(String resource, String field, Object value) {
        super(resource + " en uso, no se puede borrar (" + field + "=" + value + ")");
        this.resource = resource;
        this.field = field;
        this.value = value;
    }
}
