package com.historico.crud;

import com.historico.db.ConexionBBDD;
import com.historico.model.Cliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

public class ClienteCRUD {

    private final ConexionBBDD conexion;

    public ClienteCRUD() {
        this.conexion = ConexionBBDD.getInstancia();
    }

    public Cliente crear(Cliente cliente) {
        EntityManager em = conexion.abrirConexion();
        try {
            em.getTransaction().begin();
            em.persist(cliente);
            em.getTransaction().commit();
            System.out.println("cliente creado con id: " + cliente.getId());
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.err.println("error al crear: " + e.getMessage());
        } finally {
            conexion.cerrarConexion(em);
        }
        return cliente;
    }

    public Cliente buscarPorId(long id) {
        EntityManager em = conexion.abrirConexion();
        Cliente cliente = null;
        try {
            cliente = em.find(Cliente.class, id);
        } finally {
            conexion.cerrarConexion(em);
        }
        return cliente;
    }

    public List<Cliente> listarTodos() {
        EntityManager em = conexion.abrirConexion();
        List<Cliente> clientes;
        try {
            TypedQuery<Cliente> query = em.createQuery("SELECT c FROM Cliente c", Cliente.class);
            clientes = query.getResultList();
        } finally {
            conexion.cerrarConexion(em);
        }
        return clientes;
    }

    public boolean actualizar(long id, String nombre, String apellido1,
                              String apellido2, String comercial, long idEmpresa) {
        EntityManager em = conexion.abrirConexion();
        boolean exito = false;
        try {
            em.getTransaction().begin();
            Cliente cliente = em.find(Cliente.class, id);
            if (cliente != null) {
                if (nombre != null)    cliente.setNombre(nombre);
                if (apellido1 != null) cliente.setApellido1(apellido1);
                if (apellido2 != null) cliente.setApellido2(apellido2);
                if (comercial != null) cliente.setComercialPrincipal(comercial);
                if (idEmpresa >= 0)    cliente.setIdEmpresa(idEmpresa);
                em.getTransaction().commit();
                exito = true;
                System.out.println("cliente actualizado.");
            } else {
                em.getTransaction().rollback();
                System.out.println("cliente no encontrado.");
            }
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.err.println("error al actualizar: " + e.getMessage());
        } finally {
            conexion.cerrarConexion(em);
        }
        return exito;
    }

    public boolean eliminar(long id) {
        EntityManager em = conexion.abrirConexion();
        boolean exito = false;
        try {
            em.getTransaction().begin();
            Cliente cliente = em.find(Cliente.class, id);
            if (cliente != null) {
                em.remove(cliente);
                em.getTransaction().commit();
                exito = true;
                System.out.println("cliente eliminado.");
            } else {
                em.getTransaction().rollback();
                System.out.println("cliente no encontrado.");
            }
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.err.println("error al eliminar: " + e.getMessage());
        } finally {
            conexion.cerrarConexion(em);
        }
        return exito;
    }

    public boolean agregarVisita(long idCliente, Date fecha) {
        EntityManager em = conexion.abrirConexion();
        boolean exito = false;
        try {
            em.getTransaction().begin();
            Cliente cliente = em.find(Cliente.class, idCliente);
            if (cliente != null) {
                cliente.agregarVisita(fecha);
                em.getTransaction().commit();
                exito = true;
                System.out.println("visita registrada.");
            } else {
                em.getTransaction().rollback();
                System.out.println("cliente no encontrado.");
            }
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.err.println("error al agregar visita: " + e.getMessage());
        } finally {
            conexion.cerrarConexion(em);
        }
        return exito;
    }
}