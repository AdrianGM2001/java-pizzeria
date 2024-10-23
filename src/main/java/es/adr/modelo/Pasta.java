package es.adr.modelo;

import java.util.List;

public class Pasta extends Producto {
    private final List<Ingrediente> ingredientes;

    public Pasta(int id, String nombre, double precio, List<Ingrediente> ingredientes) {
        super(id, nombre, precio);
        this.ingredientes = ingredientes;
    }

    public List<Ingrediente> getIngredientes() {
        return ingredientes;
    }
}
