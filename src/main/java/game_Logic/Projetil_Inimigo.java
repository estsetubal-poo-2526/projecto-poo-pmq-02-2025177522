package game_Logic;

public class Projetil_Inimigo extends Projetil {

    public static final double LARGURA = 6;
    public static final double ALTURA = 14;
    public static final double VELOCIDADE_BASE = 4.0;

    public enum TipoTrajeto {
        ZIGZAG,
        ROLO_LENTO
    }

    private TipoTrajeto tipoTrajeto;
    private double tick; // controla a oscilação do zigzag

    public Projetil_Inimigo(double x, double y, TipoTrajeto tipo) {
        super(x, y, LARGURA, ALTURA, resolverVelocidade(tipo));
        this.tipoTrajeto = tipo;
        this.tick = 0;
    }

    private static double resolverVelocidade(TipoTrajeto tipo) {
        return tipo == TipoTrajeto.ROLO_LENTO ? VELOCIDADE_BASE * 0.6 : VELOCIDADE_BASE;
    }

    /**
     * O zigzag oscila horizontalmente enquanto desce.
     * O rolo lento desce a velocidade reduzida, em linha reta.
     */
    @Override
    public void moverVerticalmente() {
        this.y += velocidadeY;
        if (tipoTrajeto == TipoTrajeto.ZIGZAG) {
            tick += 0.3;
            this.x += Math.sin(tick) * 3.0;
        }
    }

    public TipoTrajeto getTipoTrajeto() { return tipoTrajeto; }
}
