package SERVICE;

import ENUMS.*;
import MODELO.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class EpsService {
    private ArrayList<Paciente> pacientes;
    private ArrayList<Medico> medicos;
    private ArrayList<Cita> citas;
    private ArrayList<Factura> facturas;
    private static int contadorCita=1;
    private static int contadorConsulta=1;
    private static int contadorFactura=1;
}