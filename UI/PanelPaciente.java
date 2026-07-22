package UI;

import SERVICE.EpsService;
import MODELO.Paciente;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelPaciente extends JPanel {
    private EpsService epsService;
    private JTable table;
    private DefaultTableModel tableModel;  // ✅ DECLARADO
    private JLabel lblEstadisticas;

    public PanelPaciente(EpsService epsService) {
        this.epsService = epsService;
        initComponents();
        cargarDatos();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel superior
        JPanel panelSuperior = new JPanel(new BorderLayout());
        JLabel lblTitulo = new JLabel("Gestión de Pacientes", JLabel.LEFT);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        panelSuperior.add(lblTitulo, BorderLayout.WEST);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAgregar = new JButton("➕ Agregar");
        JButton btnRefrescar = new JButton("🔄 Refrescar");
        
        btnAgregar.addActionListener(e -> agregarPaciente());
        btnRefrescar.addActionListener(e -> cargarDatos());
        
        panelBotones.add(btnAgregar);
        panelBotones.add(btnRefrescar);
        panelSuperior.add(panelBotones, BorderLayout.EAST);
        add(panelSuperior, BorderLayout.NORTH);

        // ✅ IMPORTANTE: Inicializar el modelo ANTES de crear la tabla
        String[] columnas = {"ID", "Nombre Completo", "Edad", "HC", "Tipo Sangre", "Teléfono"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Panel inferior
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblEstadisticas = new JLabel("Total pacientes: 0");
        panelInferior.add(lblEstadisticas);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void cargarDatos() {
        // ✅ VERIFICAR QUE EL MODELO NO SEA NULO
        if (tableModel == null) {
            System.err.println("❌ Error: tableModel es null");
            return;
        }
        
        // ✅ VERIFICAR QUE EPS SERVICE NO SEA NULO
        if (epsService == null) {
            System.err.println("❌ Error: epsService es null");
            return;
        }
        
        try {
            // LIMPIAR TABLA
            tableModel.setRowCount(0);
            
            // OBTENER DATOS
            List<Paciente> pacientes = epsService.getPacientes();
            
            // ✅ VERIFICAR QUE LA LISTA NO SEA NULL
            if (pacientes == null) {
                System.err.println("⚠️ Lista de pacientes es null");
                lblEstadisticas.setText("Total pacientes: 0");
                return;
            }
            
            // AGREGAR DATOS A LA TABLA
            for (Paciente p : pacientes) {
                tableModel.addRow(new Object[]{
                    p.getId(),
                    p.getNombreCompleto(),
                    p.calcularEdad(),
                    p.getHistorialClinica(),
                    p.getTipoSangre(),
                    p.getTelefono()
                });
            }
            
            // ACTUALIZAR ESTADÍSTICAS
            lblEstadisticas.setText("Total pacientes: " + pacientes.size());
            
        } catch (Exception e) {
            System.err.println("❌ Error al cargar pacientes: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error al cargar pacientes:\n" + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarPaciente() {
        // TODO: Implementar diálogo para agregar paciente
        JOptionPane.showMessageDialog(this, "Función en desarrollo");
    }
}