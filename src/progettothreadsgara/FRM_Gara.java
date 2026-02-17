package progettothreadsgara;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Frame principale dell'applicazione "Gara di Macchine".
 *
 * RESPONSABILITA':
 *  1. Costruire e mostrare l'interfaccia grafica
 *  2. Raccogliere la configurazione dai PannelloConfigurazione
 *  3. Creare le Macchine e passarle al GestoreGara
 *  4. Implementare GestoreGara.AscoltatoreGara per ricevere notifiche
 *  5. Aggiornare il log dei risultati
 *
 * NON si occupa di: logica dei thread, calcolo velocità, avanzamento.
 * Quello è compito di Macchine e GestoreGara.
 */
public class FRM_Gara extends JFrame implements GestoreGara.AscoltatoreGara {

    private static final int MIN_MACCHINE = 2;
    private static final int MAX_MACCHINE = 6;
    private static final int DEF_MACCHINE = 3;

    // ─── Componenti GUI ───────────────────────────────────────────────────────
    private final List<PannelloConfigurazione> pannelliConfigurazione = new ArrayList<>();
    private final List<BarraGara>              barre                  = new ArrayList<>();
    private JPanel    pannelloConfig;
    private JPanel    pannelloPista;
    private JTextArea logRisultati;
    private JSpinner  spnNumeroDiMacchine;
    private JButton   btnPartenza;
    private JButton   btnStop;

    // ─── Logica ───────────────────────────────────────────────────────────────
    private final GestoreGara gestoreGara = new GestoreGara();
    private boolean garaInCorso = false;

    // ─── Costruttore ──────────────────────────────────────────────────────────

    public FRM_Gara() {
        gestoreGara.setAscoltatore(this);
        inizializzaInterfaccia();
        aggiornaNumeroDiMacchine(DEF_MACCHINE);
    }

    // ─── Costruzione interfaccia ──────────────────────────────────────────────

    private void inizializzaInterfaccia() {
        setTitle("Gara di Macchine - Progetto Thread");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(880, 580);
        setMinimumSize(new Dimension(780, 480));
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);

        JPanel radice = (JPanel) getContentPane();
        radice.setLayout(new BorderLayout(8, 8));
        radice.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        radice.setBackground(Color.WHITE);

        // ── Pannello sinistro (configurazione + pista) ────────────────────────
        JPanel pnlSinistra = new JPanel(new BorderLayout(0, 6));
        pnlSinistra.setBackground(Color.WHITE);
        radice.add(pnlSinistra, BorderLayout.CENTER);

