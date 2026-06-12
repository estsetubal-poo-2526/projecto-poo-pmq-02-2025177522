package game_Logic;

/**
 * Representa um projétil disparado pelas forças inimigas (Frota ou Invasor Aleatório).
 *
 * <p>Esta classe estende {@link Projetil} e introduz comportamentos específicos,
 * como a possibilidade de ter trajetórias variadas (linhas retas ou oscilações
 * horizontais em ziguezague) que descem pelo ecrã em direção ao jogador.</p>
 */
public class Projetil_Inimigo extends Projetil {

    /** Largura da caixa de colisão (hitbox) do projétil inimigo. */
    public static final double LARGURA = 6;

    /** Altura da caixa de colisão (hitbox) do projétil inimigo. */
    public static final double ALTURA = 14;

    /** Velocidade de descida base do projétil (píxeis por frame). */
    public static final double VELOCIDADE_BASE = 4.0;

    /**
     * Enumeração que define os padrões de trajetória possíveis para os disparos inimigos.
     */
    public enum TipoTrajeto {
        /** Trajetória que oscila no eixo X enquanto desce, dificultando o desvio do jogador. */
        ZIGZAG,

        /** Trajetória linear clássica, que desce a uma velocidade ligeiramente reduzida. */
        RETO
    }

    /** O padrão de movimento atribuído a este projétil específico. */
    private TipoTrajeto tipoTrajeto;

    /** Variável de controlo de tempo (tick) utilizada para calcular o deslocamento da função seno no zigzag. */
    private double tick;

    /**
     * Construtor da classe {@code Projetil_Inimigo}.
     *
     * @param x    A coordenada X inicial onde o projétil será gerado (normalmente a base do atirador).
     * @param y    A coordenada Y inicial onde o projétil será gerado.
     * @param tipo O {@link TipoTrajeto} que define como o projétil se vai movimentar no ecrã.
     */
    public Projetil_Inimigo(double x, double y, TipoTrajeto tipo) {
        super(x, y, LARGURA, ALTURA, resolverVelocidade(tipo));
        this.tipoTrajeto = tipo;
        this.tick = 0;
    }

    /**
     * Método auxiliar interno para determinar a velocidade vertical consoante o tipo de trajetória.
     * <p>Os projéteis em linha reta viajam a 60% da velocidade base para equilibrar a jogabilidade,
     * enquanto os de ziguezague descem à velocidade total.</p>
     *
     * @param tipo O tipo de trajetória selecionado.
     * @return O valor da velocidade ajustada para o eixo Y.
     */
    private static double resolverVelocidade(TipoTrajeto tipo) {
        if (tipo == TipoTrajeto.RETO) {
            return VELOCIDADE_BASE * 0.6;
        } else {
            return VELOCIDADE_BASE;
        }
    }

    /**
     * Sobrescreve a lógica de movimento padrão do projétil.
     * <p>Todos os projéteis descem (aumentam a coordenada Y). No entanto, se o tipo
     * de trajetória for {@link TipoTrajeto#ZIGZAG}, é aplicada uma função matemática
     * de seno ({@code Math.sin}) para adicionar uma oscilação contínua e suave no eixo X.</p>
     */
    @Override
    public void moverVerticalmente() {
        this.y += velocidadeY;
        if (tipoTrajeto == TipoTrajeto.ZIGZAG) {
            tick += 0.3;
            this.x += Math.sin(tick) * 3.0; // Aplica oscilação de 3 píxeis de amplitude
        }
    }

    // =========================================================
    //  GETTERS
    // =========================================================

    /**
     * Obtém o tipo de trajetória deste projétil.
     * <p>Útil para a View (interface gráfica) decidir qual o formato ou cor
     * com que deve renderizar (desenhar) o projétil no ecrã.</p>
     *
     * @return O {@link TipoTrajeto} correspondente.
     */
    public TipoTrajeto getTipoTrajeto() {
        return tipoTrajeto;
    }
}