package MODELO;

import ENUMS.TipoServicio;

public class DetalleFactura {
    private TipoServicio servicio;
    private String descripcion;
    private double costo;
    private int cantidad;

    public DetalleFactura(TipoServicio servicio, String descripcion, double costo, int cantidad){
        this.servicio=servicio;
        this.descripcion=descripcion;
        this.costo=costo;
        this.cantidad=cantidad;
    }

    public TipoServicio getServicio(){ return servicio;}
    public String getDescripcion(){ return descripcion;}
    public double getCosto(){ return costo;}
    public int getCantidad(){ return cantidad;}
    public double getSubtotal(){ return costo*cantidad;}

    @Override
    public String toString(){
        return String.format("%s x%d - $%.2f c/u = $%.2f", descripcion, cantidad, costo, getSubtotal());
    }
}