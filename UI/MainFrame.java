package UI;

import javax.swing.*;
import java.awt.*;
import SERVICE.EpsService;

public class MainFrame extends JFrame {
    private EpsService epsService;
    private JTabbedPane tabbedPane;

    private PanelPacientes panelPacientes;
    private PanelMedicos panelMedicos;
    private PanelCitas panelCitas;
    private PanelConsultorios panelConsultorios;
    private PanelFacturas panelFacturas;

    public MainFrame() {
        epsService = new EpsService();
        initComponents();
        setTitle("Sistema de Gestión Hospitalaria/EPS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuArchivo = new JMenu("Archivo");
        JMenu menuSalir = new JMenu("Salir");
        menuSalir.addActionListener(e -> System.exit(0));
        menuArchivo.add(menuSalir);

        JMenu menuAyuda = new JMenu("Ayuda");
        JMenuItem menuAcerca = new JMenuItem("Acerca de");
        menuAcerca.addActionListener(e -> JOptionPane.showMessageDialog(this, "Sistema de Gestión Hospitalaria/EPS\nVersión 1.0", 
        "Acerca de", JOptionPane.INFORMATION_MESSAGE));
        menuAyuda.add(menuAcerca);

        menuBar.add(menuArchivo);
        menuBar.add(menuAyuda);
        setJMenuBar(menuBar);

        panelPacientes = new PanelPacientes(epsService);
        panelMedicos = new PanelMedicos(epsService);
        panelCitas = new PanelCitas(epsService);
        panelConsultorios = new PanelConsultorios(epsService);
        panelFacturas = new PanelFacturas(epsService);

        tabbedPane.addTab("Pacientes", panelPacientes);
        tabbedPane.addTab("Médicos", panelMedicos);
        tabbedPane.addTab("Citas", panelCitas);
        tabbedPane.addTab("Consultorios", panelConsultorios);
        tabbedPane.addTab("Facturas", panelFacturas);

        add(tabbedPane, BorderLayout.CENTER);

        JLabel statusBar = new JLabel("Listo");
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        add(statusBar, BorderLayout.SOUTH);
    }

     private void cargarDatosEjemplo() {
        // Datos de ejemplo (mismo que antes)
        epsService.registrarPaciente("12345678A", "Ana", "García",
                java.time.LocalDate.of(1985, 3, 15), "3111234567",
                "ana@email.com", "Calle 45 #23-12", "HC-001",
                "O+", "Ninguna", "Ninguno");
        epsService.registrarPaciente("87654321B", "Luis", "Pérez",
                java.time.LocalDate.of(1990, 7, 22), "3117654321",
                "luis@email.com", "Carrera 12 #34-56", "HC-002",
                "A-", "Polen", "Losartán");
        
        epsService.registrarMedico("98765432C", "Carlos", "Rodríguez",
                java.time.LocalDate.of(1975, 5, 10), "3101234567",
                "carlos@email.com", "Calle 56 #78-90",
                "RM-001", ENUMS.Especialidad.MEDICINA_GENERAL, 50000);
        epsService.registrarMedico("54321678D", "Laura", "Martínez",
                java.time.LocalDate.of(1980, 9, 20), "3107654321",
                "laura@email.com", "Carrera 34 #56-78",
                "RM-002", ENUMS.Especialidad.CARDIOLOGIA, 80000);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new MainFrame();// Cargar datos de ejemplo al iniciar la aplicación
        });
    }
}