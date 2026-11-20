package org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.services;

import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.daos.EstablecimientoDAO;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.daos.TipoEntradaDAO;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.dtos.TipoEntradaCreateDTO;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.dtos.TipoEntradaDTO;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.dtos.TipoEntradaDetailDTO;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.dtos.TipoEntradaUpdateDTO;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.entities.Establecimiento;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.entities.TipoEntrada;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.exceptions.DuplicateResourceException;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.exceptions.ResourceNotFoundException;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.mappers.TipoEntradaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación del servicio de TipoEntrada.
 * Usa los DAOs para acceder a la base de datos y devuelve DTOs al controlador.
 */
@Service
@Transactional
public class TipoEntradaServiceImpl implements TipoEntradaService {

    /**
     * DAO de tipos de entrada.
     */
    @Autowired
    private TipoEntradaDAO tipoEntradaDAO;

    /**
     * DAO de establecimientos. Hace falta para cargar el establecimiento real al crear o editar un tipo de entrada.
     */
    @Autowired
    private EstablecimientoDAO establecimientoDAO;

    /**
     * Devuelve una página de tipos de entrada para el listado.
     *
     * @param pageable número de página, tamaño y ordenación
     * @return página con los tipos de entrada
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TipoEntradaDTO> listar(Pageable pageable) {
        // Sacamos el campo y la dirección de ordenación del Pageable
        String campoOrden = "nombre";
        String direccionOrden = "asc";
        if (pageable.getSort().isSorted()) {
            Sort.Order orden = pageable.getSort().iterator().next();
            campoOrden = orden.getProperty();
            direccionOrden = orden.getDirection().name().toLowerCase();
        }

        List<TipoEntrada> tipos = tipoEntradaDAO.listarTiposEntradaPaginados(
                pageable.getPageNumber(), pageable.getPageSize(), campoOrden, direccionOrden);
        long total = tipoEntradaDAO.contarTiposEntrada();

        // Construimos el Page que espera la vista
        return new PageImpl<>(TipoEntradaMapper.aListaDTO(tipos), pageable, total);
    }

    /**
     * Devuelve los datos de un tipo de entrada para rellenar el formulario de edición.
     *
     * @param id id del tipo de entrada
     * @return los datos del tipo de entrada
     * @throws ResourceNotFoundException si el tipo de entrada no existe
     */
    @Override
    @Transactional(readOnly = true)
    public TipoEntradaUpdateDTO obtenerParaEditar(Long id) {
        TipoEntrada tipo = tipoEntradaDAO.obtenerTipoEntradaPorId(id);
        if (tipo == null) {
            throw new ResourceNotFoundException("tipoEntrada", "id", id);
        }
        return TipoEntradaMapper.aUpdateDTO(tipo);
    }

    /**
     * Crea un tipo de entrada nuevo comprobando que el establecimiento existe
     * y que no tiene ya otro tipo de entrada con el mismo nombre.
     *
     * @param dto datos del formulario de alta
     * @throws DuplicateResourceException si el establecimiento ya tiene un tipo de entrada con ese nombre
     * @throws ResourceNotFoundException  si el establecimiento elegido no existe
     */
    @Override
    public void crear(TipoEntradaCreateDTO dto) {
        if (tipoEntradaDAO.existeTipoEntradaPorNombreYEstablecimiento(dto.getNombre().trim(), dto.getEstablecimientoId())) {
            throw new DuplicateResourceException("tipoEntrada", "nombre", dto.getNombre());
        }

        TipoEntrada tipo = TipoEntradaMapper.aEntidad(dto);
        tipo.setEstablecimiento(buscarEstablecimiento(dto.getEstablecimientoId()));
        tipoEntradaDAO.insertarTipoEntrada(tipo);
    }

    /**
     * Actualiza un tipo de entrada comprobando que existe, que el establecimiento existe
     * y que ese establecimiento no tiene otro tipo de entrada con el mismo nombre.
     *
     * @param dto datos del formulario de edición
     * @throws DuplicateResourceException si el establecimiento ya tiene otro tipo de entrada con ese nombre
     * @throws ResourceNotFoundException  si el tipo de entrada o el establecimiento no existen
     */
    @Override
    public void actualizar(TipoEntradaUpdateDTO dto) {
        if (tipoEntradaDAO.existeTipoEntradaPorNombreYEstablecimientoYDistintoId(
                dto.getNombre().trim(), dto.getEstablecimientoId(), dto.getId())) {
            throw new DuplicateResourceException("tipoEntrada", "nombre", dto.getNombre());
        }

        TipoEntrada tipo = tipoEntradaDAO.obtenerTipoEntradaPorId(dto.getId());
        if (tipo == null) {
            throw new ResourceNotFoundException("tipoEntrada", "id", dto.getId());
        }

        TipoEntradaMapper.copiarAEntidadExistente(dto, tipo);
        tipo.setEstablecimiento(buscarEstablecimiento(dto.getEstablecimientoId()));
        tipoEntradaDAO.actualizarTipoEntrada(tipo);
    }

    /**
     * Borra un tipo de entrada comprobando antes que existe.
     *
     * @param id id del tipo de entrada que se quiere borrar
     * @throws ResourceNotFoundException si el tipo de entrada no existe
     */
    @Override
    public void borrar(Long id) {
        if (tipoEntradaDAO.obtenerTipoEntradaPorId(id) == null) {
            throw new ResourceNotFoundException("tipoEntrada", "id", id);
        }
        tipoEntradaDAO.borrarTipoEntrada(id);
    }

    /**
     * Devuelve el detalle de un tipo de entrada con su establecimiento.
     * Se hace dentro de la transacción para que el establecimiento (LAZY) se pueda cargar.
     *
     * @param id id del tipo de entrada
     * @return el detalle del tipo de entrada
     * @throws ResourceNotFoundException si el tipo de entrada no existe
     */
    @Override
    @Transactional(readOnly = true)
    public TipoEntradaDetailDTO obtenerDetalle(Long id) {
        TipoEntrada tipo = tipoEntradaDAO.obtenerTipoEntradaPorId(id);
        if (tipo == null) {
            throw new ResourceNotFoundException("tipoEntrada", "id", id);
        }
        return TipoEntradaMapper.aDetalleDTO(tipo);
    }

    /**
     * Busca el establecimiento por su id y lanza una excepción si no existe.
     *
     * @param establecimientoId id del establecimiento elegido en el formulario
     * @return el establecimiento encontrado
     * @throws ResourceNotFoundException si el establecimiento no existe
     */
    private Establecimiento buscarEstablecimiento(Long establecimientoId) {
        Establecimiento establecimiento = (establecimientoId != null)
                ? establecimientoDAO.obtenerEstablecimientoPorId(establecimientoId) : null;
        if (establecimiento == null) {
            throw new ResourceNotFoundException("establecimiento", "id", establecimientoId);
        }
        return establecimiento;
    }
}
