package game_Logic;

import java.util.Random;

/**
 * Invasor bónus que atravessa o topo do ecrã horizontalmente.
 * Ao ser abatido concede pontos variáveis (50–300).
 *
 * <p>Não pertence à grelha da frota, por isso o índice de coluna
 * passado à superclasse é -1 (valor sentinela sem significado).
 *
 * <p>Herda o estado de vida de {@link Inimigo} — não declara
 * campo 'vivo' próprio para evitar sombreamento do campo da superclasse.
 */
public class Inimigo_aleatorio extends Inimigo {

    public static final double LARGURA    = 52;
    public static final double ALTURA     = 24;
    public static final double VELOCIDADE = 3.5;

    /** Valores de pontos possíveis, sorteados ao criar o invasor. */
    private static final int[] PONTOS_POSSIVEIS = {50, 100, 150, 200, 300};

    /** Pontuação atribuída ao jogador que destruir este invasor. */
    private final int pontosVariaveis;

    /** +1 move para a direita, -1 move para a esquerda. */
    private final double direcao;

    private static final Random random = new Random();

    /**
     * @param xInicial posição horizontal de entrada (fora do ecrã)
     * @param y        posição vertical fixa (geralmente perto do topo)
     * @param direcao  +1 para direita, -1 para esquerda
     */
    public Inimigo_aleatorio(double xInicial, double y, double direcao) {
        // coluna = -1 porque este inimigo não pertence à grelha
        super(xInicial, y, 0, -1);
        this.direcao         = direcao;
        this.pontosVariaveis = PONTOS_POSSIVEIS[random.nextInt(PONTOS_POSSIVEIS.length)];
    }

    /**
     * Move o invasor na horizontal à velocidade constante.
     * A direção determina o sentido do movimento.
     */
    @Override
    public void mover() {
        this.x += VELOCIDADE * direcao;
    }

    /**
     * Devolve a pontuação variável deste invasor.
     * Deve ser consultado antes de chamar {@link #destruir()}.
     */
    public int getPontosVariaveis() {
        return pontosVariaveis;
    }
}
