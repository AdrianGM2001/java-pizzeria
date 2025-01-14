package es.adr.modelo.dao.impl;

import es.adr.modelo.Cliente;
import es.adr.modelo.dao.Dao;
import es.adr.utilidades.ConfiguracionBD;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ClienteDaoTest {
    private Connection conn;
    private Dao<Cliente> dao;
    private Cliente pepe, juan, maria;

    @BeforeEach
    void setUp() throws SQLException {
        ConfiguracionBD.dropAndCreateTables();
        conn = DriverManager.getConnection(ConfiguracionBD.URL, ConfiguracionBD.USER, ConfiguracionBD.PASS);
        dao = new ClienteDao(conn);

        pepe = new Cliente("12345678A", "Pepe", "Calle Falsa 123", "676893703", "pepe@mail.com", "password");
        juan = new Cliente("87654321B", "Juan", "Calle Falsa 321", "684329483", "juan@mail.com", "pass1234", true);
        maria = new Cliente("11111111C", "Maria", "Calle Falsa 111", "784902564", "maria@mail.com", "1234pass");
    }

    void saveClientes() {
        try {
            dao.save(pepe);
            dao.save(juan);
            dao.save(maria);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void find() throws SQLException {
        saveClientes();
        Optional<Cliente> cliente = dao.find(2);
        assertTrue(cliente.isPresent());
    }

    @Test
    void findAll() throws SQLException {
        saveClientes();
        assertEquals(3, dao.findAll().size());
    }

    @Test
    void save() {
        saveClientes();
    }

    @Test
    void update() throws SQLException {
        saveClientes();
        pepe.setNombre("Pepe2");
        pepe.setDireccion("Calle Falsa 321");
        pepe.setTelefono("638974563");
        pepe.setEmail("pepe2@mail.com");
        pepe.setPassword("password2");
        pepe.setAdmin(true);
        dao.update(pepe);
    }

    @Test
    void delete() throws SQLException {
        saveClientes();
        dao.delete(pepe);
    }
}