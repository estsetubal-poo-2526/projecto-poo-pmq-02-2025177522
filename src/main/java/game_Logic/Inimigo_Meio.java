package game_Logic;

/**
 * Inimigo das filas do meio (linhas 1–2 da grelha).
 * Vale 20 pontos.
 */
public class Inimigo_Meio extends Inimigo {

    public static final int PONTOS = 20;

    /**
     * @param x      posição X inicial
     * @param y      posição Y inicial
     * @param coluna índice de coluna na grelha (0–10)
     */
    public Inimigo_Meio(double x, double y, int coluna) {
        super(x, y, PONTOS, coluna);
    }
}
