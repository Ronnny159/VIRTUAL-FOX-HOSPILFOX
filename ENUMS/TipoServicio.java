package ENUMS;

public enum TipoServicio{
    CONSULTA_GENERAL("Consulta General", 50000),
    CONSULTA_ESPECIALISTA("Consulta Especialista", 80000),
    URGENCIAS("Urgencias", 120000),
    EXAMEN_LABORATORIO("Examen de Laboratorio", 45000),
    Imagenologia("Imagenologia (Rayos X, Ecografia)", 90000),

    private String nombre;
    private double costoBase;

    Especialidad(Sreing nombre, double costoBase){
        this.nombre=nombre;
        this.costoBase=costoBase;
    }
    public String getNombre(){
        return nombre;
    }
    public double getCostoBase() {
        return costoBase;
    }
    @Override
    public String toString(){
        return nombre+" ($"+costoBase+")";
    }
}