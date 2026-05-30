package game_Logic;

/**
 * Inimigo das filas da frente (linhas 3–4 da grelha).
 * Vale 10 pontos.
 */
public class Inimigo_Frente extends Inimigo {

    public static final int PONTOS = 10;

    /**
     * @param x      posição X inicial
     * @param y      posição Y inicial
     * @param coluna índice de coluna na grelha (0–10)
     */
    public Inimigo_Frente(double x, double y, int coluna) {
        super(x, y, PONTOS, coluna);
    }
}
