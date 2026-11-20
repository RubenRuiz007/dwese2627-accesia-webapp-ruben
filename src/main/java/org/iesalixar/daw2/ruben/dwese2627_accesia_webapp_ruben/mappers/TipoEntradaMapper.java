package org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.mappers;

import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.dtos.TipoEntradaCreateDTO;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.dtos.TipoEntradaDTO;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.dtos.TipoEntradaDetailDTO;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.dtos.TipoEntradaUpdateDTO;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.entities.TipoEntrada;

import java.util.List;

/**
 * Mapper para pasar de la entidad TipoEntrada a sus DTOs y al revés.
 * Se hace a mano, sin usar ninguna librería de mapeo.
 * El establecimiento real lo pone el servicio, porque el mapper no accede a la base de datos.
 */
public class TipoEntradaMapper {

    // ─────────────────────────────────────────
    // Entidad → DTO (listado básico)
    // ─────────────────────────────────────────

    /**
     * Convierte un tipo de entrada en un TipoEntradaDTO para el listado.
     * Del establecimiento solo se copia el nombre, que es lo que se muestra en la tabla.
     *
     * @param entidad tipo de entrada que se quiere convertir
     * @return el DTO del listado, o null si el tipo de entrada es null
     */
    public static TipoEntradaDTO aDTO(TipoEntrada entidad) {
        if (entidad == null) return null;
        TipoEntradaDTO dto = new TipoEntradaDTO();
        dto.setId(entidad.getId());
        dto.setNombre(entidad.getNombre());
        dto.setPrecioReferencia(entidad.getPrecioReferencia());
        dto.setCupo(entidad.getCupo());
        dto.setEstablecimientoNombre(entidad.getEstablecimiento() != null ? entidad.getEstablecimiento().getNombre() : null);
        return dto;
    }

    /**
     * Convierte una lista de tipos de entrada en una lista de TipoEntradaDTO.
     *
     * @param entidades lista de tipos de entrada
     * @return lista de DTOs (vacía si la lista es null)
     */
    public static List<TipoEntradaDTO> aListaDTO(List<TipoEntrada> entidades) {
        if (entidades == null) return List.of();
        return entidades.stream().map(TipoEntradaMapper::aDTO).toList();
    }

    // ─────────────────────────────────────────
    // Entidad → DTO (detalle con su establecimiento)
    // ─────────────────────────────────────────

    /**
     * Convierte un tipo de entrada en un TipoEntradaDetailDTO, incluyendo su establecimiento.
     *
     * @param entidad tipo de entrada que se quiere convertir
     * @return el DTO de detalle, o null si el tipo de entrada es null
     */
    public static TipoEntradaDetailDTO aDetalleDTO(TipoEntrada entidad) {
        if (entidad == null) return null;
        TipoEntradaDetailDTO dto = new TipoEntradaDetailDTO();
        dto.setId(entidad.getId());
        dto.setNombre(entidad.getNombre());
        dto.setPrecioReferencia(entidad.getPrecioReferencia());
        dto.setCupo(entidad.getCupo());
        dto.setEstablecimiento(EstablecimientoMapper.aDTO(entidad.getEstablecimiento()));
        return dto;
    }

    /**
     * Convierte un tipo de entrada en un TipoEntradaUpdateDTO para rellenar el formulario de edición.
     * Así el desplegable sale con el establecimiento actual ya seleccionado.
     *
     * @param entidad tipo de entrada que se quiere editar
     * @return el DTO de edición, o null si el tipo de entrada es null
     */
    public static TipoEntradaUpdateDTO aUpdateDTO(TipoEntrada entidad) {
        if (entidad == null) return null;
        TipoEntradaUpdateDTO dto = new TipoEntradaUpdateDTO();
        dto.setId(entidad.getId());
        dto.setNombre(entidad.getNombre());
        dto.setPrecioReferencia(entidad.getPrecioReferencia());
        dto.setCupo(entidad.getCupo());
        dto.setEstablecimientoId(entidad.getEstablecimiento() != null ? entidad.getEstablecimiento().getId() : null);
        return dto;
    }

    // ─────────────────────────────────────────
    // DTO (crear / editar) → Entidad
    // ─────────────────────────────────────────

    /**
     * Crea un tipo de entrada nuevo a partir del formulario de alta.
     * El id se deja vacío para que lo genere la base de datos y el establecimiento lo pone el servicio.
     *
     * @param dto datos del formulario de alta
     * @return el tipo de entrada nuevo, o null si el DTO es null
     */
    public static TipoEntrada aEntidad(TipoEntradaCreateDTO dto) {
        if (dto == null) return null;
        TipoEntrada tipo = new TipoEntrada();
        tipo.setNombre(dto.getNombre().trim());
        tipo.setPrecioReferencia(dto.getPrecioReferencia());
        tipo.setCupo(dto.getCupo());
        return tipo;
    }

    /**
     * Copia los datos del formulario de edición en un tipo de entrada que ya existe en la base de datos.
     * No se toca el id. El establecimiento lo pone el servicio.
     *
     * @param dto     datos del formulario de edición
     * @param entidad tipo de entrada que ya existe
     */
    public static void copiarAEntidadExistente(TipoEntradaUpdateDTO dto, TipoEntrada entidad) {
        if (dto == null || entidad == null) return;
        entidad.setNombre(dto.getNombre().trim());
        entidad.setPrecioReferencia(dto.getPrecioReferencia());
        entidad.setCupo(dto.getCupo());
    }
}
