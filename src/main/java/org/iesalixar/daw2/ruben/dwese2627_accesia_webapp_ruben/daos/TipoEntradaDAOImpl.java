package org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.entities.Establecimiento;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.entities.TipoEntrada;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Implementación del DAO de TipoEntrada usando JPA (EntityManager).
 */
@Repository
@Transactional
public class TipoEntradaDAOImpl implements TipoEntradaDAO {

    private static final Logger logger = LoggerFactory.getLogger(TipoEntradaDAOImpl.class);

    /**
     * EntityManager de JPA con el que se hacen las consultas a la base de datos.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Devuelve todos los tipos de entrada ordenados por nombre.
     * Con JOIN FETCH el establecimiento de cada tipo viene cargado en la misma consulta.
     *
     * @return lista con todos los tipos de entrada
     */
    @Override
    public List<TipoEntrada> listarTiposEntrada() {
        logger.info("Listando todos los tipos de entrada con su establecimiento.");
        String jpql = "SELECT t FROM TipoEntrada t JOIN FETCH t.establecimiento ORDER BY t.nombre";
        List<TipoEntrada> tipos = entityManager.createQuery(jpql, TipoEntrada.class).getResultList();
        logger.info("Se han obtenido {} tipos de entrada de la base de datos.", tipos.size());
        return tipos;
    }

    /**
     * Guarda un tipo de entrada nuevo en la base de datos.
     *
     * @param tipoEntrada tipo de entrada que se quiere guardar
     */
    @Override
    public void insertarTipoEntrada(TipoEntrada tipoEntrada) {
        logger.info("Insertando tipo de entrada {} para el establecimiento {}",
                tipoEntrada.getNombre(),
                tipoEntrada.getEstablecimiento() != null ? tipoEntrada.getEstablecimiento().getId() : null);
        entityManager.persist(tipoEntrada);
        logger.info("Tipo de entrada insertado correctamente con id {}", tipoEntrada.getId());
    }

    /**
     * Actualiza un tipo de entrada que ya existe.
     *
     * @param tipoEntrada tipo de entrada con los datos nuevos
     */
    @Override
    public void actualizarTipoEntrada(TipoEntrada tipoEntrada) {
        logger.info("Actualizando tipo de entrada con id {}", tipoEntrada.getId());
        entityManager.merge(tipoEntrada);
        logger.info("Tipo de entrada con id {} actualizado correctamente", tipoEntrada.getId());
    }

    /**
     * Borra el tipo de entrada con el id indicado. Si no existe, solo lo indica en el log.
     *
     * @param id id del tipo de entrada que se quiere borrar
     */
    @Override
    public void borrarTipoEntrada(Long id) {
        logger.info("Borrando tipo de entrada con id {}", id);
        TipoEntrada tipoEntrada = entityManager.find(TipoEntrada.class, id);
        if (tipoEntrada != null) {
            entityManager.remove(tipoEntrada);
            logger.info("El tipo de entrada con id {} ha sido borrado correctamente", id);
        } else {
            logger.warn("No existe ningún tipo de entrada con el id {}", id);
        }
    }

    /**
     * Busca un tipo de entrada por su id.
     *
     * @param id id del tipo de entrada
     * @return el tipo de entrada encontrado o null si no existe
     */
    @Override
    public TipoEntrada obtenerTipoEntradaPorId(Long id) {
        logger.info("Buscando tipo de entrada con id {}", id);
        TipoEntrada tipoEntrada = entityManager.find(TipoEntrada.class, id);
        if (tipoEntrada != null) {
            logger.info("Tipo de entrada encontrado: {} - {}", tipoEntrada.getId(), tipoEntrada.getNombre());
        } else {
            logger.warn("No existe el tipo de entrada con id {}", id);
        }
        return tipoEntrada;
    }

    /**
     * Comprueba si el establecimiento ya tiene un tipo de entrada con ese nombre
     * (sin distinguir mayúsculas y minúsculas).
     *
     * @param nombre            nombre que se quiere comprobar
     * @param establecimientoId id del establecimiento
     * @return true si ya existe
     */
    @Override
    public boolean existeTipoEntradaPorNombreYEstablecimiento(String nombre, Long establecimientoId) {
        logger.info("Comprobando si el establecimiento {} ya tiene un tipo de entrada {}", establecimientoId, nombre);
        String jpql = "SELECT COUNT(t) FROM TipoEntrada t "
                + "WHERE UPPER(t.nombre) = :nombre AND t.establecimiento.id = :establecimientoId";
        Long total = entityManager.createQuery(jpql, Long.class)
                .setParameter("nombre", nombre.toUpperCase())
                .setParameter("establecimientoId", establecimientoId)
                .getSingleResult();
        boolean existe = total != null && total > 0;
        logger.info("¿El establecimiento {} ya tiene un tipo de entrada {}? {}", establecimientoId, nombre, existe);
        return existe;
    }

