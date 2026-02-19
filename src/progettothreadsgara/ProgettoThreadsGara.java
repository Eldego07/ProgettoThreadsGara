package progettothreadsgara;

/**
 * Punto di ingresso dell'applicazione
 */
public class ProgettoThreadsGara {

    public static void main(String[] args) {
        try {
            javax.swing.UIManager.setLookAndFeel(
                    javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignorato) {
        }

        java.awt.EventQueue.invokeLater(() -> new FRM_Gara().setVisible(true));
    }
}