        // Sezione configurazione (in alto a sinistra)
        pannelloConfig = new JPanel();
        pannelloConfig.setLayout(new BoxLayout(pannelloConfig, BoxLayout.Y_AXIS));
        pannelloConfig.setBackground(Color.WHITE);
        pannelloConfig.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180)),
            "Configura Macchine",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("Arial", Font.BOLD, 12)));

        JScrollPane scrollConfig = new JScrollPane(pannelloConfig);
        scrollConfig.setBorder(null);
        scrollConfig.setPreferredSize(new Dimension(0, 190));
        scrollConfig.getViewport().setBackground(Color.WHITE);
        pnlSinistra.add(scrollConfig, BorderLayout.NORTH);

        // Sezione pista (barre di avanzamento, in basso a sinistra)
        pannelloPista = new JPanel();
        pannelloPista.setLayout(new BoxLayout(pannelloPista, BoxLayout.Y_AXIS));
        pannelloPista.setBackground(Color.WHITE);
        pannelloPista.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180)),
            "Pista",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("Arial", Font.BOLD, 12)));

        JScrollPane scrollPista = new JScrollPane(pannelloPista);
        scrollPista.setBorder(null);
        scrollPista.getViewport().setBackground(Color.WHITE);
        pnlSinistra.add(scrollPista, BorderLayout.CENTER);

        // ── Pannello destro (log + controlli) ─────────────────────────────────
        JPanel pnlDestra = costruisciPannelloDestra();
        pnlDestra.setPreferredSize(new Dimension(225, 0));
        radice.add(pnlDestra, BorderLayout.EAST);
    }

    private JPanel costruisciPannelloDestra() {
        JPanel pnl = new JPanel(new BorderLayout(0, 8));
        pnl.setBackground(Color.WHITE);

        // ── Spinner numero macchine ───────────────────────────────────────────
        JPanel pnlAlto = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        pnlAlto.setBackground(Color.WHITE);
        JLabel lbl = new JLabel("Num. Macchine:");
        lbl.setFont(new Font("Arial", Font.BOLD, 12));
        pnlAlto.add(lbl);
        spnNumeroDiMacchine = new JSpinner(
            new SpinnerNumberModel(DEF_MACCHINE, MIN_MACCHINE, MAX_MACCHINE, 1));
        spnNumeroDiMacchine.setPreferredSize(new Dimension(55, 24));
        spnNumeroDiMacchine.addChangeListener(e -> {
            if (!garaInCorso)
                aggiornaNumeroDiMacchine((int) spnNumeroDiMacchine.getValue());
        });
        pnlAlto.add(spnNumeroDiMacchine);
        pnl.add(pnlAlto, BorderLayout.NORTH);

        // ── Log risultati ─────────────────────────────────────────────────────
        logRisultati = new JTextArea("Configura le macchine e\npremi Partenza!\n");
        logRisultati.setEditable(false);
        logRisultati.setFont(new Font("Courier New", Font.PLAIN, 12));
        logRisultati.setBackground(new Color(248, 248, 248));
        logRisultati.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
        JScrollPane scrollLog = new JScrollPane(logRisultati);
        scrollLog.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        pnl.add(scrollLog, BorderLayout.CENTER);

        // ── Bottoni ───────────────────────────────────────────────────────────
        JPanel pnlBottoni = new JPanel(new GridLayout(2, 1, 0, 6));
        pnlBottoni.setBackground(Color.WHITE);
        btnPartenza = bottoneColorato("Partenza!", new Color(0, 150, 70), Color.BLACK);
        btnPartenza.addActionListener(this::alClickPartenza);
        btnStop     = bottoneColorato("Stop",      new Color(200, 40,  40), Color.BLACK);
        btnStop.setEnabled(false);
        btnStop.addActionListener(e -> fermaGara());
        pnlBottoni.add(btnPartenza);
        pnlBottoni.add(btnStop);
        pnl.add(pnlBottoni, BorderLayout.SOUTH);

        return pnl;
    }

    // ─── Gestione numero macchine ─────────────────────────────────────────────

    private void aggiornaNumeroDiMacchine(int numero) {
        pannelliConfigurazione.clear();
        pannelloConfig.removeAll();
        for (int i = 0; i < numero; i++) {
            PannelloConfigurazione pnl = new PannelloConfigurazione(i + 1);
            pannelliConfigurazione.add(pnl);
            pannelloConfig.add(pnl);
        }
        pannelloConfig.revalidate();
        pannelloConfig.repaint();
    }

    // ─── Gestione eventi ──────────────────────────────────────────────────────

    private void alClickPartenza(ActionEvent e) {
        if (garaInCorso) return;

        gestoreGara.resetta();
        barre.clear();
        pannelloPista.removeAll();
        logRisultati.setText("Gara iniziata!\n\n");

        for (PannelloConfigurazione cfg : pannelliConfigurazione) {
            ModelloVeicolo modello = cfg.getModelloSelezionato();
            if (modello == null) continue;
            boolean tuned = cfg.isTuned();

            BarraGara barra = new BarraGara(modello, tuned);
            barra.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
            barre.add(barra);
            pannelloPista.add(barra);
            pannelloPista.add(Box.createVerticalStrut(4));

            Macchine macchina = new Macchine(modello, tuned, barra, gestoreGara);
            gestoreGara.aggiungeMacchina(macchina);
        }

        pannelloPista.revalidate();
        pannelloPista.repaint();

        setConfigurazioneAbilitata(false);
        btnPartenza.setEnabled(false);
        btnStop.setEnabled(true);
        spnNumeroDiMacchine.setEnabled(false);
        garaInCorso = true;

        gestoreGara.avviaGara();
    }

    private void fermaGara() {
        gestoreGara.fermaGara();
        garaInCorso = false;
        aggiungiLog("Gara interrotta.\n");
        ripristinaControlli();
    }

    // ─── Implementazione AscoltatoreGara ──────────────────────────────────────

    @Override
    public void alTermineMacchina(String nome, int posizione) {
        String pos = posizione == 1 ? "1°" : posizione == 2 ? "2°" : posizione == 3 ? "3°" : posizione + "°";
        SwingUtilities.invokeLater(() -> logRisultati.append(pos + " - " + nome + "\n"));
    }

    @Override
    public void alTermineGara(List<String> risultatiFinali) {
        SwingUtilities.invokeLater(() -> {
            garaInCorso = false;
            logRisultati.append("\nGara completata!\n");
            ripristinaControlli();
            if (!risultatiFinali.isEmpty()) {
                String vincitore = risultatiFinali.get(0).replaceFirst("^1 posto - ", "");
                // Unico uso di Unicode/emoji: la coppa nel dialogo del vincitore
                JOptionPane.showMessageDialog(this,
                    "\uD83C\uDFC6 Vincitore: " + vincitore,
                    "Gara Terminata",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    // ─── Metodi di supporto ───────────────────────────────────────────────────

    private void aggiungiLog(String messaggio) {
        SwingUtilities.invokeLater(() -> logRisultati.append(messaggio));
    }

    private void setConfigurazioneAbilitata(boolean abilitata) {
        for (PannelloConfigurazione pnl : pannelliConfigurazione)
            pnl.setTuttoAbilitato(abilitata);
    }

    private void ripristinaControlli() {
        setConfigurazioneAbilitata(true);
        btnPartenza.setEnabled(true);
        btnPartenza.setText("Nuova Gara");
        btnStop.setEnabled(false);
        spnNumeroDiMacchine.setEnabled(true);
    }

    private JButton bottoneColorato(String testo, Color sfondo, Color testColor) {
        JButton b = new JButton(testo);
        b.setBackground(sfondo);
        b.setForeground(testColor);
        b.setFont(new Font("Arial", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
}
