package progettothreadsgara;

/**
 * Categorie per il raggruppamento nella ComboBox a cascata
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
