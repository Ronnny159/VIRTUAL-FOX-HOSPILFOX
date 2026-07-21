package UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import SERVICE.EpsService;
import MODELO.Paciente;
import java.util.List;

public class PanelPaciente extends JPanel{
    private EpsService epsService;
    private JTable tablePacientes;
    private DefaultTableModel tableModel;
    private JButton btnAgregar, btnEditar, btnEliminar, btnRefrescar;

    public PanelPaciente(EpsService epsService){
        this.epsService = epsService;
        initComponents();
        cargarDatos();
    }

    private void initComponents(){
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JPanel panelSuperior = new JPanel(new BorderLayout());
        JLabel lblTitulo = new JLabel("Gestión de Pacientes", JLabel.LEFT);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        panelSuperior.add(lblTitulo, BorderLayout.WEST);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAgregar = new JButton("Agregar");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnRefrescar = new JButton("Refrescar");

        btnAgregar.addActionListener(e -> agregarPaciente());
        btnEditar.addActionListener(e -> editarPaciente());
        btnEliminar.addActionListener(e -> eliminarPaciente());
        btnRefrescar.addActionListener(e -> cargarDatos());

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnRefrescar);
        panelSuperior.add(panelBotones, BorderLayout.EAST);

        add(panelSuperior, BorderLayout.NORTH);

        String[] columnas ={"ID", "Nombre", "Apellido", "Fecha de Nacimiento", "Teléfono", "Email", "Dirección", "Historia Clínica", "Tipo de Sangre", "Alergias", "Medicamentos Actuales"};
        tableModel = new DefaultTableModel(columnas, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablePacientes = new JTable(tableModel);
        tablePacientes.setRowHeight(25);
        tablePacientes.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tablePacientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount() == 2){
                    editarPaciente();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablePacientes);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblEstadisticas = new JLabel("Total de Pacientes: 0");
        panelInferior.add(lblEstadisticas);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void cargarDatos(){
        List<Paciente> pacientes = epsService.getPacientes();
        tableModel.setRowCount(0);
        for(Paciente p: pacientes){
            Object[] fila = {
                p.getId(), p.getNombreCompleto(), p.calcularEdad(), p.getHistorialClinica(),
                p.getTipoSangre(), p.getTelefono()};
            tableModel.addRow(fila);
        }
        JPanel panelInferior = (JPanel) getComponent(2);
        JLabel lblEstadisticas = (JLabel) panelInferior.getComponent(0);
        lblEstadisticas.setText("Total de Pacientes: " + pacientes.size());
    }

    private void agregarPaciente(){
        DialogoPaciente dialogo = new DialogoPaciente(SwingUtilities.getWindowAncestor(this), epsService);
        dialogo.setVisible(true);
        cargarDatos();
    }

    private void editarPaciente(){
        int selectedRow = tablePacientes.getSelectedRow();
        if(selectedRow == -1){
            JOptionPane.showMessageDialog(this,"Seleccione un paciente", "error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String id = (String) tableModel.getValueAt(selectedRow, 0);
        Paciente paciente = epsService.buscarPaciente(id);
        if(paciente != null){
            DialogoPaciente dialogo = new DialogoPaciente(SwingUtilities.getWindowAncestor(this), epsService, paciente);
            dialogo.setVisible(true);
            cargarDatos();
        }
    }

    private void eliminarPaciente(){
        int selectedRow = tablePacientes.getSelectedRow();
        if(selectedRow == -1){
            JOptionPane.showMessageDialog(this,"Seleccione un paciente", "error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String id = (String) tableModel.getValueAt(selectedRow, 0);
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar el paciente con ID: " + id + "?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if(confirmacion == JOptionPane.YES_OPTION){
            epsService.eliminarPaciente(id);
            cargarDatos();
        }
    }
}
