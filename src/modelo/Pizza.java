package modelo;

import java.util.List;

public class Pizza extends Producto {
    private final SIZE tamanyo;
    private final List<Ingrediente> ingredientes;

    public Pizza(int id, String nombre, double precio, SIZE tamanyo, List<Ingrediente> ingredientes) {
        super(id, nombre, precio);
        this.tamanyo = tamanyo;
        this.ingredientes = ingredientes;
    }

    public SIZE getTamanyo() {
        return tamanyo;
    }

    public List<Ingrediente> getIngredientes() {
        return ingredientes;
    }
}
