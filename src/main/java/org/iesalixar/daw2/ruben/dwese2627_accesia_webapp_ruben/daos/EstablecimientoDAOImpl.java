package org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import org.iesalixar.daw2.ruben.dwese2627_accesia_webapp_ruben.entities.Establecimiento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Implementación del DAO de Establecimiento usando JPA (EntityManager).
 */
@Repository
@Transactional
public class EstablecimientoDAOImpl implements EstablecimientoDAO {

    private static final Logger logger = LoggerFactory.getLogger(EstablecimientoDAOImpl.class);

    /**
     * EntityManager de JPA con el que se hacen las consultas a la base de datos.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Devuelve todos los establecimientos ordenados por nombre.
     *
     * @return lista con todos los establecimientos
     */
    @Override
    public List<Establecimiento> listarEstablecimientos() {
        logger.info("Listando todos los establecimientos de la base de datos.");
        String jpql = "SELECT e FROM Establecimiento e ORDER BY e.nombre";
        List<Establecimiento> establecimientos = entityManager.createQuery(jpql, Establecimiento.class).getResultList();
        logger.info("Se han obtenido {} establecimientos de la base de datos.", establecimientos.size());
        return establecimientos;
    }

    /**
     * Guarda un establecimiento nuevo en la base de datos.
     *
     * @param establecimiento establecimiento que se quiere guardar
     */
    @Override
    public void insertarEstablecimiento(Establecimiento establecimiento) {
        logger.info("Insertando establecimiento con nombre {}", establecimiento.getNombre());
        entityManager.persist(establecimiento);
        logger.info("Establecimiento insertado correctamente con id {}", establecimiento.getId());
    }

    /**
     * Actualiza un establecimiento que ya existe.
     *
     * @param establecimiento establecimiento con los datos nuevos
     */
    @Override
    public void actualizarEstablecimiento(Establecimiento establecimiento) {
        logger.info("Actualizando establecimiento con id {}", establecimiento.getId());
        entityManager.merge(establecimiento);
        logger.info("Establecimiento con id {} actualizado correctamente", establecimiento.getId());
    }

    /**
     * Borra el establecimiento con el id indicado. Si no existe, solo lo indica en el log.
     *
     * @param id id del establecimiento que se quiere borrar
     */
    @Override
    public void borrarEstablecimiento(Long id) {
        logger.info("Borrando establecimiento con id {}", id);
        Establecimiento establecimiento = entityManager.find(Establecimiento.class, id);
        if (establecimiento != null) {
            entityManager.remove(establecimiento);
            logger.info("El establecimiento con id {} ha sido borrado correctamente", id);
        } else {
            logger.warn("No existe ningún establecimiento con el id {}", id);
        }
    }

    /**
     * Busca un establecimiento por su id.
     *
     * @param id id del establecimiento
     * @return el establecimiento encontrado o null si no existe
     */
    @Override
    public Establecimiento obtenerEstablecimientoPorId(Long id) {
        logger.info("Buscando establecimiento con id {}", id);
        Establecimiento establecimiento = entityManager.find(Establecimiento.class, id);
        if (establecimiento != null) {
            logger.info("Establecimiento encontrado con id {}", id);
        } else {
            logger.warn("No existe el establecimiento con id {}", id);
        }
        return establecimiento;
    }

    /**
     * Busca un establecimiento por su id y trae también sus tipos de entrada en la misma consulta.
     * Lleva DISTINCT porque al hacer JOIN con una lista (tiposEntrada) el establecimiento saldría repetido.
     *
     * @param id id del establecimiento
     * @return el establecimiento con sus tipos de entrada o null si no existe
     */
    @Override
    public Establecimiento obtenerEstablecimientoPorIdConTiposEntrada(Long id) {
        logger.info("Buscando establecimiento con sus tipos de entrada por id {}", id);
        String jpql = "SELECT DISTINCT e FROM Establecimiento e LEFT JOIN FETCH e.tiposEntrada WHERE e.id = :id";
        List<Establecimiento> resultado = entityManager.createQuery(jpql, Establecimiento.class)
                .setParameter("id", id)
                .getResultList();
        if (resultado.isEmpty()) {
            logger.warn("No se ha encontrado el establecimiento con id {}", id);
            return null;
        }
        logger.info("Establecimiento con id {} encontrado con {} tipos de entrada",
                id, resultado.get(0).getTiposEntrada().size());
        return resultado.get(0);
    }

