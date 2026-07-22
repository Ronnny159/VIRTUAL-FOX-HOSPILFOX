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
    private JComboBox<String> cmbPaciente;
    private JComboBox<String> cmbMedico;
    private JFormattedTextField txtFechaHora;
    private JTextArea txtMotivo;
    private JButton btnGuardar, btnCancelar;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public DialogoCita(Window parent, EpsService epsService) {
        super(parent, "📅 Agendar Cita", ModalityType.APPLICATION_MODAL);
        this.epsService = epsService;
        initComponents();
        setSize(550, 450);
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);
        
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        int row = 0;

        // Paciente
        gbc.gridx = 0; gbc.gridy = row;
        gbc.weightx = 0.3;
        panelForm.add(crearLabel("Paciente:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        cmbPaciente = new JComboBox<>();
        cargarPacientes();
        panelForm.add(cmbPaciente, gbc);
        row++;

        // Médico
        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(crearLabel("Médico:"), gbc);
        gbc.gridx = 1;
        cmbMedico = new JComboBox<>();
        cargarMedicos();
        panelForm.add(cmbMedico, gbc);
        row++;

        // Fecha y Hora
        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(crearLabel("Fecha/Hora:"), gbc);
        gbc.gridx = 1;
        txtFechaHora = new JFormattedTextField(formatter);
        txtFechaHora.setColumns(20);
        txtFechaHora.setValue(LocalDateTime.now().plusDays(1).format(formatter));
        panelForm.add(txtFechaHora, gbc);
        row++;

        // Motivo
        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(crearLabel("Motivo:"), gbc);
        gbc.gridx = 1;
        txtMotivo = new JTextArea(4, 20);
        txtMotivo.setLineWrap(true);
        txtMotivo.setWrapStyleWord(true);
        JScrollPane scrollMotivo = new JScrollPane(txtMotivo);
        panelForm.add(scrollMotivo, gbc);

        add(panelForm, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panelBotones.setBackground(Color.WHITE);
        panelBotones.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)));
        
        btnGuardar = new JButton("💾 Agendar");
        btnGuardar.setBackground(new Color(46, 204, 113));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnCancelar = new JButton("❌ Cancelar");
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(52, 73, 94));
        return label;
    }

    private void cargarPacientes() {
        cmbPaciente.removeAllItems();
        List<Paciente> pacientes = epsService.getPacientes();
        for (Paciente p : pacientes) {
            cmbPaciente.addItem(p.getId() + " - " + p.getNombreCompleto());
        }
        if (pacientes.isEmpty()) {
            cmbPaciente.addItem("No hay pacientes registrados");
        }
    }

    private void cargarMedicos() {
        cmbMedico.removeAllItems();
        List<Medico> medicos = epsService.getMedicos();
        for (Medico m : medicos) {
            cmbMedico.addItem(m.getId() + " - Dr. " + m.getNombreCompleto() + 
                             " (" + m.getEspecialidad().getNombre() + ")");
        }
        if (medicos.isEmpty()) {
            cmbMedico.addItem("No hay médicos registrados");
        }
    }

    private void guardar() {
        try {
            String pacienteStr = (String) cmbPaciente.getSelectedItem();
            String medicoStr = (String) cmbMedico.getSelectedItem();
            
            if (pacienteStr == null || medicoStr == null || 
                pacienteStr.contains("No hay") || medicoStr.contains("No hay")) {
                JOptionPane.showMessageDialog(this, 
                    "Debe seleccionar un paciente y un médico",
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String pacienteId = pacienteStr.split(" - ")[0];
            String medicoId = medicoStr.split(" - ")[0];
            
            LocalDateTime fechaHora = LocalDateTime.parse(txtFechaHora.getText(), formatter);
            String motivo = txtMotivo.getText().trim();

            if (motivo.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Ingrese el motivo de la cita",
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            epsService.agendarCita(pacienteId, medicoId, fechaHora, motivo);
            JOptionPane.showMessageDialog(this, "✅ Cita agendada exitosamente");
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al agendar: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}