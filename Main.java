import UI.MainFrame;
import javax.swing.*;

/**
 * Clase principal del Sistema de Gestión Hospitalaria / EPS
 * Punto de entrada de la aplicación
 */
public class Main {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new MainFrame();
        });
    }
}