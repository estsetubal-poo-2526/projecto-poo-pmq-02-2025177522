package game_Logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Gere a grelha de inimigos (frota) e o seu movimento em bloco.
 *
 * <p><b>Velocidade por vaga:</b><br>
 * A velocidade é fixa durante toda a vaga — não muda conforme os inimigos
 * morrem. Apenas aumenta quando começa uma nova vaga, tornando o jogo
 * progressivamente mais difícil de forma previsível e justa.
 *
 * <p>Organização da grelha:
 * <pre>
 * Linha 0       → Inimigo_Tras   (30 pts) — fila de trás
 * Linhas 1-2    → Inimigo_Meio   (20 pts) — filas do meio
 * Linhas 3-4    → Inimigo_Frente (10 pts) — filas da frente
 * </pre>
 */
public class FrotaInimigo {

    // ---- Dimensões da grelha ----

    /** Número de linhas que compõem a frota inimiga. */
    public static final int LINHAS  = 5;

    /** Número de colunas que compõem a frota inimiga. */
    public static final int COLUNAS = 11;

    /** Espaçamento horizontal em píxeis entre cada inimigo na grelha. */
    private static final double ESPACO_H     = 54;

    /** Espaçamento vertical em píxeis entre cada inimigo na grelha. */
    private static final double ESPACO_V     = 44;

    /** Distância em píxeis que a frota desce quando atinge a borda lateral do ecrã. */
    private static final double PASSO_DESCIDA = 20;

    // ---- Velocidade fixa por vaga ----

    /** Velocidade base na vaga 1 (píxeis por frame). */
    private static final double VELOCIDADE_BASE = 0.2;

    /** Incremento de velocidade por cada vaga completada. */
    private static final double INCREMENTO_POR_VAGA = 0.3;

    // ---- Probabilidade de disparo ----

    /** Probabilidade de disparo por tentativa na vaga 1. */
    private static final double CHANCE_DISPARO_BASE = 0.4;

    /** Incremento de probabilidade de disparo por cada vaga completada. */
    private static final double INCREMENTO_DISPARO_POR_VAGA = 0.01;

    /** Lista que armazena todas as instâncias de inimigos que compõem a frota. */
    private final List<Inimigo> frota;

    /** Direção atual do movimento horizontal da frota (1 para a direita, -1 para a esquerda). */
    private double direcaoX;

    /** Limite do ecrã à esquerda, usado para inverter a marcha da frota. */
    private final double limiteEsquerdo;

    /** Limite do ecrã à direita, usado para inverter a marcha da frota. */
    private final double limiteDireito;

    /**
     * Velocidade fixa para toda esta vaga.
     * Calculada no construtor e nunca alterada durante o decorrer da mesma vaga.
     */
    private final double velocidadeDaVaga;

    /** Probabilidade de disparo por tentativa nesta vaga específica. */
    private final double chanceDisparo;

    /** Gerador de números aleatórios para determinar os disparos inimigos. */
    private final Random random = new Random();

    /**
     * Construtor da classe {@code FrotaInimigo}.
     * <p>Inicializa a grelha de inimigos, calcula a velocidade e a taxa de disparo
     * fixas para a vaga atual, baseando-se no nível em que o jogador se encontra.</p>
     *
     * @param xInicio        A posição X do canto superior esquerdo da grelha inicial.
     * @param yInicio        A posição Y do canto superior esquerdo da grelha inicial.
     * @param limiteEsquerdo O limite esquerdo do campo de jogo.
     * @param limiteDireito  O limite direito do campo de jogo.
     * @param vagaAtual      O número da vaga atual (inicia em 1).
     */
    public FrotaInimigo(double xInicio, double yInicio,
                        double limiteEsquerdo, double limiteDireito,
                        int vagaAtual) {
        this.frota          = new ArrayList<>();
        this.direcaoX       = 1;
        this.limiteEsquerdo = limiteEsquerdo;
        this.limiteDireito  = limiteDireito;

        // Velocidade calculada uma vez — mantém-se igual durante toda a vaga
        this.velocidadeDaVaga = VELOCIDADE_BASE + (vagaAtual - 1) * INCREMENTO_POR_VAGA;
        this.chanceDisparo    = CHANCE_DISPARO_BASE + (vagaAtual - 1) * INCREMENTO_DISPARO_POR_VAGA;

        inicializarFrota(xInicio, yInicio);
    }

