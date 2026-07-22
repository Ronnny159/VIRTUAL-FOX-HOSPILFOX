package UI;

import SERVICE.EpsService;
import MODELO.Factura;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PanelFacturas extends JPanel {
    private EpsService epsService;
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JButton btnGenerar, btnVerDetalle, btnPagar, btnRefrescar;
    private JComboBox<String> cmbFiltroEstado;
    private JLabel lblTotal, lblTotalFacturado;
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
        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblTitulo = new JLabel("🧾 Gestión de Facturas");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(0, 102, 204));
        panelTitulo.add(lblTitulo);
        panelSuperior.add(panelTitulo, BorderLayout.WEST);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        
        cmbFiltroEstado = new JComboBox<>(new String[]{"Todas", "PAGADA", "PENDIENTE"});
        cmbFiltroEstado.addActionListener(e -> filtrarPorEstado());
        panelBotones.add(new JLabel("Filtrar:"));
        panelBotones.add(cmbFiltroEstado);
        panelBotones.add(new JSeparator(SwingConstants.VERTICAL));
        
        btnGenerar = crearBoton("🧾 Generar Factura", new Color(46, 204, 113));
        btnVerDetalle = crearBoton("🔍 Ver Detalle", new Color(52, 152, 219));
        btnPagar = crearBoton("💳 Pagar", new Color(241, 196, 15));
        btnRefrescar = crearBoton("🔄 Refrescar", new Color(149, 165, 166));
        
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
        table = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    String estado = (String) getValueAt(row, 6);
                    if (estado.equals("PAGADA")) {
                        c.setBackground(new Color(200, 255, 200));
                    } else {
                        c.setBackground(new Color(255, 240, 200));
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
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        panelInferior.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        
        lblTotal = new JLabel("Total facturas: 0");
        lblTotal.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelInferior.add(lblTotal);
        
        lblTotalFacturado = new JLabel("Total facturado: $0");
        lblTotalFacturado.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTotalFacturado.setForeground(new Color(46, 204, 113));
        panelInferior.add(lblTotalFacturado);
        
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
        String estado = (String) cmbFiltroEstado.getSelectedItem();
        if (estado.equals("Todas")) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter(estado, 6));
        }
    }

    public void cargarDatos() {
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
        lblTotal.setText("Total facturas: " + facturas.size());
        lblTotalFacturado.setText("Total facturado: $" + String.format("%,.2f", totalFacturado));
        filtrarPorEstado();
    }

    private void generarFactura() {
        DialogoFactura dialogo = new DialogoFactura(
            SwingUtilities.getWindowAncestor(this), epsService);
        dialogo.setVisible(true);
        cargarDatos();
    }

    private void verDetalle() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una factura", 
                "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(selectedRow);
        int idFactura = (int) tableModel.getValueAt(modelRow, 0);
        Factura factura = epsService.buscarFactura(idFactura);
        if (factura != null) {
            factura.mostrarFactura();
        }
    }

    private void pagarFactura() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una factura", 
                "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(selectedRow);
        String estado = (String) tableModel.getValueAt(modelRow, 6);
        if (estado.equals("PAGADA")) {
            JOptionPane.showMessageDialog(this, "Esta factura ya está pagada",
                "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int idFactura = (int) tableModel.getValueAt(modelRow, 0);
        String paciente = (String) tableModel.getValueAt(modelRow, 2);
        String total = (String) tableModel.getValueAt(modelRow, 5);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "¿Confirmar pago de la factura #" + idFactura + "\n" +
            "Paciente: " + paciente + "\n" +
            "Total: " + total,
            "Confirmar pago", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            epsService.pagarFactura(idFactura);
            cargarDatos();
            JOptionPane.showMessageDialog(this, "✅ Factura pagada exitosamente");
        }
    }
}