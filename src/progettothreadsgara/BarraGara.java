package progettothreadsgara;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.*;
import java.net.URL;

public class BarraGara extends JPanel {

    // Larghezza riservata all'immagine a destra della barra
    private static final int LARGHEZZA_IMMAGINE = 100;
    // Altezza preferita del componente
    private static final int ALTEZZA_PREFERITA = 56;

    private int valore = 0;         // 0..100
    private Color colore;
    private String etichetta;            // nome della macchina
    private Image immagineMacchina;     // immagine caricata dal classpath

    /**
     * @param modello Il modello della macchina (determina colore, nome e
     * immagine)
     * @param tuned Se true, aggiunge "(Tuned)" all'etichetta
     */
    public BarraGara(ModelloVeicolo modello, boolean tuned) {
        this.colore = modello.getColore();
        this.etichetta = modello.getNome() + (tuned ? " (Tuned)" : "");
        setOpaque(true);
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(400, ALTEZZA_PREFERITA));
        setMinimumSize(new Dimension(150, ALTEZZA_PREFERITA));

        caricaImmagine(modello.getNomeFile());
    }

    /**
     * Carica l'immagine dal classpath in /Immagini/<nomeFile>. In NetBeans:
     * metti i file in src/Immagini/ → vengono copiati nel classpath.
     *
     * Se l'immagine non esiste, immagineMacchina rimane null e verrà usata una
     * sagoma colorata di riserva.
     *
     * @param nomeFile Nome del file PNG (es. "Ferrari_F40.png")
     */
    private void caricaImmagine(String nomeFile) {
        try {
            URL urlImmagine = getClass().getResource("/Immagini/" + nomeFile);
            if (urlImmagine != null) {
                immagineMacchina = new javax.swing.ImageIcon(urlImmagine).getImage();
            }
        } catch (Exception e) {
            immagineMacchina = null; // usa sagoma di riserva
        }
    }

    /**
     * Aggiorna il valore e ridisegna la barra. DEVE usare invokeLater() perché
     * viene chiamato dai thread delle macchine, non dall'EDT di Swing.
     *
     * @param nuovoValore Valore da 0 a 100
     */
    public void aggiornaValore(int nuovoValore) {
        SwingUtilities.invokeLater(() -> {
            this.valore = nuovoValore;
            repaint();
        });
    }

    /**
     * Resetta a 0 tra una gara e l'altra
     */
    public void resetta() {
        SwingUtilities.invokeLater(() -> {
            this.valore = 0;
            repaint();
        });
    }

    /**
     * Disegna l'intera barra + immagine mobile della macchina.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        int larghezzaTotale = getWidth();
        int altezzaTotale = getHeight();
        int larghezzaBarra = larghezzaTotale - LARGHEZZA_IMMAGINE;
        int margineY = 5;
        int altezzaBarra = altezzaTotale - margineY * 2;

        // 1. Sfondo grigio chiaro della barra
        g2.setColor(new Color(220, 220, 220));
        g2.fillRoundRect(0, margineY, larghezzaBarra, altezzaBarra, 8, 8);

        // 2. Barra colorata proporzionale al progresso
        int larghezzaColorata = (int) Math.round(larghezzaBarra * valore / 100.0);
        if (larghezzaColorata > 0) {
            g2.setColor(colore);
            g2.fillRoundRect(0, margineY, larghezzaColorata, altezzaBarra, 8, 8);
        }

        // 3. Testo con il nome della macchina — centrato nella barra
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        FontMetrics fm = g2.getFontMetrics();
        int testoX = (larghezzaBarra - fm.stringWidth(etichetta)) / 2;
        int testoY = margineY + (altezzaBarra + fm.getAscent() - fm.getDescent()) / 2;
        // Ombra nera semitrasparente per leggibilità su qualsiasi sfondo
        g2.setColor(new Color(0, 0, 0, 70));
        g2.drawString(etichetta, testoX + 1, testoY + 1);
        g2.setColor(Color.WHITE);
        g2.drawString(etichetta, testoX, testoY);

        // 4. Immagine (o sagoma) posizionata alla fine della barra colorata
        //    xImmagine si muove con il progresso → effetto "macchina che corre"
        int xImmagine = larghezzaColorata;
        int yImmagine = margineY;
        int wImg = LARGHEZZA_IMMAGINE;
        int hImg = altezzaBarra;

        if (immagineMacchina != null) {
            // Disegna l'immagine reale ridimensionata all'area dell'icona
            g2.drawImage(immagineMacchina, xImmagine, yImmagine, wImg, hImg, null);
        } else {
            // Sagoma di riserva se l'immagine non è disponibile
            disegnaSagoma(g2, xImmagine, yImmagine, wImg, hImg);
        }

        g2.dispose();
    }

    /**
     * Sagoma di riserva usata se l'immagine reale non è disponibile. Disegna
     * una semplice macchina stilizzata con Graphics2D.
     *
     * Per sostituire con un'immagine esterna: Image img = ImageIO.read(new
     * File("percorso/auto.png")); g2.drawImage(img, x, y, w, h, null);
     */
    private void disegnaSagoma(Graphics2D g2, int x, int y, int w, int h) {
        int margine = 4;
        int altCarro = h - margine * 2;
        int rRuota = Math.max(4, altCarro / 5);
        int topCofano = y + margine + altCarro / 3;
        int fondoCarr = y + h - margine - rRuota;
        int altCarr = fondoCarr - topCofano;

        // Carrozzeria
        g2.setColor(colore.darker());
        g2.fillRect(x + 2, topCofano + altCarr / 2, w - 4, altCarr / 2);
        int[] xp = {x + 5, x + 9, x + w - 6, x + w - 3};
        int[] yp = {topCofano + altCarr / 2, topCofano, topCofano, topCofano + altCarr / 2};
        g2.fillPolygon(xp, yp, 4);

        // Contorno
        g2.setColor(colore.darker().darker());
        g2.setStroke(new BasicStroke(1f));
        g2.drawRect(x + 2, topCofano + altCarr / 2, w - 4, altCarr / 2);
        g2.drawPolygon(xp, yp, 4);

        // Ruote
        g2.setColor(Color.DARK_GRAY);
        g2.fillOval(x + 3, fondoCarr - rRuota, rRuota * 2, rRuota * 2);
        g2.fillOval(x + w - 3 - rRuota * 2, fondoCarr - rRuota, rRuota * 2, rRuota * 2);
    }
}
