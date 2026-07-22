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
    private JTextField txtDireccion, txthistoriaClinica, txtTipoSangre;
    private JTextArea txtAlergias, txtMedicamentos;
    private JFormattedTextField txtFechaNacimiento;
    private JButton btnGuardar, btnCancelar;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public DialogoPaciente(Window parent, EpsService espService){
        this(parent, espService, null);
    }

    public DialogoPaciente(Window parent, EpsService epsService, Paciente paciente){
        super(parent, "Gestion de pacientes", ModalityType.APPLICATION_MODAL);
        this.epsService = epsService;
        this.paciente = paciente;
        this.editando = paciente != null;
        initComponents();
        if(editando){
            cargarDatos();
        }
        setSize(550, 600);
        setLocationRelativeTo(parent);
    }

    private void initComponents(){
        setLayout(new BorderLayout(10,10));

        JPanel panelForm = new JPanel(new GridLayout());
        panelForm.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5,5,5,5);

        int row=0;

        gbc.gridx=0; gbc.gridy=row;
        panelForm.add(new JLabel("ID/Cedula:"), gbc);
        gbc.gridx=1;
        txtId = new JTextField(20);
        if(editando) txtId.setEditable(false);
        panelForm.add(txtId, gbc);
        row++;

        gbc.gridx=0; gbc.gridy=row;
        panelForm.add(new JLabel("Nombre:"), gbc);
        gbc.gridx=1;
        txtNombre = new JTextField(20);
        panelForm.add(txtNombre, gbc);
        row++;

        gbc.gridx=0; gbc.gridy=row;
        panelForm.add(new JLabel("Apellido:"), gbc);
        gbc.gridx=1;
        txtApellido = new JTextField(20);
        panelForm.add(txtApellido, gbc);
        row++;

        gbc.gridx=0; gbc.gridy=row;
        panelForm.add(new JLabel("Fecha de Nacimiento:"), gbc);
        gbc.gridx=1;
        txtFechaNacimiento = new JFormattedTextField(dateFormatter);
        txtFechaNacimiento.setColumns(20);
        panelForm.add(txtFechaNacimiento, gbc);
        row++;

        gbc.gridx=0; gbc.gridy=row;
        panelForm.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx=1;
        txtTelefono = new JTextField(20);
        panelForm.add(txtTelefono, gbc);
        row++;

        gbc.gridx=0; gbc.gridy=row;
        panelForm.add(new JLabel("Email:"), gbc);
        gbc.gridx=1;
        txtEmail = new JTextField(20);
        panelForm.add(txtEmail, gbc);
        row++;

        gbc.gridx=0; gbc.gridy=row;
        panelForm.add(new JLabel("Dirección:"), gbc);
        gbc.gridx=1;
        txtDireccion = new JTextField(20);
        panelForm.add(txtDireccion, gbc);
        row++;

        gbc.gridx=0; gbc.gridy=row;
        panelForm.add(new JLabel("Historia Clínica:"), gbc);
        gbc.gridx=1;
        txthistoriaClinica = new JTextField(20);
        panelForm.add(txthistoriaClinica, gbc);
        row++;

        gbc.gridx=0; gbc.gridy=row;
        panelForm.add(new JLabel("Tipo de Sangre:"), gbc);
        gbc.gridx=1;
        txtTipoSangre = new JTextField(20);
        panelForm.add(txtTipoSangre, gbc);
        row++;

        gbc.gridx=0; gbc.gridy=row;
        panelForm.add(new JLabel("Alergias:"), gbc);
        gbc.gridx=1;
        txtAlergias = new JTextArea(3, 20);
        txtAlergias.setLineWrap(true);
        txtAlergias.setWrapStyleWord(true);
        panelForm.add(new JScrollPane(txtAlergias), gbc);
        row++;

        gbc.gridx=0;	gbc.gridy=row;
        panelForm.add(new JLabel("Medicamentos Actuales:"),	gbc);
       	gbc.gridx=1;
        txtMedicamentos = new JTextArea(3, 20);
        txtMedicamentos.setLineWrap(true);
        txtMedicamentos.setWrapStyleWord(true);
        panelForm.add(new JScrollPane(txtMedicamentos),	gbc);
        row++;

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> guardarPaciente());
        btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarDatos(){
        if(paciente != null){
            txtId.setText(paciente.getId());
            txtNombre.setText(paciente.getNombre());
            txtApellido.setText(paciente.getApellido());
            txtFechaNacimiento.setText(paciente.getFechaNacimiento().format(dateFormatter));
            txtTelefono.setText(paciente.getTelefono());
            txtEmail.setText(paciente.getEmail());
            txtDireccion.setText(paciente.getDireccion());
            txthistoriaClinica.setText(paciente.getHistorialClinica());
            txtTipoSangre.setText(paciente.getTipoSangre());
            txtAlergias.setText(paciente.getAlergias());
            txtMedicamentos.setText(paciente.getMedicamentoAcruales());
        }
    }

    private void guardarPaciente(){
        try {
            
            String id = txtId.getText().trim();
            String nombre = txtNombre.getText().trim();
            String apellido = txtApellido.getText().trim();
            LocalDate fechaNac = LocalDate.parse(txtFechaNacimiento.getText(), dateFormatter);
            String telefono = txtTelefono.getText().trim();
            String email = txtEmail.getText().trim();
            String direccion = txtDireccion.getText().trim();
            String historiaClinica = txthistoriaClinica.getText().trim();
            String tipoSangre = txtTipoSangre.getText().trim();
            String alergias = txtAlergias.getText().trim();
            String medicamentosActuales = txtMedicamentos.getText().trim();

            if(id.isEmpty() || nombre.isEmpty() || apellido.isEmpty()){
                JOptionPane.showMessageDialog(this, "ID, Nombre y Apellido son obligatorios", 
                "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if(editando){
                paciente.setNombre(nombre);
                paciente.setApellido(apellido);
                paciente.setFechaNacimiento(fechaNac);
                paciente.setTelefono(telefono);
                paciente.setEmail(email);
                paciente.setDireccion(direccion);
                paciente.setHistoriaClinica(historiaClinica);
                paciente.setTipoSangre(tipoSangre);
                paciente.setAlergias(alergias);
                paciente.setMedicamentosActuales(medicamentosActuales);
            } else {
                epsService.registrarPaciente(id, nombre, apellido, fechaNac, telefono, email,
                        direccion, historiaClinica, tipoSangre, alergias, medicamentosActuales);
                JOptionPane.showMessageDialog(this, "Paciente registrado exitosamente");
            }
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar el paciente: " + 
            ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);   
        }
    }
}
