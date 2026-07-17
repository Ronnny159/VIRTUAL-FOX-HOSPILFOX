package MODELO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Consulta{
    private int idConsulta;
    private Cita cita;
    private String diagnostico;
    private String sintomas;
    private String tratamiento;
    private String medicamentosRecetados;
    private String observaciones;
    private LocalDateTime fecha;

    public Consulta(Cita cita, String diagnostico, String sintomas,
        String tratamiento, String medicamentosRecetados, String observaciones){
            this.cita=cita;
            this.diagnostico=diagnostico;
            this.sintomas=sintomas;
            this.tratamiento=tratamiento;
            this.medicamentosRecetados=medicamentosRecetados;
            this.observaciones=observaciones;
            this.fecha=LocalDateTime.now();
    }
    public int getIdConsulta(){ return idConsulta;}
    public Cita getCita(){ return cita;}
    public String getDiagnostico(){ return diagnostico;}
    public String getSintomas(){ return sintomas;}
    public String getTratamiento(){ return tratamiento;}
    public String getMedicamentosRecetados(){ return medicamentosRecetados;}
    public String getObservaciones(){ return observaciones;}
    public LocalDateTime getFecha(){ return fecha;}

    public void setIdConsulta(int idConsulta){this.idConsulta=idConsulta;}
    public void setCita(Cita cita){this.cita=cita;}
    public void setDiagnostico(String diagnostico){this.diagnostico=diagnostico;}
    public void setSintomas(String sintomas){this.sintomas=sintomas;}
    public void settratamiento(String tratamiento){this.tratamiento=tratamiento;}
    public void setMedicamentosRecetados(String medicamentosRecetados){this.medicamentosRecetados=medicamentosRecetados;}

    public String getResumen(){
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return String.format("Consulta #%d - %s\n"+"Paciente: %s\n"+"Medico: Dr. %s\n"+"Diagnostico: %s\n"+
            "Tratamiento: %s\n"+"Medicamento: %s\n"+"Observaciones: %s", idConsulta,fecha.format(formatter), cita.getPaciente().getNombreCompleto(),
            cita.getMedico().getNombreCompleto(), diagnostico, tratamiento, medicamentosRecetados != null ? medicamentosRecetados: "Ninguno",
            observaciones != null ? observaciones:"Ninguna");
    }
    @Override
    public String toString(){
        return getResumen();
    }
}