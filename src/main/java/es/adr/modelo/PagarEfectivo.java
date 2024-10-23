package es.adr.modelo;

public class PagarEfectivo implements Pagable {
    @Override
    public void pagar(double cantidad) {
        System.out.printf("Se ha pagado %.2feur en efectivo%n", cantidad);
    }
}
