## Estructura del proyecto

El proyecto sigue una arquitectura en capas con separación clara de responsabilidades:
```
historico-visitas/
├── pom.xml
├── lib/
│   └── objectdb-jk-2.9.5.jar
└── src/main/
    ├── java/com/historico/
    │   ├── Main.java
    │   ├── model/Cliente.java
    │   ├── db/ConexionBBDD.java
    │   ├── crud/ClienteCRUD.java
    │   └── ui/InteraccionUsuario.java
    └── resources/META-INF/
        └── persistence.xml
```

| Clase | Responsabilidad |
|---|---|
| `Cliente` | Entidad JPA que modela el cliente con su histórico de visitas (`List<Date>`) |
| `ConexionBBDD` | Singleton que gestiona el `EntityManagerFactory` y las conexiones |
| `ClienteCRUD` | Operaciones crear, buscar, listar, actualizar, eliminar y agregar visitas |
| `InteraccionUsuario` | Menú interactivo por consola que delega en `ClienteCRUD` |
| `Main` | Punto de entrada: lanza el menú o ejecuta pruebas automáticas |

---

## Desarrollo

### Clase `Cliente`

La clase `Cliente` es la entidad central del sistema. Se anota con `@Entity` para que ObjectDB la gestione como objeto persistente. El campo más relevante es `fechasVisitas`, una `List<Date>` anotada con `@ElementCollection` que permite almacenar el array de fechas directamente sobre el objeto, sin tablas adicionales:
```java
@Entity
public class Cliente {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String nombre, apellido1, apellido2;
    private String comercialPrincipal;
    private long idEmpresa;

    @ElementCollection
    private List<Date> fechasVisitas;

    public void agregarVisita(Date fecha) {
        this.fechasVisitas.add(fecha);
    }
}
```

El uso de `@ElementCollection` es la clave del diseño: ObjectDB persiste la colección de forma nativa, eliminando la necesidad de una entidad de visita separada.

### Clase `ConexionBBDD`

Implementa el patrón Singleton para garantizar que solo existe un `EntityManagerFactory` durante el ciclo de vida de la aplicación. La URL de conexión se pasa directamente como propiedad para compatibilidad con ObjectDB 2.9.5 y Jakarta Persistence 3.1:
```java
private ConexionBBDD() {
    Map<String, String> props = new HashMap<>();
    props.put("jakarta.persistence.jdbc.url",
              "objectdb:historico-visitas.odb");
    emf = Persistence.createEntityManagerFactory(
              PERSISTENCE_UNIT, props);
}
```

Expone tres métodos públicos: `abrirConexion()`, `cerrarConexion(EntityManager)` y `cerrarFabrica()`.

### Clase `ClienteCRUD`

Implementa las cuatro operaciones CRUD sobre `Cliente`, más un método específico para el histórico de visitas. Cada método abre y cierra su propio `EntityManager` con manejo de transacciones y rollback ante errores:

- `crear(Cliente)` — persiste un nuevo cliente y devuelve el objeto con el id asignado.
- `buscarPorId(long)` — usa `em.find()` para localizar un cliente por su identificador.
- `listarTodos()` — ejecuta la query JPQL `SELECT c FROM Cliente c`.
- `actualizar(long, ...)` — carga el cliente, aplica solo los campos no nulos y confirma la transacción.
- `eliminar(long)` — localiza y elimina el cliente si existe.
- `agregarVisita(long, Date)` — carga el cliente y añade la fecha al array de visitas.

### Clase `InteraccionUsuario`

Gestiona el menú de consola con un bucle `do-while`. Utiliza `Scanner` para leer la entrada y delega toda la lógica en `ClienteCRUD`. El método `imprimirCliente()` muestra el histórico de visitas formateadas con `SimpleDateFormat`:
```java
private void imprimirCliente(Cliente c) {
    System.out.println("  id: " + c.getId());
    System.out.println("  nombre: " + c.getNombre()
        + " " + c.getApellido1() + " " + c.getApellido2());
    System.out.println("  visitas: "
        + c.getFechasVisitas().size());
    c.getFechasVisitas().forEach(f ->
        System.out.println("    - " + FMT.format(f)));
}
```

### Clase `Main`

El punto de entrada acepta un argumento opcional. Sin argumentos arranca el menú interactivo; con el argumento `test` ejecuta pruebas automáticas que verifican todas las operaciones del CRUD:
```java
public static void main(String[] args) {
    if (args.length > 0 && args[0].equalsIgnoreCase("test")) {
        ejecutarPruebas();
    } else {
        new InteraccionUsuario().iniciar();
    }
    ConexionBBDD.getInstancia().cerrarFabrica();
}
```
