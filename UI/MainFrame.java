package UI;

import javax.swing.*;
import java.awt.*;
import SERVICE.EpsService;
import ENUMS.Especialidad;

public class MainFrame extends JFrame {
    private EpsService epsService;
    private JTabbedPane tabbedPane;
    private PanelPaciente panelPacientes;
    private PanelMedicos panelMedicos;
    private PanelCitas panelCitas;
    private PanelConsultas panelConsultorios;
    private PanelFacturas panelFacturas;

    public MainFrame() {
        try {
            epsService = new EpsService();
            initComponents();
            cargarDatosEjemplo();
            configurarVentana();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Error al iniciar la aplicación:\n" + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void configurarVentana() {
        setTitle("🏥 Sistema de Gestión Hospitalaria / EPS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        // Barra de menú
        JMenuBar menuBar = new JMenuBar();
        
        // Menú Archivo
        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem menuSalir = new JMenuItem("Salir");
        menuSalir.addActionListener(e -> System.exit(0));
        menuArchivo.add(menuSalir);
        
        // Menú Ayuda
        JMenu menuAyuda = new JMenu("Ayuda");
        JMenuItem menuAcerca = new JMenuItem("Acerca de");
        menuAcerca.addActionListener(e -> mostrarAcercaDe());
        menuAyuda.add(menuAcerca);
        
        menuBar.add(menuArchivo);
        menuBar.add(menuAyuda);
        setJMenuBar(menuBar);

        // Panel de pestañas
        tabbedPane = new JTabbedPane();
        
        // Inicializar paneles
        panelPacientes = new PanelPaciente(epsService);
        panelMedicos = new PanelMedicos(epsService);
        panelCitas = new PanelCitas(epsService);
        panelConsultorios = new PanelConsultas(epsService);
        panelFacturas = new PanelFacturas(epsService);
        
        // Agregar pestañas
        tabbedPane.addTab("👥 Pacientes", panelPacientes);
        tabbedPane.addTab("👨‍⚕️ Médicos", panelMedicos);
        tabbedPane.addTab("📅 Citas", panelCitas);
        tabbedPane.addTab("📋 Consultas", panelConsultorios);
        tabbedPane.addTab("🧾 Facturas", panelFacturas);
        
        add(tabbedPane, BorderLayout.CENTER);

        // Barra de estado
        JLabel statusBar = new JLabel("✅ Sistema listo");
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        add(statusBar, BorderLayout.SOUTH);
    }

    private void mostrarAcercaDe() {
        String mensaje = """
            🏥 Sistema de Gestión Hospitalaria / EPS
            Versión 1.0
            © 2024
            
            Desarrollado con Java Swing
            """;
        JOptionPane.showMessageDialog(this, mensaje, 
            "Acerca de", JOptionPane.INFORMATION_MESSAGE);
    }

    private void cargarDatosEjemplo() {
        // Pacientes
        epsService.registrarPaciente("12345678A", "Ana", "García",
                java.time.LocalDate.of(1985, 3, 15), "3111234567",
                "ana@email.com", "Calle 45 #23-12", "HC-001",
                "O+", "Ninguna", "Ninguno");
        
        epsService.registrarPaciente("87654321B", "Luis", "Pérez",
                java.time.LocalDate.of(1990, 7, 22), "3117654321",
                "luis@email.com", "Carrera 12 #34-56", "HC-002",
                "A-", "Polen", "Losartán");
        
        epsService.registrarPaciente("11111111C", "María", "López",
                java.time.LocalDate.of(1978, 11, 5), "3119876543",
                "maria@email.com", "Calle 78 #90-12", "HC-003",
                "B+", "Ninguna", "Metformina");

        // Médicos
        epsService.registrarMedico("98765432C", "Carlos", "Rodríguez",
                java.time.LocalDate.of(1975, 5, 10), "3101234567",
                "carlos@email.com", "Calle 56 #78-90",
                "RM-001", Especialidad.MEDICINA_GENERAL, 50000);
        
        epsService.registrarMedico("54321678D", "Laura", "Martínez",
                java.time.LocalDate.of(1980, 9, 20), "3107654321",
                "laura@email.com", "Carrera 34 #56-78",
                "RM-002", Especialidad.CARDIOLOGIA, 80000);
        
        epsService.registrarMedico("22222222E", "Juan", "Sánchez",
                java.time.LocalDate.of(1972, 12, 1), "3105432167",
                "juan@email.com", "Calle 90 #12-34",
                "RM-003", Especialidad.PEDIATRIA, 60000);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("⚠️ Error al configurar Look and Feel: " + e.getMessage());
            }
            new MainFrame();
        });
    }
}