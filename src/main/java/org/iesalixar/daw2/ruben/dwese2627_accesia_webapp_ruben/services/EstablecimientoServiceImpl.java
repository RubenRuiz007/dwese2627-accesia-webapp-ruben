package org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.services;

import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.daos.EstablecimientoDAO;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.dtos.EstablecimientoCreateDTO;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.dtos.EstablecimientoDTO;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.dtos.EstablecimientoDetailDTO;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.dtos.EstablecimientoUpdateDTO;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.entities.Establecimiento;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.exceptions.DuplicateResourceException;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.exceptions.InvalidFileException;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.exceptions.ResourceInUseException;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.exceptions.ResourceNotFoundException;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.mappers.EstablecimientoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Implementación del servicio de Establecimiento.
 * Usa el DAO para acceder a la base de datos y devuelve DTOs al controlador.
 * Desde un servicio se puede llamar a otros servicios: aquí se usa FileStorageService para las imágenes.
 */
@Service
@Transactional
public class EstablecimientoServiceImpl implements EstablecimientoService {

    /**
     * Límite de tamaño máximo permitido para la imagen (2 MB).
     */
    private static final long MAX_IMAGE_SIZE_BYTES = 2 * 1024 * 1024;

    /**
     * DAO de establecimientos.
     */
    @Autowired
    private EstablecimientoDAO establecimientoDAO;

    /**
     * Servicio para guardar y borrar las imágenes de los establecimientos.
     */
    @Autowired
    private FileStorageService fileStorageService;

    /**
     * Devuelve una página de establecimientos para el listado.
     *
     * @param pageable número de página, tamaño y ordenación
     * @return página con los establecimientos
     */
    @Override
    @Transactional(readOnly = true)
    public Page<EstablecimientoDTO> listar(Pageable pageable) {
        // Sacamos el campo y la dirección de ordenación del Pageable
        String campoOrden = "nombre";
        String direccionOrden = "asc";
        if (pageable.getSort().isSorted()) {
            Sort.Order orden = pageable.getSort().iterator().next();
            campoOrden = orden.getProperty();
            direccionOrden = orden.getDirection().name().toLowerCase();
        }

        List<Establecimiento> establecimientos = establecimientoDAO.listarEstablecimientosPaginados(
                pageable.getPageNumber(), pageable.getPageSize(), campoOrden, direccionOrden);
        long total = establecimientoDAO.contarEstablecimientos();

        // Construimos el Page que espera la vista
        return new PageImpl<>(EstablecimientoMapper.aListaDTO(establecimientos), pageable, total);
    }

    /**
     * Devuelve todos los establecimientos para el desplegable del formulario de tipos de entrada.
     *
     * @return lista con todos los establecimientos
     */
    @Override
    @Transactional(readOnly = true)
    public List<EstablecimientoDTO> listarTodos() {
        return EstablecimientoMapper.aListaDTO(establecimientoDAO.listarEstablecimientos());
    }

    /**
     * Devuelve los datos de un establecimiento para rellenar el formulario de edición.
     *
     * @param id id del establecimiento
     * @return los datos del establecimiento
     * @throws ResourceNotFoundException si el establecimiento no existe
     */
    @Override
    @Transactional(readOnly = true)
    public EstablecimientoUpdateDTO obtenerParaEditar(Long id) {
        Establecimiento establecimiento = establecimientoDAO.obtenerEstablecimientoPorId(id);
        if (establecimiento == null) {
            throw new ResourceNotFoundException("establecimiento", "id", id);
        }
        return EstablecimientoMapper.aUpdateDTO(establecimiento);
    }

    /**
     * Crea un establecimiento nuevo comprobando que no exista otro con el mismo nombre en la misma dirección.
     * Si se adjunta una imagen, se valida (tipo y tamaño) y se guarda.
     *
     * @param dto        datos del formulario de alta
     * @param imagenFile imagen del establecimiento (opcional)
     * @throws DuplicateResourceException si ya existe un establecimiento con ese nombre y esa dirección
     * @throws InvalidFileException       si la imagen no es válida o no se puede guardar
     */
    @Override
    public void crear(EstablecimientoCreateDTO dto, MultipartFile imagenFile) {
        if (establecimientoDAO.existeEstablecimientoPorNombreYDireccion(dto.getNombre().trim(), dto.getDireccion().trim())) {
            throw new DuplicateResourceException("establecimiento", "nombre", dto.getNombre());
        }

        // Si hay imagen, validar + guardar
        if (imagenFile != null && !imagenFile.isEmpty()) {
            // Validaciones (lanzan InvalidFileException)
            validarImagen(imagenFile);

            String newImageWebPath = fileStorageService.saveFile(imagenFile);
            if (newImageWebPath == null || newImageWebPath.isBlank()) {
                throw new InvalidFileException(
                        "establecimiento",
                        "imagenFile",
                        imagenFile.getOriginalFilename(),
                        "No se pudo guardar la imagen."
                );
            }
            dto.setImagen(newImageWebPath);
        }

        Establecimiento establecimiento = EstablecimientoMapper.aEntidad(dto);
        establecimientoDAO.insertarEstablecimiento(establecimiento);
    }

