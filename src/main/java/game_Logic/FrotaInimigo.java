package game_Logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Gere a grelha de inimigos 5×11 e o seu movimento em bloco.
 *
 * <p><b>Velocidade por vaga:</b><br>
 * A velocidade é fixa durante toda a vaga — não muda conforme os inimigos
 * morrem. Apenas aumenta quando começa uma nova vaga, tornando o jogo
 * progressivamente mais difícil de forma previsível e justa.
 *
 * <p>Organização da grelha:
 * <pre>
 *   Linha 0       → Inimigo_Tras   (30 pts) — fila de trás
 *   Linhas 1-2    → Inimigo_Meio   (20 pts) — filas do meio
 *   Linhas 3-4    → Inimigo_Frente (10 pts) — filas da frente
 * </pre>
 */
public class FrotaInimigo {

    // ---- Dimensões da grelha ----
    public static final int LINHAS  = 5;
    public static final int COLUNAS = 11;

    private static final double ESPACO_H     = 54;
    private static final double ESPACO_V     = 44;
    private static final double PASSO_DESCIDA = 20;

    // ---- Velocidade fixa por vaga ----
    /** Velocidade base na vaga 1 (pixels/frame). */
    private static final double VELOCIDADE_BASE = 0.2;

    /** Incremento de velocidade por cada vaga completada. */
    private static final double INCREMENTO_POR_VAGA = 0.3;

    // ---- Probabilidade de disparo ----
    /** Probabilidade de disparo por tentativa na vaga 1. */
    private static final double CHANCE_DISPARO_BASE = 0.4;

    /** Incremento de probabilidade de disparo por vaga. */
    private static final double INCREMENTO_DISPARO_POR_VAGA = 0.01;

    private final List<Inimigo> frota;

    private double direcaoX;
    private final double limiteEsquerdo;
    private final double limiteDireito;

    /**
     * Velocidade fixa para toda esta vaga.
     * Calculada no construtor e nunca alterada durante a vaga.
     */
    private final double velocidadeDaVaga;

    /** Probabilidade de disparo por tentativa nesta vaga. */
    private final double chanceDisparo;

    private final Random random = new Random();

    /**
     * @param xInicio        posição X do canto superior esquerdo da grelha
     * @param yInicio        posição Y do canto superior esquerdo da grelha
     * @param limiteEsquerdo limite esquerdo do campo de jogo
     * @param limiteDireito  limite direito do campo de jogo
     * @param vagaAtual      número da vaga atual (começa em 1)
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
     * Cria o inimigo correto para a linha e coluna indicadas.
     * O índice de coluna é guardado no inimigo para uso em {@link #getFrenteColuna()}.
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
     * Move toda a frota um passo com a velocidade fixa desta vaga.
     *
     * <p>Quando a frota toca numa borda, desce um passo fixo
     * e inverte a direção horizontal — tal como no Space Invaders.
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

    /** Desce toda a frota um passo fixo. Chamado ao tocar numa borda lateral. */
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
     * <p>Primeiro testa a probabilidade de disparo da vaga.
     * Se passar, escolhe aleatoriamente um inimigo da frente de cada coluna.
     *
     * @return novo projétil, ou {@code null} se não houver disparo neste frame
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
     * Devolve o inimigo mais à frente (maior Y) de cada coluna.
     *
     * <p>Usa o índice de coluna guardado no inimigo em vez de calcular
     * pela posição X, evitando erros quando a frota se desloca.
     *
     * @return lista de atiradores — um por coluna com inimigos vivos
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
     * Verifica se algum inimigo atingiu o limite inferior do campo.
     *
     * @param limiteBase coordenada Y do jogador (linha de derrota)
     * @return {@code true} se a frota alcançou a base
     */
    public boolean atingiuBase(double limiteBase) {
        return getVivos().stream().anyMatch(i -> i.getY() + i.getAltura() >= limiteBase);
    }

    // =========================================================
    //  GETTERS
    // =========================================================

    /** Devolve apenas os inimigos ainda vivos. */
    public List<Inimigo> getVivos() {
        return frota.stream()
                .filter(Inimigo::isVivo)
                .collect(Collectors.toList());
    }

    /** Devolve todos os inimigos (vivos e destruídos). */
    public List<Inimigo> getTodos() {
        return frota;
    }

    /** Devolve {@code true} se não existir nenhum inimigo vivo. */
    public boolean estaVazia() {
        return getVivos().isEmpty();
    }

    /** Devolve a velocidade fixa desta vaga (útil para HUD ou debug). */
    public double getVelocidadeAtual() {
        return velocidadeDaVaga;
    }
}
