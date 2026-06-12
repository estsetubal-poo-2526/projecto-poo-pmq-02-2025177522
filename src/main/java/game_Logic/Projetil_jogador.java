package game_Logic;

/**
 * Representa o projétil disparado pelo jogador.
 *
 * <p>Esta classe estende {@link Projetil} e define as propriedades físicas específicas
 * do armamento principal do submarino. Ao contrário dos disparos inimigos, este projétil
 * desloca-se linearmente em direção ao topo do ecrã (velocidade vertical negativa)
 * com o objetivo de destruir a frota inimiga e os invasores bónus.</p>
 */
public class Projetil_jogador extends Projetil {

    /** Largura da caixa de colisão (hitbox) do projétil do jogador. */
    public static final double LARGURA = 4;

    /** Altura da caixa de colisão (hitbox) do projétil do jogador. */
    public static final double ALTURA = 16;

    /** * Velocidade de deslocamento do projétil (píxeis por frame).
     * <p>O valor é negativo ({@code -9.0}) para que a coordenada Y decresça a cada tick,
     * fazendo com que o projétil suba visualmente no ecrã.</p>
     */
    public static final double VELOCIDADE = -9.0;

    /** Identificador textual do tipo de armamento. */
    public static final String TIPO = "Laser Retilíneo";

    /**
     * Construtor da classe {@code Projetil_jogador}.
     * <p>Instancia um novo tiro nas coordenadas fornecidas (normalmente
     * correspondentes à ponta do canhão do jogador), aplicando as dimensões
     * e a velocidade definidas pelas constantes da classe.</p>
     *
     * @param x A coordenada X inicial do projétil.
     * @param y A coordenada Y inicial do projétil.
     */
    public Projetil_jogador(double x, double y) {
        super(x, y, LARGURA, ALTURA, VELOCIDADE);
    }

    // =========================================================
    //  GETTERS
    // =========================================================

    /**
     * Obtém a descrição do tipo de disparo.
     * <p>Pode ser útil para estatísticas de fim de jogo ou elementos de HUD.</p>
     *
     * @return Uma {@code String} com a designação da arma (ex: "Laser Retilíneo").
     */
    public String getTipo() {
        return TIPO;
    }
}