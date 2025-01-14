package es.adr.controlador;

import es.adr.modelo.*;
import es.adr.modelo.dao.Dao;
import es.adr.modelo.dao.impl.ClienteDao;
import es.adr.modelo.dao.impl.PedidoDao;
import es.adr.modelo.dao.impl.ProductoDao;
import es.adr.utilidades.ConfiguracionBD;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ClienteControladorTest {
    private Connection conn;
    private Cliente cliente;
    private ClienteControlador clienteControlador;
    private Dao<Cliente> daoCliente;
    private Dao<Pedido> daoPedido;
    private Dao<Producto> daoProducto;
    private Ingrediente pollo, jamon, champinones, cebolla, queso, tomate, harina;
    private Pizza pizza1, pizza2;
    private Pasta pasta1;
    private Bebida bebida1;

    @BeforeEach
    void setUp() throws SQLException {
        ConfiguracionBD.dropAndCreateTables();
        conn = DriverManager.getConnection(ConfiguracionBD.URL, ConfiguracionBD.USER, ConfiguracionBD.PASS);
        clienteControlador = ClienteControlador.getInstance(conn);
        daoCliente = new ClienteDao(conn);
        cliente = new Cliente("12345678Z", "Pepe Navarro", "Calle Falsa 123", "645668745", "pepe.navarro@email.com", "pass1234", true);

        daoProducto = ProductoDao.getInstance(DriverManager.getConnection(ConfiguracionBD.URL, ConfiguracionBD.USER, ConfiguracionBD.PASS));
        daoPedido = new PedidoDao(DriverManager.getConnection(ConfiguracionBD.URL, ConfiguracionBD.USER, ConfiguracionBD.PASS));

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

        pasta1 = new Pasta("Pasta Carbonara", 8.0, List.of(jamon, cebolla, champinones));

        bebida1 = new Bebida("Coca Cola", 2.0, SIZE.GRANDE);
    }

    @Test
    void registrar() throws SQLException {
        clienteControlador.registrar(cliente);
        Optional<Cliente> clienteEnBD = ((ClienteDao) daoCliente).findByEmail(cliente.getEmail());
        assertTrue(clienteEnBD.isPresent());
    }

    @Test
    void registrarMismoUsuario() throws SQLException {
        clienteControlador.registrar(cliente);
        Optional<Cliente> clienteEnBD = ((ClienteDao) daoCliente).findByEmail(cliente.getEmail());
        assertTrue(clienteEnBD.isPresent());
        assertThrows(IllegalArgumentException.class, () -> clienteControlador.registrar(cliente));
        assertEquals(1, daoCliente.findAll().size());
    }

    @Test
    void iniciarSesion() throws SQLException {
        clienteControlador.registrar(cliente);
        clienteControlador.iniciarSesion(cliente.getEmail(), cliente.getPassword());
    }

    @Test
    void iniciarSesionEmailErroneo() throws SQLException {
        clienteControlador.registrar(cliente);
        assertThrows(IllegalArgumentException.class, () -> clienteControlador.iniciarSesion("noexiste@email.com", cliente.getPassword()));
    }

    @Test
    void iniciarSesionPasswordErronea() throws SQLException {
        clienteControlador.registrar(cliente);
        assertThrows(IllegalArgumentException.class, () -> clienteControlador.iniciarSesion(cliente.getEmail(), "passwordErronea"));
    }

    @Test
    void mostrarProductosDelCatalogo() throws SQLException {
        daoProducto.save(pizza1);
        daoProducto.save(pasta1);
        daoProducto.save(bebida1);
        assertEquals(3, daoProducto.findAll().size());
        clienteControlador.mostrarProductosDelCatalogo();
        assert true;
    }

    @Test
    void agregarProductoAlCatalogo() throws SQLException {
        clienteControlador.registrar(cliente);
        clienteControlador.iniciarSesion(cliente.getEmail(), cliente.getPassword());
        assertEquals(0, daoProducto.findAll().size());
        clienteControlador.agregarProductoAlCatalogo(pizza1);
        assertEquals(1, daoProducto.findAll().size());
    }

    @Test
    void agregarProductoRepetidoAlCatalogo() throws SQLException {
        clienteControlador.registrar(cliente);
        clienteControlador.iniciarSesion(cliente.getEmail(), cliente.getPassword());
        assertEquals(0, daoProducto.findAll().size());
        clienteControlador.agregarProductoAlCatalogo(pizza1);
        assertEquals(1, daoProducto.findAll().size());
        assertThrows(SQLException.class, () -> clienteControlador.agregarProductoAlCatalogo(pizza1));
        assertEquals(1, daoProducto.findAll().size());
    }

    @Test
    void agregarProductoAlCatalogoSinUsuarioAdmin() throws SQLException {
        cliente.setAdmin(false);
        clienteControlador.registrar(cliente);
        clienteControlador.iniciarSesion(cliente.getEmail(), cliente.getPassword());
        assertEquals(0, daoProducto.findAll().size());
        assertThrows(IllegalArgumentException.class, () -> clienteControlador.agregarProductoAlCatalogo(pizza1));
        assertEquals(0, daoProducto.findAll().size());
    }

    @Test
    void agregarProductoAlCatalogoSinUsuario() throws SQLException {
        assertEquals(0, daoProducto.findAll().size());
        assertThrows(IllegalArgumentException.class, () -> clienteControlador.agregarProductoAlCatalogo(pizza1));
        assertEquals(0, daoProducto.findAll().size());
    }

    @Test
    void agregarAlCarrito() throws SQLException {
        daoProducto.save(pizza1);
        clienteControlador.registrar(cliente);
        clienteControlador.iniciarSesion(cliente.getEmail(), cliente.getPassword());
        clienteControlador.agregarAlCarrito(pizza1, 2);
        assertEquals(1, ((PedidoDao) daoPedido).findLineasPedidoByPedidoId(1).size());
    }

    @Test
    void agregarAlCarritoLineaRepetida() throws SQLException {
        daoProducto.save(pizza1);
        daoProducto.save(pizza2);
        clienteControlador.registrar(cliente);
        clienteControlador.iniciarSesion(cliente.getEmail(), cliente.getPassword());
        clienteControlador.agregarAlCarrito(pizza1, 2);
        clienteControlador.agregarAlCarrito(pizza2, 3);
        clienteControlador.agregarAlCarrito(pizza2, 3);
        clienteControlador.agregarAlCarrito(pizza2, 3);
        clienteControlador.agregarAlCarrito(pizza1, 2);
        assertEquals(2, ((PedidoDao) daoPedido).findLineasPedidoByPedidoId(1).size());
    }

    @Test
    void finalizarPedido() throws SQLException {
        daoProducto.save(pizza1);
        clienteControlador.registrar(cliente);
        clienteControlador.iniciarSesion(cliente.getEmail(), cliente.getPassword());
        clienteControlador.agregarAlCarrito(pizza1, 2);
        Pagable pago = new PagarTarjeta();
        clienteControlador.finalizarPedido(pago);
        Optional<Pedido> pedido = daoPedido.find(1);

        if (pedido.isPresent())
            assertEquals(ESTADO_PEDIDO.FINALIZADO, pedido.get().getEstado());
        else
            fail();
    }

    @Test
    void cancelarPedido() throws SQLException {
        daoProducto.save(pizza1);
        clienteControlador.registrar(cliente);
        clienteControlador.iniciarSesion(cliente.getEmail(), cliente.getPassword());
        clienteControlador.agregarAlCarrito(pizza1, 2);
        clienteControlador.cancelarPedido();
        Optional<Pedido> pedido = daoPedido.find(1);

        if (pedido.isPresent())
            assertEquals(ESTADO_PEDIDO.CANCELADO, pedido.get().getEstado());
        else
            fail();
    }

    @Test
    void recibirPedido() throws SQLException {
        daoProducto.save(pizza1);
        clienteControlador.registrar(cliente);
        clienteControlador.iniciarSesion(cliente.getEmail(), cliente.getPassword());
        clienteControlador.agregarAlCarrito(pizza1, 2);
        Pagable pago = new PagarEfectivo();
        clienteControlador.finalizarPedido(pago);
        Optional<Pedido> pedido = clienteControlador.getPedidoActual();

        if (pedido.isPresent())
            clienteControlador.recibirPedido(pedido.get());
        else
            fail();
    }
}