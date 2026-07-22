import javax.swing.*;
import UI.MainFrame;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Configurar Look and Feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                
                // Crear ventana principal con try-catch
                new MainFrame();
                
            } catch (Exception e) {
                System.err.println("❌ Error al iniciar la aplicación:");
                e.printStackTrace();
                
                // Mostrar error en ventana emergente
                JOptionPane.showMessageDialog(null,
                    "Error al iniciar la aplicación:\n" + e.getMessage() +
                    "\n\nRevise la consola para más detalles.",
                    "Error de Inicio",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}