package UI;

import SERVICE.EpsService;
import javax.swing.*;
import MODELO.Consulta;
import MODELO.Cita;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PanelConsultas extends JPanel {
    private EpsService epsService;
    private JTable tableConsultas;
    private DefaultTableModel tableModel;
    private JButton btnRegistrar, btnVerDetalles, btnRefrescar;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public PanelConsultas(EpsService epsService) {
        this.epsService = epsService;
        initComponents();
        cargarDatos();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelSuperior = new JPanel(new BorderLayout());
        JLabel lblTitulo = new JLabel("Gestión de Consultas", JLabel.LEFT);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        panelSuperior.add(lblTitulo, BorderLayout.WEST);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnRegistrar = new JButton("Registrar");
        btnVerDetalles = new JButton("Ver Detalles");
        btnRefrescar = new JButton("Refrescar");

        btnRegistrar.addActionListener(e -> registrarConsulta());
        btnVerDetalles.addActionListener(e -> verDetallesConsulta());
        btnRefrescar.addActionListener(e -> cargarDatos());

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnVerDetalles);
        panelBotones.add(btnRefrescar);
        panelSuperior.add(panelBotones, BorderLayout.EAST);

        add(panelSuperior, BorderLayout.NORTH);

        String[] columnas = {"ID", "Cita ID", "Paciente", "Medico", "Fecha y Hora", "Diagnóstico", "Tratamiento"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableConsultas = new JTable(tableModel);
        tableConsultas.setRowHeight(25);
        tableConsultas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(tableConsultas);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblEstadisticas = new JLabel("Total de Consultas: 0");
        panelInferior.add(lblEstadisticas);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void cargarDatos() {
        tableModel.setRowCount(0);
        List<Consulta> consultas = epsService.getConsultas();
        for (Consulta consulta : consultas) {
            Object[] fila = {
                    consulta.getIdConsulta(),
                    consulta.getFecha().format(formatter),
                    consulta.getCita().getPaciente().getNombreCompleto(),
                    "Dr. "+ consulta.getCita().getMedico().getNombreCompleto(),
                    consulta.getDiagnostico().length() > 30 ? 
                    consulta.getDiagnostico().substring(0, 20) + "..." : 
                    consulta.getDiagnostico(),
            };
            tableModel.addRow(fila);
        }
        JPanel panelInferior = (JPanel) getComponent(2);
        JLabel lblEstadisticas = (JLabel) panelInferior.getComponent(0);
        lblEstadisticas.setText("Total de Consultas: " + consultas.size());
    }

    private void registrarConsulta() {
        DialogoConsulta dialogo = new DialogoConsulta(SwingUtilities.getWindowAncestor(this), epsService);
        dialogo.setVisible(true);
        cargarDatos();
    }

    private void verDetallesConsulta() {
        int selectedRow = tableConsultas.getSelectedRow();
        if(selectedRow != -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una consulta.", 
            "Error", JOptionPane.WARNING_MESSAGE);
        }
        int consultaId = (int) tableModel.getValueAt(selectedRow, 0);
        Consulta consulta = epsService.buscarConsulta(consultaId);
        if(consulta !=null){
            JTextArea txtArea = new JTextArea(consulta.getResumen());
            txtArea.setEditable(false);
            txtArea.setFont(new Font("monospaced", Font.PLAIN, 12));
            JScrollPane scrollPane = new JScrollPane(txtArea);
            scrollPane.setPreferredSize(new Dimension(500, 400));
            JOptionPane.showMessageDialog(this, scrollPane, "Detalle de la consulta #"+consultaId,
             JOptionPane.INFORMATION_MESSAGE);
        }
    }

}
