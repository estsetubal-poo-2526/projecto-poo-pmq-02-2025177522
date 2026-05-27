package game_Logic;
import java.util.Random;

public class Inimigo_aleatorio extends EntidadeJogo {

    public static final double LARGURA = 52;
    public static final double ALTURA = 24;
    public static final double VELOCIDADE = 3.5;

    private static final int PONTOS_MIN = 50;
    private static final int PONTOS_MAX = 300;
    private static final int[] PONTOS_POSSIVEIS = {50, 100, 150, 200, 300};

    private int pontosVariaveis;
    private boolean vivo;
    private double direcao; // +1 = direita, -1 = esquerda

    private static final Random random = new Random();

    public Inimigo_aleatorio(double xInicial, double y, double direcao) {
        super(xInicial, y, LARGURA, ALTURA);
        this.direcao = direcao;
        this.vivo = true;
        this.pontosVariaveis = PONTOS_POSSIVEIS[random.nextInt(PONTOS_POSSIVEIS.length)];
    }

    /** Move-se rapidamente na horizontal. */
    @Override
    public void mover() {
        moverRapidamente();
    }

    public void moverRapidamente() {
        this.x += VELOCIDADE * direcao;
    }

    public void destruir() { this.vivo = false; }

    public boolean isVivo() { return vivo; }
    public int getPontosVariaveis() { return pontosVariaveis; }
}

