package UI;

import javax.swing.*;
import java.awt.*;

public class DialogoAcercaDe extends JDialog {
    public DialogoAcercaDe(Window parent) {
        super(parent, "❓ Acerca de...", ModalityType.APPLICATION_MODAL);
        initComponents();
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);
        
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JLabel lblTitulo = new JLabel("🏥 Sistema de Gestión Hospitalaria / EPS", JLabel.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(0, 102, 204));
        panelPrincipal.add(lblTitulo, gbc);
        
        JLabel lblVersion = new JLabel("Versión 1.0", JLabel.CENTER);
        lblVersion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panelPrincipal.add(lblVersion, gbc);
        
        panelPrincipal.add(new JLabel(" "), gbc);
        
        JLabel lblDesarrollador = new JLabel("Desarrollado por:", JLabel.CENTER);
        lblDesarrollador.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panelPrincipal.add(lblDesarrollador, gbc);
        
        JLabel lblNombre = new JLabel("Equipo de Desarrollo - Java", JLabel.CENTER);
        lblNombre.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelPrincipal.add(lblNombre, gbc);
        
        panelPrincipal.add(new JLabel(" "), gbc);
        
        JLabel lblTecnologias = new JLabel("Tecnologías utilizadas:", JLabel.CENTER);
        lblTecnologias.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panelPrincipal.add(lblTecnologias, gbc);
        
        JLabel lblTec = new JLabel("• Java SE 17\n• Swing (GUI)\n• POO", JLabel.CENTER);
        lblTec.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelPrincipal.add(lblTec, gbc);
        
        add(panelPrincipal, BorderLayout.CENTER);
        
        // Botón cerrar
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setBackground(new Color(52, 152, 219));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.addActionListener(e -> dispose());
        
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(Color.WHITE);
        panelBotones.add(btnCerrar);
        add(panelBotones, BorderLayout.SOUTH);
    }
}