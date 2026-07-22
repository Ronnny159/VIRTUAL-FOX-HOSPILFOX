package UI;

import SERVICE.EpsService;
import MODELO.Paciente;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DialogoPaciente extends JDialog {
    private EpsService epsService;
    private Paciente paciente;
    private boolean editando;
    
    private JTextField txtId, txtNombre, txtApellido, txtTelefono, txtEmail;
    private JTextField txtDireccion, txtHistoriaClinica, txtTipoSangre;
    private JTextArea txtAlergias, txtMedicamentos;
    private JFormattedTextField txtFechaNacimiento;
    private JButton btnGuardar, btnCancelar;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public DialogoPaciente(Window parent, EpsService epsService) {
        this(parent, epsService, null);
    }

    public DialogoPaciente(Window parent, EpsService epsService, Paciente paciente) {
        super(parent, paciente == null ? "📝 Registrar Paciente" : "✏️ Editar Paciente", 
              ModalityType.APPLICATION_MODAL);
        this.epsService = epsService;
        this.paciente = paciente;
        this.editando = paciente != null;
        initComponents();
        if (editando) {
            cargarDatos();
        }
        setSize(600, 650);
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
        
        // ID
        gbc.gridx = 0; gbc.gridy = row;
        gbc.weightx = 0.3;
        panelForm.add(crearLabel("ID / Cédula:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtId = new JTextField(20);
        txtId.setBackground(editando ? new Color(240, 240, 240) : Color.WHITE);
        txtId.setEditable(!editando);
        panelForm.add(txtId, gbc);
        row++;

        // Nombre
        gbc.gridx = 0; gbc.gridy = row;
        gbc.weightx = 0.3;
        panelForm.add(crearLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtNombre = new JTextField(20);
        panelForm.add(txtNombre, gbc);
        row++;

        // Apellido
        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(crearLabel("Apellido:"), gbc);
        gbc.gridx = 1;
        txtApellido = new JTextField(20);
        panelForm.add(txtApellido, gbc);
        row++;

        // Fecha Nacimiento
        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(crearLabel("Fecha Nacimiento:"), gbc);
        gbc.gridx = 1;
        txtFechaNacimiento = new JFormattedTextField(formatter);
        txtFechaNacimiento.setColumns(20);
        txtFechaNacimiento.setValue(LocalDate.now().minusYears(25).format(formatter));
        panelForm.add(txtFechaNacimiento, gbc);
        row++;

        // Teléfono
        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(crearLabel("Teléfono:"), gbc);
        gbc.gridx = 1;
        txtTelefono = new JTextField(20);
        panelForm.add(txtTelefono, gbc);
        row++;

        // Email
        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(crearLabel("Email:"), gbc);
        gbc.gridx = 1;
        txtEmail = new JTextField(20);
        panelForm.add(txtEmail, gbc);
        row++;

        // Dirección
        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(crearLabel("Dirección:"), gbc);
        gbc.gridx = 1;
        txtDireccion = new JTextField(20);
        panelForm.add(txtDireccion, gbc);
        row++;

        // Historia Clínica
        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(crearLabel("Historia Clínica:"), gbc);
        gbc.gridx = 1;
        txtHistoriaClinica = new JTextField(20);
        panelForm.add(txtHistoriaClinica, gbc);
        row++;

        // Tipo Sangre
        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(crearLabel("Tipo Sangre:"), gbc);
        gbc.gridx = 1;
        String[] sangres = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        JComboBox<String> cmbSangre = new JComboBox<>(sangres);
        cmbSangre.setEditable(true);
        txtTipoSangre = new JTextField(20);
        // Usar el combo box en lugar del text field
        gbc.gridx = 1;
        panelForm.add(cmbSangre, gbc);
        // Guardar referencia del combo
        this.txtTipoSangre = new JTextField(20);
        row++;

        // Alergias
        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(crearLabel("Alergias:"), gbc);
        gbc.gridx = 1;
        txtAlergias = new JTextArea(3, 20);
        txtAlergias.setLineWrap(true);
        txtAlergias.setWrapStyleWord(true);
        JScrollPane scrollAlergias = new JScrollPane(txtAlergias);
        panelForm.add(scrollAlergias, gbc);
        row++;

        // Medicamentos
        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(crearLabel("Medicamentos:"), gbc);
        gbc.gridx = 1;
        txtMedicamentos = new JTextArea(3, 20);
        txtMedicamentos.setLineWrap(true);
        txtMedicamentos.setWrapStyleWord(true);
        JScrollPane scrollMedicamentos = new JScrollPane(txtMedicamentos);
        panelForm.add(scrollMedicamentos, gbc);

        JScrollPane scrollPanel = new JScrollPane(panelForm);
        scrollPanel.setBorder(null);
        scrollPanel.getViewport().setBackground(Color.WHITE);
        add(scrollPanel, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panelBotones.setBackground(Color.WHITE);
        panelBotones.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)));
        
        btnGuardar = new JButton("💾 Guardar");
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

    private void cargarDatos() {
        if (paciente != null) {
            txtId.setText(paciente.getId());
            txtNombre.setText(paciente.getNombre());
            txtApellido.setText(paciente.getApellido());
            txtFechaNacimiento.setText(paciente.getFechaNacimiento().format(formatter));
            txtTelefono.setText(paciente.getTelefono());
            txtEmail.setText(paciente.getEmail());
            txtDireccion.setText(paciente.getDireccion());
            txtHistoriaClinica.setText(paciente.getHistorialClinica());
            txtTipoSangre.setText(paciente.getTipoSangre());
            txtAlergias.setText(paciente.getAlergias());
            txtMedicamentos.setText(paciente.getMedicamentoAcruales());
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
            String hc = txtHistoriaClinica.getText().trim();
            String sangre = txtTipoSangre.getText().trim();
            String alergias = txtAlergias.getText().trim();
            String medicamentos = txtMedicamentos.getText().trim();

            if (id.isEmpty() || nombre.isEmpty() || apellido.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Los campos ID, Nombre y Apellido son obligatorios",
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!editando && epsService.buscarPaciente(id) != null) {
                JOptionPane.showMessageDialog(this, 
                    "Ya existe un paciente con ID: " + id,
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (editando) {
                paciente.setNombre(nombre);
                paciente.setApellido(apellido);
                paciente.setFechaNacimiento(fechaNac);
                paciente.setTelefono(telefono);
                paciente.setEmail(email);
                paciente.setDireccion(direccion);
                paciente.setHistoriaClinica(hc);
                paciente.setTipoSangre(sangre);
                paciente.setAlergias(alergias);
                paciente.setMedicamentosActuales(medicamentos);
                JOptionPane.showMessageDialog(this, "✅ Paciente actualizado exitosamente");
            } else {
                epsService.registrarPaciente(id, nombre, apellido, fechaNac,
                    telefono, email, direccion, hc, sangre, alergias, medicamentos);
                JOptionPane.showMessageDialog(this, "✅ Paciente registrado exitosamente");
            }
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al guardar: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}