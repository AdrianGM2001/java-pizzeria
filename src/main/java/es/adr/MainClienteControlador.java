package es.adr;

import es.adr.controlador.ClienteControlador;
import es.adr.modelo.Bebida;
import es.adr.modelo.Cliente;
import es.adr.modelo.Producto;
import es.adr.modelo.SIZE;
import es.adr.utilidades.ConfiguracionBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MainClienteControlador {
    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(ConfiguracionBD.URL, ConfiguracionBD.USER, ConfiguracionBD.PASS)) {
            ClienteControlador cc = ClienteControlador.getInstance(conn);
            ConfiguracionBD.dropAndCreateTables();
            Cliente cliente = new Cliente("12345678Z", "Pepe Navarro", "Calle Falsa 123", "645668745", "pepe.navarro@email.com", "contrasenya", true);
            cc.registrar(cliente);
            cc.iniciarSesion(cliente.getEmail(), cliente.getPassword());
            Producto bebida = new Bebida("Coca Cola", 1.5, SIZE.MEDIANA);
            cc.agregarProductoAlCatalogo(bebida);
            cc.mostrarProductosDelCatalogo();

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }


    }
}
