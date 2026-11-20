package org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.exceptions;

import lombok.Getter;

/**
 * Excepción que se lanza cuando se busca un dato que no existe en la base de datos
 * (por ejemplo, un establecimiento con un id que no está guardado).
 */
@Getter
public class ResourceNotFoundException extends RuntimeException {

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
    public ResourceNotFoundException(String resource, String field, Object value) {
        super(resource + " no encontrado (" + field + "=" + value + ")");
        this.resource = resource;
        this.field = field;
        this.value = value;
    }
}
