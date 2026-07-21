package SERVICE;

import ENUMS.*;
import MODELO.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EpsService {
    private ArrayList<Paciente> pacientes;
    private ArrayList<Medico> medicos;
    private ArrayList<Cita> citas;
    private ArrayList<Factura> facturas;
    private ArrayList<Consulta> consultas;
    private static int contadorCita=1;
    private static int contadorConsulta=1;
    private static int contadorFactura=1;

    public EpsService(){
        this.pacientes=new ArrayList<>();
        this.medicos=new ArrayList<>();
        this.facturas=new ArrayList<>();
        this.citas=new ArrayList<>();
    }

    public Paciente registrarPaciente(String id,String nombre,String apellido,LocalDate fechaNacimiento,String telefono,
        String email,String direccion,String historiaClinica,String tipoSangre,String alergias,String medicamentosActuales){
            if(buscarPaciente(id) !=null){
                System.out.println("Ya existe un paciente con ID: "+id);
                return null;
            }
            Paciente paciente =new Paciente(id,nombre,apellido,fechaNacimiento,telefono,email,direccion,historiaClinica,tipoSangre,alergias,medicamentosActuales);
            pacientes.add(paciente);
            System.out.println("Paciente registrado exitosamente");
            return paciente;
    }
    
    public Medico registrarMedico(String id,String nombre,String apellido,LocalDate fechaNacimiento,String telefono,
        String email,String direccion,String registroMedico,Especialidad especialidad,double honorarios){
            if(buscarMedico(id) !=null){
                System.out.println("Ya existe un medico con ID: "+id);
                return null;
            }
            Medico medico =new Medico(id,nombre,apellido,fechaNacimiento,telefono,email,direccion,registroMedico,especialidad,honorarios);
            medicos.add(medico);
            System.out.println("Medico registrado exitosamente");
            return medico;
    }

    public Paciente buscarPaciente(String id){
        for(Paciente p: pacientes){
            if(p.getId().equals(id)){
                return p;
            }
        }
        return null;
    }

    public Consulta buscarConsulta(int idConsulta){
        for(Consulta c: consultas){
            if(c.getIdConsulta()==idConsulta){
                return c;
            }
        }
        return null;
    }

    public Medico buscarMedico(String id){
        for(Medico m: medicos){
            if(m.getId().equals(id)){
                return m;
            }
        }
        return null;
    }

    public Cita buscarCita(int idCita){
        for(Cita c: citas){
            if(c.getIdCita()==idCita){
                return c;
            }
        }
        return null;
    }

    public Medico buscarMedicoPorEspecialidad(Especialidad especialidad){
        for(Medico m: medicos){
            if(m.getEspecialidad() == especialidad && m.isDisponible()){
                return m;
            }
        }
        return null;
    }

    public Cita agendarCita(String pacienteId,String medicoId,LocalDateTime fechaHora,String motivo){
        Paciente paciente=buscarPaciente(pacienteId);
        Medico medico=buscarMedico(medicoId);
        if(paciente==null){
            System.out.println("Paciente no encontrado");
            return null;
        }
        if(medico==null){
            System.out.println("Medico no encontrado");
            return null;
        }
        if(!medico.verificarDisponibilidad(fechaHora)){
            System.out.println("Medico no disponible en ese horario");
            return null;
        }

        Cita cita=new Cita(paciente,medico,fechaHora,motivo);
        cita.setIdCita(contadorCita++);
        citas.add(cita);
        paciente.agregarCita(cita);
        medico.agregarCita(cita);

        System.out.println("Cita agendada existosamente. ID: "+cita.getIdCita());
        return cita;
    }

    public boolean confirmarCita(int idCita){
        Cita cita=buscarCita(idCita);
        if(cita==null){
            System.out.println("Cita no encontrada");
            return false;
        }
        cita.confirmar();
        return true;
    }

    public boolean cancelarCita(int idCita){
        Cita cita=buscarCita(idCita);
        if(cita==null){
            System.out.println("Cita no encontrada");
            return false;
        }
        cita.cancelar();
        return true;
    }

    public boolean iniciarCita(int idCita){
        Cita cita=buscarCita(idCita);
        if(cita==null){
            System.out.println("Cita no encontrada");
            return false;
        }
        cita.iniciar();
        return true;
    }

    public Consulta registrarConsulta(int idCita,String diagnostico,String sintomas,String tratamiento,String medicamentos,String observaciones){
        Cita cita = buscarCita(idCita);
        if(cita==null){
            System.out.println("Cita no encontrada");
            return null;
        }
        if(cita.getEstadoCita() != EstadoCita.EN_CURSO){
            System.out.println("La cita debe esstar EN_CURSO para registrar consulta");
            return null;
        }
        Consulta consulta = new Consulta(cita,diagnostico,sintomas,tratamiento,medicamentos,observaciones);
        consulta.setIdConsulta(contadorConsulta++);
        cita.getPaciente().agregarConsulta(consulta);
        cita.completar();

        System.out.println("Consulta registrada exitosamente. ID: "+consulta.getIdConsulta());
        return consulta;
    }

    public Factura generarFactura(String pacienteId,ArrayList<TipoServicio> servicios){
        Paciente paciente = buscarPaciente(pacienteId);
        if(paciente==null){
            System.out.println("Paciente no encontrado");
            return null;
        }

        Factura factura= new Factura(paciente);
        factura.setIdFactura(contadorFactura++);

        for(TipoServicio servicio:servicios){
            factura.agregarDetalle(servicio, servicio.getNombre(), 1);
        }

        facturas.add(factura);
        System.out.println("Factura generada exitosamente. ID: "+factura.getIdFactura());
        return factura;
    }

    public Factura generarFacturaConsulta(Consulta consulta){
        Paciente paciente=consulta.getCita().getPaciente();
        Factura factura = new Factura(paciente);
        factura.setIdFactura(contadorFactura++);

        factura.agregarDetalle(TipoServicio.CONSULTA_ESPECIALISTA, "Consulta con Dr. "+consulta.getCita().getMedico().getNombreCompleto(), 1);

        if(consulta.getMedicamentosRecetados() != null && !consulta.getMedicamentosRecetados().isEmpty()){
            factura.agregarDetallePersonalizado("Medicamentos recetados",15000,1);
        }

        facturas.add(factura);
        return factura;
    }

    public void pagarFactura(int idFactura){
        for(Factura f: facturas){
            if(f.getIdFactura()==idFactura){
                f.pagar();
                return;
            }
        }
        System.out.println("Factura no enonctrada");
    }

    public void listarPacientes(){
        if(pacientes.isEmpty()){
            System.out.println("No hay pacientes registrados");
            return;
        }
        System.out.println("\n LISTA DE PACIIENTES ");
        System.out.println("========================");
        for(Paciente p: pacientes){
            System.out.println(p);
        }
        System.out.println("Total: "+pacientes.size()+" pacientes");
    }

    public void listarMedicos(){
        if(medicos.isEmpty()){
            System.out.println("No hay medicos registrados");
            return;
        }
        System.out.println("\n LISTA DE MEDICOS ");
        System.out.println("========================");
        for(Medico m: medicos){
            System.out.println(m);
        }
        System.out.println("Total: "+medicos.size()+" medicos");
    }

    public void listarCitas(){
        if(citas.isEmpty()){
            System.out.println("No hay Citas registrados");
            return;
        }
        System.out.println("\n LISTA DE CITAS ");
        System.out.println("========================");
        for(Cita c: citas){
            System.out.println(c);
        }
        System.out.println("Total: "+citas.size()+" citas");
    }

    public void listarCitasPorPaciente(String pacienteId){
        Paciente paciente = buscarPaciente(pacienteId);
        if(paciente == null){
            System.out.println("Paciente no encontrado");
            return;
        }
        paciente.mostrarCitas();
    }

    public void listarCitasPormedico(String medicoId){
        Medico medico = buscarMedico(medicoId);
        if(medico == null){
            System.out.println("Medico no encontrado");
            return;
        }
        medico.mostrarCitas();
    }

    public void listarFacturas(){
        if(facturas.isEmpty()){
            System.out.println("No hoy facturas registradas");
            return;
        }
        System.out.println("\n LISTA DE FACTURAS ");
        System.out.println("========================");
        for(Factura f: facturas){
            System.out.printf("Factura #%d | Paciente: %s |  Total:$%.2f | %s\n",
                f.getIdFactura(),f.getPaciente().getFechaNacimiento(),f.getTotal(),f.isPagada()?"PAGADA":"PENDIENTE");
        }
    }

    public void mostrarFactura(int idFactura){
        for(Factura f: facturas){
            if(f.getIdFactura()==idFactura){
                f.mostrarFactura();
                return;
            }
        }
        System.out.println("Factura no enonctrada");
    }

    public ArrayList<Paciente> getPacientes(){return new ArrayList<>(pacientes);}
    public ArrayList<Medico> getMedicos(){return new ArrayList<>(medicos);}
    public ArrayList<Cita> getCitas(){return new ArrayList<>(citas);}
    public ArrayList<Factura> getFacturas(){return new ArrayList<>(facturas);}
    public ArrayList<Consulta> getConsultas(){return new ArrayList<>(consultas);}

    public boolean eliminarPaciente(String id){
        Paciente paciente = buscarPaciente(id);
        if(paciente == null){
            System.out.println("Paciente no encontrado");
            return false;
        }
        pacientes.remove(paciente);
        System.out.println("Paciente eliminado exitosamente");
        return true;
    }

    public boolean eliminarMedico(String id){
        Medico medico = buscarMedico(id);
        if(medico == null){
            System.out.println("Medico no encontrado");
            return false;
        }
        medicos.remove(medico);
        System.out.println("Medico eliminado exitosamente");
        return true;
    }

    public int getTotalPacientes(){return pacientes.size();}
    public int getTotalMedicos(){return medicos.size();}
    public int getTotalCitas(){return citas.size();}
    public int getTotalFacturas(){return facturas.size();}
    public int getTotalConsultas(){return consultas.size();}
    public double getTotalFacturado(){return facturas.stream().mapToDouble(Factura::getTotal).sum();}
    public double getTotalPendiente(){return facturas.stream().filter(f -> !f.isPagada()).mapToDouble(Factura::getTotal).sum();}
    public List<Cita> getCitasPorEstado(EstadoCita estado){return citas.stream().filter(c -> c.getEstadoCita() == estado).collect(Collectors.toList());}
    public List<Cita> getCitasPorPaciente(String pacienteId){return citas.stream().filter(c -> c.getPaciente().getId().equals(pacienteId)).collect(Collectors.toList());}
    public List<Cita> getCitasPorMedico(String medicoId){return citas.stream().filter(c -> c.getMedico().getId().equals(medicoId)).collect(Collectors.toList());}
    public List<Consulta> getConsultasPorPaciente(String pacienteId){return consultas.stream().filter(c -> c.getCita().getPaciente().getId().equals(pacienteId)).collect(Collectors.toList());}
    public List<Paciente> buscarPacientesPorNombre(String nombre){return pacientes.stream().filter(p -> p.getNombreCompleto().toLowerCase().contains(nombre.toLowerCase())).collect(Collectors.toList());}
    public List<Medico> buscarMedicosPorEspecialidad(Especialidad especialidad){return medicos.stream().filter(m -> m.getEspecialidad() == especialidad).collect(Collectors.toList());}
    public List<Medico> getMedicosDisponibles(){return medicos.stream().filter(Medico::isDisponible).collect(Collectors.toList());}

    public boolean existePaciente(String id){return buscarPaciente(id) != null;}
    public boolean existeMedico(String id){return buscarMedico(id) != null;}
    public boolean existeCita(int idCita){return buscarCita(idCita) != null;}
}