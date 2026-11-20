package org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO con los datos del formulario para editar un tipo de entrada.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TipoEntradaUpdateDTO {

    /**
     * Id del tipo de entrada que se está editando. Es obligatorio.
     */
    @NotNull(message = "{msg.tipoEntrada.id.notNull}")
    private Long id;

    /**
     * Nombre del tipo de entrada: obligatorio y con 100 caracteres como máximo.
     */
    @NotBlank(message = "{msg.tipoEntrada.nombre.notEmpty}")
    @Size(max = 100, message = "{msg.tipoEntrada.nombre.size}")
    private String nombre;

    /**
     * Precio de referencia: obligatorio, no negativo y con 2 decimales como máximo.
     */
    @NotNull(message = "{msg.tipoEntrada.precioReferencia.notNull}")
    @DecimalMin(value = "0.00", message = "{msg.tipoEntrada.precioReferencia.min}")
    @Digits(integer = 6, fraction = 2, message = "{msg.tipoEntrada.precioReferencia.digits}")
    private BigDecimal precioReferencia;

    /**
     * Cupo de entradas (opcional). Si se pone, tiene que ser mayor que 0.
     * Que no supere el aforo del establecimiento se comprueba en el controlador.
     */
    @Positive(message = "{msg.tipoEntrada.cupo.positive}")
    private Integer cupo;

    /**
     * Id del establecimiento elegido en el desplegable. Es obligatorio.
     */
    @NotNull(message = "{msg.tipoEntrada.establecimientoId.notNull}")
    private Long establecimientoId;
}
