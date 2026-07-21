package UI;

import SERVICE.EpsService;
import MODELO.Cita;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class PanelCitas extends JPanel {
    private EpsService epsService;
    private JTable tableCitas;
    private DefaultTableModel tableModel;
    private JButton btnAgendar, btnConfirmar, btnCancelar, btnRefrescar, btnInciar;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public PanelCitas(EpsService epsService) {
        this.epsService = epsService;
        initComponents();
        cargarDatos();
    }

    public void initComponents(){
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JPanel panelSuperior = new JPanel(new BorderLayout());
        JLabel lblTitulo = new JLabel("Gestión de Citas", JLabel.LEFT);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        panelSuperior.add(lblTitulo, BorderLayout.WEST);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAgendar = new JButton("Agendar");
        btnConfirmar = new JButton("Confirmar");
        btnCancelar = new JButton("Cancelar");
        btnInciar = new JButton("Iniciar");
        btnRefrescar = new JButton("Refrescar");

        btnAgendar.addActionListener(e -> agendarCita());
        btnConfirmar.addActionListener(e -> confirmarCita());  
        btnCancelar.addActionListener(e -> cancelarCita());
        btnInciar.addActionListener(e -> iniciarCita());
        btnRefrescar.addActionListener(e -> cargarDatos());

        panelBotones.add(btnAgendar);
        panelBotones.add(btnConfirmar);
        panelBotones.add(btnCancelar);
        panelBotones.add(btnInciar);
        panelBotones.add(btnRefrescar);
        panelSuperior.add(panelBotones, BorderLayout.EAST);

        add(panelSuperior, BorderLayout.NORTH);

        String[] columnas = {"ID", "Paciente", "Medico", "Fecha y Hora", "Estado"};
        tableModel = new DefaultTableModel(columnas, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableCitas = new JTable(tableModel);
        tableCitas.setRowHeight(25);
        tableCitas.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tableCitas.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer(){
            @Override
            public java.awt.Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, 
                boolean hasFocus, int row, int column) {
                java.awt.Component c = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
                String estado = (String) table.getValueAt(row, 5);
                if(!isSelected){
                    switch (estado) {
                        case "COMPLETADA": c.setBackground(new Color(200,255,200)); break;
                        case "CANCELADA": c.setBackground(new Color(200,255,200)); break;
                        case "NO_ASISTIO": c.setBackground(new Color(200,255,200)); break;
                        case "EN_CURSO": c.setBackground(new Color(200,255,200)); break;
                        default: c.setBackground(Color.WHITE);
                    }

                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(tableCitas);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblEstadisticas = new JLabel("Total de Citas: 0");
        panelInferior.add(lblEstadisticas);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void cargarDatos(){
        List<Cita> citas = epsService.getCitas();
        tableModel.setRowCount(0);
        for(Cita c: citas){
            Object[] fila = {
                c.getIdCita(), c.getPaciente().getNombreCompleto(), c.getMedico().getNombreCompleto(),
                c.getFechaHora().format(formatter), c.getEstadoCita().name()};
            tableModel.addRow(fila);
        }
        JPanel panelInferior = (JPanel) getComponent(2);
        JLabel lblEstadisticas = (JLabel) panelInferior.getComponent(0);
        lblEstadisticas.setText("Total de Citas: " + citas.size());
    }

    private void agendarCita(){
        DialogoCita dialogo = new DialogoCita(SwingUtilities.getWindowAncestor(this), epsService);
        dialogo.setVisible(true);
        cargarDatos();
    }

    private void confirmarCita(){
        int selectedRow = tableCitas.getSelectedRow();
        if(selectedRow == -1){
            JOptionPane.showMessageDialog(this,"Seleccione una cita", 
            "error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idCita = (int) tableModel.getValueAt(selectedRow, 0);
        epsService.confirmarCita(idCita);
        cargarDatos();
    }

    private void cancelarCita(){
        int selectedRow = tableCitas.getSelectedRow();
        if(selectedRow == -1){
            JOptionPane.showMessageDialog(this,"Seleccione una cita", 
            "error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idCita = (int) tableModel.getValueAt(selectedRow, 0);
        epsService.cancelarCita(idCita);
        cargarDatos();
    }

    private void iniciarCita(){
        int selectedRow = tableCitas.getSelectedRow();
        if(selectedRow == -1){
            JOptionPane.showMessageDialog(this,"Seleccione una cita", 
            "error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idCita = (int) tableModel.getValueAt(selectedRow, 0);
        epsService.iniciarCita(idCita);
        cargarDatos();
    }
}
