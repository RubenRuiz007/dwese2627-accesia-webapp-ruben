package org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.dtos.EstablecimientoCreateDTO;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.dtos.EstablecimientoDTO;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.dtos.EstablecimientoDetailDTO;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.dtos.EstablecimientoUpdateDTO;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.entities.TipoEstablecimiento;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.exceptions.DuplicateResourceException;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.exceptions.InvalidFileException;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.exceptions.ResourceInUseException;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.exceptions.ResourceNotFoundException;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.services.EstablecimientoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

/**
 * Controlador que gestiona el CRUD de establecimientos (listado, detalle, alta, edición y borrado).
 * Usa EstablecimientoService para la lógica y devuelve las vistas de la carpeta views/establecimiento.
 */
@Controller
@RequestMapping("/establecimientos")
public class EstablecimientoController {

    private static final Logger logger = LoggerFactory.getLogger(EstablecimientoController.class);

    /**
     * Sirve para leer los mensajes de los ficheros messages_es y messages_en según el idioma.
     */
    @Autowired
    private MessageSource messageSource;

    /**
     * Servicio con la lógica del CRUD de establecimientos.
     */
    @Autowired
    private EstablecimientoService establecimientoService;

    /**
     * Añade a todas las vistas de este controlador la lista de tipos de establecimiento
     * (DISCOTECA, BAR...) para el desplegable del formulario.
     *
     * @return todos los valores del enum TipoEstablecimiento
     */
    @ModelAttribute("tiposEstablecimiento")
    public TipoEstablecimiento[] tiposEstablecimiento() {
        return TipoEstablecimiento.values();
    }

