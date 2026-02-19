package progettothreadsgara;

import java.util.Random;

/**
 * Macchina che corre in parallelo. Il tuned riduce il ritardo del 25%.
 */
public class Macchine implements java.lang.Runnable {

    private final ModelloVeicolo modello;
    private final boolean tuned;
    private volatile int progresso = 0;
    private final BarraGara barra;
    private final GestoreGara gestoreGara;
    private final Random casuale = new Random();
    private long tempoInizio;

    public Macchine(ModelloVeicolo modello, boolean tuned,
            BarraGara barra, GestoreGara gestoreGara) {
        this.modello = modello;
        this.tuned = tuned;
        this.barra = barra;
        this.gestoreGara = gestoreGara;
    }

    @Override
    public void run() {
        tempoInizio = System.currentTimeMillis();

        while (progresso < 100 && !Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(calcolaRitardo());

                int passo = modello.getPassoMinimo()
                        + casuale.nextInt(modello.getPassoMassimo() - modello.getPassoMinimo() + 1);
                progresso = Math.min(100, progresso + passo);

                barra.aggiornaValore(progresso);

                if (progresso >= 100) {
                    long tempoTotale = System.currentTimeMillis() - tempoInizio;
                    double secondi = tempoTotale / 1000.0;
                    gestoreGara.notificaArrivo(this, secondi);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private int calcolaRitardo() {
        int ritardo = modello.getRitardoBase() + casuale.nextInt(modello.getVariazioneRitardo() + 1);

        if (tuned) {
            ritardo = (int) (ritardo * 0.75);
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

    public String getNomeCompleto() {
        return modello.getNome() + (tuned ? " (Tuned)" : "");
    }
}
