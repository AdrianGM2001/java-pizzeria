package es.adr;

import es.adr.controlador.ClienteControlador;

public class LeerFicheros {
    public static void main(String[] args) {
        ClienteControlador cc = ClienteControlador.getInstance();

        try {
            cc.importarAdministradores().forEach(System.out::println);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        try {
            cc.importarClientes().forEach(System.out::println);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        try {
            cc.importarIngredientes().forEach(System.out::println);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
