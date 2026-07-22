package UI;

import SERVICE.EpsService;
import MODELO.Cita;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PanelCitas extends JPanel {
    private EpsService epsService;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel lblEstadisticas;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public PanelCitas(EpsService epsService) {
        this.epsService = epsService;
        initComponents();
        cargarDatos();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelSuperior = new JPanel(new BorderLayout());
        JLabel lblTitulo = new JLabel("Gestión de Citas", JLabel.LEFT);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        panelSuperior.add(lblTitulo, BorderLayout.WEST);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAgendar = new JButton("📅 Agendar");
        JButton btnRefrescar = new JButton("🔄 Refrescar");
        
        btnAgendar.addActionListener(e -> agendarCita());
        btnRefrescar.addActionListener(e -> cargarDatos());
        
        panelBotones.add(btnAgendar);
        panelBotones.add(btnRefrescar);
        panelSuperior.add(panelBotones, BorderLayout.EAST);
        add(panelSuperior, BorderLayout.NORTH);

        // ✅ INICIALIZAR MODELO
        String[] columnas = {"ID", "Fecha/Hora", "Paciente", "Médico", "Especialidad", "Estado"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblEstadisticas = new JLabel("Total citas: 0");
        panelInferior.add(lblEstadisticas);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void cargarDatos() {
        if (tableModel == null || epsService == null) {
            System.err.println("❌ Error: tableModel o epsService es null");
            return;
        }
        
        try {
            tableModel.setRowCount(0);
            List<Cita> citas = epsService.getCitas();
            
            if (citas == null) {
                System.err.println("⚠️ Lista de citas es null");
                lblEstadisticas.setText("Total citas: 0");
                return;
            }
            
            for (Cita c : citas) {
                tableModel.addRow(new Object[]{
                    c.getIdCita(),
                    c.getFechaHora().format(formatter),
                    c.getPaciente().getNombreCompleto(),
                    "Dr. " + c.getMedico().getNombreCompleto(),
                    c.getMedico().getEspecialidad().getNombre(),
                    c.getEstadoCita().getNombre()
                });
            }
            
            lblEstadisticas.setText("Total citas: " + citas.size());
            
        } catch (Exception e) {
            System.err.println("❌ Error al cargar citas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void agendarCita() {
        JOptionPane.showMessageDialog(this, "Función en desarrollo");
    }
}