package progettothreadsgara;

/**
 * Raggruppa le macchine in categorie solo per la ComboBox a cascata. La
 * velocità reale di ogni macchina è definita in ModelloVeicolo, non qui: questa
 * enum serve solo come "etichetta di gruppo".
 */
public enum CategoriaVeicolo {
    DA_CORSA("Auto da Corsa"),
    SLEEPER("Sleeper"),
    SUV("SUV");

    private final String nome;

    CategoriaVeicolo(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return nome;
    }
}
