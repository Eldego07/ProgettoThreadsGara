package progettothreadsgara;

import java.util.Random;

public class Macchine implements java.lang.Runnable {

    private final ModelloVeicolo modello;
    private final boolean tuned;         // solo estetico, non influenza velocità
    private volatile int progresso = 0; // volatile: visibile a tutti i thread
    private final BarraGara barra;
    private final GestoreGara gestoreGara;
    private final Random casuale = new Random();

    /**
     * @param modello Il modello della macchina (determina velocità e immagine)
     * @param tuned True se la macchina è "tuned" (solo visivo, nessun bonus)
     * @param barra La barra grafica da aggiornare durante la gara
     * @param gestoreGara Il gestore della gara da notificare al traguardo
     */
    public Macchine(ModelloVeicolo modello, boolean tuned, BarraGara barra, GestoreGara gestoreGara) {
        this.modello = modello;
        this.tuned = tuned;
        this.barra = barra;
        this.gestoreGara = gestoreGara;
    }

    /**
     * Eseguito in parallelo per ogni macchina. Thread.sleep() può essere
     * interrotto da gestoreGara.fermaGara() → in quel caso,
     * InterruptedException ferma il ciclo pulitamente.
     */
    @Override
    public void run() {
        while (progresso < 100 && !Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(calcolaRitardo());

                // Calcola il passo di avanzamento casuale
                int passo = modello.getPassoMinimo()
                        + casuale.nextInt(modello.getPassoMassimo() - modello.getPassoMinimo() + 1);
                progresso = Math.min(100, progresso + passo);

                // Aggiorna la barra grafica (thread-safe via invokeLater in BarraGara)
                barra.aggiornaValore(progresso);

                if (progresso >= 100) {
                    gestoreGara.notificaArrivo(this);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // re-imposta il flag di interruzione
            }
        }
    }

    /**
     * Calcola i millisecondi di attesa per questo passo. Formula: ritardoBase +
     * casuale(0..variazioneRitardo) Math.max(10) garantisce almeno 10ms per non
     * saturare la CPU.
     */
    private int calcolaRitardo() {
        int ritardo = modello.getRitardoBase() + casuale.nextInt(modello.getVariazioneRitardo() + 1);

        // Se la macchina è tuned, riduciamo il ritardo (quindi aumenta la velocità)
        if (tuned) {
            ritardo = (int) (ritardo * 0.75); // 25% di velocità in più
        }

        return Math.max(10, ritardo);
    }

    public ModelloVeicolo getModello() {
        return modello;
    }

    public boolean isTuned() {
        return tuned;
    }

    public int getProgresso() {
        return progresso;
    }

    /**
     * Nome completo: aggiunge "(Tuned)" se selezionato
     */
    public String getNomeCompleto() {
        return modello.getNome() + (tuned ? " (Tuned)" : "");
    }
}
