package org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO básico de TipoEntrada para el listado.
 * Del establecimiento solo se guarda el nombre, que es lo único que se muestra en la tabla.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TipoEntradaDTO {

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
     * Nombre del establecimiento que ofrece este tipo de entrada.
     */
    private String establecimientoNombre;
}
