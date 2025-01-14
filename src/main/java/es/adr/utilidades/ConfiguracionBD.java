package es.adr.utilidades;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConfiguracionBD {
    // CONNECTION
    public static final String URL = "jdbc:mysql://localhost:3306/pizzeria";
    public static final String USER = "root";
    public static final String PASS = "admin";

    // CREATE TABLES
    public static final String CREATE_TABLE_CLIENTE = "CREATE TABLE IF NOT EXISTS cliente (" +
            "id INT AUTO_INCREMENT PRIMARY KEY," +
            "dni VARCHAR(255) NOT NULL UNIQUE," +
            "nombre VARCHAR(255) NOT NULL," +
            "direccion VARCHAR(255) NOT NULL," +
            "telefono VARCHAR(255) NULL UNIQUE," +
            "email VARCHAR(255) NOT NULL UNIQUE," +
            "password VARCHAR(255) NOT NULL," +
            "administrador BOOLEAN NOT NULL DEFAULT false" +
            ");";

    public static final String CREATE_TABLE_PRODUCTO = "CREATE TABLE IF NOT EXISTS producto (" +
            "id INT AUTO_INCREMENT PRIMARY KEY," +
            "nombre VARCHAR(255) NOT NULL," +
            "precio DECIMAL(4,2) NOT NULL," +
            "tipo ENUM('pizza', 'pasta', 'bebida') NOT NULL," +
            "size ENUM('pequenya', 'mediana', 'grande') DEFAULT NULL," +
            "UNIQUE(nombre, size)" +
            ");";

    public static final String CREATE_TABLE_PRODUCTO_INGREDIENTE = "CREATE TABLE IF NOT EXISTS producto_ingrediente (" +
            "producto_id INT NOT NULL," +
            "ingrediente_id INT NOT NULL," +
            "PRIMARY KEY (producto_id, ingrediente_id)," +
            "FOREIGN KEY(producto_id) REFERENCES producto(id) ON DELETE CASCADE ON UPDATE CASCADE," +
            "FOREIGN KEY(ingrediente_id) REFERENCES ingrediente(id) ON DELETE CASCADE ON UPDATE CASCADE" +
            ");";

    public static final String CREATE_TABLE_INGREDIENTE = "CREATE TABLE IF NOT EXISTS ingrediente (" +
            "id INT AUTO_INCREMENT PRIMARY KEY," +
            "nombre VARCHAR(255) NOT NULL UNIQUE" +
            ");";

    public static final String CREATE_TABLE_INGREDIENTE_ALERGENO = "CREATE TABLE IF NOT EXISTS ingrediente_alergeno (" +
            "ingrediente_id INT NOT NULL," +
            "alergeno_id INT NOT NULL," +
            "PRIMARY KEY (ingrediente_id, alergeno_id)," +
            "FOREIGN KEY(ingrediente_id) REFERENCES ingrediente(id) ON DELETE CASCADE ON UPDATE CASCADE," +
            "FOREIGN KEY(alergeno_id) REFERENCES alergeno(id) ON DELETE CASCADE ON UPDATE CASCADE" +
            ");";

    public static final String CREATE_TABLE_ALERGENO = "CREATE TABLE IF NOT EXISTS alergeno (" +
            "id INT AUTO_INCREMENT PRIMARY KEY," +
            "nombre VARCHAR(255) UNIQUE NOT NULL" +
            ");";

    public static final String CREATE_TABLE_PEDIDO = "CREATE TABLE IF NOT EXISTS pedido (" +
            "id INT AUTO_INCREMENT PRIMARY KEY," +
            "fecha DATE NOT NULL," +
            "estado VARCHAR(255) NOT NULL," +
            "cliente_id INT NOT NULL," +
            "metodo_pago ENUM('tarjeta', 'efectivo') DEFAULT NULL," +
            "FOREIGN KEY(cliente_id) REFERENCES cliente(id)" +
            ");";

    public static final String CREATE_TABLE_PEDIDO_PRODUCTO = "CREATE TABLE IF NOT EXISTS pedido_producto (" +
            "id INT AUTO_INCREMENT PRIMARY KEY," +
            "pedido_id INT NOT NULL," +
            "producto_id INT NOT NULL," +
            "cantidad INT NOT NULL," +
            "FOREIGN KEY(pedido_id) REFERENCES pedido(id) ON DELETE CASCADE ON UPDATE CASCADE," +
            "FOREIGN KEY(producto_id) REFERENCES producto(id) ON DELETE CASCADE ON UPDATE CASCADE" +
            ");";

    // DROP TABLES
    private static final String DROP_TABLE_CLIENTE = "DROP TABLE IF EXISTS cliente";
    private static final String DROP_TABLE_PRODUCTO = "DROP TABLE IF EXISTS producto";
    private static final String DROP_TABLE_PRODUCTO_INGREDIENTE = "DROP TABLE IF EXISTS producto_ingrediente";
    private static final String DROP_TABLE_INGREDIENTE = "DROP TABLE IF EXISTS ingrediente";
    private static final String DROP_TABLE_ALERGENO = "DROP TABLE IF EXISTS alergeno";
    private static final String DROP_TABLE_PEDIDO = "DROP TABLE IF EXISTS pedido";
    private static final String DROP_TABLE_INGREDIENTE_ALERGENO = "DROP TABLE IF EXISTS ingrediente_alergeno";
    private static final String DROP_TABLE_PEDIDO_PRODUCTO = "DROP TABLE IF EXISTS pedido_producto";

    // ClienteDao
    public static final String FIND_CLIENTE = "SELECT id, dni, nombre, direccion, telefono, email, password, administrador FROM cliente WHERE id = ?";
    public static final String FIND_ALL_CLIENTE = "SELECT id, dni, nombre, direccion, telefono, email, password, administrador FROM cliente";
    public static final String FIND_CLIENTE_BY_EMAIL = "SELECT id, dni, nombre, direccion, telefono, email, password, administrador FROM cliente WHERE email = ?";
    public static final String SAVE_CLIENTE = "INSERT INTO cliente (dni, nombre, direccion, telefono, email, password, administrador) VALUES (?, ?, ?, ?, ?, ?, ?)";
    public static final String UPDATE_CLIENTE = "UPDATE cliente SET nombre = ?, direccion = ?, telefono = ?, email = ?, password = ?, administrador = ? WHERE id = ?";
    public static final String DELETE_CLIENTE = "DELETE FROM cliente WHERE id = ?";

    // ProductoDao
    public static final String FIND_PRODUCTO = "SELECT id, nombre, precio, tipo, size FROM producto WHERE id = ?";
    public static final String FIND_ALL_PRODUCTO = "SELECT id, nombre, precio, tipo, size FROM producto";
    public static final String FIND_INGREDIENTE_BY_PRODUCTO = "SELECT id, nombre FROM ingrediente JOIN producto_ingrediente ON id = ingrediente_id WHERE producto_id = ?";
    public static final String FIND_ALERGENO_BY_INGREDIENTE = "SELECT nombre FROM alergeno JOIN ingrediente_alergeno ON id = alergeno_id WHERE ingrediente_id = ?";
    public static final String SAVE_PRODUCTO = "INSERT INTO producto (nombre, precio, tipo, size) VALUES (?, ?, ?, ?)";
    public static final String UPDATE_PRODUCTO = "UPDATE producto SET nombre = ?, precio = ?, size = ? WHERE id = ?";
    public static final String NUMERO_PEDIDOS_PRODUCTO = "SELECT COUNT(*) FROM pedido_producto WHERE producto_id = ?";
    public static final String DELETE_PRODUCTO = "DELETE FROM producto WHERE id = ?";
    public static final String NUMERO_PRODUCTOS_INGREDIENTE = "SELECT COUNT(*) FROM producto_ingrediente WHERE ingrediente_id = ?";
    public static final String DELETE_INGREDIENTE = "DELETE FROM ingrediente WHERE id = ?";
    public static final String NUMERO_INGREDIENTES_ALERGENO = "SELECT COUNT(*) FROM ingrediente_alergeno JOIN alergeno ON id = alergeno_id WHERE nombre = ?";
    public static final String DELETE_ALERGENO = "DELETE FROM alergeno WHERE nombre = ?";
    public static final String SAVE_INGREDIENTE = "INSERT INTO ingrediente (nombre) VALUES (?)";
    public static final String SAVE_ALERGENO = "INSERT INTO alergeno (nombre) VALUES (?)";
    public static final String SAVE_INGREDIENTE_ALERGENO = "INSERT INTO ingrediente_alergeno (ingrediente_id, alergeno_id) VALUES (?, ?)";
    public static final String FIND_ALERGENO_ID = "SELECT id FROM alergeno WHERE nombre = ?";
    public static final String FIND_INGREDIENTE_ID = "SELECT id FROM ingrediente WHERE nombre = ?";
    public static final String SAVE_PRODUCTO_INGREDIENTE = "INSERT INTO producto_ingrediente (producto_id, ingrediente_id) VALUES (?, ?)";

    // PedidoDao
    public static final String FIND_PEDIDO = "SELECT id, fecha, estado, cliente_id, metodo_pago FROM pedido WHERE id = ?";
    public static final String FIND_PEDIDO_ALL_IDS = "SELECT id FROM pedido";
    public static final String FIND_PEDIDO_PRODUCTO_BY_PEDIDO = "SELECT id, producto_id, cantidad FROM pedido_producto WHERE pedido_id = ?";
    public static final String FIND_LINEA_PEDIDO = "SELECT id, pedido_id, producto_id, cantidad FROM pedido_producto WHERE pedido_id = ? AND producto_id = ?";
    public static final String FIND_PEDIDO_IDS_BY_ESTADO_PEDIDO = "SELECT id FROM pedido WHERE estado = ?";
    public static final String FIND_PEDIDO_IDS_BY_CLIENTE = "SELECT id FROM pedido WHERE cliente_id = ?";
    public static final String SAVE_PEDIDO = "INSERT INTO pedido (fecha, estado, cliente_id) VALUES (?, ?, ?)";
            // NOTA: pedido_producto aka LineaPedido
    public static final String SAVE_LINEA_PEDIDO = "INSERT INTO pedido_producto (pedido_id, producto_id, cantidad) VALUES (?, ?, ?)";
    public static final String UPDATE_LINEA_PEDIDO = "UPDATE pedido_producto SET cantidad = ? WHERE pedido_id = ? AND producto_id = ?";
    public static final String UPDATE_PEDIDO = "UPDATE pedido SET fecha = ?, estado = ?, metodo_pago = ? WHERE id = ?";
    public static final String DELETE_PEDIDO = "DELETE FROM pedido WHERE id = ?";


    public static void createTables() throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement()) {
            stmt.execute(CREATE_TABLE_CLIENTE);
            System.out.println("Se ha creado la tabla clientes");
            stmt.execute(CREATE_TABLE_PRODUCTO);
            System.out.println("Se ha creado la tabla productos");
            stmt.execute(CREATE_TABLE_INGREDIENTE);
            System.out.println("Se ha creado la tabla ingredientes");
            stmt.execute(CREATE_TABLE_ALERGENO);
            System.out.println("Se ha creado la tabla alergenos");
            stmt.execute(CREATE_TABLE_PEDIDO);
            System.out.println("Se ha creado la tabla pedidos");
            stmt.execute(CREATE_TABLE_PRODUCTO_INGREDIENTE);
            System.out.println("Se ha creado la tabla producto_ingrediente");
            stmt.execute(CREATE_TABLE_INGREDIENTE_ALERGENO);
            System.out.println("Se ha creado la tabla ingrediente_alergeno");
            stmt.execute(CREATE_TABLE_PEDIDO_PRODUCTO);
            System.out.println("Se ha creado la tabla pedido_producto");
        }
    }

    public static void dropAndCreateTables() throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement()) {
            stmt.execute(DROP_TABLE_PRODUCTO_INGREDIENTE);
            System.out.println("Se ha borrado la tabla producto_ingrediente");
            stmt.execute(DROP_TABLE_INGREDIENTE_ALERGENO);
            System.out.println("Se ha borrado la tabla ingrediente_alergeno");
            stmt.execute(DROP_TABLE_PEDIDO_PRODUCTO);
            System.out.println("Se ha borrado la tabla pedido_producto");
            stmt.execute(DROP_TABLE_PEDIDO);
            System.out.println("Se ha borrado la tabla pedidos");
            stmt.execute(DROP_TABLE_PRODUCTO);
            System.out.println("Se ha borrado la tabla productos");
            stmt.execute(DROP_TABLE_CLIENTE);
            System.out.println("Se ha borrado la tabla clientes");
            stmt.execute(DROP_TABLE_INGREDIENTE);
            System.out.println("Se ha borrado la tabla ingredientes");
            stmt.execute(DROP_TABLE_ALERGENO);
            System.out.println("Se ha borrado la tabla alergenos");
            createTables();
        }
    }
}