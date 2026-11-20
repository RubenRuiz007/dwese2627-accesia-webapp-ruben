package org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.mappers;

import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.dtos.*;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.entities.Establecimiento;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.entities.TipoEntrada;

import java.util.List;

/**
 * Mapper para pasar de la entidad Establecimiento a sus DTOs y al revés.
 * Se hace a mano, sin usar ninguna librería de mapeo.
 */
public class EstablecimientoMapper {

    // ─────────────────────────────────────────
    // Entidad → DTO (listado básico)
    // ─────────────────────────────────────────

    /**
     * Convierte un establecimiento en un EstablecimientoDTO.
     *
     * @param entidad establecimiento que se quiere convertir
     * @return el DTO básico, o null si el establecimiento es null
     */
    public static EstablecimientoDTO aDTO(Establecimiento entidad) {
        if (entidad == null) return null;
        EstablecimientoDTO dto = new EstablecimientoDTO();
        dto.setId(entidad.getId());
        dto.setNombre(entidad.getNombre());
        dto.setTipo(entidad.getTipo());
        dto.setDireccion(entidad.getDireccion());
        dto.setAforoMaximo(entidad.getAforoMaximo());
        dto.setActivo(entidad.isActivo());
        return dto;
    }

    /**
     * Convierte una lista de establecimientos en una lista de EstablecimientoDTO.
     *
     * @param entidades lista de establecimientos
     * @return lista de DTOs (vacía si la lista es null)
     */
    public static List<EstablecimientoDTO> aListaDTO(List<Establecimiento> entidades) {
        if (entidades == null) return List.of();
        return entidades.stream().map(EstablecimientoMapper::aDTO).toList();
    }

    // ─────────────────────────────────────────
    // Entidad → DTO (detalle con sus tipos de entrada)
    // ─────────────────────────────────────────

    /**
     * Convierte un establecimiento en un EstablecimientoDetailDTO, incluyendo sus tipos de entrada.
     *
     * @param entidad establecimiento con sus tipos de entrada ya cargados
     * @return el DTO de detalle, o null si el establecimiento es null
     */
    public static EstablecimientoDetailDTO aDetalleDTO(Establecimiento entidad) {
        if (entidad == null) return null;
        EstablecimientoDetailDTO dto = new EstablecimientoDetailDTO();
        dto.setId(entidad.getId());
        dto.setNombre(entidad.getNombre());
        dto.setTipo(entidad.getTipo());
        dto.setDireccion(entidad.getDireccion());
        dto.setDescripcion(entidad.getDescripcion());
        dto.setAforoMaximo(entidad.getAforoMaximo());
        dto.setPrecioReferencia(entidad.getPrecioReferencia());
        dto.setEdadMinima(entidad.getEdadMinima());
        dto.setExigeCuentaConfirmada(entidad.isExigeCuentaConfirmada());
        dto.setAforoPublico(entidad.isAforoPublico());
        dto.setActivo(entidad.isActivo());
        dto.setImagen(entidad.getImagen());
        dto.setTiposEntrada(aListaTipoEntradaDTO(entidad.getTiposEntrada()));
        return dto;
    }

    /**
     * Convierte un tipo de entrada en un TipoEntradaDTO sencillo para la tabla del detalle del establecimiento.
     *
     * @param tipo tipo de entrada que se quiere convertir
     * @return el DTO del tipo de entrada, o null si es null
     */
    public static TipoEntradaDTO aTipoEntradaDTO(TipoEntrada tipo) {
        if (tipo == null) return null;
        TipoEntradaDTO dto = new TipoEntradaDTO();
        dto.setId(tipo.getId());
        dto.setNombre(tipo.getNombre());
        dto.setPrecioReferencia(tipo.getPrecioReferencia());
        dto.setCupo(tipo.getCupo());
        return dto;
    }

    /**
     * Convierte una lista de tipos de entrada en una lista de TipoEntradaDTO.
     *
     * @param tipos lista de tipos de entrada
     * @return lista de DTOs (vacía si la lista es null)
     */
    public static List<TipoEntradaDTO> aListaTipoEntradaDTO(List<TipoEntrada> tipos) {
        if (tipos == null) return List.of();
        return tipos.stream().map(EstablecimientoMapper::aTipoEntradaDTO).toList();
    }

