package es.adr;

import es.adr.modelo.*;
import es.adr.modelo.dao.Dao;
import es.adr.modelo.dao.impl.ProductoDao;
import es.adr.utilidades.ConfiguracionBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class MainProducto {
    static Ingrediente pollo, jamon, champinones, cebolla, cacahuetes;
    static Pizza pizza1, pizza2, pizza3;
    static Pasta pasta1, pasta2, pasta3;
    static Bebida bebida1, bebida2, bebida3;

    public static void main(String[] args) throws SQLException {
        pollo = new Ingrediente("Pollo", List.of("gluten", "lactosa"));
        jamon = new Ingrediente("Jamon", List.of("gluten", "lactosa"));
        champinones = new Ingrediente("Champiñones", List.of("sulfitos"));
        cebolla = new Ingrediente("Cebolla", List.of());
        cacahuetes = new Ingrediente("Cacahuetes", List.of("cacahuetes"));
        pizza1 = new Pizza("Pizza 4 quesos", 12.0, SIZE.GRANDE, List.of(pollo, jamon, champinones));
        pizza2 = new Pizza("Pizza 4 quesos", 10.0, SIZE.MEDIANA, List.of(pollo, jamon, champinones));
        pizza3 = new Pizza("Pizza 4 quesos", 8.0, SIZE.PEQUENYA, List.of(pollo, jamon, champinones));
        pasta1 = new Pasta("Pasta Carbonara", 8.0, List.of(jamon, cebolla));
        pasta2 = new Pasta("Pasta Boloñesa", 8.0, List.of(pollo, cebolla));
        pasta3 = new Pasta("Pasta Pesto", 8.0, List.of(cacahuetes, cebolla));
        bebida1 = new Bebida("Coca Cola", 2.0, SIZE.GRANDE);
        bebida2 = new Bebida("Fanta", 2.0, SIZE.PEQUENYA);
        bebida3 = new Bebida("Agua", 1.0, SIZE.MEDIANA);
        try (Connection conn = DriverManager.getConnection(ConfiguracionBD.URL, ConfiguracionBD.USER, ConfiguracionBD.PASS)) {
            ConfiguracionBD.dropAndCreateTables();
            Dao<Producto> dao = ProductoDao.getInstance(conn);

            guardarProductos(dao);
            //actualizarProductos(dao);
            //borrarProductos(dao);

            ((ProductoDao) dao).findIngredienteByProducto(pizza1.getId()).forEach(System.out::println);
            ((ProductoDao) dao).findAlergenoByIngrediente(pollo.getId()).forEach(System.out::println);

        }
    }

    public static void guardarProductos(Dao<Producto> dao) throws SQLException {
        dao.save(pizza1);
        dao.save(pizza2);
        dao.save(pizza3);
        dao.save(pasta1);
        dao.save(pasta2);
        dao.save(pasta3);
        dao.save(bebida1);
        dao.save(bebida2);
        dao.save(bebida3);

        System.out.println("-------------------------------------------------");
        System.out.println("-------------------INSERCIONES-------------------");
        System.out.println("-------------------------------------------------");
        List<Producto> productos = dao.findAll();
        productos.forEach(System.out::println);
    }

    public static void borrarProductos(Dao<Producto> dao) throws SQLException {
        System.out.println("-------------------------------------------------");
        System.out.println("---------------------BORRADOS--------------------");
        System.out.println("-------------------------------------------------");
        dao.delete(pizza1);
        dao.delete(pizza2);
        dao.delete(pizza3);
        dao.delete(pasta1);
        dao.delete(pasta2);
        dao.delete(pasta3);
        dao.delete(bebida1);
        dao.delete(bebida2);
        dao.delete(bebida3);
        List<Producto> productos = dao.findAll();
        productos.forEach(System.out::println);
    }

    public static void actualizarProductos(Dao<Producto> dao) throws SQLException {
        System.out.println("-------------------------------------------------");
        System.out.println("--------------------ACTUALIZADOS-----------------");
        System.out.println("-------------------------------------------------");
        pizza1.setNombre("Pizza 4 quesos grande");
        pizza1.setPrecio(pizza1.getPrecio() + 2.0);
        pizza2.setNombre("Pizza 4 quesos mediana");
        pizza2.setPrecio(pizza2.getPrecio() + 2.0);
        pizza3.setNombre("Pizza 4 quesos pequeña");
        pizza3.setPrecio(pizza3.getPrecio() + 2.0);
        pasta1.setNombre("Pasta Carbonara con cebolla");
        pasta1.setPrecio(pasta1.getPrecio() + 2.0);
        pasta2.setNombre("Pasta Boloñesa con cebolla");
        pasta2.setPrecio(pasta2.getPrecio() + 2.0);
        pasta3.setNombre("Pasta Pesto con cebolla");
        pasta3.setPrecio(pasta3.getPrecio() + 2.0);
        bebida1.setNombre("Coca Cola Grande");
        bebida1.setPrecio(bebida1.getPrecio() + 2.0);
        bebida2.setNombre("Fanta Pequeña");
        bebida2.setPrecio(bebida2.getPrecio() + 2.0);
        bebida3.setNombre("Agua Mediana");
        bebida3.setPrecio(bebida3.getPrecio() + 2.0);
        dao.update(pizza1);
        dao.update(pizza2);
        dao.update(pizza3);
        dao.update(pasta1);
        dao.update(pasta2);
        dao.update(pasta3);
        dao.update(bebida1);
        dao.update(bebida2);
        dao.update(bebida3);
        List<Producto> productos = dao.findAll();
        productos.forEach(System.out::println);
    }
}