    /**
     * Popula a lista da frota com as entidades inimigas, organizando-as na grelha
     * com os devidos espaçamentos calculados.
     *
     * @param xInicio A posição X inicial da grelha.
     * @param yInicio A posição Y inicial da grelha.
     */
    private void inicializarFrota(double xInicio, double yInicio) {
        for (int linha = 0; linha < LINHAS; linha++) {
            for (int col = 0; col < COLUNAS; col++) {
                double x = xInicio + col * ESPACO_H;
                double y = yInicio + linha * ESPACO_V;
                frota.add(criarInimigo(linha, col, x, y));
            }
        }
    }

    /**
     * Cria a instância correta do inimigo consoante a linha em que se encontra.
     * <p>O índice da coluna é guardado no inimigo para uso posterior no método
     * {@link #getFrenteColuna()}, garantindo que as mecânicas de disparo funcionam
     * corretamente mesmo após a morte de outros inimigos.</p>
     *
     * @param linha  A linha da grelha (determina o tipo de inimigo e a sua pontuação).
     * @param coluna A coluna da grelha.
     * @param x      A coordenada X onde o inimigo será posicionado.
     * @param y      A coordenada Y onde o inimigo será posicionado.
     * @return Uma nova instância de {@link Inimigo}.
     */
    private Inimigo criarInimigo(int linha, int coluna, double x, double y) {
        if (linha == 0) {
            return new Inimigo_Tras(x, y, coluna);
        }
        if (linha == 1 || linha == 2) {
            return new Inimigo_Meio(x, y, coluna);
        }
        return new Inimigo_Frente(x, y, coluna);
    }

    // =========================================================
    //  MOVIMENTO
    // =========================================================

    /**
     * Move toda a frota de inimigos vivos num passo com a velocidade fixa desta vaga.
     *
     * <p>Mecanismo clássico: Quando o inimigo mais na extremidade (seja direita ou esquerda)
     * toca numa borda do ecrã, toda a frota desce um passo fixo verticalmente e inverte
     * a sua direção horizontal de marcha.</p>
     */
    public void moverEmBloco() {
        List<Inimigo> vivos = getVivos();
        if (vivos.isEmpty()) {
            return;
        }

        double extremaDireita  = vivos.stream()
                .mapToDouble(i -> i.getX() + i.getLargura())
                .max().orElse(0);
        double extremaEsquerda = vivos.stream()
                .mapToDouble(EntidadeJogo::getX)
                .min().orElse(0);

        boolean atingiuBorda = false;
        if (direcaoX > 0 && extremaDireita + velocidadeDaVaga >= limiteDireito) {
            atingiuBorda = true;
        } else if (direcaoX < 0 && extremaEsquerda - velocidadeDaVaga <= limiteEsquerdo) {
            atingiuBorda = true;
        }

        if (atingiuBorda) {
            descerLinha();
            direcaoX = -direcaoX;
        } else {
            for (Inimigo inimigo : vivos) {
                inimigo.setX(inimigo.getX() + velocidadeDaVaga * direcaoX);
            }
        }
    }

    /** * Desce toda a frota um passo fixo no eixo Y.
     * Este método é invocado internamente quando a frota toca numa das bordas laterais.
     */
    public void descerLinha() {
        for (Inimigo inimigo : getVivos()) {
            inimigo.setY(inimigo.getY() + PASSO_DESCIDA);
        }
    }