    /**
     * Muestra el listado de establecimientos paginado y ordenado.
     *
     * @param pageable página, tamaño y ordenación (por defecto 10 por página ordenados por nombre)
     * @param model    modelo para pasar datos a la vista
     * @param locale   idioma actual
     * @return la vista del listado de establecimientos
     */
    @GetMapping
    public String listarEstablecimientos(
            @PageableDefault(size = 10, sort = "nombre", direction = Sort.Direction.ASC) Pageable pageable,
            Model model,
            Locale locale) {

        logger.info("Listando establecimientos page={}, size={}, sort={}",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        try {
            Page<EstablecimientoDTO> paginaEstablecimientos = establecimientoService.listar(pageable);

            logger.info("Se han cargado {} establecimientos en la página {}.",
                    paginaEstablecimientos.getNumberOfElements(), paginaEstablecimientos.getNumber());

            model.addAttribute("page", paginaEstablecimientos);

            // Para mantener la ordenación actual en los enlaces de la vista (sort=campo,asc|desc)
            String sortParam = "nombre,asc";
            if (paginaEstablecimientos.getSort().isSorted()) {
                Sort.Order orden = paginaEstablecimientos.getSort().iterator().next();
                sortParam = orden.getProperty() + "," + orden.getDirection().name().toLowerCase();
            }
            model.addAttribute("sortParam", sortParam);
        } catch (Exception e) {
            logger.error("Error al listar los establecimientos: {}", e.getMessage(), e);
            String errorMessage = messageSource.getMessage("msg.establecimiento-controller.list.error", null, locale);
            model.addAttribute("errorMessage", errorMessage);
        }
        return "views/establecimiento/establecimiento-list";
    }

    /**
     * Muestra el formulario vacío para crear un establecimiento.
     * Por defecto sale marcado como activo y con el aforo público.
     *
     * @param model modelo para pasar datos a la vista
     * @return la vista del formulario
     */
    @GetMapping("/new")
    public String mostrarFormularioNuevo(Model model) {
        logger.info("Mostrando formulario para nuevo establecimiento.");
        EstablecimientoCreateDTO establecimientoDTO = new EstablecimientoCreateDTO();
        establecimientoDTO.setActivo(true);
        establecimientoDTO.setAforoPublico(true);
        model.addAttribute("establecimiento", establecimientoDTO);
        return "views/establecimiento/establecimiento-form";
    }

    /**
     * Muestra el formulario de edición con los datos del establecimiento.
     *
     * @param id                 id del establecimiento que se quiere editar
     * @param model              modelo para pasar datos a la vista
     * @param redirectAttributes para enviar mensajes al redirigir
     * @param locale             idioma actual
     * @return la vista del formulario o una redirección al listado si hay error
     */
    @GetMapping("/edit")
    public String mostrarFormularioEditar(@RequestParam("id") Long id,
                                          Model model,
                                          RedirectAttributes redirectAttributes,
                                          Locale locale) {

        logger.info("Mostrando formulario de edición para el establecimiento con ID {}", id);

        try {
            EstablecimientoUpdateDTO establecimientoDTO = establecimientoService.obtenerParaEditar(id);
            model.addAttribute("establecimiento", establecimientoDTO);
            return "views/establecimiento/establecimiento-form";

        } catch (ResourceNotFoundException ex) {
            logger.warn("No se encontró el establecimiento con ID {}", id);
            String msg = messageSource.getMessage("msg.establecimiento-controller.notFound", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", msg);
            return "redirect:/establecimientos";

        } catch (Exception e) {
            logger.error("Error al obtener el establecimiento con ID {}: {}", id, e.getMessage(), e);
            String msg = messageSource.getMessage("msg.establecimiento-controller.edit.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", msg);
            return "redirect:/establecimientos";
        }
    }

    /**
     * Guarda un establecimiento nuevo.
     * Primero se comprueban las validaciones del DTO, después la validación propia del nombre
     * y por último el servicio comprueba que no esté repetido y guarda la imagen si se ha subido.
     *
     * @param establecimientoDTO datos del formulario
     * @param result             errores de validación del formulario
     * @param imagenFile         imagen del establecimiento (opcional)
     * @param redirectAttributes para enviar mensajes al redirigir
     * @param locale             idioma actual
     * @return redirección al listado o la vista del formulario si hay errores
     */
    @PostMapping("/insert")
    public String insertarEstablecimiento(@Valid @ModelAttribute("establecimiento") EstablecimientoCreateDTO establecimientoDTO,
                                          BindingResult result,
                                          @RequestParam(value = "imagenFile", required = false) MultipartFile imagenFile,
                                          RedirectAttributes redirectAttributes,
                                          Locale locale) {

        logger.info("Insertando nuevo establecimiento con nombre {}", establecimientoDTO.getNombre());

        // Validación propia: el nombre tiene que tener al menos una letra
        validarNombre(establecimientoDTO.getNombre(), result);

        // Si hay errores de validación, se vuelve al formulario para mostrarlos
        if (result.hasErrors()) {
            return "views/establecimiento/establecimiento-form";
        }

        try {
            establecimientoService.crear(establecimientoDTO, imagenFile);
            logger.info("Establecimiento {} insertado con éxito.", establecimientoDTO.getNombre());
            return "redirect:/establecimientos";

        } catch (DuplicateResourceException ex) {
            // Ya existe uno con ese nombre en esa dirección: se marca el error en el campo nombre
            logger.warn("Ya existe el establecimiento {} en {}.", establecimientoDTO.getNombre(), establecimientoDTO.getDireccion());
            result.rejectValue("nombre", "msg.establecimiento-controller.duplicado");
            return "views/establecimiento/establecimiento-form";

        } catch (InvalidFileException ex) {
            logger.warn("Imagen no válida: {}", ex.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.establecimiento.image.invalid", null, locale));
            return "redirect:/establecimientos/new";

        } catch (Exception e) {
            logger.error("Error al insertar el establecimiento {}: {}", establecimientoDTO.getNombre(), e.getMessage(), e);
            String errorMessage = messageSource.getMessage("msg.establecimiento-controller.insert.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/establecimientos/new";
        }
    }

    /**
     * Actualiza un establecimiento que ya existe.
     *
     * @param establecimientoDTO datos del formulario
     * @param result             errores de validación del formulario
     * @param imagenFile         imagen nueva (opcional)
     * @param redirectAttributes para enviar mensajes al redirigir
     * @param locale             idioma actual
     * @return redirección al listado o la vista del formulario si hay errores
     */
    @PostMapping("/update")
    public String actualizarEstablecimiento(@Valid @ModelAttribute("establecimiento") EstablecimientoUpdateDTO establecimientoDTO,
                                            BindingResult result,
                                            @RequestParam(value = "imagenFile", required = false) MultipartFile imagenFile,
                                            RedirectAttributes redirectAttributes,
                                            Locale locale) {

        logger.info("Actualizando establecimiento con ID {}", establecimientoDTO.getId());

        // Validación propia: el nombre tiene que tener al menos una letra
        validarNombre(establecimientoDTO.getNombre(), result);

        if (result.hasErrors()) {
            return "views/establecimiento/establecimiento-form";
        }

        try {
            establecimientoService.actualizar(establecimientoDTO, imagenFile);
            logger.info("Establecimiento con ID {} actualizado con éxito.", establecimientoDTO.getId());
            return "redirect:/establecimientos";

        } catch (DuplicateResourceException ex) {
            logger.warn("Ya existe otro establecimiento {} en {}.", establecimientoDTO.getNombre(), establecimientoDTO.getDireccion());
            result.rejectValue("nombre", "msg.establecimiento-controller.duplicado");
            return "views/establecimiento/establecimiento-form";

        } catch (ResourceNotFoundException ex) {
            logger.warn("No se encontró el establecimiento con ID {}", establecimientoDTO.getId());
            String msg = messageSource.getMessage("msg.establecimiento-controller.notFound", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", msg);
            return "redirect:/establecimientos";

        } catch (InvalidFileException ex) {
            logger.warn("Imagen no válida: {}", ex.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.establecimiento.image.invalid", null, locale));
            return "redirect:/establecimientos/edit?id=" + establecimientoDTO.getId();

        } catch (Exception e) {
            logger.error("Error al actualizar el establecimiento con ID {}: {}", establecimientoDTO.getId(), e.getMessage(), e);
            String errorMessage = messageSource.getMessage("msg.establecimiento-controller.update.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/establecimientos/edit?id=" + establecimientoDTO.getId();
        }
    }

    /**
     * Borra un establecimiento. Si todavía tiene tipos de entrada no se borra y se muestra un aviso.
     *
     * @param id                 id del establecimiento que se quiere borrar
     * @param redirectAttributes para enviar mensajes al redirigir
     * @param locale             idioma actual
     * @return redirección al listado
     */
    @PostMapping("/delete")
    public String borrarEstablecimiento(@RequestParam("id") Long id,
                                        RedirectAttributes redirectAttributes,
                                        Locale locale) {

        logger.info("Eliminando establecimiento con ID {}", id);

        try {
            establecimientoService.borrar(id);
            logger.info("Establecimiento con ID {} eliminado con éxito.", id);

        } catch (ResourceNotFoundException ex) {
            logger.warn("No se encontró el establecimiento con ID {}", id);
            String msg = messageSource.getMessage("msg.establecimiento-controller.notFound", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", msg);

        } catch (ResourceInUseException ex) {
            logger.warn("El establecimiento con ID {} tiene tipos de entrada y no se puede borrar.", id);
            String msg = messageSource.getMessage("msg.establecimiento-controller.delete.inUse", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", msg);

        } catch (Exception e) {
            logger.error("Error al eliminar el establecimiento con ID {}: {}", id, e.getMessage(), e);
            String msg = messageSource.getMessage("msg.establecimiento-controller.delete.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", msg);
        }
        return "redirect:/establecimientos";
    }

    /**
     * Muestra el detalle de un establecimiento con la lista de sus tipos de entrada.
     *
     * @param id                 id del establecimiento
     * @param model              modelo para pasar datos a la vista
     * @param redirectAttributes para enviar mensajes al redirigir
     * @param locale             idioma actual
     * @return la vista de detalle o una redirección al listado si hay error
     */
    @GetMapping("/detail")
    public String mostrarDetalle(@RequestParam("id") Long id,
                                 Model model,
                                 RedirectAttributes redirectAttributes,
                                 Locale locale) {

        logger.info("Mostrando detalle del establecimiento con ID {}", id);

        try {
            EstablecimientoDetailDTO establecimientoDTO = establecimientoService.obtenerDetalle(id);
            model.addAttribute("establecimiento", establecimientoDTO);
            return "views/establecimiento/establecimiento-detail";

        } catch (ResourceNotFoundException ex) {
            String msg = messageSource.getMessage("msg.establecimiento-controller.notFound", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", msg);
            return "redirect:/establecimientos";

        } catch (Exception e) {
            logger.error("Error al obtener el detalle del establecimiento {}: {}", id, e.getMessage(), e);
            String msg = messageSource.getMessage("msg.establecimiento-controller.detail.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", msg);
            return "redirect:/establecimientos";
        }
    }

    /**
     * Validación propia: el nombre del establecimiento tiene que tener al menos una letra
     * (no se permiten nombres como "123" o "---").
     * Si no la cumple, se añade el error al campo nombre del formulario.
     *
     * @param nombre nombre escrito en el formulario
     * @param result errores del formulario
     */
    private void validarNombre(String nombre, BindingResult result) {
        if (nombre != null && !nombre.isBlank() && !nombre.matches(".*\\p{L}.*")) {
            result.rejectValue("nombre", "msg.establecimiento.nombre.letras");
        }
    }
}
