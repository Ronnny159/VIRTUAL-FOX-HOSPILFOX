// src/Main.java
import ENUMS.*;
import MODELO.*;
import SERVICE.EpsService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static EpsService eps = new EpsService();
    private static Scanner scanner = new Scanner(System.in);
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static void main(String[] args) {
        cargarDatosEjemplo();

        int opcion;
        do {
            mostrarMenu();
            opcion = leerOpcion();

            switch (opcion) {
                case 1 -> registrarPaciente();
                case 2 -> registrarMedico();
                case 3 -> agendarCita();
                case 4 -> gestionarCita();
                case 5 -> registrarConsulta();
                case 6 -> gestionarFacturas();
                case 7 -> eps.listarPacientes();
                case 8 -> eps.listarMedicos();
                case 9 -> eps.listarCitas();
                case 10 -> eps.mostrarEstadisticas();
                case 11 -> System.out.println("👋 ¡Gracias por usar el sistema de salud!");
                default -> System.out.println("❌ Opción inválida");
            }
        } while (opcion != 11);

        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n🏥 SISTEMA DE GESTIÓN HOSPITALARIA / EPS");
        System.out.println("==========================================");
        System.out.println("1. Registrar paciente");
        System.out.println("2. Registrar médico");
        System.out.println("3. Agendar cita");
        System.out.println("4. Gestionar cita (confirmar/cancelar/iniciar)");
        System.out.println("5. Registrar consulta médica");
        System.out.println("6. Gestionar facturas");
        System.out.println("7. Listar pacientes");
        System.out.println("8. Listar médicos");
        System.out.println("9. Listar citas");
        System.out.println("10. Mostrar estadísticas");
        System.out.println("11. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private static int leerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static String leerString(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine();
    }

    private static int leerInt(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ Ingrese un número válido");
            }
        }
    }

    private static double leerDouble(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ Ingrese un número válido");
            }
        }
    }

    private static LocalDate leerFecha(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje + " (DD/MM/YYYY): ");
                String fechaStr = scanner.nextLine();
                return LocalDate.parse(fechaStr, dateFormatter);
            } catch (Exception e) {
                System.out.println("❌ Formato inválido. Use DD/MM/YYYY");
            }
        }
    }

    private static LocalDateTime leerFechaHora(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje + " (DD/MM/YYYY HH:MM): ");
                String fechaStr = scanner.nextLine();
                return LocalDateTime.parse(fechaStr, dateTimeFormatter);
            } catch (Exception e) {
                System.out.println("❌ Formato inválido. Use DD/MM/YYYY HH:MM");
            }
        }
    }

    // ========== OPCIÓN 1: REGISTRAR PACIENTE ==========
    private static void registrarPaciente() {
        System.out.println("\n📝 REGISTRAR PACIENTE");
        String id = leerString("Cédula/ID: ");
        String nombre = leerString("Nombre: ");
        String apellido = leerString("Apellido: ");
        LocalDate fechaNac = leerFecha("Fecha de nacimiento");
        String telefono = leerString("Teléfono: ");
        String email = leerString("Email: ");
        String direccion = leerString("Dirección: ");
        String historiaClinica = leerString("Número de historia clínica: ");
        String tipoSangre = leerString("Tipo de sangre: ");
        String alergias = leerString("Alergias (o 'Ninguna'): ");
        String medicamentos = leerString("Medicamentos actuales (o 'Ninguno'): ");
        
        eps.registrarPaciente(id, nombre, apellido, fechaNac, telefono, 
                email, direccion, historiaClinica, tipoSangre, alergias, medicamentos);
    }

    // ========== OPCIÓN 2: REGISTRAR MÉDICO ==========
    private static void registrarMedico() {
        System.out.println("\n👨‍⚕️ REGISTRAR MÉDICO");
        String id = leerString("Cédula/ID: ");
        String nombre = leerString("Nombre: ");
        String apellido = leerString("Apellido: ");
        LocalDate fechaNac = leerFecha("Fecha de nacimiento");
        String telefono = leerString("Teléfono: ");
        String email = leerString("Email: ");
        String direccion = leerString("Dirección: ");
        String registroMedico = leerString("Registro médico: ");
        
        System.out.println("Especialidades disponibles:");
        for (Especialidad e : Especialidad.values()) {
            System.out.println("  " + e.ordinal() + ". " + e.getNombre());
        }
        int idxEspecialidad = leerInt("Seleccione especialidad (0-" + (Especialidad.values().length - 1) + "): ");
        Especialidad especialidad = Especialidad.values()[idxEspecialidad];
        
        double honorarios = leerDouble("Honorarios por consulta: $");
        
        eps.registrarMedico(id, nombre, apellido, fechaNac, telefono,
                email, direccion, registroMedico, especialidad, honorarios);
    }

    // ========== OPCIÓN 3: AGENDAR CITA ==========
    private static void agendarCita() {
        System.out.println("\n📅 AGENDAR CITA");
        String pacienteId = leerString("ID del paciente: ");
        String medicoId = leerString("ID del médico: ");
        LocalDateTime fechaHora = leerFechaHora("Fecha y hora de la cita");
        String motivo = leerString("Motivo de la consulta: ");
        
        eps.agendarCita(pacienteId, medicoId, fechaHora, motivo);
    }

    // ========== OPCIÓN 4: GESTIONAR CITA ==========
    private static void gestionarCita() {
        System.out.println("\n🔧 GESTIONAR CITA");
        int idCita = leerInt("ID de la cita: ");
        
        Cita cita = eps.buscarCita(idCita);
        if (cita == null) {
            System.out.println("❌ Cita no encontrada");
            return;
        }
        
        System.out.println("Cita actual: " + cita);
        System.out.println("\nOpciones:");
        System.out.println("1. Confirmar cita");
        System.out.println("2. Cancelar cita");
        System.out.println("3. Iniciar cita (marcar como EN_CURSO)");
        System.out.println("4. Registrar no asistencia");
        System.out.println("5. Volver");
        
        int opcion = leerInt("Seleccione: ");
        switch (opcion) {
            case 1 -> eps.confirmarCita(idCita);
            case 2 -> eps.cancelarCita(idCita);
            case 3 -> eps.iniciarCita(idCita);
            case 4 -> {
                cita.registrarNoAsistencia();
                System.out.println("✅ No asistencia registrada");
            }
            case 5 -> System.out.println("Volviendo...");
            default -> System.out.println("❌ Opción inválida");
        }
    }

    // ========== OPCIÓN 5: REGISTRAR CONSULTA ==========
    private static void registrarConsulta() {
        System.out.println("\n📋 REGISTRAR CONSULTA MÉDICA");
        int idCita = leerInt("ID de la cita: ");
        
        Cita cita = eps.buscarCita(idCita);
        if (cita == null) {
            System.out.println("❌ Cita no encontrada");
            return;
        }
        
        if (cita.getEstadoCita() != EstadoCita.EN_CURSO) {
            System.out.println("⚠️ La cita debe estar EN_CURSO. Use la opción 4 para iniciarla.");
            return;
        }
        
        System.out.println("Paciente: " + cita.getPaciente().getNombreCompleto());
        System.out.println("Médico: Dr. " + cita.getMedico().getNombreCompleto());
        
        String diagnostico = leerString("Diagnóstico: ");
        String sintomas = leerString("Síntomas: ");
        String tratamiento = leerString("Tratamiento indicado: ");
        String medicamentos = leerString("Medicamentos recetados (o 'Ninguno'): ");
        String observaciones = leerString("Observaciones adicionales: ");
        
        Consulta consulta = eps.registrarConsulta(idCita, diagnostico, sintomas,
                tratamiento, medicamentos, observaciones);
        
        if (consulta != null) {
            System.out.println("\n¿Desea generar factura por esta consulta?");
            System.out.println("1. Sí, generar factura");
            System.out.println("2. No, omitir");
            int opcion = leerInt("Seleccione: ");
            
            if (opcion == 1) {
                Factura factura = eps.generarFacturaConsulta(consulta);
                System.out.println("✅ Factura generada: #" + factura.getIdFactura());
                factura.mostrarFactura();
            }
        }
    }

    // ========== OPCIÓN 6: GESTIONAR FACTURAS ==========
    private static void gestionarFacturas() {
        System.out.println("\n🧾 GESTIÓN DE FACTURAS");
        System.out.println("1. Ver todas las facturas");
        System.out.println("2. Ver detalle de factura");
        System.out.println("3. Pagar factura");
        System.out.println("4. Generar factura manual");
        System.out.println("5. Volver");
        
        int opcion = leerInt("Seleccione: ");
        switch (opcion) {
            case 1 -> eps.listarFacturas();
            case 2 -> {
                int idFactura = leerInt("ID de factura: ");
                eps.mostrarFactura(idFactura);
            }
            case 3 -> {
                int idFactura = leerInt("ID de factura: ");
                eps.pagarFactura(idFactura);
            }
            case 4 -> generarFacturaManual();
            case 5 -> System.out.println("Volviendo...");
            default -> System.out.println("❌ Opción inválida");
        }
    }

    private static void generarFacturaManual() {
        String pacienteId = leerString("ID del paciente: ");
        Paciente paciente = eps.buscarPaciente(pacienteId);
        if (paciente == null) {
            System.out.println("❌ Paciente no encontrado");
            return;
        }
        
        ArrayList<TipoServicio> servicios = new ArrayList<>();
        System.out.println("\nSeleccione servicios (0 para terminar):");
        for (TipoServicio s : TipoServicio.values()) {
            System.out.println("  " + s.ordinal() + ". " + s.getNombre() + " ($" + s.getCostoBase() + ")");
        }
        
        while (true) {
            int idx = leerInt("Servicio (0-" + (TipoServicio.values().length - 1) + ", -1 para terminar): ");
            if (idx == -1) break;
            if (idx >= 0 && idx < TipoServicio.values().length) {
                servicios.add(TipoServicio.values()[idx]);
                System.out.println("✅ Agregado: " + TipoServicio.values()[idx].getNombre());
            } else {
                System.out.println("❌ Opción inválida");
            }
        }
        
        if (!servicios.isEmpty()) {
            Factura factura = eps.generarFactura(pacienteId, servicios);
            factura.mostrarFactura();
        } else {
            System.out.println("No se agregaron servicios");
        }
    }

    // ========== DATOS DE EJEMPLO ==========
    private static void cargarDatosEjemplo() {
        System.out.println("📥 Cargando datos de ejemplo...");
        
        // Pacientes
        Paciente p1 = eps.registrarPaciente("12345678A", "Ana", "García",
                LocalDate.of(1985, 3, 15), "3111234567", "ana@email.com",
                "Calle 45 #23-12", "HC-001", "O+", "Ninguna", "Ninguno");
        
        Paciente p2 = eps.registrarPaciente("87654321B", "Luis", "Pérez",
                LocalDate.of(1990, 7, 22), "3117654321", "luis@email.com",
                "Carrera 12 #34-56", "HC-002", "A-", "Polen", "Losartán");
        
        Paciente p3 = eps.registrarPaciente("11111111C", "María", "López",
                LocalDate.of(1978, 11, 5), "3119876543", "maria@email.com",
                "Calle 78 #90-12", "HC-003", "B+", "Ninguna", "Metformina");

        // Médicos
        Medico m1 = eps.registrarMedico("98765432C", "Carlos", "Rodríguez",
                LocalDate.of(1975, 5, 10), "3101234567", "carlos@email.com",
                "Calle 56 #78-90", "RM-001", Especialidad.MEDICINA_GENERAL, 50000);
        
        Medico m2 = eps.registrarMedico("54321678D", "Laura", "Martínez",
                LocalDate.of(1980, 9, 20), "3107654321", "laura@email.com",
                "Carrera 34 #56-78", "RM-002", Especialidad.CARDIOLOGIA, 80000);
        
        Medico m3 = eps.registrarMedico("22222222E", "Juan", "Sánchez",
                LocalDate.of(1972, 12, 1), "3105432167", "juan@email.com",
                "Calle 90 #12-34", "RM-003", Especialidad.PEDIATRIA, 60000);

        // Citas de ejemplo
        if (p1 != null && m1 != null) {
            LocalDateTime fecha1 = LocalDateTime.now().plusDays(2).withHour(10).withMinute(0);
            eps.agendarCita(p1.getId(), m1.getId(), fecha1, "Chequeo general");
        }
        
        if (p2 != null && m2 != null) {
            LocalDateTime fecha2 = LocalDateTime.now().plusDays(3).withHour(14).withMinute(30);
            eps.agendarCita(p2.getId(), m2.getId(), fecha2, "Dolor en el pecho");
        }
        
        if (p3 != null && m3 != null) {
            LocalDateTime fecha3 = LocalDateTime.now().plusDays(4).withHour(11).withMinute(0);
            eps.agendarCita(p3.getId(), m3.getId(), fecha3, "Control pediátrico");
        }

        System.out.println("✅ Datos cargados correctamente\n");
    }
}