    /**
     * Actualiza un establecimiento comprobando que existe y que no hay otro con el mismo nombre en la misma dirección.
     * Si se adjunta una imagen nueva, se valida, se guarda y se borra la anterior.
     *
     * @param dto        datos del formulario de edición
     * @param imagenFile imagen nueva (opcional)
     * @throws DuplicateResourceException si otro establecimiento ya tiene ese nombre y esa dirección
     * @throws ResourceNotFoundException  si el establecimiento no existe
     * @throws InvalidFileException       si la imagen no es válida o no se puede guardar
     */
    @Override
    public void actualizar(EstablecimientoUpdateDTO dto, MultipartFile imagenFile) {
        if (establecimientoDAO.existeEstablecimientoPorNombreYDireccionYDistintoId(
                dto.getNombre().trim(), dto.getDireccion().trim(), dto.getId())) {
            throw new DuplicateResourceException("establecimiento", "nombre", dto.getNombre());
        }

        Establecimiento establecimiento = establecimientoDAO.obtenerEstablecimientoPorId(dto.getId());
        if (establecimiento == null) {
            throw new ResourceNotFoundException("establecimiento", "id", dto.getId());
        }

        // Si hay imagen nueva, validar + guardar + borrar la anterior
        if (imagenFile != null && !imagenFile.isEmpty()) {
            // Validaciones (lanzan InvalidFileException)
            validarImagen(imagenFile);

            String oldImagePath = dto.getImagen(); // ruta actual (puede ser null o vacía)

            String newImageWebPath = fileStorageService.saveFile(imagenFile);
            if (newImageWebPath == null || newImageWebPath.isBlank()) {
                throw new InvalidFileException(
                        "establecimiento",
                        "imagenFile",
                        imagenFile.getOriginalFilename(),
                        "No se pudo guardar la imagen."
                );
            }
            dto.setImagen(newImageWebPath);

            // Borrar la anterior si existía
            if (oldImagePath != null && !oldImagePath.isBlank()) {
                fileStorageService.deleteFile(oldImagePath);
            }
        }

        EstablecimientoMapper.copiarAEntidadExistente(dto, establecimiento);
        establecimientoDAO.actualizarEstablecimiento(establecimiento);
    }

    /**
     * Borra un establecimiento. No se deja borrar si todavía tiene tipos de entrada,
     * porque esos tipos de entrada se quedarían sin establecimiento.
     *
     * @param id id del establecimiento que se quiere borrar
     * @throws ResourceNotFoundException si el establecimiento no existe
     * @throws ResourceInUseException    si el establecimiento todavía tiene tipos de entrada
     */
    @Override
    public void borrar(Long id) {
        Establecimiento establecimiento = establecimientoDAO.obtenerEstablecimientoPorIdConTiposEntrada(id);
        if (establecimiento == null) {
            throw new ResourceNotFoundException("establecimiento", "id", id);
        }
        if (!establecimiento.getTiposEntrada().isEmpty()) {
            throw new ResourceInUseException("establecimiento", "id", id);
        }
        establecimientoDAO.borrarEstablecimiento(id);
    }

    /**
     * Devuelve el detalle de un establecimiento con sus tipos de entrada (los trae con JOIN FETCH).
     *
     * @param id id del establecimiento
     * @return el detalle del establecimiento
     * @throws ResourceNotFoundException si el establecimiento no existe
     */
    @Override
    @Transactional(readOnly = true)
    public EstablecimientoDetailDTO obtenerDetalle(Long id) {
        Establecimiento establecimiento = establecimientoDAO.obtenerEstablecimientoPorIdConTiposEntrada(id);
        if (establecimiento == null) {
            throw new ResourceNotFoundException("establecimiento", "id", id);
        }
        return EstablecimientoMapper.aDetalleDTO(establecimiento);
    }

    /**
     * Valida la imagen del establecimiento:
     * - el tipo MIME tiene que empezar por image/
     * - el tamaño máximo es 2 MB
     *
     * @param file imagen subida
     * @throws InvalidFileException si no cumple las validaciones
     */
    private void validarImagen(MultipartFile file) {
        String contentType = file.getContentType();
        // Tipo no válido
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new InvalidFileException(
                    "establecimiento",
                    "imagenFile",
                    contentType,
                    "Tipo de archivo no permitido"
            );
        }
        // Tamaño excedido
        if (file.getSize() > MAX_IMAGE_SIZE_BYTES) {
            throw new InvalidFileException(
                    "establecimiento",
                    "imagenFile",
                    file.getSize(),
                    "Archivo demasiado grande (máximo " + MAX_IMAGE_SIZE_BYTES + " bytes)"
            );
        }
    }
}
