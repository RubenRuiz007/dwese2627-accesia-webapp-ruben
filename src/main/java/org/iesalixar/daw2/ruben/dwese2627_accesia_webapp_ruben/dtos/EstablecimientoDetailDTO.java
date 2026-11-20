package org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.entities.TipoEstablecimiento;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO para la vista de detalle de un establecimiento. Incluye todos sus datos y sus tipos de entrada.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstablecimientoDetailDTO {

    /**
     * Id del establecimiento.
     */
    private Long id;

    /**
     * Nombre del establecimiento.
     */
    private String nombre;

    /**
     * Tipo de establecimiento.
     */
    private TipoEstablecimiento tipo;

    /**
     * Dirección.
     */
    private String direccion;

    /**
     * Descripción.
     */
    private String descripcion;

    /**
     * Aforo máximo.
     */
    private Integer aforoMaximo;

    /**
     * Precio de referencia.
     */
    private BigDecimal precioReferencia;

    /**
     * Edad mínima.
     */
    private Integer edadMinima;

    /**
     * Si hace falta tener la cuenta confirmada.
     */
    private boolean exigeCuentaConfirmada;

    /**
     * Si el aforo es público.
     */
    private boolean aforoPublico;

    /**
     * Si el establecimiento está activo.
     */
    private boolean activo;

    /**
     * Ruta web de la imagen.
     */
    private String imagen;

    /**
     * Tipos de entrada que ofrece el establecimiento.
     */
    private List<TipoEntradaDTO> tiposEntrada;
}
