package org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.entities;

/**
 * Tipos de establecimiento posibles (es el atributo codificable C·Tipo del diagrama).
 * En la base de datos se guarda el nombre del valor como texto (por ejemplo "DISCOTECA").
 */
public enum
TipoEstablecimiento {
    DISCOTECA,
    BAR,
    PUB,
    SALA_CONCIERTOS,
    RESTAURANTE,
    GIMNASIO,
    OTRO
}
