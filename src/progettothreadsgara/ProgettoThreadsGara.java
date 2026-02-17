package progettothreadsgara;

/**
 * Punto di ingresso dell'applicazione.
 *
 * La GUI viene creata sull'Event Dispatch Thread (EDT) tramite invokeLater().
 * Senza invokeLater, la finestra verrebbe creata sul thread main e potrebbe
 * causare problemi di concorrenza con i componenti Swing.
 */
public class ProgettoThreadsGara {

    public static void main(String[] args) {
        // Imposta il look and feel del sistema operativo
        try {
            javax.swing.UIManager.setLookAndFeel(
                javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignorato) { }

        // Crea e mostra la finestra sull'EDT
        java.awt.EventQueue.invokeLater(() -> new FRM_Gara().setVisible(true));
    }
}
