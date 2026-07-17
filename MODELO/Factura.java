package MODELO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import ENUMS.TipoServicio;

public class Factura{
    private int idFactura;
    private Paciente paciente;
    private ArrayList<DetalleFactura> detalles;
    private double subtotal;
    private double iva;
    private double total;
    private LocalDateTime fecha;
    private boolean pagada;
    private static final double IVA_RATE=0.19;

    public Factura(Paciente paciente){
        this.paciente=paciente;
        this.detalles=new ArrayList<>();
        this.subtotal=0;
        this.iva=0;
        this.fecha=LocalDateTime.now();
        this.pagada=false;
    }

    public int getIdFactura(){ return idFactura;}
    public void setIdFactura(int idFactura){this.idFactura=idFactura}
    public Paciente getPaciente(){ return paciente;}
    public ArrayList<DetalleFactura> getDetalles(){ return detalles;}
    public double getSubtotal(){ return subtotal;}
    public double getIva(){ return iva;}
    public double getTotal(){ return total;}
    public LocalDateTime getFecha(){ return fecha;}
    public boolean isPagada(){ return pagada;}

    public void agregarDetalle(TipoServicio servicio, String descripcion, int cantidad){
        DetalleFactura detalle = new DetalleFactura(null,descripcion,costo,cantidad);
        detalles.add(detalle);
        calcularTotales();
    }
    private void calcularTotales(){
        subtotal=0;
        for(DetalleFactura detalle:detalles){
            subtotal += detalle.getSubtotal();
        }
        iva = subtotal * IVA_RATE;
        total = subtotal + iva;
    }
    public void pagar(){
        this.pagada = true;
        System.out.println("Factura pagada existosamente. Total: $" + total);
    }
    public void mostrarFactura(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        System.out.println("FACTURA #" + idFactura);
        System.out.println("==============================================");
        System.out.println("Fecha" + fecha.format(formatter));
        System.out.println("Paciente:"+ paciente.getNombreCompleto());
        System.out.println("----------------------------------------------");
        System.out.println("DETALLES:");
        for(DetalleFactura detalle:detalles){
            System.out.println("  "+detalle);
        }
        System.out.println("----------------------------------------------");
        System.out.println("Subtotal: $%.2f\n" + subtotal);
        System.out.println("IVA (19%%): $%.2f\n" + iva);
        System.out.println("Total: $%.2f" + total);
        System.out.println("Estado: "+ (pagada?"PAGADA":"PENDIENTE"));
        System.out.println("==============================================");
    }
}