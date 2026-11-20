package org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.dtos.EstablecimientoDTO;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.dtos.TipoEntradaCreateDTO;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.dtos.TipoEntradaDTO;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.dtos.TipoEntradaDetailDTO;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.dtos.TipoEntradaUpdateDTO;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.exceptions.DuplicateResourceException;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.exceptions.ResourceNotFoundException;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.services.EstablecimientoService;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.services.TipoEntradaService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;

/**
 * Controlador que gestiona el CRUD de tipos de entrada (listado, detalle, alta, edición y borrado).
 * Usa TipoEntradaService para la lógica y EstablecimientoService para rellenar el desplegable de establecimientos.
 */
@Controller
@RequestMapping("/tipos-entrada")
public class TipoEntradaController {

    private static final Logger logger = LoggerFactory.getLogger(TipoEntradaController.class);

    /**
     * Sirve para leer los mensajes de los ficheros messages_es y messages_en según el idioma.
     */
    @Autowired
    private MessageSource messageSource;

    /**
     * Servicio con la lógica del CRUD de tipos de entrada.
     */
    @Autowired
    private TipoEntradaService tipoEntradaService;

    /**
     * Servicio de establecimientos, para el desplegable del formulario.
     */
    @Autowired
    private EstablecimientoService establecimientoService;

