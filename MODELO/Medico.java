package MODELO;

import ENUMS.Especialidad;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Medico extends Persona{
    private String registroMedico;
    private Especialidad especialidad;
    private ArrayList<String> horariosDisponibles;
    private boolean disponible;
    private double honorarios;
    private ArrayList<Cita> citasAsignadas;

   public Medico(String id, String nombre, String apellido, LocalDate fechaNacimiento,
        String telefono, String email, String direccion, String registroMedico,
        Especialidad especialidad, double honorarios){
            super(id,nombre,apellido,fechaNacimiento,telefono,email,direccion);
            this.registroMedico=registroMedico;
            this.especialidad=especialidad;
            this.honorarios=honorarios;
            this.horariosDisponibles=new ArrayList<>();
            this.disponible=true;
            this.citasAsignadas=new ArrayList<>();
    }

     public String getRegistroMedico(){return registroMedico; }
     public Especialidad getEspecialidad(){return especialidad; }
     public ArrayList<String> getHorarioDisponibles(){return horariosDisponibles; }
     public boolean isDisponible(){return disponible; }
     public double getHonorarios(){return honorarios; }
     public ArrayList<Cita> getCitasAsignadas(){return citasAsignadas; }
     
     public void setRegistroMedico(String registroMedico){this.registroMedico=registroMedico; }
     public void setEspecialidad(Especialidad especialidad){this.especialidad=especialidad; }
     public void setDisponible(boolean disponible){this.disponible=disponible; }
     public void setHonorarios(double honorarios){this.honorarios=honorarios; }

     public void agregarHorario(String horario){
        if(!horariosDisponibles.contains(horario)){
            horariosDisponibles.add(horario);
            System.out.println("Horario agregado: " + horario);
        }else {
            System.out.println("El horario ya existente");
        }
    }

    public void eliminarHorario(String horario){
        if(!horariosDisponibles.remove(horario)){
            System.out.println("Horario Eliminado: " + horario);
        }else {
            System.out.println("Horario no encontrado");
        }
    }

    public boolean verificarDisponibilidad(LocalDateTime fechaHora){
        for(Cita cita: citasAsignadas){
            if (cita.getFechaHora().equals(fechaHora) && 
            cita.getEstadoCita() !=ENUMS.EstadoCita.CANCELADA && 
            cita.getEstadoCita() != ENUMS.EstadoCita.NO_ASISTIO) 
            {return false;}
        }
        return true;
    }

    public void mostrarCitas(){
        if(citasAsignadas.isEmpty()){
            System.out.println("No hay Citas asignadas");
            return;
        }
        System.out.println("\n CITAS DEL DR. "+getNombreCompleto());
        System.out.println("============================================");
        for(Cita c:citasAsignadas){
            System.out.println(c);
        }
    }

    public void agregarCita(Cita cita){
        citasAsignadas.add(cita);
    }

    @Override
    public String toString(){
        return super.toString()+String.format("| REG: %S | ESPECIALIDAD: %S | HONORARIOS: $%.2f", 
        registroMedico, especialidad.getNombre(), honorarios);
    }

}