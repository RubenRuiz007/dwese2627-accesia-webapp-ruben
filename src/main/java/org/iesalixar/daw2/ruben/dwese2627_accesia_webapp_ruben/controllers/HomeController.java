package org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador de la página de inicio de la aplicación.
 */
@Controller
public class HomeController {

    /**
     * Muestra la página de inicio.
     *
     * @return la vista index
     */
    @GetMapping("/")
    public String inicio() {
        return "index";
    }
}
