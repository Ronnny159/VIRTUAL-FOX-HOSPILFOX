package UI;

import SERVICE.EpsService;
import MODELO.Cita;
import ENUMS.EstadoCita;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DialogoConsulta extends JDialog {
    private EpsService epsService;
    private JComboBox<Cita> cmbCita;
    private JTextArea txtSintomas, txtDiagnostico, txtTratamiento;
    private JTextArea txtMedicamentos, txtObservaciones;
    private JButton btnGuardar, btnCancelar;

    public DialogoConsulta(Window parent, EpsService epsService) {
        super(parent, "Registrar Consulta Médica", ModalityType.APPLICATION_MODAL);
        this.epsService = epsService;
        initComponents();
        setSize(600, 650);
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

        // Cita
        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(new JLabel("Cita:"), gbc);
        gbc.gridx = 1;
        cmbCita = new JComboBox<>();
        cargarCitasDisponibles();
        cmbCita.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof Cita) {
                    Cita c = (Cita) value;
                    value = String.format("#%d - %s - Dr. %s", 
                        c.getIdCita(), 
                        c.getPaciente().getNombreCompleto(),
                        c.getMedico().getNombreCompleto());
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });
        panelForm.add(cmbCita, gbc);
        row++;

        // Síntomas
        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(new JLabel("Síntomas:"), gbc);
        gbc.gridx = 1;
        txtSintomas = new JTextArea(4, 20);
        txtSintomas.setLineWrap(true);
        JScrollPane scrollSintomas = new JScrollPane(txtSintomas);
        panelForm.add(scrollSintomas, gbc);
        row++;

        // Diagnóstico
        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(new JLabel("Diagnóstico:"), gbc);
        gbc.gridx = 1;
        txtDiagnostico = new JTextArea(4, 20);
        txtDiagnostico.setLineWrap(true);
        JScrollPane scrollDiagnostico = new JScrollPane(txtDiagnostico);
        panelForm.add(scrollDiagnostico, gbc);
        row++;

        // Tratamiento
        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(new JLabel("Tratamiento:"), gbc);
        gbc.gridx = 1;
        txtTratamiento = new JTextArea(4, 20);
        txtTratamiento.setLineWrap(true);
        JScrollPane scrollTratamiento = new JScrollPane(txtTratamiento);
        panelForm.add(scrollTratamiento, gbc);
        row++;

        // Medicamentos
        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(new JLabel("Medicamentos:"), gbc);
        gbc.gridx = 1;
        txtMedicamentos = new JTextArea(3, 20);
        txtMedicamentos.setLineWrap(true);
        JScrollPane scrollMedicamentos = new JScrollPane(txtMedicamentos);
        panelForm.add(scrollMedicamentos, gbc);
        row++;

        // Observaciones
        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(new JLabel("Observaciones:"), gbc);
        gbc.gridx = 1;
        txtObservaciones = new JTextArea(3, 20);
        txtObservaciones.setLineWrap(true);
        JScrollPane scrollObservaciones = new JScrollPane(txtObservaciones);
        panelForm.add(scrollObservaciones, gbc);

        JScrollPane scrollPanel = new JScrollPane(panelForm);
        add(scrollPanel, BorderLayout.CENTER);

        // Panel de información de la cita seleccionada
        JPanel panelInfo = new JPanel(new BorderLayout());
        panelInfo.setBorder(BorderFactory.createTitledBorder("Información de la Cita"));
        JLabel lblInfo = new JLabel("Seleccione una cita para ver los detalles");
        lblInfo.setFont(new Font("Arial", Font.PLAIN, 12));
        panelInfo.add(lblInfo, BorderLayout.CENTER);
        
        cmbCita.addActionListener(e -> {
            Cita cita = (Cita) cmbCita.getSelectedItem();
            if (cita != null) {
                lblInfo.setText(String.format(
                    "<html><b>Paciente:</b> %s<br>" +
                    "<b>Médico:</b> Dr. %s<br>" +
                    "<b>Especialidad:</b> %s<br>" +
                    "<b>Motivo:</b> %s</html>",
                    cita.getPaciente().getNombreCompleto(),
                    cita.getMedico().getNombreCompleto(),
                    cita.getMedico().getEspecialidad().getNombre(),
                    cita.getMotivo()
                ));
            }
        });
        
        add(panelInfo, BorderLayout.NORTH);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnGuardar = new JButton(" Registrar Consulta");
        btnCancelar = new JButton(" Cancelar");
        
        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarCitasDisponibles() {
        cmbCita.removeAllItems();
        List<Cita> citas = epsService.getCitas();
        for (Cita c : citas) {
            // Solo mostrar citas confirmadas o en curso
            if (c.getEstadoCita() == EstadoCita.CONFIRMADA || 
                c.getEstadoCita() == EstadoCita.EN_CURSO) {
                cmbCita.addItem(c);
            }
        }
        if (cmbCita.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, 
                "No hay citas disponibles para consulta.\n" +
                "Debe tener citas CONFIRMADAS o EN_CURSO.",
                "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void guardar() {
        try {
            Cita cita = (Cita) cmbCita.getSelectedItem();
            if (cita == null) {
                JOptionPane.showMessageDialog(this, 
                    "Seleccione una cita",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String sintomas = txtSintomas.getText().trim();
            String diagnostico = txtDiagnostico.getText().trim();
            String tratamiento = txtTratamiento.getText().trim();
            String medicamentos = txtMedicamentos.getText().trim();
            String observaciones = txtObservaciones.getText().trim();

            if (diagnostico.isEmpty() || tratamiento.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Diagnóstico y Tratamiento son obligatorios",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verificar que la cita esté en curso
            if (cita.getEstadoCita() != EstadoCita.EN_CURSO) {
                int opcion = JOptionPane.showConfirmDialog(this,
                    "La cita no está EN_CURSO. ¿Desea iniciarla ahora?",
                    "Iniciar Cita", JOptionPane.YES_NO_OPTION);
                if (opcion == JOptionPane.YES_OPTION) {
                    epsService.iniciarCita(cita.getIdCita());
                } else {
                    return;
                }
            }

            // Registrar consulta
            epsService.registrarConsulta(cita.getIdCita(), diagnostico, 
                sintomas, tratamiento, medicamentos, observaciones);

            // Preguntar si desea generar factura
            int opcionFactura = JOptionPane.showConfirmDialog(this,
                "¿Desea generar factura por esta consulta?",
                "Generar Factura", JOptionPane.YES_NO_OPTION);
            
            if (opcionFactura == JOptionPane.YES_OPTION) {
                // Buscar la consulta recién creada
                epsService.generarFacturaConsulta(epsService.getConsultas().get(
                    epsService.getConsultas().size() - 1));
                JOptionPane.showMessageDialog(this, 
                    " Factura generada exitosamente",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }

            JOptionPane.showMessageDialog(this, " Consulta registrada exitosamente");
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al registrar consulta: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}