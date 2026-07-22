package UI;

import SERVICE.EpsService;
import MODELO.Consulta;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PanelConsultas extends JPanel {
    private EpsService epsService;
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JButton btnRegistrar, btnVerDetalle, btnRefrescar;
    private JTextField txtBuscar;
    private JLabel lblTotal;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public PanelConsultas(EpsService epsService) {
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
        JLabel lblTitulo = new JLabel("📋 Historial de Consultas");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(0, 102, 204));
        panelTitulo.add(lblTitulo);
        panelSuperior.add(panelTitulo, BorderLayout.WEST);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        
        txtBuscar = new JTextField(15);
        txtBuscar.setToolTipText("Buscar por paciente o diagnóstico");
        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                filtrar();
            }
        });
        panelBotones.add(new JLabel("🔍"));
        panelBotones.add(txtBuscar);
        
        btnRegistrar = crearBoton("📝 Registrar Consulta", new Color(46, 204, 113));
        btnVerDetalle = crearBoton("🔍 Ver Detalle", new Color(52, 152, 219));
        btnRefrescar = crearBoton("🔄 Refrescar", new Color(149, 165, 166));
        
        btnRegistrar.addActionListener(e -> registrarConsulta());
        btnVerDetalle.addActionListener(e -> verDetalle());
        btnRefrescar.addActionListener(e -> {
            txtBuscar.setText("");
            cargarDatos();
        });

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnVerDetalle);
        panelBotones.add(btnRefrescar);
        panelSuperior.add(panelBotones, BorderLayout.EAST);

        add(panelSuperior, BorderLayout.NORTH);

        // Tabla
        String[] columnas = {"ID", "Fecha", "Paciente", "Médico", "Diagnóstico", "Tratamiento"};
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
        
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Panel inferior
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        lblTotal = new JLabel("Total consultas: 0");
        lblTotal.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelInferior.add(lblTotal, BorderLayout.WEST);
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
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto, 2, 4));
        }
    }

    public void cargarDatos() {
        tableModel.setRowCount(0);
        List<Consulta> consultas = epsService.getConsultas();
        for (Consulta c : consultas) {
            tableModel.addRow(new Object[]{
                c.getIdConsulta(),
                c.getFecha().format(formatter),
                c.getCita().getPaciente().getNombreCompleto(),
                "Dr. " + c.getCita().getMedico().getNombreCompleto(),
                c.getDiagnostico().length() > 30 ? 
                    c.getDiagnostico().substring(0, 30) + "..." : 
                    c.getDiagnostico(),
                c.getTratamiento().length() > 30 ? 
                    c.getTratamiento().substring(0, 30) + "..." : 
                    c.getTratamiento()
            });
        }
        lblTotal.setText("Total consultas: " + consultas.size());
    }

    private void registrarConsulta() {
        DialogoConsulta dialogo = new DialogoConsulta(
            SwingUtilities.getWindowAncestor(this), epsService);
        dialogo.setVisible(true);
        cargarDatos();
    }

    private void verDetalle() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una consulta", 
                "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(selectedRow);
        int idConsulta = (int) tableModel.getValueAt(modelRow, 0);
        Consulta consulta = epsService.buscarConsulta(idConsulta);
        if (consulta != null) {
            JTextArea textArea = new JTextArea(consulta.getResumen());
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
            textArea.setBackground(new Color(245, 245, 245));
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(600, 450));
            JOptionPane.showMessageDialog(this, scrollPane, 
                "📋 Detalle de Consulta #" + idConsulta, 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
}