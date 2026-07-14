package ENUMS;

public enum Especialidad{
    MEDICINA_GENERAL("Medicina Genetal"),
    PEDIATRIA("Pediatria"),
    CARDIOLOGIA("Cardiologia"),
    DERMATOLOGIA("Dermatologia"),
    GINECOLOGIA("Ginecologia"),
    TRAUMOLOGIA("Traumologia"),
    PIQUIATRIA("Psiquiatria");

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