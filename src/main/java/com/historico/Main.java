package com.historico;

import com.historico.crud.ClienteCRUD;
import com.historico.db.ConexionBBDD;
import com.historico.model.Cliente;
import com.historico.ui.InteraccionUsuario;
import java.util.Date;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("test")) {
            ejecutarPruebas();
        } else {
            new InteraccionUsuario().iniciar();
        }
        ConexionBBDD.getInstancia().cerrarFabrica();
    }

    private static void ejecutarPruebas() {
        System.out.println("=== inicio de pruebas ===\n");
        ClienteCRUD crud = new ClienteCRUD();

        System.out.println("-- crear --");
        Cliente c1 = crud.crear(new Cliente("Ana", "Garcia", "Lopez", "Pedro Ruiz", 10));
        Cliente c2 = crud.crear(new Cliente("Luis", "Martinez", "Sanchez", "Marta Gil", 20));
        Cliente c3 = crud.crear(new Cliente("Sofia", "Fernandez", "Torres", "Pedro Ruiz", 10));

        System.out.println("\n-- buscar --");
        Cliente encontrado = crud.buscarPorId(c1.getId());
        System.out.println("encontrado: " + encontrado.getNombre());

        System.out.println("\n-- listar --");
        List<Cliente> todos = crud.listarTodos();
        System.out.println("total: " + todos.size());

        System.out.println("\n-- actualizar --");
        crud.actualizar(c2.getId(), null, null, "Perez", null, -1);

        System.out.println("\n-- visitas --");
        crud.agregarVisita(c1.getId(), new Date());
        crud.agregarVisita(c1.getId(), new Date(System.currentTimeMillis() - 86400000L));
        System.out.println("visitas: " + crud.buscarPorId(c1.getId()).getFechasVisitas().size());

        System.out.println("\n-- eliminar --");
        crud.eliminar(c3.getId());

        System.out.println("\n=== pruebas completadas ===");
    }
}