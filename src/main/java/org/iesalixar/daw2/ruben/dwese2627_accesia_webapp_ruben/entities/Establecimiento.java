package org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa un establecimiento (discoteca, bar, gimnasio...).
 * Es el lado 1 de la relación 1:N "Ofrece": un establecimiento ofrece muchos tipos de entrada.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "establecimiento")
public class Establecimiento {

    /**
     * Identificador del establecimiento. Lo genera la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Nombre del establecimiento.
     */
    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    /**
     * Tipo de establecimiento. Se guarda como texto (DISCOTECA, BAR...).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 30)
    private TipoEstablecimiento tipo;

    /**
     * Dirección del establecimiento.
     */
    @Column(name = "direccion", nullable = false, length = 255)
    private String direccion;

    /**
     * Descripción del establecimiento (opcional).
     */
    @Column(name = "descripcion", length = 1000)
    private String descripcion;

    /**
     * Número máximo de personas que caben a la vez.
     */
    @Column(name = "aforo_maximo", nullable = false)
    private Integer aforoMaximo;

    /**
     * Precio de referencia de la entrada (con dos decimales).
     */
    @Column(name = "precio_referencia", nullable = false, precision = 8, scale = 2)
    private BigDecimal precioReferencia;

    /**
     * Edad mínima para entrar (opcional).
     */
    @Column(name = "edad_minima")
    private Integer edadMinima;

    /**
     * Indica si para entrar hace falta tener la cuenta confirmada.
     */
    @Column(name = "exige_cuenta_confirmada", nullable = false)
    private boolean exigeCuentaConfirmada;

    /**
     * Indica si el aforo se muestra al público.
     */
    @Column(name = "aforo_publico", nullable = false)
    private boolean aforoPublico;

    /**
     * Indica si el establecimiento está activo.
     */
    @Column(name = "activo", nullable = false)
    private boolean activo;

    /**
     * Ruta web de la imagen del establecimiento (por ejemplo /uploads/uuid.png). Es opcional.
     */
    @Column(name = "imagen", length = 500)
    private String imagen;

    /**
     * Tipos de entrada que ofrece este establecimiento.
     * No lleva cascade para que al borrar un establecimiento no se borren sus tipos de entrada sin avisar.
     * Se excluye de toString, equals y hashCode para evitar bucles infinitos con TipoEntrada.
     */
    @OneToMany(mappedBy = "establecimiento", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<TipoEntrada> tiposEntrada = new ArrayList<>();
}
