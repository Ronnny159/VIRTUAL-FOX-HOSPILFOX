package MODELO;

import ENUMS.Especialidad;
import ENUMS.EstadoCita;
import java.time.LocalDate;
import java.util.ArrayList;

public class Medico extends Persona{
    private String registroMedico;
    private Especialidad especialidad;
    private ArrayList<String> horariosDisponibles;
    private boolean disponible;
    private double honorarios;
    private ArrayList<cita> citasAsignadas;

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
     public ArrayList<cita> getCitasAsignadas(){return citasAsignadas; }
     public void setRegistroMedico(String registroMedico){this.registroMedico=registroMedico; }
     public void setEspecialidad(Especialidad especialidad){this.especialidad; }
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
        for(cita cita: citasAsignadas){
            if (cita.getFechaHora().equals(fechaHora) && 
            cita.getEstado() !=ENUMS.EstadoCita.CANCELADA && 
            cita.getEstado() != Enum.EstadoCita.NO_ASISTIO) 
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
        for(cita c:citasAsignadas){
            System.out.println(c);
        }
    }
    @Override
    public String toString(){
        return super.toString()+String.format("| REG: %S | ESPECIALIDAD: %S | HONORARIOS: $%.2f", 
        registroMedico, especialidad.getNombre(), honorarios);
    }

}