package progettothreadsgara;

import javax.swing.*;
import java.awt.*;

/**
 * Pannello di configurazione per una singola macchina.
 * Contiene: etichetta "Macchina N", ComboBox categoria, ComboBox modello, Radio Normale/Tuned.
 *
 * ComboBox a cascata: quando cambia la categoria, la ComboBox dei modelli
 * si aggiorna automaticamente mostrando solo i modelli di quella categoria.
 */
public class PannelloConfigurazione extends JPanel {

    private final JComboBox<CategoriaVeicolo> cbCategoria;
    private final JComboBox<ModelloVeicolo>   cbModello;
    private final JRadioButton                rbNormale;
    private final JRadioButton                rbTuned;

    public PannelloConfigurazione(int numero) {
        setLayout(new FlowLayout(FlowLayout.LEFT, 6, 4));
        setBackground(Color.WHITE);

        JLabel etichetta = new JLabel("Macchina " + numero + ":");
        etichetta.setFont(new Font("Arial", Font.BOLD, 12));
        etichetta.setPreferredSize(new Dimension(80, 24));
        add(etichetta);

        cbCategoria = new JComboBox<>(CategoriaVeicolo.values());
        cbCategoria.setPreferredSize(new Dimension(115, 24));
        cbCategoria.setFont(new Font("Arial", Font.PLAIN, 12));
        cbCategoria.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(
                    JList<?> l, Object val, int idx, boolean sel, boolean foc) {
                super.getListCellRendererComponent(l, val, idx, sel, foc);
                if (val instanceof CategoriaVeicolo)
                    setText(((CategoriaVeicolo) val).getNome());
                return this;
            }
        });
        add(cbCategoria);
        
        cbModello = new JComboBox<>();
        cbModello.setPreferredSize(new Dimension(190, 24));
        cbModello.setFont(new Font("Arial", Font.PLAIN, 12));
        popolaModelli();
        // Aggiorna i modelli ogni volta che cambia la categoria
        cbCategoria.addActionListener(e -> popolaModelli());
        add(cbModello);

        rbNormale = new JRadioButton("Normale");
        rbTuned   = new JRadioButton("Tuned");
        rbNormale.setSelected(true);
        rbNormale.setBackground(Color.WHITE);
        rbTuned.setBackground(Color.WHITE);
        rbNormale.setFont(new Font("Arial", Font.PLAIN, 12));
        rbTuned.setFont(new Font("Arial", Font.PLAIN, 12));
        ButtonGroup gruppoTuned = new ButtonGroup();
        gruppoTuned.add(rbNormale);
        gruppoTuned.add(rbTuned);
        add(rbNormale);
        add(rbTuned);
    }


    /** Popola la ComboBox dei modelli in base alla categoria selezionata */
    private void popolaModelli() {
        CategoriaVeicolo cat = (CategoriaVeicolo) cbCategoria.getSelectedItem();
        cbModello.removeAllItems();
        if (cat != null)
            for (ModelloVeicolo m : ModelloVeicolo.getPerCategoria(cat))
                cbModello.addItem(m);
    }

    public ModelloVeicolo getModelloSelezionato() { return (ModelloVeicolo) cbModello.getSelectedItem(); }
    public boolean        isTuned()               { return rbTuned.isSelected(); }

    /** Abilita o disabilita tutti i controlli (durante la gara vengono disabilitati) */
    public void setTuttoAbilitato(boolean abilitato) {
        cbCategoria.setEnabled(abilitato);
        cbModello.setEnabled(abilitato);
        rbNormale.setEnabled(abilitato);
        rbTuned.setEnabled(abilitato);
    }
}
