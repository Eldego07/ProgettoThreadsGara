package progettothreadsgara;

import java.awt.Color;
import java.util.Arrays;

/**
 * Ogni modello ha la propria velocità basata sulle prestazioni reali della vettura.
 * La categoria serve solo per il raggruppamento nella ComboBox a cascata.
 *
 * VELOCITA' REALI (0-100 km/h di riferimento):
 *  Bugatti Chiron    : 2.4s → ritardoBase=25
 *  McLaren P1        : 2.8s → ritardoBase=30
 *  Lamborghini Huracan: 2.9s → ritardoBase=32
 *  Ferrari F40       : 3.8s → ritardoBase=38
 *  Porsche Cayenne T : 3.9s → ritardoBase=40
 *  BMW X5 M          : 3.9s → ritardoBase=42
 *  Ford Focus RS     : 4.7s → ritardoBase=50
 *  Subaru WRX        : 4.8s → ritardoBase=52
 *  Honda Civic Type R: 5.4s → ritardoBase=58
 *  Range Rover Sport : 5.5s → ritardoBase=60
 *  VW Golf GTI       : 6.3s → ritardoBase=68
 *  Jeep Wrangler     : 8.9s → ritardoBase=92
 *
 * Parametri:
 *  ritardoBase      = ms base tra un avanzamento e l'altro (meno = più veloce)
 *  variazioneRitardo = variabilità casuale aggiunta al base (0..variazioneRitardo ms)
 *  passoMinimo       = % minima di avanzamento per passo
 *  passoMassimo      = % massima di avanzamento per passo
 *  nomeFile          = nome del file immagine in /Immagini/
 */
public enum ModelloVeicolo {

    // ── Auto da Corsa ─────────────── nome                        categoria              colore                   base var min max  file immagine
    FERRARI      ("Ferrari F40",          CategoriaVeicolo.DA_CORSA, new Color(204,  0,  0),  38, 16, 3, 7, "Ferrari_F40.png"),
    LAMBORGHINI  ("Lamborghini Huracan",  CategoriaVeicolo.DA_CORSA, new Color(230,230,230),  32, 14, 4, 7, "Lamborghini_Huracan.png"),
    MCLAREN      ("McLaren P1",           CategoriaVeicolo.DA_CORSA, new Color(220,140,  0),  30, 12, 4, 8, "McLaren_P1.png"),
    BUGATTI      ("Bugatti Chiron",       CategoriaVeicolo.DA_CORSA, new Color( 30,150,220),  25, 10, 4, 8, "Bugatti_Chiron.png"),

    // ── Sleeper ───────────────────────────────────────────────────────────────────────────────────────────────────────
    HONDA_CIVIC  ("Honda Civic Type R",   CategoriaVeicolo.SLEEPER,  new Color(180,  0,  0),  58, 25, 2, 5, "Honda_Civic_Type_R.png"),
    SUBARU       ("Subaru Impreza WRX",   CategoriaVeicolo.SLEEPER,  new Color( 20,100,200),  52, 22, 2, 5, "Subaru_Impreza_WRX.png"),
    FORD_FOCUS   ("Ford Focus RS",        CategoriaVeicolo.SLEEPER,  new Color( 30,160,220),  50, 20, 2, 5, "Ford_Focus_RS.png"),
    VOLKSWAGEN   ("VW Golf GTI",          CategoriaVeicolo.SLEEPER,  new Color(190,  0,  0),  68, 30, 1, 4, "VW_Golf_GTI.png"),

    // ── SUV ───────────────────────────────────────────────────────────────────────────────────────────────────────────
    RANGE_ROVER  ("Range Rover Sport",    CategoriaVeicolo.SUV,      new Color(180,195,210),  60, 25, 1, 4, "Range_Rover_Sport.png"),
    JEEP         ("Jeep Wrangler",        CategoriaVeicolo.SUV,      new Color(110,110,110),  92, 40, 1, 3, "Jeep_Wrangler.png"),
    BMW_X5       ("BMW X5 M",            CategoriaVeicolo.SUV,      new Color( 10, 10, 10),  42, 18, 3, 6, "BMW_X5_M.png"),
    PORSCHE_CAY  ("Porsche Cayenne Turbo",CategoriaVeicolo.SUV,      new Color( 35, 35, 50),  40, 16, 3, 6, "Porsche_Cayenne_Turbo.png");

    // ─── Campi ────────────────────────────────────────────────────────────────
    private final String           nome;
    private final CategoriaVeicolo categoria;
    private final Color            colore;
    private final int              ritardoBase;
    private final int              variazioneRitardo;
    private final int              passoMinimo;
    private final int              passoMassimo;
    private final String           nomeFile;

    ModelloVeicolo(String nome, CategoriaVeicolo categoria, Color colore, int ritardoBase, int variazioneRitardo, int passoMinimo, int passoMassimo, String nomeFile) {
        this.nome              = nome;
        this.categoria         = categoria;
        this.colore            = colore;
        this.ritardoBase       = ritardoBase;
        this.variazioneRitardo = variazioneRitardo;
        this.passoMinimo       = passoMinimo;
        this.passoMassimo      = passoMassimo;
        this.nomeFile          = nomeFile;
    }

    public String           getNome()             { return nome; }
    public CategoriaVeicolo getCategoria()        { return categoria; }
    public Color            getColore()           { return colore; }
    public int              getRitardoBase()      { return ritardoBase; }
    public int              getVariazioneRitardo(){ return variazioneRitardo; }
    public int              getPassoMinimo()      { return passoMinimo; }
    public int              getPassoMassimo()     { return passoMassimo; }
    public String           getNomeFile()         { return nomeFile; }

    /** Filtra i modelli per categoria — usato dalla ComboBox a cascata */
    public static ModelloVeicolo[] getPerCategoria(CategoriaVeicolo cat) {
        return Arrays.stream(values())
                     .filter(m -> m.categoria == cat)
                     .toArray(ModelloVeicolo[]::new);
    }

    @Override public String toString() { return nome; }
}
