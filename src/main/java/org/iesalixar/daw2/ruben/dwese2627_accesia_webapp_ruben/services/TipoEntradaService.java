package org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.services;

import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.dtos.TipoEntradaCreateDTO;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.dtos.TipoEntradaDTO;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.dtos.TipoEntradaDetailDTO;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.dtos.TipoEntradaUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Servicio de TipoEntrada: contiene la lógica del CRUD de tipos de entrada.
 * El controlador llama a este servicio y el servicio llama a los DAOs.
 */
public interface TipoEntradaService {

    /**
     * Devuelve una página de tipos de entrada para el listado.
     *
     * @param pageable número de página, tamaño y ordenación
     * @return página con los tipos de entrada
     */
    Page<TipoEntradaDTO> listar(Pageable pageable);

    /**
     * Devuelve los datos de un tipo de entrada para rellenar el formulario de edición.
     *
     * @param id id del tipo de entrada
     * @return los datos del tipo de entrada
     */
    TipoEntradaUpdateDTO obtenerParaEditar(Long id);

    /**
     * Crea un tipo de entrada nuevo.
     *
     * @param dto datos del formulario de alta
     */
    void crear(TipoEntradaCreateDTO dto);

    /**
     * Actualiza un tipo de entrada que ya existe.
     *
     * @param dto datos del formulario de edición
     */
    void actualizar(TipoEntradaUpdateDTO dto);

    /**
     * Borra un tipo de entrada.
     *
     * @param id id del tipo de entrada que se quiere borrar
     */
    void borrar(Long id);

    /**
     * Devuelve el detalle de un tipo de entrada con su establecimiento.
     *
     * @param id id del tipo de entrada
     * @return el detalle del tipo de entrada
     */
    TipoEntradaDetailDTO obtenerDetalle(Long id);
}
