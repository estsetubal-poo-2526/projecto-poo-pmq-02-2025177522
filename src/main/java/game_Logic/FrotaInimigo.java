package game_Logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * A frota é organizada em 5 linhas x 11 colunas:
 * Linha 0       → InvasorTopo  (30 pts)
 * Linhas 1-2    → InvasorMeio  (20 pts)
 * Linhas 3-4    → InvasorBase  (10 pts)
 */
public class FrotaInimigo {

    public static final int LINHAS = 5;
    public static final int COLUNAS = 11;
    private static final double ESPACO_H = 54;
    private static final double ESPACO_V = 44;
    private static final double PASSO_DESCIDA = 20;

    private static final double VELOCIDADE_BASE = 1.0;

    private List<Inimigo> frota;
    private double direcaoX;
    private double velocidadeAtual;
    private double limiteEsquerdo;
    private double limiteDireito;

    // NOVO: Controla a probabilidade de a frota atirar
    private double chanceDisparo;

    private final Random random = new Random();

    // ATENÇÃO: O construtor agora recebe a vagaAtual
    public FrotaInimigo(double xInicio, double yInicio,
                        double limiteEsquerdo, double limiteDireito, int vagaAtual) {
        this.frota = new ArrayList<>();
        this.direcaoX = 1;
        this.limiteEsquerdo = limiteEsquerdo;
        this.limiteDireito = limiteDireito;

        configurarDificuldade(vagaAtual);
        inicializarFrota(xInicio, yInicio);
    }

    /** * Define a velocidade e a taxa de tiro com base na vaga em que o jogador está.
     */
    private void configurarDificuldade(int vagaAtual) {
        // Velocidade suave: Sobe apenas 0.15 por vaga.
        // Vaga 1: 1.0 | Vaga 2: 1.15 | Vaga 3: 1.30 (fica mais rápido a cada nível, sem ser impossível)
        this.velocidadeAtual = VELOCIDADE_BASE + ((vagaAtual - 1) * 0.15);

        // Tiro mais agressivo: Começa com 3% de chance de disparo (se o jogo correr a 60 FPS, dá uns 2 tiros por segundo no total da frota).
        // Vaga 1: 3% | Vaga 2: 4.5% | Vaga 3: 6%
        this.chanceDisparo = 0.03 + ((vagaAtual - 1) * 0.015);
    }

    private void inicializarFrota(double xInicio, double yInicio) {
        for (int linha = 0; linha < LINHAS; linha++) {
            for (int col = 0; col < COLUNAS; col++) {
                double x = xInicio + col * ESPACO_H;
                double y = yInicio + linha * ESPACO_V;
                Inimigo inimigo = criarInimigo(linha, x, y);
                frota.add(inimigo);
            }
        }
    }

    private Inimigo criarInimigo(int linha, double x, double y) {
        // Removido o switch, substituído por if-else tradicional
        if (linha == 0) {
            return new Inimigo_Tras(x, y);
        } else if (linha == 1 || linha == 2) {
            return new Inimigo_Meio(x, y);
        } else {
            return new Inimigo_Frente(x, y);
        }
    }

    public void moverEmBloco() {
        List<Inimigo> vivos = getVivos();
        if (vivos.isEmpty()) {
            return;
        }

        double extremaDireita = vivos.stream()
                .mapToDouble(i -> i.getX() + i.getLargura())
                .max().orElse(0);
        double extremaEsquerda = vivos.stream()
                .mapToDouble(EntidadeJogo::getX)
                .min().orElse(0);

        boolean inverter = false;
        if (direcaoX > 0 && extremaDireita + velocidadeAtual >= limiteDireito) {
            inverter = true;
        } else if (direcaoX < 0 && extremaEsquerda - velocidadeAtual <= limiteEsquerdo) {
            inverter = true;
        }

        if (inverter) {
            descerLinha();
            direcaoX = -direcaoX;
        } else {
            for (Inimigo i : vivos) {
                i.setX(i.getX() + velocidadeAtual * direcaoX);
            }
        }
    }

    public Projetil_Inimigo atirarAleatoriamente() {
        // A frota só tenta disparar se passar no teste de probabilidade da vaga
        if (random.nextDouble() > chanceDisparo) {
            return null;
        }

        List<Inimigo> atiradores = getFrenteColuna();
        if (atiradores.isEmpty()) {
            return null;
        }

        Inimigo atirador = atiradores.get(random.nextInt(atiradores.size()));
        double px = atirador.getX() + atirador.getLargura() / 2 - Projetil_Inimigo.LARGURA / 2;
        double py = atirador.getY() + atirador.getAltura();

        Projetil_Inimigo.TipoTrajeto tipo = Projetil_Inimigo.TipoTrajeto.RETO;

        return new Projetil_Inimigo(px, py, tipo);
    }

    public void descerLinha() {
        for (Inimigo i : getVivos()) {
            i.setY(i.getY() + PASSO_DESCIDA);
        }
    }

    private List<Inimigo> getFrenteColuna() {
        List<Inimigo> frente = new ArrayList<>();
        for (int col = 0; col < COLUNAS; col++) {
            final int c = col;
            frota.stream()
                    .filter(Inimigo::isVivo)
                    .filter(i -> Math.round(i.getX() / ESPACO_H) % COLUNAS == c % COLUNAS)
                    .max((a, b) -> Double.compare(a.getY(), b.getY()))
                    .ifPresent(frente::add);
        }

        if (frente.isEmpty()) {
            return getVivos();
        } else {
            return frente;
        }
    }

    public boolean atingiuBase(double limiteBase) {
        return getVivos().stream().anyMatch(i -> i.getY() + i.getAltura() >= limiteBase);
    }

    public List<Inimigo> getVivos() {
        return frota.stream().filter(Inimigo::isVivo).collect(Collectors.toList());
    }

    public List<Inimigo> getTodos() { return frota; }

    public boolean estaVazia() { return getVivos().isEmpty(); }

    public double getVelocidadeAtual() { return velocidadeAtual; }

    /** * NOVO: Este método substitui o resetarVelocidade().
     * Deve ser chamado quando a vaga é limpa e queres criar a próxima.
     */
    public void prepararNovaVaga(double xInicio, double yInicio, int novaVaga) {
        configurarDificuldade(novaVaga);
        this.frota.clear();
        this.direcaoX = 1;
        inicializarFrota(xInicio, yInicio);
    }
}