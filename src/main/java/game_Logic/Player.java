package game_Logic;

/**
 * Representa o jogador (submarino) controlado pelo utilizador.
 * Move-se horizontalmente na base do ecrã e dispara projéteis.
 */
public class Player extends EntidadeJogo {

    public static final int VIDAS_INICIAIS = 3;
    public static final double VELOCIDADE = 5.0;
    public static final double LARGURA = 48;
    public static final double ALTURA = 32;

    private int vidas;
    private int pontuacao;
    private boolean ativo; // false = jogador destruído

    public Player(double x, double y) {
        super(x, y, LARGURA, ALTURA);
        this.vidas = VIDAS_INICIAIS;
        this.pontuacao = 0;
        this.ativo = true;
    }

    /**
     * Move o jogador horizontalmente.
     * @param dx deslocamento em x (negativo = esquerda, positivo = direita)
     */
    public void moverHorizontal(double dx) {
        this.x += dx;
    }



    /**
     * Cria e devolve um projétil disparado pelo jogador.
     * O projétil parte do centro do jogador, em direção ao topo.
     */
    public Projetil_jogador disparar() {
        double px = this.x + this.largura / 2 - Projetil_jogador.LARGURA / 2;
        double py = this.y - Projetil_jogador.ALTURA;
        return new Projetil_jogador(px, py);
    }

    /** O jogador perde uma vida. Se chegar a zero, é desativado. */
    public void perderVida() {
        if (vidas > 0) {
            vidas--;
        }
        if (vidas <= 0) {
            ativo = false;
        }
    }

    /** Adiciona pontos à pontuação do jogador. */
    public void adicionarPontos(int pontos) {
        if (pontos < 0) throw new IllegalArgumentException("Pontos não podem ser negativos.");
        this.pontuacao += pontos;
    }

    /**
     * O jogador não tem movimento autónomo;
     * o movimento é controlado explicitamente via moverHorizontal().
     */
    @Override
    public void mover() {
        // Movimento gerido pelo controlador com base no input
    }

    // ---- Getters ----
    public int getVidas() { return vidas; }
    public int getPontuacao() { return pontuacao; }
    public boolean isAtivo() { return ativo; }

    // ---- Para testes / reset ----
    public void setVidas(int vidas) { this.vidas = vidas; }
    public void setPontuacao(int pontuacao) { this.pontuacao = pontuacao; }
}
