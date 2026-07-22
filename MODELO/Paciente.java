package MODELO;

import java.time.LocalDate;
import java.util.ArrayList;

public class Paciente extends Persona{
    private String historiaClinica;
    private String tipoSangre;
    private String alergias;
    private String medicamentosActuales;
    private ArrayList<Cita> citas;
    private ArrayList<Consulta > historialMedico;

    public Paciente(String id, String nombre, String apellido, LocalDate fechaNacimiento,
            String telefono, String email, String direccion,
            String historiaClinica, String tipoSangre, String alergias,
            String medicamentosActuales) {
                super(id, nombre, apellido, fechaNacimiento, telefono, email, direccion);
                this.historiaClinica = historiaClinica;  
                this.tipoSangre = tipoSangre;
                this.alergias = alergias;               
                this.medicamentosActuales = medicamentosActuales;
                this.citas = new ArrayList<>();
                this.historialMedico = new ArrayList<>();
    }
    public String getHistorialClinica(){return historiaClinica;}
    public String getTipoSangre(){return tipoSangre;}
    public String getAlergias(){return alergias;}
    public String getMedicamentoAcruales(){return medicamentosActuales;}
    public ArrayList<Cita> getCitas(){return citas;}
    public ArrayList<Consulta > getHistorialMedico(){return historialMedico;}
    
    public void setHistoriaClinica(String historiaClinica) { this.historiaClinica = historiaClinica; }
    public void setTipoSangre(String tipoSangre){this.tipoSangre=tipoSangre;}
    public void setAlergias(String alergias){this.alergias=alergias;}
    public void setMedicamentosActuales(String medicamentosActuales){this.medicamentosActuales=medicamentosActuales;}
    
    public void agregarCita(Cita cita){
        citas.add(cita);
    }
    public void agregarConsulta(Consulta consulta){
        historialMedico.add(consulta);
    }
    public void mostrarHistorialMedico(){
        if(historialMedico.isEmpty()){
            System.out.println("No hay historial medico registrado");
            return;
        }
        System.out.println("\n Historial Medico de "+getNombreCompleto());
        System.out.println("============================================");
        for(Consulta c:historialMedico){
            System.out.println(c.getResumen());
            System.out.println("-------------------------------------------");
        }
    }
    public void mostrarCitas(){
        if(citas.isEmpty()){
            System.out.println("No hay Citas registradas");
            return;
        }
        System.out.println("\n Citas de "+getNombreCompleto());
        System.out.println("============================================");
        for(Cita c:citas){
            System.out.println(c);
        }
    }
    @Override
    public String toString(){
        return super.toString()+String.format("| HC: %s | Sangre %s", historiaClinica,tipoSangre);
    }

}