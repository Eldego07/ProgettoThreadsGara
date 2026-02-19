package progettothreadsgara;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.*;
import java.net.URL;

/**
 * Barra di avanzamento con immagine della macchina che si muove. Le immagini
 * vengono caricate da /Immagini/ nel classpath (in NetBeans: src/Immagini/).
 * aggiornaValore() usa invokeLater() per thread-safety.
 */
public class BarraGara extends JPanel {

    private static final int LARGHEZZA_IMMAGINE = 100;
    private static final int ALTEZZA_PREFERITA = 56;

    private int valore = 0;
    private Color colore;
    private String etichetta;
    private Image immagineMacchina;

    public BarraGara(ModelloVeicolo modello, boolean tuned) {
        this.colore = modello.getColore();
        this.etichetta = modello.getNome() + (tuned ? " (Tuned)" : "");
        setOpaque(true);
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(400, ALTEZZA_PREFERITA));
        setMinimumSize(new Dimension(150, ALTEZZA_PREFERITA));
        caricaImmagine(modello.getNomeFile());
    }

    private void caricaImmagine(String nomeFile) {
        try {
            URL urlImmagine = getClass().getResource("/Immagini/" + nomeFile);
            if (urlImmagine != null) {
                immagineMacchina = new javax.swing.ImageIcon(urlImmagine).getImage();
            }
        } catch (Exception e) {
            immagineMacchina = null;
        }
    }

    public void aggiornaValore(int nuovoValore) {
        SwingUtilities.invokeLater(() -> {
            this.valore = nuovoValore;
            repaint();
        });
    }

    public void resetta() {
        SwingUtilities.invokeLater(() -> {
            this.valore = 0;
            repaint();
        });
    }

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

        // Sfondo grigio
        g2.setColor(new Color(220, 220, 220));
        g2.fillRoundRect(0, margineY, larghezzaBarra, altezzaBarra, 8, 8);

        // Barra colorata
        int larghezzaColorata = (int) Math.round(larghezzaBarra * valore / 100.0);
        if (larghezzaColorata > 0) {
            g2.setColor(colore);
            g2.fillRoundRect(0, margineY, larghezzaColorata, altezzaBarra, 8, 8);
        }

        // Testo centrato
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        FontMetrics fm = g2.getFontMetrics();
        int testoX = (larghezzaBarra - fm.stringWidth(etichetta)) / 2;
        int testoY = margineY + (altezzaBarra + fm.getAscent() - fm.getDescent()) / 2;
        g2.setColor(new Color(0, 0, 0, 70));
        g2.drawString(etichetta, testoX + 1, testoY + 1);
        g2.setColor(Color.WHITE);
        g2.drawString(etichetta, testoX, testoY);

        // Immagine che si muove con il progresso
        int xImmagine = larghezzaColorata;
        int yImmagine = margineY;
        int wImg = LARGHEZZA_IMMAGINE;
        int hImg = altezzaBarra;

        if (immagineMacchina != null) {
            g2.drawImage(immagineMacchina, xImmagine, yImmagine, wImg, hImg, null);
        } else {
            disegnaSagoma(g2, xImmagine, yImmagine, wImg, hImg);
        }

        g2.dispose();
    }

    private void disegnaSagoma(Graphics2D g2, int x, int y, int w, int h) {
        int margine = 4;
        int altCarro = h - margine * 2;
        int rRuota = Math.max(4, altCarro / 5);
        int topCofano = y + margine + altCarro / 3;
        int fondoCarr = y + h - margine - rRuota;
        int altCarr = fondoCarr - topCofano;

        g2.setColor(colore.darker());
        g2.fillRect(x + 2, topCofano + altCarr / 2, w - 4, altCarr / 2);
        int[] xp = {x + 5, x + 9, x + w - 6, x + w - 3};
        int[] yp = {topCofano + altCarr / 2, topCofano, topCofano, topCofano + altCarr / 2};
        g2.fillPolygon(xp, yp, 4);

        g2.setColor(colore.darker().darker());
        g2.setStroke(new BasicStroke(1f));
        g2.drawRect(x + 2, topCofano + altCarr / 2, w - 4, altCarr / 2);
        g2.drawPolygon(xp, yp, 4);

        g2.setColor(Color.DARK_GRAY);
        g2.fillOval(x + 3, fondoCarr - rRuota, rRuota * 2, rRuota * 2);
        g2.fillOval(x + w - 3 - rRuota * 2, fondoCarr - rRuota, rRuota * 2, rRuota * 2);
    }
}
