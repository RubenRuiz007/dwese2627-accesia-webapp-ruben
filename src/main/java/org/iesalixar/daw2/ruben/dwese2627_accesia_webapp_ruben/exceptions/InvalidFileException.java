package org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.exceptions;

import lombok.Getter;

/**
 * Excepción para los ficheros subidos que no cumplen las reglas de la aplicación
 * (tipo no permitido, tamaño demasiado grande, error al guardar en disco...).
 */
@Getter
public class InvalidFileException extends RuntimeException {

    /**
     * Recurso al que pertenece el fichero (por ejemplo "establecimiento").
     */
    private final String resource;

    /**
     * Nombre del campo del formulario donde se sube el fichero (por ejemplo "imagenFile").
     */
    private final String field;

    /**
     * Valor o detalle del error (por ejemplo "application/pdf" o el tamaño del fichero).
     * Puede ser null si no hace falta.
     */
    private final Object value;

    /**
     * Crea la excepción indicando el recurso, el campo y el valor del fichero no válido.
     *
     * @param resource nombre del recurso (por ejemplo "establecimiento")
     * @param field    nombre del campo del formulario (por ejemplo "imagenFile")
     * @param value    valor o detalle del error (por ejemplo "application/pdf")
     */
    public InvalidFileException(String resource, String field, Object value) {
        super("Invalid file for " + resource + " (" + field + "=" + value + ")");
        this.resource = resource;
        this.field = field;
        this.value = value;
    }

    /**
     * Crea la excepción añadiendo un detalle más al mensaje.
     * Sirve para errores al guardar en disco, permisos, rutas no válidas, etc.
     *
     * @param resource nombre del recurso (por ejemplo "establecimiento")
     * @param field    nombre del campo del formulario (por ejemplo "imagenFile")
     * @param value    valor o detalle del error (por ejemplo "image/png")
     * @param detail   explicación del error (por ejemplo "Archivo demasiado grande")
     */
    public InvalidFileException(String resource, String field, Object value, String detail) {
        super("Invalid file for " + resource + " (" + field + "=" + value + "): " + detail);
        this.resource = resource;
        this.field = field;
        this.value = value;
    }
}
