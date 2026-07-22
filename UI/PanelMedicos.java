package UI;

import SERVICE.EpsService;
import MODELO.Medico;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class PanelMedicos extends JPanel {
    private EpsService epsService;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnAgregar, btnEditar, btnEliminar, btnRefrescar;

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
        btnAgregar = new JButton("➕ Agregar");
        btnEditar = new JButton("✏️ Editar");
        btnEliminar = new JButton("🗑️ Eliminar");
        btnRefrescar = new JButton("🔄 Refrescar");

        btnAgregar.addActionListener(e -> agregarMedico());
        btnEditar.addActionListener(e -> editarMedico());
        btnEliminar.addActionListener(e -> eliminarMedico());
        btnRefrescar.addActionListener(e -> cargarDatos());

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnRefrescar);
        panelSuperior.add(panelBotones, BorderLayout.EAST);

        add(panelSuperior, BorderLayout.NORTH);

        // Tabla
        String[] columnas = {"ID", "Nombre Completo", "Especialidad", "Registro", "Honorarios", "Teléfono"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editarMedico();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Panel inferior
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblEstadisticas = new JLabel("Total médicos: 0");
        panelInferior.add(lblEstadisticas);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void cargarDatos() {
        tableModel.setRowCount(0);
        List<Medico> medicos = epsService.getMedicos();
        for (Medico m : medicos) {
            tableModel.addRow(new Object[]{
                m.getId(),
                "Dr. " + m.getNombreCompleto(),
                m.getEspecialidad().getNombre(),
                m.getRegistroMedico(),
                String.format("$%.2f", m.getHonorarios()),
                m.getTelefono()
            });
        }
        JPanel panelInferior = (JPanel) getComponent(2);
        JLabel lblEstadisticas = (JLabel) panelInferior.getComponent(0);
        lblEstadisticas.setText("Total médicos: " + medicos.size());
    }

    private void agregarMedico() {
        DialogoMedico dialogo = new DialogoMedico(SwingUtilities.getWindowAncestor(this), epsService);
        dialogo.setVisible(true);
        cargarDatos();
    }

    private void editarMedico() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un médico", 
                "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String id = (String) tableModel.getValueAt(selectedRow, 0);
        Medico medico = epsService.buscarMedico(id);
        if (medico != null) {
            DialogoMedico dialogo = new DialogoMedico(
                SwingUtilities.getWindowAncestor(this), epsService, medico);
            dialogo.setVisible(true);
            cargarDatos();
        }
    }

    private void eliminarMedico() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un médico", 
                "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String id = (String) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de eliminar al médico con ID: " + id + "?",
            "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            // Implementar eliminación en EpsService
            // epsService.eliminarMedico(id);
            cargarDatos();
        }
    }
}