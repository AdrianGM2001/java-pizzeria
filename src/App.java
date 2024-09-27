import java.util.List;

import controlador.ClienteControlador;
import modelo.Bebida;
import modelo.Ingrediente;
import modelo.LineaPedido;
import modelo.Pagable;
import modelo.PagarEfectivo;
import modelo.PagarTarjeta;
import modelo.Pasta;
import modelo.Pizza;
import modelo.SIZE;

public class App {
    public static void main(String[] args) throws Exception {
        ClienteControlador cc = ClienteControlador.getInstance();

        // INGREDIENTES
        Ingrediente ingrediente1 = new Ingrediente(1, "Harina de trigo", List.of("Gluten"));
        Ingrediente ingrediente2 = new Ingrediente(2, "Agua", List.of());
        Ingrediente ingrediente3 = new Ingrediente(3, "Levadura", List.of("Gluten"));
        Ingrediente ingrediente4 = new Ingrediente(4, "Sal", List.of());
        Ingrediente ingrediente5 = new Ingrediente(5, "Aceite de oliva", List.of());
        Ingrediente ingrediente6 = new Ingrediente(6, "Tomate", List.of());
        Ingrediente ingrediente7 = new Ingrediente(7, "Queso mozzarella", List.of("Lactosa"));
        Ingrediente ingrediente8 = new Ingrediente(8, "Albahaca", List.of());
        Ingrediente ingrediente9 = new Ingrediente(9, "Carne", List.of("Lactosa"));
        Ingrediente ingrediente10 = new Ingrediente(10, "Pasta", List.of("Gluten"));

        // LISTAS DE INGREDIENTES
        // PIZZAS
        List<Ingrediente> ingredientesPizza1 = List.of(ingrediente1, ingrediente2, ingrediente3, ingrediente4,
                ingrediente5, ingrediente6, ingrediente6);
        List<Ingrediente> ingredientesPizza2 = List.of(ingrediente1, ingrediente2, ingrediente3, ingrediente4,
                ingrediente5, ingrediente6, ingrediente6, ingrediente8);
        List<Ingrediente> ingredientesPizza3 = List.of(ingrediente1, ingrediente2, ingrediente3, ingrediente4,
                ingrediente5, ingrediente6, ingrediente6);
        List<Ingrediente> ingredientesPizza4 = List.of(ingrediente1, ingrediente2, ingrediente3, ingrediente4,
                ingrediente5, ingrediente6, ingrediente6);
        List<Ingrediente> ingredientesPizza5 = List.of(ingrediente1, ingrediente2, ingrediente3, ingrediente4,
                ingrediente5, ingrediente6, ingrediente6);

        // PASTAS
        List<Ingrediente> ingredientesPasta1 = List.of(ingrediente10, ingrediente6, ingrediente7, ingrediente8);
        List<Ingrediente> ingredientesPasta2 = List.of(ingrediente10, ingrediente6, ingrediente7, ingrediente9);
        List<Ingrediente> ingredientesPasta3 = List.of(ingrediente10, ingrediente6, ingrediente7, ingrediente8,
                ingrediente5);
        List<Ingrediente> ingredientesPasta4 = List.of(ingrediente10, ingrediente6, ingrediente7, ingrediente9,
                ingrediente4);
        List<Ingrediente> ingredientesPasta5 = List.of(ingrediente10, ingrediente6, ingrediente7, ingrediente8,
                ingrediente5, ingrediente4);

        // PRODUCTOS
        // PIZZAS
        Pizza pizza1 = new Pizza(1, "Pizza Margherita", 8.99, SIZE.GRANDE, ingredientesPizza1);
        Pizza pizza2 = new Pizza(2, "Pizza Caprese", 9.49, SIZE.GRANDE, ingredientesPizza2);
        Pizza pizza3 = new Pizza(3, "Pizza Pepperoni", 10.99, SIZE.GRANDE, ingredientesPizza3);
        Pizza pizza4 = new Pizza(4, "Pizza de Champiñones", 9.99, SIZE.GRANDE, ingredientesPizza4);
        Pizza pizza5 = new Pizza(5, "Pizza Mixta", 11.49, SIZE.GRANDE, ingredientesPizza5);

        // PASTAS
        Pasta pasta1 = new Pasta(6, "Pasta al Pomodoro", 8.99, ingredientesPasta1);
        Pasta pasta2 = new Pasta(7, "Pasta a la Carne", 10.49, ingredientesPasta2);
        Pasta pasta3 = new Pasta(8, "Pasta Pesto", 9.99, ingredientesPasta3);
        Pasta pasta4 = new Pasta(9, "Pasta a la Boloñesa", 11.49, ingredientesPasta4);
        Pasta pasta5 = new Pasta(10, "Pasta Primavera", 10.99, ingredientesPasta5);

        // BEBIDAS
        Bebida bebida1 = new Bebida(11, "Coca-Cola", 1.50, SIZE.GRANDE);
        Bebida bebida2 = new Bebida(12, "Fanta", 1.30, SIZE.MEDIANA);
        Bebida bebida3 = new Bebida(13, "Agua", 0.80, SIZE.PEQUEÑA);
        Bebida bebida4 = new Bebida(14, "Cerveza", 2.00, SIZE.GRANDE);
        Bebida bebida5 = new Bebida(15, "Red Bull", 1.70, SIZE.MEDIANA);

        // REGISTRAR CLIENTES
        cc.registrar(1, "12345678A", "Juan Pérez", "Calle Falsa 123, Madrid", "612345678", "juan.perez@example.com",
                "password123");
        cc.registrar(1, "12345678A", "Juan Pérez", "Calle Falsa 123, Madrid", "612345678", "juan.perez@example.com",
                "password123");
        cc.registrar(2, "23456789B", "María García", "Avenida Siempre Viva 742, Barcelona", "623456789",
                "maria.garcia@example.com", "password234");
        cc.registrar(3, "34567890C", "Luis Fernández", "Calle de la Paz 456, Valencia", "634567890",
                "luis.fernandez@example.com", "password345");
        cc.registrar(4, "45678901D", "Ana López", "Plaza Mayor 789, Sevilla", "645678901", "ana.lopez@example.com",
                "password456");
        cc.registrar(5, "56789012E", "Carlos Sánchez", "Calle Gran Vía 321, Bilbao", "656789012",
                "carlos.sanchez@example.com", "password567");

        // LOGEAR CLIENTES
        cc.logear("juan.perez@example.com", "password123");
        cc.logear("juan.perez@example.com", "password12");
        cc.logear("maria.garcia@example.com", "password234");

        // LINEAS DE PEDIDO
        LineaPedido lineaPedido1 = new LineaPedido(1, 2, pizza1); // 2 Pizzas Margherita
        LineaPedido lineaPedido2 = new LineaPedido(2, 1, pizza2); // 1 Pizza Caprese
        LineaPedido lineaPedido3 = new LineaPedido(3, 3, pizza3); // 3 Pizzas Pepperoni
        LineaPedido lineaPedido4 = new LineaPedido(4, 1, pizza4); // 1 Pizza de Champiñones
        LineaPedido lineaPedido5 = new LineaPedido(5, 2, pizza5); // 2 Pizzas Mixtas
        LineaPedido lineaPedido6 = new LineaPedido(6, 1, bebida1); // 1 Coca-Cola
        LineaPedido lineaPedido7 = new LineaPedido(7, 2, bebida2); // 2 Fantas
        LineaPedido lineaPedido8 = new LineaPedido(8, 1, bebida3); // 1 Agua Mineral
        LineaPedido lineaPedido9 = new LineaPedido(9, 1, bebida4); // 1 Cerveza
        LineaPedido lineaPedido10 = new LineaPedido(10, 3, bebida5); // 3 Red Bull
        LineaPedido lineaPedido11 = new LineaPedido(11, 1, pasta1); // 1 Pasta al Pomodoro
        LineaPedido lineaPedido12 = new LineaPedido(12, 2, pasta2); // 2 Pasta a la Carne
        LineaPedido lineaPedido13 = new LineaPedido(13, 1, pasta3); // 1 Pasta Pesto
        LineaPedido lineaPedido14 = new LineaPedido(14, 1, pasta4); // 1 Pasta a la Boloñesa
        LineaPedido lineaPedido15 = new LineaPedido(15, 3, pasta5); // 3 Pasta Primavera

        // PAGOS
        Pagable efectivo = new PagarEfectivo();
        Pagable tarjeta = new PagarTarjeta();

        // AÑADIR LINEAS AL PEDIDO
        cc.anyadirLinea(lineaPedido3);
        cc.anyadirLinea(lineaPedido7);
        cc.anyadirLinea(lineaPedido1);
        cc.anyadirLinea(lineaPedido10);
        cc.anyadirLinea(lineaPedido5);
        cc.anyadirLinea(lineaPedido2);
        cc.anyadirLinea(lineaPedido8);
        cc.anyadirLinea(lineaPedido11);
        cc.anyadirLinea(lineaPedido4);
        cc.anyadirLinea(lineaPedido9);
        cc.anyadirLinea(lineaPedido6);
        cc.anyadirLinea(lineaPedido2);
        cc.anyadirLinea(lineaPedido1);
        cc.anyadirLinea(lineaPedido10);
        cc.anyadirLinea(lineaPedido3);
        cc.anyadirLinea(lineaPedido15);
        cc.anyadirLinea(lineaPedido12);
        cc.anyadirLinea(lineaPedido8);
        cc.anyadirLinea(lineaPedido5);
        cc.anyadirLinea(lineaPedido9);
        cc.anyadirLinea(lineaPedido4);
        cc.anyadirLinea(lineaPedido6);
        cc.anyadirLinea(lineaPedido13);
        cc.anyadirLinea(lineaPedido7);
        cc.anyadirLinea(lineaPedido10);
        cc.anyadirLinea(lineaPedido3);
        cc.anyadirLinea(lineaPedido2);
        cc.anyadirLinea(lineaPedido8);
        cc.anyadirLinea(lineaPedido1);
        cc.anyadirLinea(lineaPedido9);
        cc.anyadirLinea(lineaPedido5);
        cc.anyadirLinea(lineaPedido14);
        cc.anyadirLinea(lineaPedido4);
        cc.anyadirLinea(lineaPedido6);
        cc.anyadirLinea(lineaPedido7);
        cc.anyadirLinea(lineaPedido10);

        // MOSTRAR TICKET (se acumulan los productos)
        System.out.println(cc.getPedidoControlador().getPedidoActual());

        // FINALIZAR PEDIDO (pago efectivo)
        cc.finalizarPedido(efectivo);

        // MOSTRAR TICKET (cambia a finalizado)
        System.out.println(cc.getPedidoControlador().getPedidoActual());

        // CANCELAR PEDIDO
        cc.cancelarPedido();

        // MOSTRAR TICKET (se reinicia el ticket)
        System.out.println(cc.getPedidoControlador().getPedidoActual());

        // AÑADIR LINEAS AL PEDIDO
        cc.anyadirLinea(lineaPedido3);
        cc.anyadirLinea(lineaPedido7);
        cc.anyadirLinea(lineaPedido1);
        cc.anyadirLinea(lineaPedido10);
        cc.anyadirLinea(lineaPedido5);
        cc.anyadirLinea(lineaPedido2);
        cc.anyadirLinea(lineaPedido8);
        cc.anyadirLinea(lineaPedido4);
        cc.anyadirLinea(lineaPedido9);
        cc.anyadirLinea(lineaPedido6);
        cc.anyadirLinea(lineaPedido2);
        cc.anyadirLinea(lineaPedido1);
        cc.anyadirLinea(lineaPedido10);
        cc.anyadirLinea(lineaPedido3);
        cc.anyadirLinea(lineaPedido8);
        cc.anyadirLinea(lineaPedido5);
        cc.anyadirLinea(lineaPedido9);
        cc.anyadirLinea(lineaPedido4);
        cc.anyadirLinea(lineaPedido6);
        cc.anyadirLinea(lineaPedido7);
        cc.anyadirLinea(lineaPedido10);
        cc.anyadirLinea(lineaPedido3);
        cc.anyadirLinea(lineaPedido2);
        cc.anyadirLinea(lineaPedido8);
        cc.anyadirLinea(lineaPedido1);
        cc.anyadirLinea(lineaPedido9);
        cc.anyadirLinea(lineaPedido5);
        cc.anyadirLinea(lineaPedido4);
        cc.anyadirLinea(lineaPedido6);
        cc.anyadirLinea(lineaPedido7);
        cc.anyadirLinea(lineaPedido10);

        // CANCELAR PEDIDO
        cc.cancelarPedido();

        // MOSTRAR TICKET (se reinicia el ticket)
        System.out.println(cc.getPedidoControlador().getPedidoActual());

        // AÑADIR LINEAS AL PEDIDO
        cc.anyadirLinea(lineaPedido3);
        cc.anyadirLinea(lineaPedido7);
        cc.anyadirLinea(lineaPedido1);
        cc.anyadirLinea(lineaPedido10);
        cc.anyadirLinea(lineaPedido5);
        cc.anyadirLinea(lineaPedido2);
        cc.anyadirLinea(lineaPedido8);
        cc.anyadirLinea(lineaPedido4);
        cc.anyadirLinea(lineaPedido9);
        cc.anyadirLinea(lineaPedido6);
        cc.anyadirLinea(lineaPedido2);
        cc.anyadirLinea(lineaPedido1);
        cc.anyadirLinea(lineaPedido10);
        cc.anyadirLinea(lineaPedido3);
        cc.anyadirLinea(lineaPedido8);
        cc.anyadirLinea(lineaPedido5);
        cc.anyadirLinea(lineaPedido9);
        cc.anyadirLinea(lineaPedido4);
        cc.anyadirLinea(lineaPedido6);
        cc.anyadirLinea(lineaPedido7);
        cc.anyadirLinea(lineaPedido10);
        cc.anyadirLinea(lineaPedido3);
        cc.anyadirLinea(lineaPedido2);
        cc.anyadirLinea(lineaPedido8);
        cc.anyadirLinea(lineaPedido1);
        cc.anyadirLinea(lineaPedido9);
        cc.anyadirLinea(lineaPedido5);
        cc.anyadirLinea(lineaPedido4);
        cc.anyadirLinea(lineaPedido6);
        cc.anyadirLinea(lineaPedido7);
        cc.anyadirLinea(lineaPedido10);

        // FINALIZAR PEDIDO (pago tarjeta)
        cc.finalizarPedido(tarjeta);

        // RECIBIR PEDIDO
        cc.recibirPedido();

        // MOSTRAR TICKET (cambia a entregado)
        System.out.println(cc.getPedidoControlador().getPedidoActual());

        // MOSTRAR CLIENTE (pedidos añadidos a su lista de pedidos)
        System.out.println(cc.getClienteActual());

        // LOGEAR CLIENTE (otro)
        cc.logear("juan.perez@example.com", "password123");

        // MOSTRAR CLIENTE (es otro cliente, sin pedidos)
        System.out.println(cc.getClienteActual());

        // AÑADIR LINEAS AL PEDIDO
        cc.anyadirLinea(lineaPedido3);
        cc.anyadirLinea(lineaPedido7);
        cc.anyadirLinea(lineaPedido1);
        cc.anyadirLinea(lineaPedido10);
        cc.anyadirLinea(lineaPedido15);
        cc.anyadirLinea(lineaPedido13);
        cc.anyadirLinea(lineaPedido5);
        cc.anyadirLinea(lineaPedido2);
        cc.anyadirLinea(lineaPedido8);
        cc.anyadirLinea(lineaPedido4);
        cc.anyadirLinea(lineaPedido9);
        cc.anyadirLinea(lineaPedido6);
        cc.anyadirLinea(lineaPedido2);
        cc.anyadirLinea(lineaPedido11);
        cc.anyadirLinea(lineaPedido11);
        cc.anyadirLinea(lineaPedido14);
        cc.anyadirLinea(lineaPedido12);
        cc.anyadirLinea(lineaPedido1);
        cc.anyadirLinea(lineaPedido10);
        cc.anyadirLinea(lineaPedido3);
        cc.anyadirLinea(lineaPedido8);
        cc.anyadirLinea(lineaPedido5);
        cc.anyadirLinea(lineaPedido9);
        cc.anyadirLinea(lineaPedido4);
        cc.anyadirLinea(lineaPedido6);
        cc.anyadirLinea(lineaPedido7);
        cc.anyadirLinea(lineaPedido10);
        cc.anyadirLinea(lineaPedido3);
        cc.anyadirLinea(lineaPedido2);
        cc.anyadirLinea(lineaPedido8);
        cc.anyadirLinea(lineaPedido1);
        cc.anyadirLinea(lineaPedido9);
        cc.anyadirLinea(lineaPedido5);
        cc.anyadirLinea(lineaPedido4);
        cc.anyadirLinea(lineaPedido6);
        cc.anyadirLinea(lineaPedido7);
        cc.anyadirLinea(lineaPedido10);

        // FINALIZAR PEDIDO (pago tarjeta)
        cc.finalizarPedido(tarjeta);

        // RECIBIR PEDIDO
        cc.recibirPedido();

        // MOSTRAR TICKET (se añade el pedido a su lista)
        System.out.println(cc.getPedidoControlador().getPedidoActual());

        // EN PRINCIPIO PARECE QUE FUNCIONA... (NO PIENSO SUMAR EL TICKET A MANO)
        // NO SE HA IMPLEMENTADO QUE PERSISTA UN PEDIDO NO CANCELADO O ENTREGADO MIENTRAS SE MODIFICA O SI SE CAMBIA DE USUARIO
        // POR LO GENERAL, SE HAN ESTABLECIDO EN FINAL LA MAYORÍA DE LOS ATRIBUTOS DADO QUE NO SUFREN MODIFICACIONES, ESTO PODRÍA CAMBIAR POSTERIORMENTE
    }
}
