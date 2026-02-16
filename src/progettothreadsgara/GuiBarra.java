package progettothreadsgara;

import javax.swing.*;

/**
 * Classe per gestire la barra di progresso di una macchina
 */
public class GuiBarra {

    private JProgressBar progressBar;
    private JLabel label;

    public GuiBarra(JProgressBar progressBar, JLabel label) {
        this.progressBar = progressBar;
        this.label = label;
    }

    public void setProgresso(String nome, int progresso) {
        SwingUtilities.invokeLater(() -> {
            progressBar.setValue(progresso);
            label.setText(nome + ": " + progresso + "%");
        });
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public JLabel getLabel() {
        return label;
    }
}