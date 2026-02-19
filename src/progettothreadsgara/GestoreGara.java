package progettothreadsgara;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Gestisce la gara e calcola i distacchi in secondi.
 */
public class GestoreGara {

    public interface AscoltatoreGara {

        void alTermineMacchina(String nomeMacchina, int posizione, double tempoSecondi, double distaccoSecondi);

        void alTermineGara(List<String> risultatiFinali);
    }

    private final List<Macchine> macchine = new ArrayList<>();
    private final List<Thread> thread = new ArrayList<>();
    private final AtomicInteger posizioneArrivo = new AtomicInteger(0);
    private final List<String> risultati = new ArrayList<>();
    private AscoltatoreGara ascoltatore;
    private double tempoPrimo = 0;

    public void setAscoltatore(AscoltatoreGara a) {
        this.ascoltatore = a;
    }

    public void aggiungeMacchina(Macchine macchina) {
        macchine.add(macchina);
        thread.add(new Thread(macchina, macchina.getNomeCompleto()));
    }

    public void avviaGara() {
        posizioneArrivo.set(0);
        risultati.clear();
        tempoPrimo = 0;
        for (Thread t : thread) {
            t.start();
        }
    }

    public void fermaGara() {
        for (Thread t : thread) {
            t.interrupt();
        }
    }

    public synchronized void notificaArrivo(Macchine macchina, double tempoSecondi) {
        int pos = posizioneArrivo.incrementAndGet();

        if (pos == 1) {
            tempoPrimo = tempoSecondi;
        }

        double distacco = pos == 1 ? 0 : tempoSecondi - tempoPrimo;
        String ris = String.format("%d posto - %s - %.1fs%s",
                pos,
                macchina.getNomeCompleto(),
                tempoSecondi,
                pos == 1 ? "" : String.format(" [+%.1fs]", distacco));
        risultati.add(ris);

        if (ascoltatore != null) {
            ascoltatore.alTermineMacchina(macchina.getNomeCompleto(), pos, tempoSecondi, distacco);
            if (pos == macchine.size()) {
                ascoltatore.alTermineGara(new ArrayList<>(risultati));
            }
        }
    }

    public void resetta() {
        fermaGara();
        macchine.clear();
        thread.clear();
        risultati.clear();
        posizioneArrivo.set(0);
        tempoPrimo = 0;
    }

    public int getNumeroDiMacchine() {
        return macchine.size();
    }
}
