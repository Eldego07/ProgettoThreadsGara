package progettothreadsgara;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class GestoreGara {

    /**
     * Interfaccia implementata da FRM_Gara per ricevere notifiche dalla gara.
     */
    public interface AscoltatoreGara {
        /** Chiamato quando una macchina taglia il traguardo */
        void alTermineMacchina(String nomeMacchina, int posizione);
        /** Chiamato quando tutte le macchine hanno terminato */
        void alTermineGara(List<String> risultatiFinali);
    }

    private final List<Macchine>  macchine       = new ArrayList<>();
    private final List<Thread>    thread         = new ArrayList<>();
    private final AtomicInteger   posizioneArrivo = new AtomicInteger(0);
    private final List<String>    risultati       = new ArrayList<>();
    private AscoltatoreGara       ascoltatore;


    /** Registra l'ascoltatore (solitamente FRM_Gara) */
    public void setAscoltatore(AscoltatoreGara a) { this.ascoltatore = a; }

    /**
     * Aggiunge una macchina alla gara e crea il Thread corrispondente.
     * Il Thread non viene avviato qui, ma in avviaGara().
     *
     * @param macchina La macchina da aggiungere
     */
    public void aggiungeMacchina(Macchine macchina) {
        macchine.add(macchina);
        // Il nome del thread è utile per il debug
        thread.add(new Thread(macchina, macchina.getNomeCompleto()));
    }

    /**
     * Avvia la gara: resetta lo stato e fa partire tutti i Thread.
     * Da questo momento le macchine avanzano in parallelo.
     */
    public void avviaGara() {
        posizioneArrivo.set(0);
        risultati.clear();
        for (Thread t : thread) t.start();
    }

    /**
     * Ferma la gara interrompendo tutti i Thread.
     * Ogni Thread riceverà InterruptedException e uscirà dal ciclo.
     */
    public void fermaGara() {
        for (Thread t : thread) t.interrupt();
    }

    /**
     * Chiamato da una Macchina quando raggiunge progresso == 100.
     *
     * synchronized: garantisce che due macchine che arrivano nello stesso
     * millisecondo non ottengano la stessa posizione.
     *
     * @param macchina La macchina che ha tagliato il traguardo
     */
    public synchronized void notificaArrivo(Macchine macchina) {
        int pos      = posizioneArrivo.incrementAndGet();
        String ris   = pos + " posto - " + macchina.getNomeCompleto();
        risultati.add(ris);

        if (ascoltatore != null) {
            ascoltatore.alTermineMacchina(macchina.getNomeCompleto(), pos);
            // Se tutte le macchine sono arrivate, notifica il completamento
            if (pos == macchine.size()) {
                ascoltatore.alTermineGara(new ArrayList<>(risultati));
            }
        }
    }

    /** Resetta completamente per una nuova gara */
    public void resetta() {
        fermaGara();
        macchine.clear();
        thread.clear();
        risultati.clear();
        posizioneArrivo.set(0);
    }

    public int getNumeroDiMacchine() { return macchine.size(); }
}
