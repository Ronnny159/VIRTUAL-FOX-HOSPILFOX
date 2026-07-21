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
    private JTable tableFacturas;
    private DefaultTableModel tableModel;
    private JButton btnGenerar, btnVerDetalles, btnPagar, btnRefrescar;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public PanelFacturas(EpsService epsService) {
        this.epsService = epsService;
        initComponents();
        cargarDatos();
    }

    private void initComponents(){
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JPanel panelSuperior = new JPanel(new BorderLayout());
        JLabel lblTitulo = new JLabel("Gestion de Facturas", JLabel.LEFT);
        lblTitulo.setFont(new Font("Arial", Font.BOLD,18));
        panelSuperior.add(lblTitulo,BorderLayout.WEST);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnGenerar=new JButton("Generar Factura");
        btnVerDetalles=new JButton("Ver Detalle");
        btnPagar=new JButton("Pagar");
        btnRefrescar=new JButton("Refrescar");

        btnGenerar.addActionListener(e -> generarFactura());
        btnVerDetalles.addActionListener(e -> verDetalle());
        btnPagar.addActionListener(e -> pagarFactura());
        btnRefrescar.addActionListener(e -> cargarDatos());

        panelBotones.add(btnGenerar);
        panelBotones.add(btnVerDetalles);
        panelBotones.add(btnPagar);
        panelBotones.add(btnRefrescar);
        panelSuperior.add(panelBotones, BorderLayout.EAST);

        add(panelSuperior, BorderLayout.NORTH);

        String[] columnas = {"ID","Fecha","Paciente","Subtotal","Iva","Total","Estado"};
        tableModel = new DefaultTableModel(columnas, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        tableFacturas = new JTable(tableModel);
        tableFacturas.setRowHeight(25);
        tableFacturas.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        
    }
}