    /**
     * Comprueba si el establecimiento tiene otro tipo de entrada con ese nombre,
     * sin contar el que se está editando.
     *
     * @param nombre            nombre que se quiere comprobar
     * @param establecimientoId id del establecimiento
     * @param id                id del tipo de entrada que se está editando
     * @return true si otro tipo de entrada del establecimiento ya usa ese nombre
     */
    @Override
    public boolean existeTipoEntradaPorNombreYEstablecimientoYDistintoId(String nombre, Long establecimientoId, Long id) {
        logger.info("Comprobando si el establecimiento {} tiene otro tipo de entrada {} sin contar el de id {}",
                establecimientoId, nombre, id);
        String jpql = "SELECT COUNT(t) FROM TipoEntrada t "
                + "WHERE UPPER(t.nombre) = :nombre AND t.establecimiento.id = :establecimientoId AND t.id <> :id";
        Long total = entityManager.createQuery(jpql, Long.class)
                .setParameter("nombre", nombre.toUpperCase())
                .setParameter("establecimientoId", establecimientoId)
                .setParameter("id", id)
                .getSingleResult();
        boolean existe = total != null && total > 0;
        logger.info("¿Existe otro tipo de entrada {} en el establecimiento {} (sin contar el id {})? {}",
                nombre, establecimientoId, id, existe);
        return existe;
    }

    /**
     * Devuelve una página de tipos de entrada ordenada por el campo indicado.
     * El establecimiento se carga con fetch para poder mostrar su nombre en el listado,
     * y se puede ordenar también por el nombre del establecimiento.
     *
     * @param pagina         número de página (empieza en 0)
     * @param tamanio        número de tipos de entrada por página
     * @param campoOrden     campo por el que se ordena
     * @param direccionOrden asc o desc
     * @return lista con los tipos de entrada de esa página
     */
    @Override
    public List<TipoEntrada> listarTiposEntradaPaginados(int pagina, int tamanio, String campoOrden, String direccionOrden) {
        logger.info("Listando tipos de entrada: página={}, tamaño={}, campoOrden={}, dirección={}",
                pagina, tamanio, campoOrden, direccionOrden);
        int offset = pagina * tamanio;

        // 1. Construcción de Criteria
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TipoEntrada> cq = cb.createQuery(TipoEntrada.class);
        Root<TipoEntrada> root = cq.from(TipoEntrada.class);

        // Cargamos el establecimiento junto con el tipo de entrada (equivale al JOIN FETCH)
        root.fetch("establecimiento", JoinType.INNER);

        // Join aparte para poder ordenar por el nombre del establecimiento
        Join<TipoEntrada, Establecimiento> establecimientoJoin = root.join("establecimiento", JoinType.INNER);

        // 2. Campos por los que se permite ordenar (lista blanca)
        Path<?> rutaOrden;
        switch (campoOrden) {
            case "id" -> rutaOrden = root.get("id");
            case "nombre" -> rutaOrden = root.get("nombre");
            case "precioReferencia" -> rutaOrden = root.get("precioReferencia");
            case "cupo" -> rutaOrden = root.get("cupo");
            case "establecimientoNombre", "establecimiento.nombre" -> rutaOrden = establecimientoJoin.get("nombre");
            default -> {
                logger.warn("Campo de ordenación '{}' no válido, se ordena por 'nombre'.", campoOrden);
                rutaOrden = root.get("nombre");
            }
        }

        // 3. Dirección de ordenación
        boolean descendente = "desc".equalsIgnoreCase(direccionOrden);
        Order orden = descendente ? cb.desc(rutaOrden) : cb.asc(rutaOrden);

        // 4. Aplicar la ordenación a la consulta
        cq.select(root).orderBy(orden);

        // 5. Crear la consulta, aplicar la paginación y ejecutarla
        List<TipoEntrada> tipos = entityManager.createQuery(cq)
                .setFirstResult(offset)
                .setMaxResults(tamanio)
                .getResultList();
        logger.info("Se han obtenido {} tipos de entrada en la página {}", tipos.size(), pagina);
        return tipos;
    }

    /**
     * Cuenta cuántos tipos de entrada hay en total.
     *
     * @return número total de tipos de entrada
     */
    @Override
    public long contarTiposEntrada() {
        logger.info("Contando el total de tipos de entrada de la base de datos.");
        String jpql = "SELECT COUNT(t) FROM TipoEntrada t";
        Long total = entityManager.createQuery(jpql, Long.class).getSingleResult();
        logger.info("Total de tipos de entrada: {}", total);
        return (total != null) ? total : 0L;
    }
}
