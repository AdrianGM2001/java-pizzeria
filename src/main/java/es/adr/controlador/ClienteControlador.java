package es.adr.controlador;

import com.opencsv.exceptions.CsvFieldAssignmentException;
import es.adr.modelo.*;
import es.adr.utilidades.GestionFicheros;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClienteControlador {
    private static ClienteControlador clienteControlador;
    private final PedidoControlador pedidoControlador;

    private List<Cliente> clientes;
    private Cliente clienteActual;

    private ClienteControlador() {
        pedidoControlador = PedidoControlador.getInstance();
        clientes = new ArrayList<>();
        clienteActual = null;
    }

    public static ClienteControlador getInstance() {
        if (clienteControlador != null)
            return clienteControlador;

        return new ClienteControlador();
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    public Cliente getClienteActual() {
        return clienteActual;
    }

    public void setClienteActual(Cliente clienteActual) {
        this.clienteActual = clienteActual;
    }

    public PedidoControlador getPedidoControlador() {
        return pedidoControlador;
    }

    
    public boolean registrar(int id, String dni, String nombre, String direccion, String telefono, String email, String contrasenya) {
        Cliente cliente = null;

        if (clientes.stream().anyMatch(c -> c.getEmail().equals(email))) {
            System.out.println("REGISTRO: El cliente ya está registrado");
        } else {
            cliente = new Cliente(id, dni, nombre, direccion, telefono, email, contrasenya);
            clientes.add(cliente);
            System.out.println("REGISTRO: Registro correcto");
        }

        return cliente != null;
    }

    public boolean logear(String email, String contrasenya) {
        clienteActual = clientes.stream()
                .filter(c -> c.getEmail().equals(email) && c.getPassword().equals(contrasenya))
                .findAny()
                .orElse(null);

        if (clienteActual != null) {
            System.out.println("LOGIN: Login correcto");
            pedidoControlador.setPedidoActual(new Pedido(clienteActual.getPedidos().size(), clienteActual));
        } else
            System.out.println("LOGIN: Error");

        return clienteActual != null;
    }

    public boolean anyadirLinea(LineaPedido lp) {
        return pedidoControlador.anyadirLinea(lp);
    }

    public boolean finalizarPedido(Pagable pago) {
        return pedidoControlador.finalizarPedido(pago);
    }

    public boolean cancelarPedido() {
        clienteActual.addPedidos(pedidoControlador.cancelarPedido());
        pedidoControlador.setPedidoActual(new Pedido(clienteActual.getPedidos().size(), clienteActual));
        return true;
    }

    public boolean recibirPedido() {
        clienteActual.addPedidos(pedidoControlador.entregarPedido());
        return true;
    }

    // IMPORTACIONES Y EXPORTACIONES

    public List<Cliente> importarAdministradores() throws Exception {
        return GestionFicheros.importarSinLibreria();
    }

    public boolean exportarAdministradores(List<Cliente> administradores) throws Exception {
        return GestionFicheros.exportarSinLibreria(administradores.stream().filter(Cliente::isAdmin).toList(), ";");
    }

    public List<Cliente> importarClientes() throws Exception {
        return GestionFicheros.importarXML();
    }
    
    public boolean exportarClientes(List<Cliente> clientes) throws Exception {
        return GestionFicheros.exportarXML(clientes.stream().toList());
    }

    public List<Ingrediente> importarIngredientes() throws Exception {
        return GestionFicheros.importarCSV();
    }

    public boolean exportarIngredientes(List<Ingrediente> ingredientes) throws Exception {
        return GestionFicheros.exportarCSV(ingredientes);
    }
}
