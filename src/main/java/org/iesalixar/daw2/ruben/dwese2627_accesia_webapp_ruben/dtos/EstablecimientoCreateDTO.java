package org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.entities.TipoEstablecimiento;

import java.math.BigDecimal;

/**
 * DTO con los datos del formulario para crear un establecimiento.
 * Las anotaciones comprueban los datos antes de llegar al servicio.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstablecimientoCreateDTO {

    /**
     * Id del establecimiento. Al crear siempre llega vacío (null) porque lo genera la base de datos.
     * Se deja para que el formulario sepa si está creando o editando.
     */
    private Long id;

    /**
     * Nombre del establecimiento: obligatorio y con 150 caracteres como máximo.
     */
    @NotBlank(message = "{msg.establecimiento.nombre.notEmpty}")
    @Size(max = 150, message = "{msg.establecimiento.nombre.size}")
    private String nombre;

    /**
     * Tipo de establecimiento elegido en el desplegable. Es obligatorio.
     */
    @NotNull(message = "{msg.establecimiento.tipo.notNull}")
    private TipoEstablecimiento tipo;

    /**
     * Dirección: obligatoria y con 255 caracteres como máximo.
     */
    @NotBlank(message = "{msg.establecimiento.direccion.notEmpty}")
    @Size(max = 255, message = "{msg.establecimiento.direccion.size}")
    private String direccion;

    /**
     * Descripción (opcional), con 1000 caracteres como máximo.
     */
    @Size(max = 1000, message = "{msg.establecimiento.descripcion.size}")
    private String descripcion;

    /**
     * Aforo máximo: obligatorio y mayor que 0.
     */
    @NotNull(message = "{msg.establecimiento.aforoMaximo.notNull}")
    @Positive(message = "{msg.establecimiento.aforoMaximo.positive}")
    private Integer aforoMaximo;

    /**
     * Precio de referencia: obligatorio, no negativo y con 2 decimales como máximo.
     */
    @NotNull(message = "{msg.establecimiento.precioReferencia.notNull}")
    @DecimalMin(value = "0.00", message = "{msg.establecimiento.precioReferencia.min}")
    @Digits(integer = 6, fraction = 2, message = "{msg.establecimiento.precioReferencia.digits}")
    private BigDecimal precioReferencia;

    /**
     * Edad mínima (opcional), entre 0 y 99.
     */
    @Min(value = 0, message = "{msg.establecimiento.edadMinima.range}")
    @Max(value = 99, message = "{msg.establecimiento.edadMinima.range}")
    private Integer edadMinima;

    /**
     * Si para entrar hace falta tener la cuenta confirmada (casilla del formulario).
     */
    private boolean exigeCuentaConfirmada;

    /**
     * Si el aforo se muestra al público (casilla del formulario).
     */
    private boolean aforoPublico;

    /**
     * Si el establecimiento está activo (casilla del formulario).
     */
    private boolean activo;

    /**
     * Ruta de la imagen actual (opcional), con 500 caracteres como máximo.
     * La imagen nueva no llega aquí, sino aparte como MultipartFile.
     */
    @Size(max = 500, message = "{msg.establecimiento.imagen.size}")
    private String imagen;
}
