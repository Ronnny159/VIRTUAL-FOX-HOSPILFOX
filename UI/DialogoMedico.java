package UI;

import SERVICE.EpsService;
import MODELO.Medico;
import ENUMS.Especialidad;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DialogoMedico extends JDialog {
    private EpsService epsService;
    private Medico medico;
    private boolean editando;
    
    private JTextField txtId, txtNombre, txtApellido, txtTelefono, txtEmail;
    private JTextField txtDireccion, txtRegistroMedico;
    private JComboBox<Especialidad> cmbEspecialidad;
    private JFormattedTextField txtFechaNacimiento;
    private JTextField txtHonorarios;
    private JButton btnGuardar, btnCancelar;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public DialogoMedico(Window parent, EpsService epsService) {
        this(parent, epsService, null);
    }

    public DialogoMedico(Window parent, EpsService epsService, Medico medico) {
        super(parent, "Registro de Médico", ModalityType.APPLICATION_MODAL);
        this.epsService = epsService;
        this.medico = medico;
        this.editando = medico != null;
        initComponents();
        if (editando) {
            cargarDatos();
            setTitle("Editar Médico");
        }
        setSize(550, 600);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        int row = 0;
        
        // ID
        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(new JLabel("ID / Cédula:"), gbc);
        gbc.gridx = 1;
        txtId = new JTextField(20);
        if (editando) txtId.setEditable(false);
        panelForm.add(txtId, gbc);
        row++;

        // Nombre
        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        txtNombre = new JTextField(20);
        panelForm.add(txtNombre, gbc);
        row++;

        // Apellido
        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(new JLabel("Apellido:"), gbc);
        gbc.gridx = 1;
        txtApellido = new JTextField(20);
        panelForm.add(txtApellido, gbc);
        row++;

        // Fecha Nacimiento
        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(new JLabel("Fecha Nacimiento:"), gbc);
        gbc.gridx = 1;
        txtFechaNacimiento = new JFormattedTextField(formatter);
        txtFechaNacimiento.setColumns(20);
        panelForm.add(txtFechaNacimiento, gbc);
        row++;

        // Teléfono
        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1;
        txtTelefono = new JTextField(20);
        panelForm.add(txtTelefono, gbc);
        row++;

        // Email
        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        txtEmail = new JTextField(20);
        panelForm.add(txtEmail, gbc);
        row++;

        // Dirección
        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(new JLabel("Dirección:"), gbc);
        gbc.gridx = 1;
        txtDireccion = new JTextField(20);
        panelForm.add(txtDireccion, gbc);
        row++;

        // Registro Médico
        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(new JLabel("Registro Médico:"), gbc);
        gbc.gridx = 1;
        txtRegistroMedico = new JTextField(20);
        panelForm.add(txtRegistroMedico, gbc);
        row++;

        // Especialidad
        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(new JLabel("Especialidad:"), gbc);
        gbc.gridx = 1;
        cmbEspecialidad = new JComboBox<>(Especialidad.values());
        panelForm.add(cmbEspecialidad, gbc);
        row++;

        // Honorarios
        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(new JLabel("Honorarios ($):"), gbc);
        gbc.gridx = 1;
        txtHonorarios = new JTextField(20);
        panelForm.add(txtHonorarios, gbc);

        JScrollPane scrollPanel = new JScrollPane(panelForm);
        add(scrollPanel, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnGuardar = new JButton("💾 Guardar");
        btnCancelar = new JButton("❌ Cancelar");
        
        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarDatos() {
        if (medico != null) {
            txtId.setText(medico.getId());
            txtNombre.setText(medico.getNombre());
            txtApellido.setText(medico.getApellido());
            txtFechaNacimiento.setText(medico.getFechaNacimiento().format(formatter));
            txtTelefono.setText(medico.getTelefono());
            txtEmail.setText(medico.getEmail());
            txtDireccion.setText(medico.getDireccion());
            txtRegistroMedico.setText(medico.getRegistroMedico());
            cmbEspecialidad.setSelectedItem(medico.getEspecialidad());
            txtHonorarios.setText(String.valueOf(medico.getHonorarios()));
        }
    }

    private void guardar() {
        try {
            String id = txtId.getText().trim();
            String nombre = txtNombre.getText().trim();
            String apellido = txtApellido.getText().trim();
            LocalDate fechaNac = LocalDate.parse(txtFechaNacimiento.getText(), formatter);
            String telefono = txtTelefono.getText().trim();
            String email = txtEmail.getText().trim();
            String direccion = txtDireccion.getText().trim();
            String registroMedico = txtRegistroMedico.getText().trim();
            Especialidad especialidad = (Especialidad) cmbEspecialidad.getSelectedItem();
            double honorarios = Double.parseDouble(txtHonorarios.getText().trim());

            if (id.isEmpty() || nombre.isEmpty() || apellido.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Los campos ID, Nombre y Apellido son obligatorios",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (honorarios <= 0) {
                JOptionPane.showMessageDialog(this, 
                    "Los honorarios deben ser mayores a 0",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (editando) {
                // Actualizar médico existente
                medico.setNombre(nombre);
                medico.setApellido(apellido);
                medico.setFechaNacimiento(fechaNac);
                medico.setTelefono(telefono);
                medico.setEmail(email);
                medico.setDireccion(direccion);
                medico.setRegistroMedico(registroMedico);
                medico.setEspecialidad(especialidad);
                medico.setHonorarios(honorarios);
                JOptionPane.showMessageDialog(this, "✅ Médico actualizado");
            } else {
                // Registrar nuevo médico
                epsService.registrarMedico(id, nombre, apellido, fechaNac,
                    telefono, email, direccion, registroMedico, especialidad, honorarios);
                JOptionPane.showMessageDialog(this, "✅ Médico registrado");
            }
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al guardar: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}