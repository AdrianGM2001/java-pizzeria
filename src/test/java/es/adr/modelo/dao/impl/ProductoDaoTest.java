package es.adr.modelo.dao.impl;

import es.adr.modelo.*;
import es.adr.modelo.dao.Dao;
import es.adr.utilidades.ConfiguracionBD;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProductoDaoTest {
    private static Ingrediente pollo, jamon, champinones, cebolla, queso, tomate, harina;
    private static Pizza pizza1, pizza2, pizza3;
    private static Pasta pasta1, pasta2, pasta3;
    private static Bebida bebida1, bebida2, bebida3;
    private static Dao<Producto> dao;

    @BeforeEach
    void setUp() throws SQLException {
        // Limpiar la base de datos
        ConfiguracionBD.dropAndCreateTables();

        // Crear el dao
        dao = ProductoDao.getInstance(DriverManager.getConnection(ConfiguracionBD.URL, ConfiguracionBD.USER, ConfiguracionBD.PASS));

        // Crear productos
        pollo = new Ingrediente("Pollo", List.of("gluten", "lactosa"));
        jamon = new Ingrediente("Jamon", List.of("gluten", "lactosa"));
        champinones = new Ingrediente("Champiñones", List.of("sulfitos"));
        cebolla = new Ingrediente("Cebolla", List.of());
        queso = new Ingrediente("Queso", List.of("lactosa"));
        tomate = new Ingrediente("Tomate", List.of());
        harina = new Ingrediente("Harina", List.of("gluten"));

        pizza1 = new Pizza("Pizza 4 quesos", 12.0, SIZE.GRANDE, List.of(queso, tomate, harina));
        pizza2 = new Pizza("Pizza 4 quesos", 10.0, SIZE.MEDIANA, List.of(queso, tomate, harina));
        pizza3 = new Pizza("Pizza 4 quesos", 8.0, SIZE.PEQUENYA, List.of(queso, tomate, harina));

        pasta1 = new Pasta("Pasta Carbonara", 8.0, List.of(jamon, cebolla, champinones));
        pasta2 = new Pasta("Pasta Boloñesa", 8.0, List.of(pollo, cebolla, tomate));
        pasta3 = new Pasta("Pasta Pesto", 8.0, List.of(queso, cebolla, tomate));

        bebida1 = new Bebida("Coca Cola", 2.0, SIZE.GRANDE);
        bebida2 = new Bebida("Fanta", 2.0, SIZE.PEQUENYA);
        bebida3 = new Bebida("Agua", 1.0, SIZE.MEDIANA);

    }

    @Test
    void find() throws SQLException {
        dao.save(pizza1);
        Optional<Producto> producto = dao.find(pizza1.getId());
        assertTrue(producto.isPresent());
    }

    @Test
    void findAll() throws SQLException {
        dao.save(pizza1);
        dao.save(pasta1);
        dao.save(bebida1);
        List<Producto> productos = dao.findAll();
        assertEquals(3, productos.size());
    }

    @Test
    void findIngredienteByProducto() throws SQLException {
        dao.save(pizza1);
        Optional<Producto> producto = dao.find(pizza1.getId());
        assertTrue(producto.isPresent());
        List<Ingrediente> ingredientes = ((Pizza) producto.get()).getIngredientes();
        assertEquals(ingredientes.toString(), pizza1.getIngredientes().toString());
    }

    @Test
    void findAlergenoByIngrediente() throws SQLException {
        dao.save(pasta2);
        List<String> alergenos = ((ProductoDao) dao).findAlergenoByIngrediente(pollo.getId());
        assertTrue(alergenos.containsAll(List.of("gluten", "lactosa")));
    }

    @Test
    void save() throws SQLException {
        dao.save(pizza1);
        Optional<Producto> producto = dao.find(pizza1.getId());
        assertTrue(producto.isPresent() && producto.get().equals(pizza1));
    }

    @Test
    void saveProductoRepetido() throws SQLException {
        dao.save(pizza1);
        assertThrows(SQLException.class, () -> dao.save(pizza1));
        assertEquals(1, dao.findAll().size());
    }

    @Test
    void update() throws SQLException {
        dao.save(pizza1);
        pizza1.setNombre("Pizza 4 quesos grande");
        pizza1.setPrecio(15.0);
        dao.update(pizza1);
        Optional<Producto> producto = dao.find(pizza1.getId());
        assertTrue(producto.isPresent() && producto.get().getNombre().equals("Pizza 4 quesos grande") && producto.get().getPrecio() == 15.0);
    }

    @Test
    void deleteProductoInexistente() {
        assertThrows(IllegalArgumentException.class, () -> dao.delete(pizza1));
    }

    @Test
    void delete() throws SQLException {
        dao.save(pizza1);
        dao.delete(pizza1);
        assertEquals(0, dao.findAll().size());
    }
}