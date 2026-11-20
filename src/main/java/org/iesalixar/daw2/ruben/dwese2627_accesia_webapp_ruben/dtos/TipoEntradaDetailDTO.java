package org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para la vista de detalle de un tipo de entrada. Incluye sus datos y su establecimiento.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TipoEntradaDetailDTO {

    /**
     * Id del tipo de entrada.
     */
    private Long id;

    /**
     * Nombre del tipo de entrada.
     */
    private String nombre;

    /**
     * Precio de referencia.
     */
    private BigDecimal precioReferencia;

    /**
     * Cupo de entradas.
     */
    private Integer cupo;

    /**
     * Establecimiento que ofrece este tipo de entrada.
     */
    private EstablecimientoDTO establecimiento;
}
