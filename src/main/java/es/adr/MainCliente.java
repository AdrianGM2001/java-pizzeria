package es.adr;

import es.adr.modelo.Cliente;
import es.adr.modelo.dao.Dao;
import es.adr.modelo.dao.impl.ClienteDao;
import es.adr.utilidades.ConfiguracionBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

public class MainCliente {
    static Cliente cliente1, cliente2, cliente3;
    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(ConfiguracionBD.URL, ConfiguracionBD.USER, ConfiguracionBD.PASS)) {
            ConfiguracionBD.dropAndCreateTables();
            Dao<Cliente> dao = new ClienteDao(conn);

            guardarClientes(dao);
            //borrarClientes(dao);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void guardarClientes(Dao<Cliente> dao) throws SQLException {
        cliente1 = new Cliente("12345678Z", "Pepe Navarro", "Calle Falsa 123", "645668745", "pepe.navarro@email.com", "contrasenya", true);
        cliente2 = new Cliente("87654321A", "Juan Perez", "Calle Falsa 124", "645668746", "juan.perez@email.com", "123456password");
        cliente3 = new Cliente("56754765B", "Maria Lopez", "Calle Falsa 125", "645668747", "maria.lopez@email.com", "password");
        dao.save(cliente1);
        dao.save(cliente2);
        dao.save(cliente3);

        System.out.println("-------------------------------------------------");
        System.out.println("-------------------INSERCIONES-------------------");
        System.out.println("-------------------------------------------------");
        dao.findAll().forEach(System.out::println);
    }

    public static void borrarClientes(Dao<Cliente> dao) throws SQLException {
        dao.delete(cliente1);
        dao.delete(cliente2);
        dao.delete(cliente3);

        System.out.println("-------------------------------------------------");
        System.out.println("-------------------BORRADOS----------------------");
        System.out.println("-------------------------------------------------");
        dao.findAll().forEach(System.out::println);
    }
}
