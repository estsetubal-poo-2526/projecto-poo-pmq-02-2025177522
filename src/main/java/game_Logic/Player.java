package game_Logic;

/**
 * Representa o jogador (submarino) controlado pelo utilizador.
 *
 * <p>Esta classe herda de {@link EntidadeJogo} e define as propriedades físicas e
 * lógicas do avatar do jogador. Move-se horizontalmente ao longo da base do ecrã,
 * gerindo o seu próprio estado de vidas, pontuação acumulada e a capacidade de disparar
 * projéteis contra a frota inimiga.</p>
 */
public class Player extends EntidadeJogo {

    /** Número padrão de vidas com que o jogador inicia uma nova partida. */
    public static final int VIDAS_INICIAIS = 3;

    /** Velocidade de deslocamento horizontal do jogador em píxeis por frame. */
    public static final double VELOCIDADE = 5.0;

    /** Largura da caixa de colisão (hitbox) do jogador. */
    public static final double LARGURA = 48;

    /** Altura da caixa de colisão (hitbox) do jogador. */
    public static final double ALTURA = 32;

    /** Quantidade atual de vidas que o jogador possui. */
    private int vidas;

    /** Pontuação total acumulada pelo jogador na partida corrente. */
    private int pontuacao;

    /** Estado do jogador: {@code true} se estiver vivo e em jogo, {@code false} se for destruído. */
    private boolean ativo;

    /**
     * Construtor da classe {@code Player}.
     * <p>Inicializa o jogador na posição especificada, aplicando as dimensões padrão,
     * o número de vidas iniciais, pontuação a zeros e definindo-o como ativo.</p>
     *
     * @param x A coordenada X inicial onde o jogador será colocado.
     * @param y A coordenada Y inicial onde o jogador será colocado.
     */
    public Player(double x, double y) {
        super(x, y, LARGURA, ALTURA);
        this.vidas = VIDAS_INICIAIS;
        this.pontuacao = 0;
        this.ativo = true;
    }

    /**
     * Desloca o jogador horizontalmente pelo ecrã.
     *
     * @param dx O valor de deslocamento no eixo X.
     * Um valor negativo move para a esquerda, um valor positivo move para a direita.
     */
    public void moverHorizontal(double dx) {
        this.x += dx;
    }

    /**
     * Cria e devolve um projétil disparado pelo jogador.
     * <p>O projétil é instanciado para partir exatamente do centro da torre do submarino,
     * movendo-se em direção ao topo do ecrã.</p>
     *
     * @return Uma nova instância de {@link Projetil_jogador} pronta a ser adicionada ao modelo.
     */
    public Projetil_jogador disparar() {
        double px = this.x + this.largura / 2 - Projetil_jogador.LARGURA / 2;
        double py = this.y - Projetil_jogador.ALTURA;
        return new Projetil_jogador(px, py);
    }

    /**
     * Subtrai uma vida ao jogador resultante de um impacto inimigo.
     * <p>Se o contador de vidas chegar a zero ou um valor inferior,
     * o atributo {@code ativo} é alterado para {@code false}, despoletando o Game Over.</p>
     */
    public void perderVida() {
        if (vidas > 0) {
            vidas--;
        }
        if (vidas <= 0) {
            ativo = false;
        }
    }

    /**
     * Adiciona o valor especificado à pontuação total da partida.
     *
     * @param pontos O número de pontos a somar (tipicamente provenientes da destruição de um inimigo).
     * @throws IllegalArgumentException se o valor de pontos fornecido for negativo.
     */
    public void adicionarPontos(int pontos) {
        if (pontos < 0) throw new IllegalArgumentException("Pontos não podem ser negativos.");
        this.pontuacao += pontos;
    }

    /**
     * Implementação do método abstrato de movimento.
     * <p>Como o jogador não possui movimento autónomo (depende estritamente do input do utilizador),
     * este método encontra-se intencionalmente vazio. A movimentação deve ser invocada
     * explicitamente pelo Controlador através de métodos como {@link #moverHorizontal(double)}.</p>
     */
    @Override
    public void mover() {
        // Movimento gerido pelo controlador com base no input
    }

    // =========================================================
    //  GETTERS & SETTERS
    // =========================================================

    /**
     * Obtém a quantidade de vidas restantes.
     * @return O número de vidas atuais.
     */
    public int getVidas() { return vidas; }

    /**
     * Obtém a pontuação atual do jogador na partida.
     * @return O valor inteiro da pontuação.
     */
    public int getPontuacao() { return pontuacao; }

    /**
     * Verifica se o jogador ainda se encontra ativo (com vidas restantes).
     * @return {@code true} se estiver ativo, {@code false} se o jogo terminou para este jogador.
     */
    public boolean isAtivo() { return ativo; }

    /**
     * Define forçadamente o número de vidas do jogador.
     * Útil para ferramentas de debug, cheats ou testes unitários.
     * @param vidas O novo número de vidas.
     */
    public void setVidas(int vidas) { this.vidas = vidas; }

    /**
     * Define forçadamente a pontuação do jogador.
     * Útil para ferramentas de debug ou testes unitários.
     * @param pontuacao O novo valor de pontuação.
     */
    public void setPontuacao(int pontuacao) { this.pontuacao = pontuacao; }
}
