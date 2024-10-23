package es.adr.utilidades;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvFieldAssignmentException;
import es.adr.modelo.Cliente;
import es.adr.modelo.Ingrediente;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class GestionFicheros {
    // RUTAS
    private static final String RUTA_ADMINISTRADORES = "admin.txt";
    private static final String RUTA_CLIENTES = "clientes.xml";
    private static final String RUTA_INGREDIENTES = "ingredientes.csv";

    // Sin librería

    // Sin librería (el fichero no tiene encabezado)
    public static List<Cliente> importarSinLibreria() throws IOException, IllegalArgumentException {
        try (Stream<String> lineas = Files.lines(Path.of(RUTA_ADMINISTRADORES))) {
            return lineas.map(GestionFicheros::stringToCliente).toList();
        }
    }

    private static Cliente stringToCliente(String linea) throws IllegalArgumentException {
        List<String> elementos = Stream.of(linea.split("[,|;]")).map(String::trim).toList();

        // Validación básica
        if (elementos.size() != 7)
            throw new IllegalArgumentException("Ha habido un problema con el archivo admin.txt\nNúmero de campos incorrecto o uso de \",\", \"|\" o \";\" en los campos -> " + linea);

        if (elementos.stream().anyMatch(String::isEmpty))
            throw new IllegalArgumentException("Ha habido un problema con el archivo admin.txt\nCampo/s vacío/s -> " + linea);

        if (!elementos.get(0).matches("\\d+"))
            throw new IllegalArgumentException("Ha habido un problema con el archivo admin.txt\nID incorrecto -> " + linea);

        return new Cliente(Integer.parseInt(elementos.get(0)), elementos.get(1), elementos.get(2), elementos.get(3), elementos.get(4), elementos.get(5), elementos.get(6), true);
    }

    // Sin librería (el fichero no tiene encabezado, tampoco impide exportar con separadores admitidos en la importación, pero esto no había que hacerlo) 
    public static boolean exportarSinLibreria(List<Cliente> clientes, String separador) throws IOException {
        Files.write(Path.of(RUTA_ADMINISTRADORES), clientes.stream().map(cliente -> GestionFicheros.clienteToString(cliente, separador)).toList());
        return true;
    }

    private static String clienteToString(Cliente cliente, String separador) {
        return String.format("%d%s%s%s%s%s%s%s%s%s%s%s%s", cliente.getId(), separador, cliente.getDni(), separador, cliente.getNombre(), separador, cliente.getDireccion(), separador, cliente.getTelefono(), separador, cliente.getEmail(), separador, cliente.getPassword());
    }

    // Librería JAXB

    public static List<Cliente> importarXML() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(ClienteWrapper.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        ClienteWrapper persona = (ClienteWrapper) unmarshaller.unmarshal(new File(RUTA_CLIENTES));
        return persona.getClientes();
    }

    public static boolean exportarXML(List<Cliente> clientes) throws JAXBException {
        ClienteWrapper persona = new ClienteWrapper(clientes);
        JAXBContext jaxbContext = JAXBContext.newInstance(ClienteWrapper.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(persona, new File(RUTA_CLIENTES));
        return true;
    }

    // Librería OpenCSV

    public static List<Ingrediente> importarCSV() throws IOException {
        List<Ingrediente> ingredientes;
        try (FileReader fileReader = new FileReader(RUTA_INGREDIENTES)) {
            CsvToBean<Ingrediente> csvToBean = new CsvToBeanBuilder<Ingrediente>(fileReader).withType(Ingrediente.class).withSeparator(';').build();
            ingredientes = csvToBean.parse();
        }

        return ingredientes;
    }

    public static boolean exportarCSV(List<Ingrediente> ingredientes) throws IOException, CsvFieldAssignmentException {
        try (PrintWriter pw = new PrintWriter(RUTA_INGREDIENTES)) {
            StatefulBeanToCsv<Ingrediente> beanToCsv = new StatefulBeanToCsvBuilder<Ingrediente>(pw).withSeparator(';').build();
            beanToCsv.write(ingredientes);
        }
        return true;
    }
}
