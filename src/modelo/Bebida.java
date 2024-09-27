package modelo;

public class Bebida extends Producto {
    private final SIZE tamanyo;


    public Bebida(int id, String nombre, double precio, SIZE tamanyo) {
        super(id, nombre, precio);
        this.tamanyo = tamanyo;
    }

    public SIZE getTamanyo() {
        return tamanyo;
    }
}
