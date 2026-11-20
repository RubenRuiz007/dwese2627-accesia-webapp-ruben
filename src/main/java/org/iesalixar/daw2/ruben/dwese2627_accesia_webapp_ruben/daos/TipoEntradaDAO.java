package org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.daos;

import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.entities.TipoEntrada;

import java.util.List;

/**
 * DAO de TipoEntrada: define las operaciones de acceso a la base de datos para los tipos de entrada.
 * Sigue el mismo modelo que ProvinceDAO del proyecto ticket-logger (lado N de la relación).
 */
public interface TipoEntradaDAO {

    /**
     * Devuelve todos los tipos de entrada junto con su establecimiento.
     *
     * @return lista con todos los tipos de entrada
     */
    List<TipoEntrada> listarTiposEntrada();

    /**
     * Guarda un tipo de entrada nuevo en la base de datos.
     *
     * @param tipoEntrada tipo de entrada que se quiere guardar
     */
    void insertarTipoEntrada(TipoEntrada tipoEntrada);

    /**
     * Actualiza un tipo de entrada que ya existe.
     *
     * @param tipoEntrada tipo de entrada con los datos nuevos
     */
    void actualizarTipoEntrada(TipoEntrada tipoEntrada);

    /**
     * Borra el tipo de entrada con el id indicado.
     *
     * @param id id del tipo de entrada que se quiere borrar
     */
    void borrarTipoEntrada(Long id);

    /**
     * Busca un tipo de entrada por su id.
     *
     * @param id id del tipo de entrada
     * @return el tipo de entrada encontrado o null si no existe
     */
    TipoEntrada obtenerTipoEntradaPorId(Long id);

    /**
     * Comprueba si el establecimiento ya tiene un tipo de entrada con ese nombre (se usa al crear).
     *
     * @param nombre            nombre que se quiere comprobar
     * @param establecimientoId id del establecimiento
     * @return true si ya existe un tipo de entrada con ese nombre en ese establecimiento
     */
    boolean existeTipoEntradaPorNombreYEstablecimiento(String nombre, Long establecimientoId);

    /**
     * Comprueba si el establecimiento tiene otro tipo de entrada con ese nombre,
     * sin contar el que se está editando (se usa al actualizar).
     *
     * @param nombre            nombre que se quiere comprobar
     * @param establecimientoId id del establecimiento
     * @param id                id del tipo de entrada que se está editando
     * @return true si otro tipo de entrada del establecimiento ya usa ese nombre
     */
    boolean existeTipoEntradaPorNombreYEstablecimientoYDistintoId(String nombre, Long establecimientoId, Long id);

    /**
     * Devuelve una página de tipos de entrada (con su establecimiento) ordenada por el campo indicado.
     *
     * @param pagina         número de página (empieza en 0)
     * @param tamanio        número de tipos de entrada por página
     * @param campoOrden     campo por el que se ordena
     * @param direccionOrden asc o desc
     * @return lista con los tipos de entrada de esa página
     */
    List<TipoEntrada> listarTiposEntradaPaginados(int pagina, int tamanio, String campoOrden, String direccionOrden);

    /**
     * Cuenta cuántos tipos de entrada hay en total (para calcular el número de páginas).
     *
     * @return número total de tipos de entrada
     */
    long contarTiposEntrada();
}
