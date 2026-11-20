package org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.services;

import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.dtos.EstablecimientoCreateDTO;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.dtos.EstablecimientoDTO;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.dtos.EstablecimientoDetailDTO;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.dtos.EstablecimientoUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Servicio de Establecimiento: contiene la lógica del CRUD de establecimientos.
 * El controlador llama a este servicio y el servicio llama al DAO.
 */
public interface EstablecimientoService {

    /**
     * Devuelve una página de establecimientos para el listado.
     *
     * @param pageable número de página, tamaño y ordenación
     * @return página con los establecimientos
     */
    Page<EstablecimientoDTO> listar(Pageable pageable);

    /**
     * Devuelve todos los establecimientos (se usa para el desplegable del formulario de tipos de entrada).
     *
     * @return lista con todos los establecimientos
     */
    List<EstablecimientoDTO> listarTodos();

    /**
     * Devuelve los datos de un establecimiento para rellenar el formulario de edición.
     *
     * @param id id del establecimiento
     * @return los datos del establecimiento
     */
    EstablecimientoUpdateDTO obtenerParaEditar(Long id);

    /**
     * Crea un establecimiento nuevo y, si se adjunta, guarda su imagen.
     *
     * @param dto        datos del formulario de alta
     * @param imagenFile imagen del establecimiento (opcional)
     */
    void crear(EstablecimientoCreateDTO dto, MultipartFile imagenFile);

    /**
     * Actualiza un establecimiento que ya existe y, si se adjunta una imagen nueva, la cambia.
     *
     * @param dto        datos del formulario de edición
     * @param imagenFile imagen nueva (opcional)
     */
    void actualizar(EstablecimientoUpdateDTO dto, MultipartFile imagenFile);

    /**
     * Borra un establecimiento.
     *
     * @param id id del establecimiento que se quiere borrar
     */
    void borrar(Long id);

    /**
     * Devuelve el detalle de un establecimiento con sus tipos de entrada.
     *
     * @param id id del establecimiento
     * @return el detalle del establecimiento
     */
    EstablecimientoDetailDTO obtenerDetalle(Long id);
}