    /**
     * Muestra el listado de tipos de entrada paginado y ordenado, con el nombre de su establecimiento.
     *
     * @param pageable página, tamaño y ordenación (por defecto 10 por página ordenados por nombre)
     * @param model    modelo para pasar datos a la vista
     * @param locale   idioma actual
     * @return la vista del listado de tipos de entrada
     */
    @GetMapping
    public String listarTiposEntrada(
            @PageableDefault(size = 10, sort = "nombre", direction = Sort.Direction.ASC) Pageable pageable,
            Model model,
            Locale locale) {

        logger.info("Listando tipos de entrada page={}, size={}, sort={}",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        try {
            Page<TipoEntradaDTO> paginaTipos = tipoEntradaService.listar(pageable);

            logger.info("Se han cargado {} tipos de entrada en la página {}.",
                    paginaTipos.getNumberOfElements(), paginaTipos.getNumber());

            model.addAttribute("page", paginaTipos);

            // Para mantener la ordenación actual en los enlaces de la vista (sort=campo,asc|desc)
            String sortParam = "nombre,asc";
            if (paginaTipos.getSort().isSorted()) {
                Sort.Order orden = paginaTipos.getSort().iterator().next();
                sortParam = orden.getProperty() + "," + orden.getDirection().name().toLowerCase();
            }
            model.addAttribute("sortParam", sortParam);
        } catch (Exception e) {
            logger.error("Error al listar los tipos de entrada: {}", e.getMessage(), e);
            String errorMessage = messageSource.getMessage("msg.tipoEntrada-controller.list.error", null, locale);
            model.addAttribute("errorMessage", errorMessage);
        }
        return "views/tipo-entrada/tipo-entrada-list";
    }

    /**
     * Muestra el formulario vacío para crear un tipo de entrada, con el desplegable de establecimientos.
     *
     * @param model  modelo para pasar datos a la vista
     * @param locale idioma actual
     * @return la vista del formulario
     */
    @GetMapping("/new")
    public String mostrarFormularioNuevo(Model model, Locale locale) {
        logger.info("Mostrando formulario para nuevo tipo de entrada.");
        try {
            model.addAttribute("tipoEntrada", new TipoEntradaCreateDTO());
            model.addAttribute("listaEstablecimientos", establecimientoService.listarTodos());
        } catch (Exception e) {
            logger.error("Error al cargar los establecimientos para el formulario: {}", e.getMessage(), e);
            String errorMessage = messageSource.getMessage("msg.tipoEntrada-controller.edit.error", null, locale);
            model.addAttribute("errorMessage", errorMessage);
        }
        return "views/tipo-entrada/tipo-entrada-form";
    }

    /**
     * Muestra el formulario de edición con los datos del tipo de entrada y su establecimiento ya seleccionado.
     *
     * @param id                 id del tipo de entrada que se quiere editar
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

        logger.info("Mostrando formulario de edición para el tipo de entrada con ID {}", id);

        try {
            TipoEntradaUpdateDTO tipoDTO = tipoEntradaService.obtenerParaEditar(id);
            model.addAttribute("tipoEntrada", tipoDTO);
            model.addAttribute("listaEstablecimientos", establecimientoService.listarTodos());
            return "views/tipo-entrada/tipo-entrada-form";

        } catch (ResourceNotFoundException ex) {
            logger.warn("No se encontró el tipo de entrada con ID {}", id);
            String msg = messageSource.getMessage("msg.tipoEntrada-controller.notFound", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", msg);
            return "redirect:/tipos-entrada";

        } catch (Exception e) {
            logger.error("Error al obtener el tipo de entrada con ID {}: {}", id, e.getMessage(), e);
            String msg = messageSource.getMessage("msg.tipoEntrada-controller.edit.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", msg);
            return "redirect:/tipos-entrada";
        }
    }

    /**
     * Guarda un tipo de entrada nuevo.
     * Primero se comprueban las validaciones del DTO, después la validación propia del cupo
     * y por último el servicio comprueba que el nombre no esté repetido en ese establecimiento.
     *
     * @param tipoDTO            datos del formulario
     * @param result             errores de validación del formulario
     * @param redirectAttributes para enviar mensajes al redirigir
     * @param model              modelo para pasar datos a la vista
     * @param locale             idioma actual
     * @return redirección al listado o la vista del formulario si hay errores
     */
    @PostMapping("/insert")
    public String insertarTipoEntrada(@Valid @ModelAttribute("tipoEntrada") TipoEntradaCreateDTO tipoDTO,
                                      BindingResult result,
                                      RedirectAttributes redirectAttributes,
                                      Model model,
                                      Locale locale) {

        logger.info("Insertando nuevo tipo de entrada {}", tipoDTO.getNombre());

        // Se cargan los establecimientos (hacen falta para el select y para la validación del cupo)
        List<EstablecimientoDTO> establecimientos = establecimientoService.listarTodos();

        // Validación propia: el cupo no puede ser mayor que el aforo del establecimiento
        validarCupo(tipoDTO.getCupo(), tipoDTO.getEstablecimientoId(), establecimientos, result);

        // Si hay errores, se vuelve al formulario (con el select de establecimientos cargado)
        if (result.hasErrors()) {
            model.addAttribute("listaEstablecimientos", establecimientos);
            return "views/tipo-entrada/tipo-entrada-form";
        }

        try {
            tipoEntradaService.crear(tipoDTO);
            logger.info("Tipo de entrada {} insertado con éxito.", tipoDTO.getNombre());
            return "redirect:/tipos-entrada";

        } catch (DuplicateResourceException ex) {
            // El establecimiento ya tiene un tipo de entrada con ese nombre: error en el propio campo
            logger.warn("El establecimiento {} ya tiene un tipo de entrada {}.", tipoDTO.getEstablecimientoId(), tipoDTO.getNombre());
            result.rejectValue("nombre", "msg.tipoEntrada-controller.nombreExiste");
            model.addAttribute("listaEstablecimientos", establecimientos);
            return "views/tipo-entrada/tipo-entrada-form";

        } catch (ResourceNotFoundException ex) {
            // El establecimiento elegido no existe (por ejemplo, porque se ha borrado mientras tanto)
            logger.warn("El establecimiento con ID {} no existe.", tipoDTO.getEstablecimientoId());
            result.rejectValue("establecimientoId", "msg.tipoEntrada-controller.establecimientoNotFound");
            model.addAttribute("listaEstablecimientos", establecimientos);
            return "views/tipo-entrada/tipo-entrada-form";

        } catch (Exception e) {
            logger.error("Error al insertar el tipo de entrada {}: {}", tipoDTO.getNombre(), e.getMessage(), e);
            String errorMessage = messageSource.getMessage("msg.tipoEntrada-controller.insert.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/tipos-entrada/new";
        }
    }

    /**
     * Actualiza un tipo de entrada que ya existe.
     *
     * @param tipoDTO            datos del formulario
     * @param result             errores de validación del formulario
     * @param redirectAttributes para enviar mensajes al redirigir
     * @param model              modelo para pasar datos a la vista
     * @param locale             idioma actual
     * @return redirección al listado o la vista del formulario si hay errores
     */
    @PostMapping("/update")
    public String actualizarTipoEntrada(@Valid @ModelAttribute("tipoEntrada") TipoEntradaUpdateDTO tipoDTO,
                                        BindingResult result,
                                        RedirectAttributes redirectAttributes,
                                        Model model,
                                        Locale locale) {

        logger.info("Actualizando tipo de entrada con ID {}", tipoDTO.getId());

        List<EstablecimientoDTO> establecimientos = establecimientoService.listarTodos();

        // Validación propia: el cupo no puede ser mayor que el aforo del establecimiento
        validarCupo(tipoDTO.getCupo(), tipoDTO.getEstablecimientoId(), establecimientos, result);

        if (result.hasErrors()) {
            model.addAttribute("listaEstablecimientos", establecimientos);
            return "views/tipo-entrada/tipo-entrada-form";
        }

        try {
            tipoEntradaService.actualizar(tipoDTO);
            logger.info("Tipo de entrada con ID {} actualizado con éxito.", tipoDTO.getId());
            return "redirect:/tipos-entrada";

        } catch (DuplicateResourceException ex) {
            logger.warn("El establecimiento {} ya tiene otro tipo de entrada {}.", tipoDTO.getEstablecimientoId(), tipoDTO.getNombre());
            result.rejectValue("nombre", "msg.tipoEntrada-controller.nombreExiste");
            model.addAttribute("listaEstablecimientos", establecimientos);
            return "views/tipo-entrada/tipo-entrada-form";

        } catch (ResourceNotFoundException ex) {
            // Puede faltar el tipo de entrada o el establecimiento: se comprueba cuál ha sido
            if ("establecimiento".equals(ex.getResource())) {
                logger.warn("El establecimiento con ID {} no existe.", tipoDTO.getEstablecimientoId());
                result.rejectValue("establecimientoId", "msg.tipoEntrada-controller.establecimientoNotFound");
                model.addAttribute("listaEstablecimientos", establecimientos);
                return "views/tipo-entrada/tipo-entrada-form";
            }
            logger.warn("No se encontró el tipo de entrada con ID {}", tipoDTO.getId());
            String msg = messageSource.getMessage("msg.tipoEntrada-controller.notFound", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", msg);
            return "redirect:/tipos-entrada";

        } catch (Exception e) {
            logger.error("Error al actualizar el tipo de entrada con ID {}: {}", tipoDTO.getId(), e.getMessage(), e);
            String errorMessage = messageSource.getMessage("msg.tipoEntrada-controller.update.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/tipos-entrada/edit?id=" + tipoDTO.getId();
        }
    }

    /**
     * Borra un tipo de entrada.
     *
     * @param id                 id del tipo de entrada que se quiere borrar
     * @param redirectAttributes para enviar mensajes al redirigir
     * @param locale             idioma actual
     * @return redirección al listado
     */
    @PostMapping("/delete")
    public String borrarTipoEntrada(@RequestParam("id") Long id,
                                    RedirectAttributes redirectAttributes,
                                    Locale locale) {

        logger.info("Eliminando tipo de entrada con ID {}", id);

        try {
            tipoEntradaService.borrar(id);
            logger.info("Tipo de entrada con ID {} eliminado con éxito.", id);

        } catch (ResourceNotFoundException ex) {
            logger.warn("No se encontró el tipo de entrada con ID {}", id);
            String msg = messageSource.getMessage("msg.tipoEntrada-controller.notFound", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", msg);

        } catch (Exception e) {
            logger.error("Error al eliminar el tipo de entrada con ID {}: {}", id, e.getMessage(), e);
            String msg = messageSource.getMessage("msg.tipoEntrada-controller.delete.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", msg);
        }
        return "redirect:/tipos-entrada";
    }

    /**
     * Muestra el detalle de un tipo de entrada con los datos de su establecimiento.
     *
     * @param id                 id del tipo de entrada
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

        logger.info("Mostrando detalle del tipo de entrada con ID {}", id);

        try {
            TipoEntradaDetailDTO tipoDTO = tipoEntradaService.obtenerDetalle(id);
            model.addAttribute("tipoEntrada", tipoDTO);
            return "views/tipo-entrada/tipo-entrada-detail";

        } catch (ResourceNotFoundException ex) {
            String msg = messageSource.getMessage("msg.tipoEntrada-controller.notFound", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", msg);
            return "redirect:/tipos-entrada";

        } catch (Exception e) {
            logger.error("Error al obtener el detalle del tipo de entrada {}: {}", id, e.getMessage(), e);
            String msg = messageSource.getMessage("msg.tipoEntrada-controller.detail.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", msg);
            return "redirect:/tipos-entrada";
        }
    }

    /**
     * Validación propia: el cupo de entradas no puede ser mayor que el aforo máximo
     * del establecimiento elegido. Si no la cumple, se añade el error al campo cupo del formulario.
     *
     * @param cupo              cupo escrito en el formulario (puede venir vacío)
     * @param establecimientoId establecimiento elegido en el desplegable
     * @param establecimientos  lista de establecimientos (para buscar su aforo)
     * @param result            errores del formulario
     */
    private void validarCupo(Integer cupo, Long establecimientoId,
                             List<EstablecimientoDTO> establecimientos, BindingResult result) {
        if (cupo == null || establecimientoId == null) {
            return;
        }
        for (EstablecimientoDTO establecimiento : establecimientos) {
            if (establecimiento.getId().equals(establecimientoId)
                    && establecimiento.getAforoMaximo() != null
                    && cupo > establecimiento.getAforoMaximo()) {
                result.rejectValue("cupo", "msg.tipoEntrada.cupo.mayorQueAforo");
            }
        }
    }
}
