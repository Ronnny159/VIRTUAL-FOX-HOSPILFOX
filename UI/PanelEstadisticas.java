package UI;

import SERVICE.EpsService;
import MODELO.*;
import ENUMS.EstadoCita;
import javax.swing.*;
import java.awt.*;

public class PanelEstadisticas extends JPanel {
    private EpsService epsService;
    
    // Referencias directas a los componentes
    private JLabel lblPacientes;
    private JLabel lblMedicos;
    private JLabel lblTotalCitas;
    private JLabel lblCitasCompletadas;
    private JLabel lblCitasCanceladas;
    private JLabel lblTotalFacturado;
    private JLabel lblPendientePago;
    private JLabel lblPromedio;
    private JProgressBar progressBar;

    public PanelEstadisticas(EpsService epsService) {
        this.epsService = epsService;
        initComponents();
        actualizar();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1.0;

        // Título
        JLabel lblTitulo = new JLabel("📊 Estadísticas del Sistema", JLabel.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(0, 102, 204));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelPrincipal.add(lblTitulo, gbc);
        gbc.gridwidth = 1;

        // Fila 1
        int row = 1;
        panelPrincipal.add(crearPanelEstadistica("👥 Pacientes", "0", new Color(52, 152, 219), 
            gbc, 0, row), getGbc(gbc, 0, row));
        panelPrincipal.add(crearPanelEstadistica("👨‍⚕️ Médicos", "0", new Color(46, 204, 113), 
            gbc, 1, row));
        row++;

        // Fila 2
        panelPrincipal.add(crearPanelEstadistica("📅 Total Citas", "0", new Color(241, 196, 15), 
            gbc, 0, row), getGbc(gbc, 0, row));
        panelPrincipal.add(crearPanelEstadistica("✅ Citas Completadas", "0", new Color(46, 204, 113), 
            gbc, 1, row));
        row++;

        // Fila 3
        panelPrincipal.add(crearPanelEstadistica("❌ Citas Canceladas", "0", new Color(231, 76, 60), 
            gbc, 0, row), getGbc(gbc, 0, row));
        panelPrincipal.add(crearPanelEstadistica("💰 Total Facturado", "$0", new Color(46, 204, 113), 
            gbc, 1, row));
        row++;

        // Fila 4
        panelPrincipal.add(crearPanelEstadistica("⏳ Pendiente de Pago", "$0", new Color(241, 196, 15), 
            gbc, 0, row), getGbc(gbc, 0, row));
        panelPrincipal.add(crearPanelEstadistica("📊 Promedio por Paciente", "$0", new Color(155, 89, 182), 
            gbc, 1, row));
        row++;

        // Barra de progreso
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JPanel panelProgreso = new JPanel(new BorderLayout(10, 5));
        panelProgreso.setBackground(Color.WHITE);
        panelProgreso.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), 
            "Eficiencia de Citas"));
        
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        progressBar.setForeground(new Color(46, 204, 113));
        progressBar.setBackground(new Color(240, 240, 240));
        panelProgreso.add(progressBar, BorderLayout.CENTER);
        
        JLabel lblProgreso = new JLabel("Porcentaje de citas completadas", JLabel.CENTER);
        lblProgreso.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblProgreso.setForeground(Color.GRAY);
        panelProgreso.add(lblProgreso, BorderLayout.SOUTH);
        
        panelPrincipal.add(panelProgreso, gbc);

        JScrollPane scrollPane = new JScrollPane(panelPrincipal);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);
    }

    private GridBagConstraints getGbc(GridBagConstraints base, int x, int y) {
        GridBagConstraints gbc = (GridBagConstraints) base.clone();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        return gbc;
    }

    private JPanel crearPanelEstadistica(String titulo, String valor, Color color, 
                                         GridBagConstraints gbc, int x, int y) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(5, 5));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTitulo.setForeground(Color.GRAY);
        panel.add(lblTitulo, BorderLayout.NORTH);
        
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblValor.setForeground(color);
        panel.add(lblValor, BorderLayout.CENTER);
        
        // Guardar referencia según el título
        switch (titulo) {
            case "👥 Pacientes" -> lblPacientes = lblValor;
            case "👨‍⚕️ Médicos" -> lblMedicos = lblValor;
            case "📅 Total Citas" -> lblTotalCitas = lblValor;
            case "✅ Citas Completadas" -> lblCitasCompletadas = lblValor;
            case "❌ Citas Canceladas" -> lblCitasCanceladas = lblValor;
            case "💰 Total Facturado" -> lblTotalFacturado = lblValor;
            case "⏳ Pendiente de Pago" -> lblPendientePago = lblValor;
            case "📊 Promedio por Paciente" -> lblPromedio = lblValor;
        }
        
        return panel;
    }

    public void actualizar() {
        // Calcular estadísticas
        int totalPacientes = epsService.getPacientes().size();
        int totalMedicos = epsService.getMedicos().size();
        int totalCitas = epsService.getCitas().size();
        
        long completadas = epsService.getCitas().stream()
            .filter(c -> c.getEstadoCita() == EstadoCita.COMPLETADA)
            .count();
        long canceladas = epsService.getCitas().stream()
            .filter(c -> c.getEstadoCita() == EstadoCita.CANCELADA || 
                         c.getEstadoCita() == EstadoCita.NO_ASISTIO)
            .count();
        
        double totalFacturado = epsService.getFacturas().stream()
            .filter(Factura::isPagada)
            .mapToDouble(Factura::getTotal)
            .sum();
        
        double totalPendiente = epsService.getFacturas().stream()
            .filter(f -> !f.isPagada())
            .mapToDouble(Factura::getTotal)
            .sum();
        
        double promedio = totalPacientes > 0 ? totalFacturado / totalPacientes : 0;

        // Actualizar labels con los valores calculados
        if (lblPacientes != null) lblPacientes.setText(String.valueOf(totalPacientes));
        if (lblMedicos != null) lblMedicos.setText(String.valueOf(totalMedicos));
        if (lblTotalCitas != null) lblTotalCitas.setText(String.valueOf(totalCitas));
        if (lblCitasCompletadas != null) lblCitasCompletadas.setText(String.valueOf(completadas));
        if (lblCitasCanceladas != null) lblCitasCanceladas.setText(String.valueOf(canceladas));
        if (lblTotalFacturado != null) lblTotalFacturado.setText(String.format("$%,.0f", totalFacturado));
        if (lblPendientePago != null) lblPendientePago.setText(String.format("$%,.0f", totalPendiente));
        if (lblPromedio != null) lblPromedio.setText(String.format("$%,.0f", promedio));
        
        // Actualizar barra de progreso
        int porcentaje = totalCitas > 0 ? (int)((completadas * 100) / totalCitas) : 0;
        if (progressBar != null) {
            progressBar.setValue(porcentaje);
            progressBar.setString(porcentaje + "%");
            progressBar.setForeground(porcentaje > 70 ? new Color(46, 204, 113) : 
                                      porcentaje > 40 ? new Color(241, 196, 15) : 
                                      new Color(231, 76, 60));
        }
        
        revalidate();
        repaint();
    }
}