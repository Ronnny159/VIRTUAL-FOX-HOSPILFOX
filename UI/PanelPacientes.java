package UI;

import SERVICE.EpsService;
import MODELO.Paciente;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PanelPacientes extends JPanel {
    private EpsService epsService;
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JButton btnAgregar, btnEditar, btnEliminar, btnRefrescar, btnBuscar;
    private JTextField txtBuscar;
    private JLabel lblTotal;

    public PanelPacientes(EpsService epsService) {
        this.epsService = epsService;
        initComponents();
        cargarDatos();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel superior
        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblTitulo = new JLabel("👥 Gestión de Pacientes");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(0, 102, 204));
        panelTitulo.add(lblTitulo);
        panelSuperior.add(panelTitulo, BorderLayout.WEST);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        
        // Campo de búsqueda
        txtBuscar = new JTextField(15);
        txtBuscar.setToolTipText("Buscar por ID o nombre");
        txtBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrar();
            }
        });
        panelBotones.add(new JLabel("🔍"));
        panelBotones.add(txtBuscar);
        
        btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> filtrar());
        panelBotones.add(btnBuscar);
        
        panelBotones.add(new JSeparator(SwingConstants.VERTICAL));
        
        btnAgregar = crearBoton("➕ Agregar", new Color(46, 204, 113));
        btnEditar = crearBoton("✏️ Editar", new Color(52, 152, 219));
        btnEliminar = crearBoton("🗑️ Eliminar", new Color(231, 76, 60));
        btnRefrescar = crearBoton("🔄 Refrescar", new Color(149, 165, 166));
        
        btnAgregar.addActionListener(e -> agregarPaciente());
        btnEditar.addActionListener(e -> editarPaciente());
        btnEliminar.addActionListener(e -> eliminarPaciente());
        btnRefrescar.addActionListener(e -> {
            txtBuscar.setText("");
            cargarDatos();
        });

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnRefrescar);
        panelSuperior.add(panelBotones, BorderLayout.EAST);

        add(panelSuperior, BorderLayout.NORTH);

        // Tabla
        String[] columnas = {"ID", "Nombre Completo", "Edad", "Historia Clínica", "Tipo Sangre", "Teléfono", "Email"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(52, 73, 94));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        
        // Sorter para búsqueda
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        
        // Doble clic para editar
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    editarPaciente();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // Panel inferior
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        
        lblTotal = new JLabel("Total pacientes: 0");
        lblTotal.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelInferior.add(lblTotal, BorderLayout.WEST);
        
        JButton btnExportar = new JButton("📤 Exportar CSV");
        btnExportar.addActionListener(e -> exportarCSV());
        panelInferior.add(btnExportar, BorderLayout.EAST);
        
        add(panelInferior, BorderLayout.SOUTH);
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void filtrar() {
        String texto = txtBuscar.getText().trim();
        if (texto.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto, 0, 1));
        }
    }

    public void cargarDatos() {
        tableModel.setRowCount(0);
        List<Paciente> pacientes = epsService.getPacientes();
        for (Paciente p : pacientes) {
            tableModel.addRow(new Object[]{
                p.getId(),
                p.getNombreCompleto(),
                p.calcularEdad(),
                p.getHistorialClinica(),
                p.getTipoSangre(),
                p.getTelefono(),
                p.getEmail()
            });
        }
        lblTotal.setText("Total pacientes: " + pacientes.size());
        if (!txtBuscar.getText().isEmpty()) {
            filtrar();
        }
    }

    private void agregarPaciente() {
        DialogoPaciente dialogo = new DialogoPaciente(
            SwingUtilities.getWindowAncestor(this), epsService);
        dialogo.setVisible(true);
        cargarDatos();
    }

    private void editarPaciente() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Seleccione un paciente para editar", 
                "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Convertir índice de vista a modelo
        int modelRow = table.convertRowIndexToModel(selectedRow);
        String id = (String) tableModel.getValueAt(modelRow, 0);
        Paciente paciente = epsService.buscarPaciente(id);
        if (paciente != null) {
            DialogoPaciente dialogo = new DialogoPaciente(
                SwingUtilities.getWindowAncestor(this), epsService, paciente);
            dialogo.setVisible(true);
            cargarDatos();
        }
    }

    private void eliminarPaciente() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Seleccione un paciente para eliminar", 
                "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(selectedRow);
        String id = (String) tableModel.getValueAt(modelRow, 0);
        String nombre = (String) tableModel.getValueAt(modelRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de eliminar al paciente:\n" + id + " - " + nombre + "?",
            "Confirmar eliminación", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            epsService.eliminarPaciente(id);
            cargarDatos();
            JOptionPane.showMessageDialog(this, 
                "✅ Paciente eliminado exitosamente");
        }
    }

    private void exportarCSV() {
        // Implementar exportación a CSV
        JOptionPane.showMessageDialog(this, 
            "Función de exportación en desarrollo", 
            "Exportar CSV", JOptionPane.INFORMATION_MESSAGE);
    }
}