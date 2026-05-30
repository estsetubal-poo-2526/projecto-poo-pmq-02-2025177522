package game_Logic;

/**
 * Superclasse abstrata de todos os inimigos da frota.
 *
 * <p>Guarda o índice de coluna original da grelha para que
 * {@link FrotaInimigo} possa identificar qual o inimigo mais
 * à frente em cada coluna sem depender da posição X (que muda
 * a cada frame com o movimento em bloco).
 */
public abstract class Inimigo extends EntidadeJogo {

    public static final double LARGURA = 36;
    public static final double ALTURA  = 28;

    /** Pontuação atribuída ao jogador que destruir este inimigo. */
    protected final int valorPontos;

    /** Índice de coluna (0–10) na grelha inicial. Nunca muda. */
    private final int coluna;

    /** {@code false} após {@link #destruir()} ser chamado. */
    private boolean vivo;

    /**
     * @param x          posição X inicial
     * @param y          posição Y inicial
     * @param valorPontos pontuação concedida ao abater este inimigo
     * @param coluna     índice de coluna na grelha (0 a {@link FrotaInimigo#COLUNAS}-1)
     */
    public Inimigo(double x, double y, int valorPontos, int coluna) {
        super(x, y, LARGURA, ALTURA);
        this.valorPontos = valorPontos;
        this.coluna      = coluna;
        this.vivo        = true;
    }

    /**
     * Marca o inimigo como destruído.
     * Após esta chamada {@link #isVivo()} devolve {@code false}.
     */
    public void destruir() {
        this.vivo = false;
    }

    /** @return {@code true} enquanto o inimigo não tiver sido destruído */
    public boolean isVivo() {
        return vivo;
    }

    /** @return pontuação concedida ao abater este inimigo */
    public int getValorPontos() {
        return valorPontos;
    }

    /** @return índice de coluna original na grelha (imutável) */
    public int getColuna() {
        return coluna;
    }

    /**
     * O movimento individual não é gerido pelo inimigo.
     * A {@link FrotaInimigo} move todos os inimigos em bloco.
     */
    @Override
    public void mover() {
        // Delegado à FrotaInimigo
    }
}