    /**
     * Convierte un establecimiento en un EstablecimientoUpdateDTO para rellenar el formulario de edición.
     *
     * @param entidad establecimiento que se quiere editar
     * @return el DTO de edición, o null si el establecimiento es null
     */
    public static EstablecimientoUpdateDTO aUpdateDTO(Establecimiento entidad) {
        if (entidad == null) return null;
        EstablecimientoUpdateDTO dto = new EstablecimientoUpdateDTO();
        dto.setId(entidad.getId());
        dto.setNombre(entidad.getNombre());
        dto.setTipo(entidad.getTipo());
        dto.setDireccion(entidad.getDireccion());
        dto.setDescripcion(entidad.getDescripcion());
        dto.setAforoMaximo(entidad.getAforoMaximo());
        dto.setPrecioReferencia(entidad.getPrecioReferencia());
        dto.setEdadMinima(entidad.getEdadMinima());
        dto.setExigeCuentaConfirmada(entidad.isExigeCuentaConfirmada());
        dto.setAforoPublico(entidad.isAforoPublico());
        dto.setActivo(entidad.isActivo());
        dto.setImagen(entidad.getImagen());
        return dto;
    }

    // ─────────────────────────────────────────
    // DTO (crear / editar) → Entidad
    // ─────────────────────────────────────────

    /**
     * Crea un establecimiento nuevo a partir del formulario de alta.
     * El id se deja vacío para que lo genere la base de datos.
     *
     * @param dto datos del formulario de alta
     * @return el establecimiento nuevo, o null si el DTO es null
     */
    public static Establecimiento aEntidad(EstablecimientoCreateDTO dto) {
        if (dto == null) return null;
        Establecimiento e = new Establecimiento();
        e.setNombre(dto.getNombre().trim());
        e.setTipo(dto.getTipo());
        e.setDireccion(dto.getDireccion().trim());
        e.setDescripcion(vacioANull(dto.getDescripcion()));
        e.setAforoMaximo(dto.getAforoMaximo());
        e.setPrecioReferencia(dto.getPrecioReferencia());
        e.setEdadMinima(dto.getEdadMinima());
        e.setExigeCuentaConfirmada(dto.isExigeCuentaConfirmada());
        e.setAforoPublico(dto.isAforoPublico());
        e.setActivo(dto.isActivo());
        e.setImagen(vacioANull(dto.getImagen()));
        return e;
    }

    /**
     * Copia los datos del formulario de edición en un establecimiento que ya existe en la base de datos.
     * No se toca el id ni la lista de tipos de entrada.
     *
     * @param dto     datos del formulario de edición
     * @param entidad establecimiento que ya existe
     */
    public static void copiarAEntidadExistente(EstablecimientoUpdateDTO dto, Establecimiento entidad) {
        if (dto == null || entidad == null) return;
        entidad.setNombre(dto.getNombre().trim());
        entidad.setTipo(dto.getTipo());
        entidad.setDireccion(dto.getDireccion().trim());
        entidad.setDescripcion(vacioANull(dto.getDescripcion()));
        entidad.setAforoMaximo(dto.getAforoMaximo());
        entidad.setPrecioReferencia(dto.getPrecioReferencia());
        entidad.setEdadMinima(dto.getEdadMinima());
        entidad.setExigeCuentaConfirmada(dto.isExigeCuentaConfirmada());
        entidad.setAforoPublico(dto.isAforoPublico());
        entidad.setActivo(dto.isActivo());
        entidad.setImagen(vacioANull(dto.getImagen()));
    }

    /**
     * Los campos opcionales del formulario llegan como texto vacío si no se rellenan.
     * Este método los cambia a null para no guardar textos vacíos en la base de datos.
     *
     * @param texto texto del formulario
     * @return el texto sin espacios al principio y al final, o null si está vacío
     */
    private static String vacioANull(String texto) {
        if (texto == null || texto.isBlank()) return null;
        return texto.trim();
    }
}
