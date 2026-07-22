package UI;

import SERVICE.EpsService;
import MODELO.Medico;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelMedicos extends JPanel {
    private EpsService epsService;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel lblEstadisticas;

    public PanelMedicos(EpsService epsService) {
        this.epsService = epsService;
        initComponents();
        cargarDatos();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel superior
        JPanel panelSuperior = new JPanel(new BorderLayout());
        JLabel lblTitulo = new JLabel("Gestión de Médicos", JLabel.LEFT);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        panelSuperior.add(lblTitulo, BorderLayout.WEST);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAgregar = new JButton("➕ Agregar");
        JButton btnRefrescar = new JButton("🔄 Refrescar");
        
        btnAgregar.addActionListener(e -> agregarMedico());
        btnRefrescar.addActionListener(e -> cargarDatos());
        
        panelBotones.add(btnAgregar);
        panelBotones.add(btnRefrescar);
        panelSuperior.add(panelBotones, BorderLayout.EAST);
        add(panelSuperior, BorderLayout.NORTH);

        // ✅ INICIALIZAR MODELO
        String[] columnas = {"ID", "Nombre", "Especialidad", "Registro", "Teléfono", "Disponible"};
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

        // Panel inferior
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblEstadisticas = new JLabel("Total médicos: 0");
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
            List<Medico> medicos = epsService.getMedicos();
            
            if (medicos == null) {
                System.err.println("⚠️ Lista de médicos es null");
                lblEstadisticas.setText("Total médicos: 0");
                return;
            }
            
            for (Medico m : medicos) {
                tableModel.addRow(new Object[]{
                    m.getId(),
                    m.getNombreCompleto(),
                    m.getEspecialidad().getNombre(),
                    m.getRegistroMedico(),
                    m.getTelefono(),
                    m.isDisponible() ? "✅ Sí" : "❌ No"
                });
            }
            
            lblEstadisticas.setText("Total médicos: " + medicos.size());
            
        } catch (Exception e) {
            System.err.println("❌ Error al cargar médicos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void agregarMedico() {
        JOptionPane.showMessageDialog(this, "Función en desarrollo");
    }
}