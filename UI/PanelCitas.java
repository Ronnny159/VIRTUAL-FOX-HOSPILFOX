package UI;

import SERVICE.EpsService;
import MODELO.Cita;
import ENUMS.EstadoCita;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PanelCitas extends JPanel {
    private EpsService epsService;
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JButton btnAgendar, btnConfirmar, btnIniciar, btnCancelar, btnRefrescar;
    private JComboBox<String> cmbFiltroEstado;
    private JLabel lblTotal;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public PanelCitas(EpsService epsService) {
        this.epsService = epsService;
        initComponents();
        cargarDatos();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel superior
        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblTitulo = new JLabel("📅 Gestión de Citas");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(0, 102, 204));
        panelTitulo.add(lblTitulo);
        panelSuperior.add(panelTitulo, BorderLayout.WEST);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        
        // Filtro de estado
        cmbFiltroEstado = new JComboBox<>(new String[]{
            "Todos", 
            EstadoCita.AGENDADA.getNombre(),
            EstadoCita.CONFIRMADA.getNombre(),
            EstadoCita.EN_CURSO.getNombre(),
            EstadoCita.COMPLETADA.getNombre(),
            EstadoCita.CANCELADA.getNombre(),
            EstadoCita.NO_ASISTIO.getNombre()
        });
        cmbFiltroEstado.addActionListener(e -> filtrarPorEstado());
        panelBotones.add(new JLabel("Filtrar:"));
        panelBotones.add(cmbFiltroEstado);
        panelBotones.add(new JSeparator(SwingConstants.VERTICAL));
        
        btnAgendar = crearBoton("📅 Agendar", new Color(46, 204, 113));
        btnConfirmar = crearBoton("✅ Confirmar", new Color(52, 152, 219));
        btnIniciar = crearBoton("🔄 Iniciar", new Color(241, 196, 15));
        btnCancelar = crearBoton("❌ Cancelar", new Color(231, 76, 60));
        btnRefrescar = crearBoton("🔄 Refrescar", new Color(149, 165, 166));
        
        btnAgendar.addActionListener(e -> agendarCita());
        btnConfirmar.addActionListener(e -> confirmarCita());
        btnIniciar.addActionListener(e -> iniciarCita());
        btnCancelar.addActionListener(e -> cancelarCita());
        btnRefrescar.addActionListener(e -> cargarDatos());

        panelBotones.add(btnAgendar);
        panelBotones.add(btnConfirmar);
        panelBotones.add(btnIniciar);
        panelBotones.add(btnCancelar);
        panelBotones.add(btnRefrescar);
        panelSuperior.add(panelBotones, BorderLayout.EAST);

        add(panelSuperior, BorderLayout.NORTH);

        // Tabla con colores por estado
        String[] columnas = {"ID", "Fecha/Hora", "Paciente", "Médico", "Especialidad", "Estado", "Motivo"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    String estado = (String) getValueAt(row, 5);
                    switch (estado) {
                        case "COMPLETADA" -> c.setBackground(new Color(200, 255, 200));
                        case "CANCELADA" -> c.setBackground(new Color(255, 200, 200));
                        case "NO_ASISTIO" -> c.setBackground(new Color(255, 220, 200));
                        case "EN_CURSO" -> c.setBackground(new Color(255, 255, 200));
                        case "CONFIRMADA" -> c.setBackground(new Color(200, 220, 255));
                        default -> c.setBackground(Color.WHITE);
                    }
                }
                return c;
            }
        };
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(52, 73, 94));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Panel inferior
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        
        lblTotal = new JLabel("Total citas: 0");
        lblTotal.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelInferior.add(lblTotal, BorderLayout.WEST);
        
        JLabel lblLeyenda = new JLabel("🟢 Completada | 🔴 Cancelada | 🟡 En curso | 🔵 Confirmada");
        lblLeyenda.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        panelInferior.add(lblLeyenda, BorderLayout.EAST);
        
        add(panelInferior, BorderLayout.SOUTH);
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void filtrarPorEstado() {
        String estadoSeleccionado = (String) cmbFiltroEstado.getSelectedItem();
        if (estadoSeleccionado.equals("Todos")) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter(estadoSeleccionado, 5));
        }
    }

    public void cargarDatos() {
        tableModel.setRowCount(0);
        List<Cita> citas = epsService.getCitas();
        for (Cita c : citas) {
            tableModel.addRow(new Object[]{
                c.getIdCita(),
                c.getFechaHora().format(formatter),
                c.getPaciente().getNombreCompleto(),
                "Dr. " + c.getMedico().getNombreCompleto(),
                c.getMedico().getEspecialidad().getNombre(),
                c.getEstadoCita().getNombre(),
                c.getMotivo().length() > 20 ? c.getMotivo().substring(0, 20) + "..." : c.getMotivo()
            });
        }
        lblTotal.setText("Total citas: " + citas.size());
        filtrarPorEstado();
    }

    private void agendarCita() {
        DialogoCita dialogo = new DialogoCita(
            SwingUtilities.getWindowAncestor(this), epsService);
        dialogo.setVisible(true);
        cargarDatos();
    }

    private void confirmarCita() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una cita", 
                "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(selectedRow);
        int idCita = (int) tableModel.getValueAt(modelRow, 0);
        epsService.confirmarCita(idCita);
        cargarDatos();
    }

    private void iniciarCita() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una cita", 
                "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(selectedRow);
        int idCita = (int) tableModel.getValueAt(modelRow, 0);
        epsService.iniciarCita(idCita);
        cargarDatos();
    }

    private void cancelarCita() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una cita", 
                "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(selectedRow);
        int idCita = (int) tableModel.getValueAt(modelRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de cancelar la cita #" + idCita + "?",
            "Confirmar cancelación", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            epsService.cancelarCita(idCita);
            cargarDatos();
        }
    }
}