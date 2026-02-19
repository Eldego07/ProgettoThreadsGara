package progettothreadsgara;

import java.awt.Color;
import java.util.Arrays;

/**
 * Ogni modello ha velocità basata sulle prestazioni reali (0-100 km/h).
 * Parametri: ritardoBase (ms), variazioneRitardo (ms), passoMin/Max (%),
 * nomeFile (immagine). Il tuned riduce il ritardo del 25% (moltiplicatore
 * 0.75).
 */
public enum ModelloVeicolo {

    // Auto da Corsa
    FERRARI("Ferrari F40", CategoriaVeicolo.DA_CORSA, new Color(204, 0, 0), 38, 16, 3, 7, "Ferrari_F40.png"),
    LAMBORGHINI("Lamborghini Huracan", CategoriaVeicolo.DA_CORSA, new Color(230, 230, 230), 32, 14, 4, 7, "Lamborghini_Huracan.png"),
    MCLAREN("McLaren P1", CategoriaVeicolo.DA_CORSA, new Color(220, 140, 0), 30, 12, 4, 8, "McLaren_P1.png"),
    BUGATTI("Bugatti Chiron", CategoriaVeicolo.DA_CORSA, new Color(30, 150, 220), 25, 10, 4, 8, "Bugatti_Chiron.png"),
    PORSCHE_911("Porsche 911 GT3", CategoriaVeicolo.DA_CORSA, new Color(20, 20, 20), 36, 15, 3, 7, "Porsche_911_GT3.png"),
    AUDI_R8("Audi R8 V10", CategoriaVeicolo.DA_CORSA, new Color(180, 180, 180), 34, 14, 3, 7, "Audi_R8_V10.png"),
    NISSAN_GTR("Nissan GT-R", CategoriaVeicolo.DA_CORSA, new Color(180, 0, 0), 35, 15, 3, 7, "Nissan_GT_R.png"),
    CORVETTE("Corvette C8", CategoriaVeicolo.DA_CORSA, new Color(100, 180, 230), 37, 16, 3, 7, "Corvette_C8.png"),
    // Sleeper
    HONDA_CIVIC("Honda Civic Type R", CategoriaVeicolo.SLEEPER, new Color(180, 0, 0), 58, 25, 2, 5, "Honda_Civic_Type_R.png"),
    SUBARU("Subaru Impreza WRX", CategoriaVeicolo.SLEEPER, new Color(20, 100, 200), 52, 22, 2, 5, "Subaru_Impreza_WRX.png"),
    FORD_FOCUS("Ford Focus RS", CategoriaVeicolo.SLEEPER, new Color(30, 160, 220), 50, 20, 2, 5, "Ford_Focus_RS.png"),
    VOLKSWAGEN("VW Golf GTI", CategoriaVeicolo.SLEEPER, new Color(190, 0, 0), 68, 30, 1, 4, "VW_Golf_GTI.png"),
    AUDI_RS3("Audi RS3", CategoriaVeicolo.SLEEPER, new Color(50, 50, 50), 45, 20, 2, 5, "Audi_RS3.png"),
    MERCEDES_A45("Mercedes-AMG A45", CategoriaVeicolo.SLEEPER, new Color(255, 220, 0), 48, 22, 2, 5, "Mercedes_AMG_A45.png"),
    ALFA_GIULIA("Alfa Romeo Giulia QV", CategoriaVeicolo.SLEEPER, new Color(0, 100, 200), 44, 19, 2, 5, "Alfa_Romeo_Giulia_QV.png"),
    BMW_M3("BMW M3", CategoriaVeicolo.SLEEPER, new Color(220, 220, 220), 43, 18, 2, 6, "BMW_M3.png"),
    // SUV
    RANGE_ROVER("Range Rover Sport", CategoriaVeicolo.SUV, new Color(180, 195, 210), 60, 25, 1, 4, "Range_Rover_Sport.png"),
    JEEP("Jeep Wrangler", CategoriaVeicolo.SUV, new Color(110, 110, 110), 92, 40, 1, 3, "Jeep_Wrangler.png"),
    BMW_X5("BMW X5 M", CategoriaVeicolo.SUV, new Color(10, 10, 10), 42, 18, 3, 6, "BMW_X5_M.png"),
    PORSCHE_CAY("Porsche Cayenne Turbo", CategoriaVeicolo.SUV, new Color(35, 35, 50), 40, 16, 3, 6, "Porsche_Cayenne_Turbo.png"),
    MERCEDES_GLE("Mercedes-AMG GLE 63", CategoriaVeicolo.SUV, new Color(30, 30, 30), 44, 19, 2, 5, "Mercedes_AMG_GLE_63.png"),
    AUDI_SQ7("Audi SQ7", CategoriaVeicolo.SUV, new Color(90, 90, 90), 48, 21, 2, 5, "Audi_SQ7.png"),
    TESLA_X("Tesla Model X Plaid", CategoriaVeicolo.SUV, new Color(0, 80, 180), 28, 12, 4, 7, "Tesla_Model_X_Plaid.png"),
    LAMBO_URUS("Lamborghini Urus", CategoriaVeicolo.SUV, new Color(230, 80, 0), 38, 16, 3, 6, "Lamborghini_Urus.png");

    private final String nome;
    private final CategoriaVeicolo categoria;
    private final Color colore;
    private final int ritardoBase;
    private final int variazioneRitardo;
    private final int passoMinimo;
    private final int passoMassimo;
    private final String nomeFile;

    ModelloVeicolo(String nome, CategoriaVeicolo categoria, Color colore,
            int ritardoBase, int variazioneRitardo, int passoMinimo, int passoMassimo,
            String nomeFile) {
        this.nome = nome;
        this.categoria = categoria;
        this.colore = colore;
        this.ritardoBase = ritardoBase;
        this.variazioneRitardo = variazioneRitardo;
        this.passoMinimo = passoMinimo;
        this.passoMassimo = passoMassimo;
        this.nomeFile = nomeFile;
    }

    public String getNome() {
        return nome;
    }

    public CategoriaVeicolo getCategoria() {
        return categoria;
    }

    public Color getColore() {
        return colore;
    }

    public int getRitardoBase() {
        return ritardoBase;
    }

    public int getVariazioneRitardo() {
        return variazioneRitardo;
    }

    public int getPassoMinimo() {
        return passoMinimo;
    }

    public int getPassoMassimo() {
        return passoMassimo;
    }

    public String getNomeFile() {
        return nomeFile;
    }

    public static ModelloVeicolo[] getPerCategoria(CategoriaVeicolo cat) {
        return Arrays.stream(values())
                .filter(m -> m.categoria == cat)
                .toArray(ModelloVeicolo[]::new);
    }

    @Override
    public String toString() {
        return nome;
    }
}
