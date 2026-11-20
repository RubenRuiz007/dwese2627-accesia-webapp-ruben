package org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.daos;

import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.entities.Establecimiento;

import java.util.List;

/**
 * DAO de Establecimiento: define las operaciones de acceso a la base de datos para los establecimientos.
 * Sigue el mismo modelo que RegionDAO del proyecto ticket-logger (lado 1 de la relación).
 */
public interface EstablecimientoDAO {

    /**
     * Devuelve todos los establecimientos ordenados por nombre.
     *
     * @return lista con todos los establecimientos
     */
    List<Establecimiento> listarEstablecimientos();

    /**
     * Guarda un establecimiento nuevo en la base de datos.
     *
     * @param establecimiento establecimiento que se quiere guardar
     */
    void insertarEstablecimiento(Establecimiento establecimiento);

    /**
     * Actualiza un establecimiento que ya existe.
     *
     * @param establecimiento establecimiento con los datos nuevos
     */
    void actualizarEstablecimiento(Establecimiento establecimiento);

    /**
     * Borra el establecimiento con el id indicado.
     *
     * @param id id del establecimiento que se quiere borrar
     */
    void borrarEstablecimiento(Long id);

    /**
     * Busca un establecimiento por su id.
     *
     * @param id id del establecimiento
     * @return el establecimiento encontrado o null si no existe
     */
    Establecimiento obtenerEstablecimientoPorId(Long id);

    /**
     * Busca un establecimiento por su id y trae también sus tipos de entrada en la misma consulta (JOIN FETCH).
     *
     * @param id id del establecimiento
     * @return el establecimiento con sus tipos de entrada o null si no existe
     */
    Establecimiento obtenerEstablecimientoPorIdConTiposEntrada(Long id);

    /**
     * Comprueba si ya existe un establecimiento con ese nombre en esa dirección (se usa al crear).
     *
     * @param nombre    nombre que se quiere comprobar
     * @param direccion dirección que se quiere comprobar
     * @return true si ya existe un establecimiento con ese nombre y esa dirección
     */
    boolean existeEstablecimientoPorNombreYDireccion(String nombre, String direccion);

    /**
     * Comprueba si existe otro establecimiento con ese nombre y esa dirección sin contar el que se está editando
     * (se usa al actualizar).
     *
     * @param nombre    nombre que se quiere comprobar
     * @param direccion dirección que se quiere comprobar
     * @param id        id del establecimiento que se está editando
     * @return true si otro establecimiento ya usa ese nombre y esa dirección
     */
    boolean existeEstablecimientoPorNombreYDireccionYDistintoId(String nombre, String direccion, Long id);

    /**
     * Devuelve una página de establecimientos ordenada por el campo indicado.
     *
     * @param pagina         número de página (empieza en 0)
     * @param tamanio        número de establecimientos por página
     * @param campoOrden     campo por el que se ordena
     * @param direccionOrden asc o desc
     * @return lista con los establecimientos de esa página
     */
    List<Establecimiento> listarEstablecimientosPaginados(int pagina, int tamanio, String campoOrden, String direccionOrden);

    /**
     * Cuenta cuántos establecimientos hay en total (para calcular el número de páginas).
     *
     * @return número total de establecimientos
     */
    long contarEstablecimientos();
}
