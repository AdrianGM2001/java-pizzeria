package modelo;

import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private final int id;
    private final String dni;
    private String nombre;
    private String direccion;
    private String telefono;
    private String email;
    private String contrasenya;
    private List<Pedido> pedidos;

    public Cliente(int id, String dni, String nombre, String direccion, String telefono, String email, String contrasenya) {
        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.contrasenya = contrasenya;
        pedidos = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasenya() {
        return contrasenya;
    }

    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public void addPedidos(Pedido pedido) {
        pedidos.add(pedido);
    }

    @Override
    public String toString() {
        return "Cliente [id=" + id + ", dni=" + dni + ", nombre=" + nombre + ", direccion=" + direccion + ", telefono="
                + telefono + ", email=" + email + ", contrasenya=" + contrasenya + ", pedidos=" + pedidos + "]";
    }
}