    /**
     * Comprueba si ya existe un establecimiento con ese nombre en esa dirección
     * (sin distinguir mayúsculas y minúsculas).
     *
     * @param nombre    nombre que se quiere comprobar
     * @param direccion dirección que se quiere comprobar
     * @return true si ya existe
     */
    @Override
    public boolean existeEstablecimientoPorNombreYDireccion(String nombre, String direccion) {
        logger.info("Comprobando si existe un establecimiento con nombre {} en {}", nombre, direccion);
        String jpql = "SELECT COUNT(e) FROM Establecimiento e "
                + "WHERE UPPER(e.nombre) = :nombre AND UPPER(e.direccion) = :direccion";
        Long total = entityManager.createQuery(jpql, Long.class)
                .setParameter("nombre", nombre.toUpperCase())
                .setParameter("direccion", direccion.toUpperCase())
                .getSingleResult();
        boolean existe = total != null && total > 0;
        logger.info("¿Existe un establecimiento con nombre {} en {}? {}", nombre, direccion, existe);
        return existe;
    }

    /**
     * Comprueba si existe otro establecimiento con ese nombre y esa dirección sin contar el que se está editando.
     *
     * @param nombre    nombre que se quiere comprobar
     * @param direccion dirección que se quiere comprobar
     * @param id        id del establecimiento que se está editando
     * @return true si otro establecimiento ya los usa
     */
    @Override
    public boolean existeEstablecimientoPorNombreYDireccionYDistintoId(String nombre, String direccion, Long id) {
        logger.info("Comprobando si existe otro establecimiento con nombre {} en {} sin contar el de id {}",
                nombre, direccion, id);
        String jpql = "SELECT COUNT(e) FROM Establecimiento e "
                + "WHERE UPPER(e.nombre) = :nombre AND UPPER(e.direccion) = :direccion AND e.id <> :id";
        Long total = entityManager.createQuery(jpql, Long.class)
                .setParameter("nombre", nombre.toUpperCase())
                .setParameter("direccion", direccion.toUpperCase())
                .setParameter("id", id)
                .getSingleResult();
        boolean existe = total != null && total > 0;
        logger.info("¿Existe otro establecimiento con nombre {} en {} (sin contar el id {})? {}",
                nombre, direccion, id, existe);
        return existe;
    }

    /**
     * Devuelve una página de establecimientos ordenada por el campo indicado.
     * Solo se permite ordenar por los campos de la lista blanca.
     *
     * @param pagina         número de página (empieza en 0)
     * @param tamanio        número de establecimientos por página
     * @param campoOrden     campo por el que se ordena
     * @param direccionOrden asc o desc
     * @return lista con los establecimientos de esa página
     */
    @Override
    public List<Establecimiento> listarEstablecimientosPaginados(int pagina, int tamanio, String campoOrden, String direccionOrden) {
        logger.info("Listando establecimientos: página={}, tamaño={}, campoOrden={}, dirección={}",
                pagina, tamanio, campoOrden, direccionOrden);
        int offset = pagina * tamanio;

        // 1. Construcción de Criteria
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Establecimiento> cq = cb.createQuery(Establecimiento.class);
        Root<Establecimiento> root = cq.from(Establecimiento.class);

        // 2. Campos por los que se permite ordenar (lista blanca)
        Path<?> rutaOrden;
        switch (campoOrden) {
            case "id" -> rutaOrden = root.get("id");
            case "nombre" -> rutaOrden = root.get("nombre");
            case "tipo" -> rutaOrden = root.get("tipo");
            case "aforoMaximo" -> rutaOrden = root.get("aforoMaximo");
            case "activo" -> rutaOrden = root.get("activo");
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
        List<Establecimiento> establecimientos = entityManager.createQuery(cq)
                .setFirstResult(offset)
                .setMaxResults(tamanio)
                .getResultList();
        logger.info("Se han obtenido {} establecimientos en la página {}", establecimientos.size(), pagina);
        return establecimientos;
    }

    /**
     * Cuenta cuántos establecimientos hay en total.
     *
     * @return número total de establecimientos
     */
    @Override
    public long contarEstablecimientos() {
        logger.info("Contando el total de establecimientos de la base de datos.");
        String jpql = "SELECT COUNT(e) FROM Establecimiento e";
        Long total = entityManager.createQuery(jpql, Long.class).getSingleResult();
        logger.info("Total de establecimientos: {}", total);
        return (total != null) ? total : 0L;
    }
}
