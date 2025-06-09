/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author LAB_REDES
 */
public class Pedido {
    private int id_pedido;
    private Persona persona;
    private Double total;
    private EstadoPedido estadoPedido;
    private Timestamp fecha;
    
    
    private List<Carrito> detallePedido;

    public Pedido() {
    }

    public Pedido(int id_pedido, Persona persona, Double total, EstadoPedido estadoPedido, Timestamp fecha, List<Carrito> detallePedido) {
        this.id_pedido = id_pedido;
        this.persona = persona;
        this.total = total;
        this.estadoPedido = estadoPedido;
        this.fecha = fecha;
        this.detallePedido = detallePedido;
    }

    public int getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(int id_pedido) {
        this.id_pedido = id_pedido;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public EstadoPedido getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(EstadoPedido estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public List<Carrito> getDetallePedido() {
        return detallePedido;
    }

    public void setDetallePedido(List<Carrito> detallePedido) {
        this.detallePedido = detallePedido;
    }
    
    
    
    
    
}
