package com.historico.ui;

import com.historico.crud.ClienteCRUD;
import com.historico.model.Cliente;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class InteraccionUsuario {

    private final ClienteCRUD crud;
    private final Scanner scanner;
    private static final SimpleDateFormat FMT = new SimpleDateFormat("dd/MM/yyyy");

    public InteraccionUsuario() {
        this.crud = new ClienteCRUD();
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero();
            procesarOpcion(opcion);
        } while (opcion != 0);
    }

    private void mostrarMenu() {
        System.out.println("\n============================");
        System.out.println("  historico de visitas");
        System.out.println("============================");
        System.out.println("1. crear cliente");
        System.out.println("2. buscar cliente por id");
        System.out.println("3. listar todos");
        System.out.println("4. actualizar cliente");
        System.out.println("5. eliminar cliente");
        System.out.println("6. registrar visita");
        System.out.println("0. salir");
        System.out.print("opcion: ");
    }

    private void procesarOpcion(int opcion) {
        switch (opcion) {
            case 1 -> menuCrear();
            case 2 -> menuBuscar();
            case 3 -> menuListar();
            case 4 -> menuActualizar();
            case 5 -> menuEliminar();
            case 6 -> menuAgregarVisita();
            case 0 -> System.out.println("saliendo...");
            default -> System.out.println("opcion no valida.");
        }
    }

    private void menuCrear() {
        System.out.print("nombre: ");         String nombre    = scanner.nextLine().trim();
        System.out.print("apellido 1: ");     String ap1       = scanner.nextLine().trim();
        System.out.print("apellido 2: ");     String ap2       = scanner.nextLine().trim();
        System.out.print("comercial: ");      String comercial = scanner.nextLine().trim();
        System.out.print("id empresa: ");     long idEmpresa   = leerEntero();
        crud.crear(new Cliente(nombre, ap1, ap2, comercial, idEmpresa));
    }

    private void menuBuscar() {
        System.out.print("id: ");
        Cliente c = crud.buscarPorId(leerEntero());
        if (c != null) imprimirCliente(c);
        else System.out.println("no encontrado.");
    }

    private void menuListar() {
        List<Cliente> lista = crud.listarTodos();
        if (lista.isEmpty()) System.out.println("no hay clientes.");
        else lista.forEach(this::imprimirCliente);
    }

    private void menuActualizar() {
        System.out.print("id: ");             long id      = leerEntero();
        System.out.print("nuevo nombre: ");   String n     = scanner.nextLine().trim();
        System.out.print("nuevo ap1: ");      String a1    = scanner.nextLine().trim();
        System.out.print("nuevo ap2: ");      String a2    = scanner.nextLine().trim();
        System.out.print("nuevo comercial: "); String c    = scanner.nextLine().trim();
        System.out.print("nuevo id empresa (-1 omitir): "); long emp = leerEntero();
        crud.actualizar(id, n.isEmpty()?null:n, a1.isEmpty()?null:a1,
                a2.isEmpty()?null:a2, c.isEmpty()?null:c, emp);
    }

    private void menuEliminar() {
        System.out.print("id: ");
        crud.eliminar(leerEntero());
    }

    private void menuAgregarVisita() {
        System.out.print("id cliente: ");  long id = leerEntero();
        System.out.print("fecha (dd/MM/yyyy): ");
        try {
            Date fecha = FMT.parse(scanner.nextLine().trim());
            crud.agregarVisita(id, fecha);
        } catch (ParseException e) {
            System.out.println("formato incorrecto.");
        }
    }

    private void imprimirCliente(Cliente c) {
        System.out.println("\n  id: " + c.getId());
        System.out.println("  nombre: " + c.getNombre() + " " + c.getApellido1() + " " + c.getApellido2());
        System.out.println("  comercial: " + c.getComercialPrincipal());
        System.out.println("  empresa: " + c.getIdEmpresa());
        System.out.println("  visitas: " + c.getFechasVisitas().size());
        c.getFechasVisitas().forEach(f -> System.out.println("    - " + FMT.format(f)));
    }

    private int leerEntero() {
        try { return Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { return 0; }
    }
}