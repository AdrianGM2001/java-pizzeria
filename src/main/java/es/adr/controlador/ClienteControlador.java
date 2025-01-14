package es.adr.controlador;

import es.adr.modelo.*;
import es.adr.modelo.dao.Dao;
import es.adr.modelo.dao.impl.ClienteDao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class ClienteControlador {
    private static ClienteControlador instance;
    private final PedidoControlador pedidoControlador;
    private final ProductoControlador productoControlador;
    private final Dao<Cliente> daoCliente;

    private Optional<Cliente> clienteActual;

    private ClienteControlador(Connection conn) {
        pedidoControlador = PedidoControlador.getInstance(conn);
        productoControlador = ProductoControlador.getInstance(conn);
        clienteActual = Optional.empty();
        daoCliente = new ClienteDao(conn);
    }

    public static ClienteControlador getInstance(Connection conn) {
        if (instance == null)
            instance = new ClienteControlador(conn);

        return new ClienteControlador(conn);
    }
    
    public void registrar(Cliente cliente) throws SQLException, IllegalArgumentException {
        clienteActual = Optional.empty();
        daoCliente.save(cliente);
        clienteActual = Optional.of(cliente);
        pedidoControlador.agregarPedido(new Pedido(cliente));
        System.out.println("REGISTRO: Registro correcto");
    }

    public void iniciarSesion(String email, String password) throws SQLException, IllegalArgumentException {
        clienteActual = Optional.empty();
        Optional<Cliente> clienteEnBD = ((ClienteDao) daoCliente).findByEmail(email);

        if (clienteEnBD.isEmpty())
            throw new IllegalArgumentException("ERROR: El cliente no está registrado");
        else if (!clienteEnBD.get().getPassword().equals(password))
            throw new IllegalArgumentException("ERROR: Contraseña incorrecta");

        clienteActual = clienteEnBD;
        System.out.println("LOGIN: Inicio de sesión correcto");
    }

    public void mostrarProductosDelCatalogo() throws SQLException {
        // No hace falta estar logeado para ver los productos
        productoControlador.mostrarProductos();
    }

    public void agregarProductoAlCatalogo(Producto producto) throws SQLException, IllegalArgumentException {
        if (clienteActual.isEmpty())
            throw new IllegalArgumentException("ERROR: No hay ningún cliente logeado");
        else if (!clienteActual.get().isAdmin())
            throw new IllegalArgumentException("ERROR: Solo los administradores pueden añadir productos");

        productoControlador.agregarProducto(producto);
        System.out.println("PRODUCTO: Producto añadido");
    }

    public void agregarAlCarrito(Producto producto, int cantidad) throws SQLException, IllegalArgumentException {
        if (clienteActual.isEmpty())
            throw new IllegalArgumentException("ERROR: No hay ningún cliente logeado");
        if (cantidad <= 0)
            throw new IllegalArgumentException("ERROR: La cantidad debe ser mayor que 0");

        LineaPedido linea = new LineaPedido(cantidad, producto);

        pedidoControlador.agregarLineaPedido(linea);
        System.out.println("CARRITO: Añadido al carrito");
    }

    public void finalizarPedido(Pagable pago) throws SQLException, IllegalArgumentException {
        if (clienteActual.isEmpty())
            throw new IllegalArgumentException("ERROR: No hay ningún cliente logeado");

        pedidoControlador.finalizarPedido(pago, clienteActual.get());
        System.out.println("PEDIDO: Pedido finalizado");
    }

    public void cancelarPedido() throws SQLException, IllegalArgumentException {
        if (clienteActual.isEmpty())
            throw new IllegalArgumentException("ERROR: No hay ningún cliente logeado");

        pedidoControlador.cancelarPedido(clienteActual.get());
        System.out.println("PEDIDO: Pedido cancelado");
    }

    public void recibirPedido(Pedido pedido) throws SQLException, IllegalArgumentException {
        if (clienteActual.isEmpty())
            throw new IllegalArgumentException("ERROR: No hay ningún cliente logeado");

        pedidoControlador.entregarPedido(pedido.getId());
        System.out.println("PEDIDO: Pedido recibido");
    }

    public Optional<Pedido> getPedidoActual() throws SQLException {
        return pedidoControlador.getPedidoActual();
    }
}
