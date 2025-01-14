package es.adr.modelo.dao.impl;

import es.adr.modelo.*;
import es.adr.modelo.dao.Dao;
import es.adr.utilidades.ConfiguracionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ProductoDao implements Dao<Producto> {
    private static ProductoDao instance;
    private final Connection conn;
    // Como alergeno no es un objeto, guardamos una lista con los respectivos ids para añadirlos a la tabla intermedia ingrediente_alergeno
    private final List<Integer> alergenosIds;

    private ProductoDao(Connection conn) {
        this.conn = conn;
        alergenosIds = new ArrayList<>();
    }

    public static ProductoDao getInstance(Connection conn) {
        if (instance == null)
            instance = new ProductoDao(conn);
        return instance;
    }

    @Override
    public Optional<Producto> find(int id) throws SQLException, IllegalArgumentException {
        Optional<Producto> producto = Optional.empty();

        try (PreparedStatement ps = conn.prepareStatement(ConfiguracionBD.FIND_PRODUCTO)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                producto = switch (rs.getString("tipo")) {
                    case "pizza" -> Optional.of(new Pizza(id, rs.getString("nombre"), rs.getDouble("precio"), SIZE.valueOf(rs.getString("size").toUpperCase()), findIngredienteByProducto(id)));
                    case "pasta" -> Optional.of(new Pasta(id, rs.getString("nombre"), rs.getDouble("precio"), findIngredienteByProducto(id)));
                    case "bebida" -> Optional.of(new Bebida(id, rs.getString("nombre"), rs.getDouble("precio"), SIZE.valueOf(rs.getString("size").toUpperCase())));
                    default -> throw new IllegalArgumentException("Tipo de producto no válido");
                };
        }
        return producto;
    }

    @Override
    public List<Producto> findAll() throws SQLException, IllegalArgumentException {
        List<Producto> productos = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(ConfiguracionBD.FIND_ALL_PRODUCTO)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next())
                productos.add(switch (rs.getString("tipo")) {
                    case "pizza" -> new Pizza(rs.getInt("id"), rs.getString("nombre"), rs.getDouble("precio"), SIZE.valueOf(rs.getString("size").toUpperCase()), findIngredienteByProducto(rs.getInt("id")));
                    case "pasta" -> new Pasta(rs.getInt("id"), rs.getString("nombre"), rs.getDouble("precio"), findIngredienteByProducto(rs.getInt("id")));
                    case "bebida" -> new Bebida(rs.getInt("id"), rs.getString("nombre"), rs.getDouble("precio"), SIZE.valueOf(rs.getString("size").toUpperCase()));
                    default -> throw new IllegalArgumentException("Tipo de producto no válido");
                });
        }
        productos.sort(Comparator.comparingInt(Producto::getId));
        return productos;
    }

    public List<Ingrediente> findIngredienteByProducto(int id) throws SQLException {
        List<Ingrediente> ingredientes = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(ConfiguracionBD.FIND_INGREDIENTE_BY_PRODUCTO)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next())
                ingredientes.add(new Ingrediente(rs.getInt("id"), rs.getString("nombre"), findAlergenoByIngrediente(rs.getInt("id"))));
        }
        ingredientes.sort(Comparator.comparingInt(Ingrediente::getId));
        return ingredientes;
    }

    public List<String> findAlergenoByIngrediente(int id) throws SQLException {
        List<String> alergenos = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(ConfiguracionBD.FIND_ALERGENO_BY_INGREDIENTE)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next())
                alergenos.add(rs.getString("nombre"));
        }
        alergenos.sort(String::compareTo);
        return alergenos;
    }

    public Optional<Integer> findAlergenoId(String alergeno) throws SQLException {
        Optional<Integer> id = Optional.empty();

        try (PreparedStatement ps = conn.prepareStatement(ConfiguracionBD.FIND_ALERGENO_ID)) {
            ps.setString(1, alergeno);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                id = Optional.of(rs.getInt(1));
        }
        return id;
    }

    public Optional<Integer> findIngredienteId(String ingrediente) throws SQLException {
        Optional<Integer> id = Optional.empty();

        try (PreparedStatement ps = conn.prepareStatement(ConfiguracionBD.FIND_INGREDIENTE_ID)) {
            ps.setString(1, ingrediente);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                id = Optional.of(rs.getInt(1));
        }
        return id;
    }

    @Override
    public void save(Producto producto) throws SQLException, IllegalArgumentException {
        try (PreparedStatement ps = conn.prepareStatement(ConfiguracionBD.SAVE_PRODUCTO, PreparedStatement.RETURN_GENERATED_KEYS)) {
            conn.setAutoCommit(false);

            ps.setString(1, producto.getNombre());
            ps.setDouble(2, producto.getPrecio());

            String size;
            List<Ingrediente> ingredientes = new ArrayList<>();

            size = switch (producto) {
                case Pizza pizza -> {
                    ingredientes = pizza.getIngredientes();
                    yield pizza.getSize().name();
                }
                case Pasta pasta -> {
                    ingredientes = pasta.getIngredientes();
                    yield null;
                }
                case Bebida bebida -> bebida.getSize().name();
                default -> throw new IllegalArgumentException("Tipo de producto no válido");
            };

            ps.setString(3, producto.getClass().getSimpleName().toLowerCase());
            ps.setString(4, size);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next())
                producto.setId(rs.getInt(1));

            for (Ingrediente ingrediente : ingredientes) {
                saveIngrediente(ingrediente);
                saveProductoIngrediente(producto, ingrediente);
            }

            conn.commit();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            conn.rollback();
            conn.setAutoCommit(true);
            throw e;
        }
    }

    public void saveIngrediente(Ingrediente ingrediente) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(ConfiguracionBD.SAVE_INGREDIENTE, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, ingrediente.getNombre());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next())
                ingrediente.setId(rs.getInt(1));
        } catch (SQLException e) {
            if (e.getErrorCode() != 1062) // Si no es un duplicate key error (es de esperar que los productos compartan los mismos ingredientes)
                throw e;

            System.err.printf("ADVERTENCIA: El ingrediente '%s' ya existe. (SALTAR INSERCIÓN)%n", ingrediente.getNombre());
            findIngredienteId(ingrediente.getNombre()).ifPresent(ingrediente::setId);
            return;
        }

        List<String> alergenos = ingrediente.getAlergenos();

        for (String alergeno : alergenos)
            saveAlergeno(alergeno);

        for (int alergenoId : alergenosIds)
            saveIngredienteAlergeno(ingrediente.getId(), alergenoId);

        alergenosIds.clear();
    }

    public void saveAlergeno(String alergeno) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(ConfiguracionBD.SAVE_ALERGENO, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, alergeno);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next())
                alergenosIds.add(rs.getInt(1));
        } catch (SQLException e) {
            if (e.getErrorCode() != 1062)
                throw e;

            System.err.printf("ADVERTENCIA: El alergeno '%s' ya existe. (SALTAR INSERCIÓN)%n", alergeno);
            findAlergenoId(alergeno).ifPresent(alergenosIds::add);
        }
    }

    public void saveIngredienteAlergeno(int ingredienteId, int alergenoId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(ConfiguracionBD.SAVE_INGREDIENTE_ALERGENO)) {
            ps.setInt(1, ingredienteId);
            ps.setInt(2, alergenoId);
            ps.executeUpdate();
        }
    }

    public void saveProductoIngrediente(Producto producto, Ingrediente ingrediente) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(ConfiguracionBD.SAVE_PRODUCTO_INGREDIENTE)) {
            ps.setInt(1, producto.getId());
            ps.setInt(2, ingrediente.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void update(Producto producto) throws SQLException, IllegalArgumentException {
        try (PreparedStatement ps = conn.prepareStatement(ConfiguracionBD.UPDATE_PRODUCTO)) {
            ps.setString(1, producto.getNombre());
            ps.setDouble(2, producto.getPrecio());

            String size = switch (producto) {
                case Pizza pizza -> pizza.getSize().name();
                case Pasta ignored -> null;
                case Bebida bebida -> bebida.getSize().name();
                default -> throw new IllegalArgumentException("Tipo de producto no válido");
            };

            ps.setString(3, size);
            ps.setInt(4, producto.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Producto producto) throws SQLException, IllegalArgumentException {
        if (!isProductoEliminable(producto))
            throw new IllegalArgumentException("El producto está en varios pedidos y no se puede borrar");
        else if (find(producto.getId()).isEmpty())
            throw new IllegalArgumentException("El producto no existe en la base de datos");

        try {
            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement(ConfiguracionBD.DELETE_PRODUCTO);
            ps.setInt(1, producto.getId());
            ps.executeUpdate();

            List<Ingrediente> ingredientes = switch (producto) {
                case Pizza pizza -> pizza.getIngredientes();
                case Pasta pasta -> pasta.getIngredientes();
                case Bebida ignored -> new ArrayList<>();
                default -> throw new IllegalArgumentException("Tipo de producto no válido");
            };

            for (Ingrediente ingrediente : ingredientes)
                deleteIngrediente(ingrediente);

            conn.commit();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            conn.rollback();
            conn.setAutoCommit(true);
            throw e;
        }
    }

    public void deleteIngrediente(Ingrediente ingrediente) throws SQLException {
        if (!isIngredienteEliminable(ingrediente))
            return;

        try (PreparedStatement ps = conn.prepareStatement(ConfiguracionBD.DELETE_INGREDIENTE)) {
            ps.setInt(1, ingrediente.getId());
            ps.executeUpdate();

            for (String alergeno : ingrediente.getAlergenos())
                deleteAlergeno(alergeno);
        }
    }

    public void deleteAlergeno(String alergeno) throws SQLException {
        if (!isAlergenoEliminable(alergeno))
            return;

        try (PreparedStatement ps = conn.prepareStatement(ConfiguracionBD.DELETE_ALERGENO)) {
            ps.setString(1, alergeno);
            ps.executeUpdate();
        }
    }

    public boolean isProductoEliminable(Producto producto) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(ConfiguracionBD.NUMERO_PEDIDOS_PRODUCTO)) {
            ps.setInt(1, producto.getId());
            ResultSet rs = ps.executeQuery();

            return rs.next() && rs.getInt(1) == 0;
        }
    }

    public boolean isIngredienteEliminable(Ingrediente ingrediente) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(ConfiguracionBD.NUMERO_PRODUCTOS_INGREDIENTE)) {
            ps.setInt(1, ingrediente.getId());
            ResultSet rs = ps.executeQuery();

            return rs.next() && rs.getInt(1) == 0;
        }
    }

    public boolean isAlergenoEliminable(String alergeno) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(ConfiguracionBD.NUMERO_INGREDIENTES_ALERGENO)) {
            ps.setString(1, alergeno);
            ResultSet rs = ps.executeQuery();

            return rs.next() && rs.getInt(1) == 0;
        }
    }
}
