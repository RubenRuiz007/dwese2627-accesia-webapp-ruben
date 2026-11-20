package org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.entities.TipoEstablecimiento;

/**
 * DTO básico de Establecimiento. Se usa en el listado, en el desplegable del formulario
 * de tipos de entrada y en el detalle de un tipo de entrada.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EstablecimientoDTO {

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
     * Dirección del establecimiento.
     */
    private String direccion;

    /**
     * Aforo máximo.
     */
    private Integer aforoMaximo;

    /**
     * Si el establecimiento está activo.
     */
    private boolean activo;
}
