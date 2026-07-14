package ENUMS;

public enum EstadoCita{
    AGENDADA("Agendada"),
    CONFIRMADA("Confirmada"),
    EN_CURSO("En curso"),
    COMPLETADA("Completada"),
    CANCELADA("Cancelada"),
    NO_ASISTIO("No asistio");

    private String nombre;

    Especialidad(Sreing nombre){
        this.nombre=nombre;
    }
    public String getNombre(){
        return nombre;
    }
    @Override
    public String toString(){
        return nombre;
    }
}