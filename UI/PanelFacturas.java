package UI;

import SERVICE.EpsService;
import MODELO.Factura;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PanelFacturas extends JPanel {
    private EpsService epsService;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnGenerar, btnVerDetalle, btnPagar, btnRefrescar;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public PanelFacturas(EpsService epsService) {
        this.epsService = epsService;
        initComponents();
        cargarDatos();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel superior
        JPanel panelSuperior = new JPanel(new BorderLayout());
        JLabel lblTitulo = new JLabel("Gestión de Facturas", JLabel.LEFT);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        panelSuperior.add(lblTitulo, BorderLayout.WEST);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnGenerar = new JButton("🧾 Generar Factura");
        btnVerDetalle = new JButton("🔍 Ver Detalle");
        btnPagar = new JButton("💳 Pagar");
        btnRefrescar = new JButton("🔄 Refrescar");

        btnGenerar.addActionListener(e -> generarFactura());
        btnVerDetalle.addActionListener(e -> verDetalle());
        btnPagar.addActionListener(e -> pagarFactura());
        btnRefrescar.addActionListener(e -> cargarDatos());

        panelBotones.add(btnGenerar);
        panelBotones.add(btnVerDetalle);
        panelBotones.add(btnPagar);
        panelBotones.add(btnRefrescar);
        panelSuperior.add(panelBotones, BorderLayout.EAST);

        add(panelSuperior, BorderLayout.NORTH);

        // Tabla
        String[] columnas = {"ID", "Fecha", "Paciente", "Subtotal", "IVA", "Total", "Estado"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Color para facturas pagadas
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected, 
                    boolean hasFocus, int row, int column) {
                java.awt.Component c = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
                
                String estado = (String) tableModel.getValueAt(row, 6);
                if (!isSelected) {
                    if (estado.equals("PAGADA")) {
                        c.setBackground(new Color(200, 255, 200));
                    } else {
                        c.setBackground(new Color(255, 240, 200));
                    }
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Panel inferior
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblEstadisticas = new JLabel("Total facturas: 0 | Total facturado: $0");
        panelInferior.add(lblEstadisticas);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void cargarDatos() {
        tableModel.setRowCount(0);
        List<Factura> facturas = epsService.getFacturas();
        double totalFacturado = 0;
        for (Factura f : facturas) {
            tableModel.addRow(new Object[]{
                f.getIdFactura(),
                f.getFecha().format(formatter),
                f.getPaciente().getNombreCompleto(),
                String.format("$%.2f", f.getSubtotal()),
                String.format("$%.2f", f.getIva()),
                String.format("$%.2f", f.getTotal()),
                f.isPagada() ? "PAGADA" : "PENDIENTE"
            });
            if (f.isPagada()) {
                totalFacturado += f.getTotal();
            }
        }
        JPanel panelInferior = (JPanel) getComponent(2);
        JLabel lblEstadisticas = (JLabel) panelInferior.getComponent(0);
        lblEstadisticas.setText("Total facturas: " + facturas.size() + 
            " | Total facturado: $" + String.format("%.2f", totalFacturado));
    }

    private void generarFactura() {
        DialogoFactura dialogo = new DialogoFactura(SwingUtilities.getWindowAncestor(this), epsService);
        dialogo.setVisible(true);
        cargarDatos();
    }

    private void verDetalle() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una factura", 
                "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idFactura = (int) tableModel.getValueAt(selectedRow, 0);
        epsService.mostrarFactura(idFactura);
    }

    private void pagarFactura() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una factura", 
                "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String estado = (String) tableModel.getValueAt(selectedRow, 6);
        if (estado.equals("PAGADA")) {
            JOptionPane.showMessageDialog(this, "Esta factura ya está pagada");
            return;
        }
        
        int idFactura = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, 
            "¿Confirmar pago de la factura #" + idFactura + "?",
            "Confirmar pago", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            epsService.pagarFactura(idFactura);
            cargarDatos();
        }
    }
}