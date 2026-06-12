package game_Logic;

/**
 * Representa uma barricada ou escudo defensivo no jogo.
 *
 * <p>As barricadas são entidades estáticas posicionadas no mapa para proteger
 * o jogador dos disparos inimigos. Elas possuem uma integridade estrutural (vida)
 * que diminui à medida que recebem dano, até serem completamente destruídas.</p>
 */
public class Barricadas extends EntidadeJogo {

    /** Largura padrão da barricada. */
    public static final double LARGURA = 60;

    /** Altura padrão da barricada. */
    public static final double ALTURA = 40;

    /** Quantidade máxima de impactos (vida) que a barricada suporta antes de ser destruída. */
    public static final int INTEGRIDADE_MAX = 10;

    /** Nível atual de integridade (vida restante) da barricada. */
    private int integridade;

    /**
     * Construtor da classe {@code Barricadas}.
     * <p>Cria uma nova barricada nas coordenadas especificadas, inicializando-a
     * com as dimensões padrão e com a integridade no nível máximo.</p>
     *
     * @param x A coordenada X inicial da barricada no mapa.
     * @param y A coordenada Y inicial da barricada no mapa.
     */
    public Barricadas(double x, double y) {
        super(x, y, LARGURA, ALTURA);
        this.integridade = INTEGRIDADE_MAX;
    }

    /**
     * Aplica dano à barricada.
     * <p>Reduz a integridade atual em 1 ponto. Se a integridade já for 0 ou inferior,
     * o método não tem efeito.</p>
     */
    public void receberDano() {
        if (integridade > 0) {
            integridade--;
        }
    }

    /**
     * Verifica se a barricada foi completamente destruída.
     *
     * @return {@code true} se a integridade for menor ou igual a zero, {@code false} caso contrário.
     */
    public boolean estaDestruida() {
        return integridade <= 0;
    }

    /**
     * Obtém o valor absoluto da integridade atual da barricada.
     *
     * @return O número de impactos que a barricada ainda pode suportar.
     */
    public int getIntegridade() {
        return integridade;
    }

    /**
     * Calcula a percentagem de saúde restante da barricada.
     * <p>Este valor é especialmente útil para a interface gráfica (View),
     * permitindo alterar a cor ou desenhar rachaduras na barricada
     * consoante o seu nível de dano atual.</p>
     *
     * @return Um valor do tipo {@code double} entre 0.0 (destruída) e 1.0 (intacta).
     */
    public double getPercentagemIntegridade() {
        return (double) integridade / INTEGRIDADE_MAX;
    }

    /**
     * Atualiza a posição da entidade.
     * <p>Como as barricadas são obstáculos estáticos no cenário, este método
     * está intencionalmente vazio e não altera as coordenadas.</p>
     */
    @Override
    public void mover() {
        // As barricadas são estáticas
    }
}