package UI;

import SERVICE.EpsService;
import MODELO.Paciente;
import MODELO.Medico;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DialogoCita extends JDialog {

    private EpsService epsService;
    private JComboBox<Paciente> cmbPaciente;
    private JComboBox<Medico> cmbMedico;
    private JFormattedTextField txtFechaHora;
    private JTextArea txtMotivo;
    private JButton btnGuardar, btnCancelar;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public DialogoCita(Window parent, EpsService epsService) {
        super(parent, "Gestión de Citas", ModalityType.APPLICATION_MODAL);
        this.epsService = epsService;
        initComponents();
        setSize(500, 400);
        setLocationRelativeTo(parent);
    }

    private void initComponents(){
        setLayout(new BorderLayout(10, 10));

        JPanel panelForm = new JPanel(new GridLayout());
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        int row = 0;

        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(new JLabel("Paciente:"), gbc);
        gbc.gridx = 1;
        cmbPaciente = new JComboBox<>();
        cargarPacientes();
        panelForm.add(cmbPaciente, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(new JLabel("Medico:"), gbc);
        gbc.gridx = 1;
        cmbMedico = new JComboBox<>();
        cargarMedicos();
        panelForm.add(cmbMedico, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(new JLabel("Fecha y Hora (yyyy-MM-dd HH:mm):"), gbc);
        gbc.gridx = 1;
        txtFechaHora = new JFormattedTextField(formatter);
        txtFechaHora.setColumns(20);
        panelForm.add(txtFechaHora, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(new JLabel("Motivo:"), gbc);
        gbc.gridx = 1;
        txtMotivo = new JTextArea(4, 20);
        txtMotivo.setLineWrap(true);
        panelForm.add(txtMotivo, gbc);

        add(panelForm, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");
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

    private void cargarMedicos() {
        cmbMedico.removeAllItems();
        List<Medico> medicos = epsService.getMedicos();
        for (Medico m : medicos) {
            cmbMedico.addItem(m);
        }
    }

    private void guardar(){
        try{
            Paciente paciente = (Paciente) cmbPaciente.getSelectedItem();
            Medico medico = (Medico) cmbMedico.getSelectedItem();
            String fechaHoraStr = txtFechaHora.getText();
            LocalDateTime fechaHora = LocalDateTime.parse(fechaHoraStr, formatter);
            String motivo = txtMotivo.getText();

            if (paciente == null || medico == null || motivo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios", 
                "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            epsService.agendarCita(paciente.getId(), medico.getId(), fechaHora, motivo);
            JOptionPane.showMessageDialog(this, "Cita registrada exitosamente");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al registrar la cita: " + ex.getMessage(), 
            "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}