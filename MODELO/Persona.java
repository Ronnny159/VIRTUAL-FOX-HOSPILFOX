package MODELO;

import java.time.LocalDate;
import java.time.Period;

public adstract class Persona {

    protected String id;
    protected String nombre;
    protected String apellido;
    protected LocalDate fechaNacimiento;
    protected String telefono;
    protected String email;
    protected String direccion;

    public persona(String id, String nombre, String apellido, LocalDate fechaNacimiento,
                    String telefono, String email, String direccion){

        this.id = id;
        this.nombre = nombre;
        this.apellido = aapellido;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;

    }
    //GETTERS
    public String getId(){return id;}
    public String getNombre(){return nombre;}
    public Strign getApellido(){return apellido}
    public LocalDate getFechaNacimiento(){return fechaNacimiento}
    public String getTelefono(){return telefono}
    public String getEmail(){return email}
    public String getDireccion(){return direccion}

    //setters
    public void setId(String id){this.id=id;}
    public void setNombre(String nombre){this.nombre=nombre;}
    public void setApellido(String apellido){this.apellido=apellido;}
    public void setFechaNacimiento(LocalDate fechaNacimiento){this.fechaNacimiento=fechaNacimiento;}
    public void setTelefono(String telefono){this.telefono=telefono;}
    public void setEmail(String email){this.email=email;}
    public void setDireccion(String direccion){this.direccion=direccion;}

    public String getNombreCompleto(){
        return nombre + " " + apellido;
    }
    public int calcularEdad(){
        if(fechaNacimiento == null) return 0;
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }
    @Override
    public String toString(){
        return String.format("%s %s(ID: %s) - Edad: %d años",
         nombre, apellido, id calcularEdad());
    }

}