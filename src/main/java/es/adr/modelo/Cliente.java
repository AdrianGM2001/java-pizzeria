package es.adr.modelo;

import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private int id;
    private String dni;
    private String nombre;
    private String direccion;
    private String telefono;
    private String email;
    private String password;
    private List<Pedido> pedidos;
    private boolean admin;

    public Cliente(int id, String dni, String nombre, String direccion, String telefono, String email, String password, boolean admin) {
        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.password = password;
        this.admin = admin;
        pedidos = new ArrayList<>();
    }

    public Cliente(int id, String dni, String nombre, String direccion, String telefono, String email, String password) {
        this(id, dni, nombre, direccion, telefono, email, password, false);
    }

    public Cliente(String dni, String nombre, String direccion, String telefono, String email, String password) {
        this(0, dni, nombre, direccion, telefono, email, password, false);
    }

    public Cliente(String dni, String nombre, String direccion, String telefono, String email, String password, boolean admin) {
        this(0, dni, nombre, direccion, telefono, email, password, admin);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isAdmin() {
        return admin;
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
                + telefono + ", email=" + email + ", contrasenya=" + password + ", pedidos=" + pedidos + ", admin=" + admin + "]";
    }
}
