package game_Logic;

/**
 * Inimigo da fila de trás (linha 0 da grelha).
 * Vale 30 pontos — o mais valioso da frota regular.
 */
public class Inimigo_Tras extends Inimigo {

    public static final int PONTOS = 30;

    /**
     * @param x      posição X inicial
     * @param y      posição Y inicial
     * @param coluna índice de coluna na grelha (0–10)
     */
    public Inimigo_Tras(double x, double y, int coluna) {
        super(x, y, PONTOS, coluna);
    }
}
