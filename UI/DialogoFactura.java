// src/ui/DialogoFactura.java
package UI;

import SERVICE.EpsService;
import MODELO.Paciente;
import ENUMS.TipoServicio;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DialogoFactura extends JDialog {
    private EpsService epsService;
    private JComboBox<Paciente> cmbPaciente;
    private JList<TipoServicio> listServicios;
    private DefaultListModel<TipoServicio> listModel;
    private JButton btnAgregar, btnQuitar, btnGuardar, btnCancelar;

    public DialogoFactura(Window parent, EpsService epsService) {
        super(parent, "Generar Factura", ModalityType.APPLICATION_MODAL);
        this.epsService = epsService;
        initComponents();
        setSize(600, 500);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Paciente
        gbc.gridx = 0; gbc.gridy = 0;
        panelForm.add(new JLabel("Paciente:"), gbc);
        gbc.gridx = 1;
        cmbPaciente = new JComboBox<>();
        cargarPacientes();
        panelForm.add(cmbPaciente, gbc);

        //  ELIMINAMOS la variable no usada y creamos el panel directamente
        JPanel panelServiciosDisponibles = new JPanel(new BorderLayout(5, 5));
        panelServiciosDisponibles.setBorder(BorderFactory.createTitledBorder("Servicios Disponibles"));
        
        // Lista de servicios
        listModel = new DefaultListModel<>();
        for (TipoServicio s : TipoServicio.values()) {
            listModel.addElement(s);
        }
        listServicios = new JList<>(listModel);
        listServicios.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollServicios = new JScrollPane(listServicios);
        panelServiciosDisponibles.add(scrollServicios, BorderLayout.CENTER);

        // Botones de acción
        JPanel panelBotonesServicios = new JPanel(new GridLayout(2, 1, 5, 5));
        btnAgregar = new JButton("➡ Agregar");
        btnQuitar = new JButton("⬅ Quitar");
        panelBotonesServicios.add(btnAgregar);
        panelBotonesServicios.add(btnQuitar);
        panelServiciosDisponibles.add(panelBotonesServicios, BorderLayout.EAST);

        // Panel de servicios seleccionados
        JPanel panelSeleccionados = new JPanel(new BorderLayout(5, 5));
        panelSeleccionados.setBorder(BorderFactory.createTitledBorder("Servicios Seleccionados"));
        JList<TipoServicio> listSeleccionados = new JList<>(new DefaultListModel<>());
        JScrollPane scrollSeleccionados = new JScrollPane(listSeleccionados);
        panelSeleccionados.add(scrollSeleccionados, BorderLayout.CENTER);

        // Panel de servicios (combina ambos)
        JPanel panelServicios = new JPanel(new GridLayout(1, 2, 10, 10));
        panelServicios.add(panelServiciosDisponibles);
        panelServicios.add(panelSeleccionados);

        //  AHORA USAMOS LA VARIABLE panelServicios
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        panelForm.add(panelServicios, gbc);

        add(panelForm, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnGuardar = new JButton(" Generar Factura");
        btnCancelar = new JButton(" Cancelar");
        
        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarPacientes() {
        cmbPaciente.removeAllItems();
        List<Paciente> pacientes = epsService.getPacientes();
        for (Paciente p : pacientes) {
            cmbPaciente.addItem(p);
        }
    }

    private void guardar() {
        // Implementar lógica de guardado
        JOptionPane.showMessageDialog(this, " Factura generada");
        dispose();
    }
}