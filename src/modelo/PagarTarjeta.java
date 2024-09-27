package modelo;

public class PagarTarjeta implements Pagable {
    @Override
    public void pagar(double cantidad) {
        System.out.printf("Se ha pagado %.2feur con tarjeta%n", cantidad);
    }
}
