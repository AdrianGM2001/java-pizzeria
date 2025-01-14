package es.adr.controlador;

import es.adr.modelo.Producto;
import es.adr.modelo.dao.Dao;
import es.adr.modelo.dao.impl.ProductoDao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ProductoControlador {
    private static ProductoControlador instance;
    private static Dao<Producto> productoDao;

    private ProductoControlador(Connection conn){
        productoDao = ProductoDao.getInstance(conn);
    }

    public static ProductoControlador getInstance(Connection conn) {
        if (instance == null)
            instance = new ProductoControlador(conn);
        return instance;
    }

    public void agregarProducto(Producto producto) throws SQLException, IllegalArgumentException {
        productoDao.save(producto);
    }

    public void mostrarProductos() throws SQLException {
        List<Producto> productos = productoDao.findAll();
        if (productos.isEmpty())
            System.out.println("No hay productos en el catálogo");
        else
            productos.forEach(System.out::println);
    }
}
