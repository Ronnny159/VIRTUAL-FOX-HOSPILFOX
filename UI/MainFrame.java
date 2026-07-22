package UI;

import SERVICE.EpsService;
import javax.swing.*;
import MODELO.Paciente;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

public class MainFrame extends JFrame {
    private EpsService epsService;
    private JTabbedPane tabbedPane;
    private JLabel statusBar;
    
    // Paneles
    private PanelPacientes panelPacientes;
    private PanelMedicos panelMedicos;
    private PanelCitas panelCitas;
    private PanelConsultas panelConsultas;
    private PanelFacturas panelFacturas;
    private PanelEstadisticas panelEstadisticas;

    public MainFrame() {
        epsService = new EpsService();
        cargarDatosEjemplo();
        initComponents();
        configurarVentana();
    }

    private void configurarVentana() {
        setTitle("🏥 Sistema de Gestión Hospitalaria / EPS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 800);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1000, 700));
        setVisible(true);
    }

    private void initComponents() {
        // Crear barra de menú
        JMenuBar menuBar = new JMenuBar();
        
        // Menú Archivo
        JMenu menuArchivo = new JMenu("📁 Archivo");
        JMenuItem menuCargarDatos = new JMenuItem("Cargar datos de ejemplo");
        menuCargarDatos.addActionListener(e -> {
            cargarDatosEjemplo();
            actualizarTodosLosPaneles();
        });
        menuArchivo.add(menuCargarDatos);
        menuArchivo.addSeparator();
        JMenuItem menuSalir = new JMenuItem("Salir");
        menuSalir.setAccelerator(KeyStroke.getKeyStroke("control Q"));
        menuSalir.addActionListener(e -> System.exit(0));
        menuArchivo.add(menuSalir);
        
        // Menú Reportes
        JMenu menuReportes = new JMenu("📊 Reportes");
        JMenuItem menuReportePacientes = new JMenuItem("Reporte de pacientes");
        menuReportePacientes.addActionListener(e -> mostrarReportePacientes());
        menuReportes.add(menuReportePacientes);
        JMenuItem menuReporteCitas = new JMenuItem("Reporte de citas");
        menuReporteCitas.addActionListener(e -> mostrarReporteCitas());
        menuReportes.add(menuReporteCitas);
        JMenuItem menuReporteFacturas = new JMenuItem("Reporte de facturas");
        menuReporteFacturas.addActionListener(e -> mostrarReporteFacturas());
        menuReportes.add(menuReporteFacturas);
        
        // Menú Ayuda
        JMenu menuAyuda = new JMenu("❓ Ayuda");
        JMenuItem menuAcerca = new JMenuItem("Acerca de...");
        menuAcerca.setAccelerator(KeyStroke.getKeyStroke("F1"));
        menuAcerca.addActionListener(e -> new DialogoAcercaDe(this));
        menuAyuda.add(menuAcerca);
        
        menuBar.add(menuArchivo);
        menuBar.add(menuReportes);
        menuBar.add(menuAyuda);
        setJMenuBar(menuBar);

        // Panel principal
        setLayout(new BorderLayout());

        // Barra de herramientas superior
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setRollover(true);
        
        JButton btnPacientes = new JButton("👥 Pacientes");
        btnPacientes.addActionListener(e -> tabbedPane.setSelectedIndex(0));
        toolBar.add(btnPacientes);
        
        JButton btnMedicos = new JButton("👨‍⚕️ Médicos");
        btnMedicos.addActionListener(e -> tabbedPane.setSelectedIndex(1));
        toolBar.add(btnMedicos);
        
        JButton btnCitas = new JButton("📅 Citas");
        btnCitas.addActionListener(e -> tabbedPane.setSelectedIndex(2));
        toolBar.add(btnCitas);
        
        JButton btnConsultas = new JButton("📋 Consultas");
        btnConsultas.addActionListener(e -> tabbedPane.setSelectedIndex(3));
        toolBar.add(btnConsultas);
        
        JButton btnFacturas = new JButton("🧾 Facturas");
        btnFacturas.addActionListener(e -> tabbedPane.setSelectedIndex(4));
        toolBar.add(btnFacturas);
        
        JButton btnEstadisticas = new JButton("📊 Estadísticas");
        btnEstadisticas.addActionListener(e -> tabbedPane.setSelectedIndex(5));
        toolBar.add(btnEstadisticas);
        
        toolBar.addSeparator();
        
        JButton btnRefrescarTodo = new JButton("🔄 Refrescar todo");
        btnRefrescarTodo.addActionListener(e -> actualizarTodosLosPaneles());
        toolBar.add(btnRefrescarTodo);
        
        toolBar.add(Box.createHorizontalGlue());
        
        JLabel lblHora = new JLabel(LocalDateTime.now().format(
            java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        toolBar.add(lblHora);
        
        // Timer para actualizar hora
        new Timer(60000, e -> 
            lblHora.setText(LocalDateTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))))
            .start();
        
        add(toolBar, BorderLayout.NORTH);

        // Crear pestañas
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        // Inicializar paneles
        panelPacientes = new PanelPacientes(epsService);
        panelMedicos = new PanelMedicos(epsService);
        panelCitas = new PanelCitas(epsService);
        panelConsultas = new PanelConsultas(epsService);
        panelFacturas = new PanelFacturas(epsService);
        panelEstadisticas = new PanelEstadisticas(epsService);
        
        // Agregar pestañas
        tabbedPane.addTab("👥 Pacientes", new ImageIcon(), panelPacientes, "Gestión de pacientes");
        tabbedPane.addTab("👨‍⚕️ Médicos", new ImageIcon(), panelMedicos, "Gestión de médicos");
        tabbedPane.addTab("📅 Citas", new ImageIcon(), panelCitas, "Gestión de citas");
        tabbedPane.addTab("📋 Consultas", new ImageIcon(), panelConsultas, "Historial de consultas");
        tabbedPane.addTab("🧾 Facturas", new ImageIcon(), panelFacturas, "Gestión de facturas");
        tabbedPane.addTab("📊 Estadísticas", new ImageIcon(), panelEstadisticas, "Estadísticas del sistema");
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Barra de estado
        JPanel panelStatus = new JPanel(new BorderLayout());
        panelStatus.setBorder(BorderFactory.createEtchedBorder());
        panelStatus.setPreferredSize(new Dimension(getWidth(), 28));
        
        statusBar = new JLabel(" ✅ Sistema listo");
        statusBar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        panelStatus.add(statusBar, BorderLayout.WEST);
        
        JLabel lblVersion = new JLabel(" v1.0");
        lblVersion.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        panelStatus.add(lblVersion, BorderLayout.EAST);
        
        add(panelStatus, BorderLayout.SOUTH);
    }

    private void actualizarTodosLosPaneles() {
        panelPacientes.cargarDatos();
        panelMedicos.cargarDatos();
        panelCitas.cargarDatos();
        panelConsultas.cargarDatos();
        panelFacturas.cargarDatos();
        panelEstadisticas.actualizar();
        statusBar.setText(" ✅ Datos actualizados " + 
            LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
    }

    private void mostrarReportePacientes() {
        StringBuilder sb = new StringBuilder();
        sb.append("📊 REPORTE DE PACIENTES\n");
        sb.append("=".repeat(50)).append("\n\n");
        for (MODELO.Paciente p : epsService.getPacientes()) {
            sb.append("ID: ").append(p.getId()).append("\n");
            sb.append("Nombre: ").append(p.getNombreCompleto()).append("\n");
            sb.append("Edad: ").append(p.calcularEdad()).append(" años\n");
            sb.append("Teléfono: ").append(p.getTelefono()).append("\n");
            sb.append("-".repeat(30)).append("\n");
        }
        sb.append("\nTotal: ").append(epsService.getPacientes().size()).append(" pacientes");
        mostrarReporte("Reporte de Pacientes", sb.toString());
    }

    private void mostrarReporteCitas() {
        StringBuilder sb = new StringBuilder();
        sb.append("📊 REPORTE DE CITAS\n");
        sb.append("=".repeat(50)).append("\n\n");
        for (MODELO.Cita c : epsService.getCitas()) {
            sb.append("Cita #").append(c.getIdCita()).append("\n");
            sb.append("Paciente: ").append(c.getPaciente().getNombreCompleto()).append("\n");
            sb.append("Médico: Dr. ").append(c.getMedico().getNombreCompleto()).append("\n");
            sb.append("Estado: ").append(c.getEstadoCita().getNombre()).append("\n");
            sb.append("-".repeat(30)).append("\n");
        }
        sb.append("\nTotal: ").append(epsService.getCitas().size()).append(" citas");
        mostrarReporte("Reporte de Citas", sb.toString());
    }

    private void mostrarReporteFacturas() {
        StringBuilder sb = new StringBuilder();
        sb.append("📊 REPORTE DE FACTURAS\n");
        sb.append("=".repeat(50)).append("\n\n");
        double total = 0;
        for (MODELO.Factura f : epsService.getFacturas()) {
            sb.append("Factura #").append(f.getIdFactura()).append("\n");
            sb.append("Paciente: ").append(f.getPaciente().getNombreCompleto()).append("\n");
            sb.append("Total: $").append(String.format("%.2f", f.getTotal())).append("\n");
            sb.append("Estado: ").append(f.isPagada() ? "PAGADA" : "PENDIENTE").append("\n");
            sb.append("-".repeat(30)).append("\n");
            if (f.isPagada()) total += f.getTotal();
        }
        sb.append("\nTotal facturado: $").append(String.format("%.2f", total));
        mostrarReporte("Reporte de Facturas", sb.toString());
    }

    private void mostrarReporte(String titulo, String contenido) {
        JTextArea textArea = new JTextArea(contenido);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        JOptionPane.showMessageDialog(this, scrollPane, titulo, 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void cargarDatosEjemplo() {
        // Pacientes
        epsService.registrarPaciente("12345678A", "Ana", "García",
                java.time.LocalDate.of(1985, 3, 15), "3111234567",
                "ana.garcia@email.com", "Calle 45 #23-12", "HC-001",
                "O+", "Ninguna", "Ninguno");
        epsService.registrarPaciente("87654321B", "Luis", "Pérez",
                java.time.LocalDate.of(1990, 7, 22), "3117654321",
                "luis.perez@email.com", "Carrera 12 #34-56", "HC-002",
                "A-", "Polen", "Losartán 50mg");
        epsService.registrarPaciente("11111111C", "María", "López",
                java.time.LocalDate.of(1978, 11, 5), "3119876543",
                "maria.lopez@email.com", "Calle 78 #90-12", "HC-003",
                "B+", "Ninguna", "Metformina 850mg");
        epsService.registrarPaciente("22222222D", "Carlos", "Martínez",
                java.time.LocalDate.of(1995, 2, 28), "3114567890",
                "carlos.martinez@email.com", "Carrera 56 #78-90", "HC-004",
                "AB-", "Penicilina", "Ninguno");
        epsService.registrarPaciente("33333333E", "Laura", "Rodríguez",
                java.time.LocalDate.of(1982, 9, 14), "3117890123",
                "laura.rodriguez@email.com", "Calle 34 #56-78", "HC-005",
                "O-", "Ninguna", "Enalapril 10mg");

        // Médicos
        epsService.registrarMedico("98765432C", "Carlos", "Rodríguez",
                java.time.LocalDate.of(1975, 5, 10), "3101234567",
                "carlos.rodriguez@hospital.com", "Calle 56 #78-90",
                "RM-001", ENUMS.Especialidad.MEDICINA_GENERAL, 50000);
        epsService.registrarMedico("54321678D", "Laura", "Martínez",
                java.time.LocalDate.of(1980, 9, 20), "3107654321",
                "laura.martinez@hospital.com", "Carrera 34 #56-78",
                "RM-002", ENUMS.Especialidad.CARDIOLOGIA, 80000);
        epsService.registrarMedico("44444444F", "Juan", "Sánchez",
                java.time.LocalDate.of(1972, 12, 1), "3105432167",
                "juan.sanchez@hospital.com", "Calle 90 #12-34",
                "RM-003", ENUMS.Especialidad.PEDIATRIA, 60000);
        epsService.registrarMedico("55555555G", "Ana", "Gómez",
                java.time.LocalDate.of(1988, 6, 25), "3109876543",
                "ana.gomez@hospital.com", "Carrera 78 #90-12",
                "RM-004", ENUMS.Especialidad.DERMATOLOGIA, 70000);
        epsService.registrarMedico("66666666H", "Pedro", "Díaz",
                java.time.LocalDate.of(1979, 3, 18), "3104567890",
                "pedro.diaz@hospital.com", "Calle 12 #34-56",
                "RM-005", ENUMS.Especialidad.TRAUMOLOGIA, 75000);

        // Citas de ejemplo
        MODELO.Paciente p1 = epsService.buscarPaciente("12345678A");
        MODELO.Paciente p2 = epsService.buscarPaciente("87654321B");
        MODELO.Medico m1 = epsService.buscarMedico("98765432C");
        MODELO.Medico m2 = epsService.buscarMedico("54321678D");
        
        if (p1 != null && m1 != null) {
            epsService.agendarCita(p1.getId(), m1.getId(), 
                java.time.LocalDateTime.now().plusDays(2).withHour(10).withMinute(0),
                "Chequeo general");
        }
        if (p2 != null && m2 != null) {
            epsService.agendarCita(p2.getId(), m2.getId(),
                java.time.LocalDateTime.now().plusDays(3).withHour(14).withMinute(30),
                "Dolor en el pecho");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new MainFrame();
        });
    }
}