    // =========================================================
    //  DISPARAR
    // =========================================================

    /**
     * Tenta gerar um projétil inimigo.
     *
     * <p>Primeiro, testa a probabilidade de disparo baseada na vaga atual.
     * Se o teste for bem-sucedido, seleciona aleatoriamente um dos inimigos
     * que se encontra na linha da frente de uma coluna disponível.</p>
     *
     * @return Um novo {@link Projetil_Inimigo} instanciado no local do atirador,
     * ou {@code null} se não houver disparo neste frame.
     */
    public Projetil_Inimigo atirarAleatoriamente() {
        if (random.nextDouble() > chanceDisparo) {
            return null;
        }

        List<Inimigo> atiradores = getFrenteColuna();
        if (atiradores.isEmpty()) {
            return null;
        }

        Inimigo atirador = atiradores.get(random.nextInt(atiradores.size()));
        double px = atirador.getX() + atirador.getLargura() / 2.0 - Projetil_Inimigo.LARGURA / 2.0;
        double py = atirador.getY() + atirador.getAltura();

        return new Projetil_Inimigo(px, py, Projetil_Inimigo.TipoTrajeto.RETO);
    }

    /**
     * Filtra e devolve os inimigos que estão mais expostos na parte inferior da frota
     * (aqueles que têm o maior Y em cada uma das respetivas colunas).
     *
     * <p>Utiliza o atributo de coluna guardado na inicialização do inimigo em vez de
     * inferir através da posição X, evitando assim desalinhamentos quando a frota se move.</p>
     *
     * @return Uma lista de potenciais atiradores — apenas um por coluna onde existam inimigos vivos.
     */
    private List<Inimigo> getFrenteColuna() {
        List<Inimigo> frente = new ArrayList<>();

        for (int col = 0; col < COLUNAS; col++) {
            final int colunaAlvo = col;
            frota.stream()
                    .filter(Inimigo::isVivo)
                    .filter(i -> i.getColuna() == colunaAlvo)
                    .max((a, b) -> Double.compare(a.getY(), b.getY()))
                    .ifPresent(frente::add);
        }

        if (frente.isEmpty()) {
            return getVivos();
        }
        return frente;
    }

    // =========================================================
    //  CONDIÇÕES DE FIM
    // =========================================================

    /**
     * Verifica se a frota inimiga alcançou a base do jogador (condição de derrota direta).
     *
     * @param limiteBase A coordenada Y que representa a base do jogador.
     * @return {@code true} se pelo menos um inimigo vivo tiver ultrapassado a linha base, {@code false} caso contrário.
     */
    public boolean atingiuBase(double limiteBase) {
        return getVivos().stream().anyMatch(i -> i.getY() + i.getAltura() >= limiteBase);
    }

    // =========================================================
    //  GETTERS
    // =========================================================

    /**
     * Devolve uma lista contendo apenas os inimigos que ainda não foram destruídos.
     * * @return Lista de instâncias de {@link Inimigo} com o estado vivo.
     */
    public List<Inimigo> getVivos() {
        return frota.stream()
                .filter(Inimigo::isVivo)
                .collect(Collectors.toList());
    }

    /**
     * Devolve a grelha completa de inimigos, independentemente de estarem vivos ou destruídos.
     * * @return A lista total instanciada no início da vaga.
     */
    public List<Inimigo> getTodos() {
        return frota;
    }

    /**
     * Verifica se toda a frota foi aniquilada.
     * * @return {@code true} se não houver inimigos vivos, o que despoleta a transição de vaga.
     */
    public boolean estaVazia() {
        return getVivos().isEmpty();
    }

    /**
     * Devolve a velocidade atual do bloco de inimigos.
     * * @return A velocidade da vaga em píxeis por frame (útil para telemetria, debug ou HUD).
     */
    public double getVelocidadeAtual() {
        return velocidadeDaVaga;
    }
}