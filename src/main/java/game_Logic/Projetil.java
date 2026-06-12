package game_Logic;

/**
 * Classe abstrata base para todos os projéteis do jogo.
 *
 * <p>Esta classe estende {@link EntidadeJogo} e fornece as propriedades essenciais
 * para qualquer tiro (do jogador ou dos inimigos). Gere a velocidade de deslocamento
 * vertical e o estado de atividade (para ser removido do jogo após uma colisão
 * ou após sair dos limites do ecrã).</p>
 */
public abstract class Projetil extends EntidadeJogo {

    /** Velocidade de deslocamento no eixo Y (negativa para subir, positiva para descer). */
    protected double velocidadeY;

    /** Flag que indica se o projétil ainda está em jogo ({@code true}) ou se deve ser apagado ({@code false}). */
    protected boolean ativo;

    /**
     * Construtor da classe abstrata {@code Projetil}.
     *
     * @param x           A coordenada X inicial do projétil.
     * @param y           A coordenada Y inicial do projétil.
     * @param largura     A largura da caixa de colisão (hitbox).
     * @param altura      A altura da caixa de colisão (hitbox).
     * @param velocidadeY A velocidade de movimento vertical por frame.
     */
    public Projetil(double x, double y, double largura, double altura, double velocidadeY) {
        super(x, y, largura, altura);
        this.velocidadeY = velocidadeY;
        this.ativo = true;
    }

    /**
     * Implementação do método abstrato de movimento.
     * <p>Delega a ação para o método {@link #moverVerticalmente()}, garantindo
     * o comportamento padrão de um projétil linear.</p>
     */
    @Override
    public void mover() {
        moverVerticalmente();
    }

    /**
     * Aplica a lógica de movimento vertical padrão, somando a velocidade à coordenada Y.
     * <p>Pode ser reescrito (override) por subclasses (por exemplo, projéteis inimigos
     * com trajetórias em ziguezague) para adicionar comportamentos de deslocamento especiais.</p>
     */
    public void moverVerticalmente() {
        this.y += velocidadeY;
    }

    /**
     * Marca o projétil como inativo.
     * <p>Utilizado após o projétil colidir com outra entidade (inimigo, jogador ou barricada)
     * ou quando ultrapassa os limites da área de jogo, sinalizando ao modelo que
     * este objeto pode ser limpo da memória.</p>
     */
    public void desativar() {
        this.ativo = false;
    }

    // =========================================================
    //  GETTERS
    // =========================================================

    /**
     * Verifica o estado de atividade do projétil.
     * @return {@code true} se estiver ativo, {@code false} se já foi destruído ou saiu do ecrã.
     */
    public boolean isAtivo() { return ativo; }

    /**
     * Obtém a velocidade de deslocamento vertical do projétil.
     * @return A velocidade no eixo Y.
     */
    public double getVelocidadeY() { return velocidadeY; }
}