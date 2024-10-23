package es.adr.utilidades;

import es.adr.modelo.Cliente;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "clientes")
public class ClienteWrapper {
    @XmlElement(name = "cliente")
    private List<Cliente> clientes;

    public ClienteWrapper(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    public ClienteWrapper() {}

    public List<Cliente> getClientes() {
        return clientes;
    }
}
