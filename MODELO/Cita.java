package MODELO;

import ENUMS.EstadoCita;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Cita {
    private int idCita;
    private Paciente paciente;
    private Medico medico;
    private LocalDateTime fechaHora;
    private EstadoCita estado;
    private String motivo;
    private String observaciones;
    private LocalDateTime FechaCreacion;

    public Cita(Paciente paciente, Medico medico, LocalDateTime fechaHora, String motivo){
        this.paciente=paciente;
        this.medico=medico;
        this.fechaHora=fechaHora;
        this.motivo=motivo;
        this.estado=EStadoCita.AGENDADA;
        this.FechaCreacion=LocalDateTime.now();
        this.observaciones="";
    }

    public int getIdCita(){ return idCita; }
    public Paciente getPaciente(){ return paciente; }
    public Medico getMedico(){ return Medico; }
    public LocalDateTime getFechaHora(){return fechaHora; }
    public EstadoCita getEstadoCita(){return estado; }
    public String getMotivo(){return motivo; }
    public String getObservaciones(){return observaciones; }
    public LocalDateTime getFechaCreacion(){return fechaCreacion; }

    public void setIdCita(int idCita){this.idCita=idCita;}
    public void setPaciente(Paciente paciente){this.paciente=paciente; }
    public void setMedico(Medico medico){this.medico=medico; }
    public void setFechaHora(LocalDateTime fechaHora){this.fechaHora=fechaHora; }
    public void setMotivo(String motivo){this.motivo=motivo; }
    public void setObservaciones(String observaciones){this.observaciones=observaciones; }

    public void confirmar(){
        if(estado==EstadoCita.AGENDADA){
            estado = EstadoCita.CONFIRMADA;
            System.out.println("Cita confirmada");
        } else {
            System.out.println("Solo se puede confirmar citas Agendadas");
        }
    }
    public void cancelar(){
        if(estado==EstadoCita.AGENDADA || estado==EstadoCita.CONFIRMADA){
            estado = EstadoCita.CANCELADA;
            System.out.println("Cita Cancelada");
        } else {
            System.out.println("No se pudo cancelar esta cita");
        }
    }
    public void completar(){
        if(estado==EstadoCita.EN_CURSO){
            estado = EstadoCita.COMPLETADA;
            System.out.println("Cita Completada");
        } else {
            System.out.println("La cita debe estar EN_CURSO para completarla");
        }
    }
    public void iniciar(){
        if(estado==EstadoCita.CONFIRMADA){
            estado = EstadoCita.EN_CURSO;
            System.out.println("Cita en curso");
        } else {
            System.out.println("la cita debe estar CONFIRMADA para iniciarla");
        }
    }
    public void  registrarNoAsistencia(){
        if(estado==EstadoCita.AGENDADA || estado==EstadoCita.CONFIRMADA){
            estado = EstadoCita.NO_ASISTIO;
            System.out.println("paciente no asistio a la cita");
        } else {
            System.out.println("No se puede registar no asistencia en este estado");
        }
    }
    public String getFechaFormateada(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return fechaHora.format(formatter);
    }
    @Override
    public String toString(){
        return String.format("Cita #%d | %s | %s | Dr. %s | Estado: %s | Motivo: %s", 
        idCita,getFechaFormateada(),paciente.getNombreCompleto(),
        medico.getNombreCompleto(), estado.getNombre(), motivo);
    }

}