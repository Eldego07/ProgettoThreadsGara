package progettothreadsgara;

import java.util.Random;

/**
 * Classe che rappresenta una macchina nella gara
 */
public class Macchine implements Runnable {

    public enum TipoMacchina {
        DA_CORSA, SLEEPER, SUV
    }

    private TipoMacchina tipo;
    private boolean tuned;
    private String nome;
    private int progresso;
    private GuiBarra barra;
    private Random random = new Random();

    public Macchine(TipoMacchina tipo, boolean tuned, String nome, GuiBarra barra) {
        this.tipo = tipo;
        this.tuned = tuned;
        this.nome = nome;
        this.progresso = 0;
        this.barra = barra;
    }

    @Override
    public void run() {
        while (progresso < 100) {
            try {
                // Simula il tempo di percorrenza basato sul tipo e se è tuned
                int delay = calcolaDelay();
                Thread.sleep(delay);

                // Aggiorna progresso
                progresso += random.nextInt(5) + 1; // Avanza di 1-5%
                if (progresso > 100) progresso = 100;

                // Aggiorna la barra
                barra.setProgresso(nome, progresso);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println(nome + " ha finito la gara!");
    }

    private int calcolaDelay() {
        int baseDelay = 100; // millisecondi base
        switch (tipo) {
            case DA_CORSA:
                baseDelay = 50;
                break;
            case SLEEPER:
                baseDelay = 150;
                break;
            case SUV:
                baseDelay = 100;
                break;
        }
        if (tuned) {
            baseDelay -= 20; // Più veloce se tuned
        }
        return baseDelay + random.nextInt(50); // Aggiungi variabilità
    }

    // Getters
    public TipoMacchina getTipo() { return tipo; }
    public boolean isTuned() { return tuned; }
    public String getNome() { return nome; }
    public int getProgresso() { return progresso; }
}