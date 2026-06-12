package game_Logic;

/**
 * Classe abstrata base que representa uma entidade física no jogo.
 *
 * <p>Esta classe fornece as propriedades fundamentais (posição geométrica e dimensões)
 * comuns a todos os objetos em campo (jogador, inimigos, projéteis, barricadas).
 * Inclui também a lógica central para deteção de colisões entre entidades,
 * mantendo a lógica de jogo completamente independente da interface gráfica (JavaFX).</p>
 */
public abstract class EntidadeJogo {

    /** Posição horizontal (eixo X) da entidade no referencial do jogo. */
    protected double x;

    /** Posição vertical (eixo Y) da entidade no referencial do jogo. */
    protected double y;

    /** Largura da entidade, utilizada para o cálculo da caixa de colisão (hitbox). */
    protected double largura;

    /** Altura da entidade, utilizada para o cálculo da caixa de colisão (hitbox). */
    protected double altura;

    /**
     * Construtor base para inicializar os atributos de uma nova entidade.
     *
     * @param x       A coordenada X inicial.
     * @param y       A coordenada Y inicial.
     * @param largura A largura total da entidade.
     * @param altura  A altura total da entidade.
     */
    public EntidadeJogo(double x, double y, double largura, double altura) {
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
    }

    /**
     * Método abstrato responsável pela lógica de deslocamento da entidade.
     * <p>Cada subclasse específica (como Jogador, Inimigo ou Projétil) deve fornecer
     * a sua própria implementação deste método com as regras de movimento apropriadas.</p>
     */
    public abstract void mover();

    /**
     * Verifica a colisão entre esta entidade e outra utilizando o algoritmo AABB
     * (Axis-Aligned Bounding Box).
     * * <p>Este cálculo analisa a sobreposição dos retângulos de delimitação baseando-se
     * estritamente nos atributos de posição (x, y) e dimensão (largura, altura).
     * Por não importar pacotes de UI, garante a separação total entre lógica e apresentação.</p>
     *
     * @param outra A {@code EntidadeJogo} com a qual se pretende testar a sobreposição.
     * @return {@code true} se as hitboxes das duas entidades se intersetarem, {@code false} caso contrário.
     */
    public boolean colideCom(EntidadeJogo outra) {
        return this.x < outra.x + outra.largura
                && this.x + this.largura > outra.x
                && this.y < outra.y + outra.altura
                && this.y + this.altura > outra.y;
    }

    // =========================================================
    //  GETTERS & SETTERS
    // =========================================================

    /**
     * Obtém a coordenada X atual da entidade.
     * @return O valor da posição horizontal.
     */
    public double getX() { return x; }

    /**
     * Obtém a coordenada Y atual da entidade.
     * @return O valor da posição vertical.
     */
    public double getY() { return y; }

    /**
     * Obtém a largura da entidade.
     * @return O valor da largura.
     */
    public double getLargura() { return largura; }

    /**
     * Obtém a altura da entidade.
     * @return O valor da altura.
     */
    public double getAltura() { return altura; }

    /**
     * Define uma nova coordenada X para a entidade.
     * @param x O novo valor para a posição horizontal.
     */
    public void setX(double x) { this.x = x; }

    /**
     * Define uma nova coordenada Y para a entidade.
     * @param y O novo valor para a posição vertical.
     */
    public void setY(double y) { this.y = y; }
}