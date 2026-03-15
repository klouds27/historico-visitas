package com.historico.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class ConexionBBDD {

    private static final String PERSISTENCE_UNIT = "historicoVisitasPU";
    private static ConexionBBDD instancia;
    private EntityManagerFactory emf;

    private ConexionBBDD() {
        Map<String, String> props = new HashMap<>();
        props.put("jakarta.persistence.jdbc.url", "objectdb:historico-visitas.odb");
        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT, props);
    }

    public static ConexionBBDD getInstancia() {
        if (instancia == null) {
            instancia = new ConexionBBDD();
        }
        return instancia;
    }

    public EntityManager abrirConexion() {
        return emf.createEntityManager();
    }

    public void cerrarConexion(EntityManager em) {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }

    public void cerrarFabrica() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}