package org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Entidad que representa un tipo de entrada de un establecimiento (por ejemplo "General" o "VIP").
 * Es el lado N de la relación 1:N "Ofrece": muchos tipos de entrada pertenecen a un mismo establecimiento.
 * Es una entidad fuerte: tiene su propio id, y la clave ajena establecimiento_id es obligatoria (1,1).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tipo_entrada")
public class TipoEntrada {

    /**
     * Identificador del tipo de entrada. Lo genera la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Nombre del tipo de entrada. No se puede repetir dentro del mismo establecimiento.
     */
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    /**
     * Precio de referencia de este tipo de entrada (con dos decimales).
     */
    @Column(name = "precio_referencia", nullable = false, precision = 8, scale = 2)
    private BigDecimal precioReferencia;

    /**
     * Número máximo de entradas de este tipo (opcional).
     */
    @Column(name = "cupo")
    private Integer cupo;

    /**
     * Establecimiento que ofrece este tipo de entrada.
     * Aquí está la clave ajena establecimiento_id, porque TipoEntrada es el lado N.
     * Es LAZY: el establecimiento solo se carga cuando se necesita o cuando se pide con JOIN FETCH.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "establecimiento_id", nullable = false)
    private Establecimiento establecimiento;
}
