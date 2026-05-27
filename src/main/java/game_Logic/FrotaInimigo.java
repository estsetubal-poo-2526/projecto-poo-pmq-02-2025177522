package game_Logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * A frota é organizada em 5 linhas x 11 colunas:
 *   Linha 0       → InvasorTopo  (30 pts)
 *   Linhas 1-2    → InvasorMeio  (20 pts)
 *   Linhas 3-4    → InvasorBase  (10 pts)
 */
public class FrotaInimigo {

    public static final int LINHAS = 5;
    public static final int COLUNAS = 11;
    private static final double ESPACO_H = 54; // espaço horizontal entre inimigos
    private static final double ESPACO_V = 44; // espaço vertical entre inimigos
    private static final double PASSO_DESCIDA = 20; // pixels que a frota desce por inversão

    private static final double VELOCIDADE_BASE = 1.0;

    private List<Inimigo> frota;
    private double direcaoX; // +1 = direita, -1 = esquerda
    private double velocidadeAtual;
    private double limiteEsquerdo;
    private double limiteDireito;

    private final Random random = new Random();

    public FrotaInimigo(double xInicio, double yInicio,
                         double limiteEsquerdo, double limiteDireito) {
        this.frota = new ArrayList<>();
        this.direcaoX = 1;
        this.velocidadeAtual = VELOCIDADE_BASE;
        this.limiteEsquerdo = limiteEsquerdo;
        this.limiteDireito = limiteDireito;
        inicializarFrota(xInicio, yInicio);
    }

    /** Preenche a grelha de inimigos de acordo com o tipo de linha. */
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
        return switch (linha) {
            case 0 -> new Inimigo_Tras(x, y);
            case 1, 2 -> new Inimigo_Meio(x, y);
            default -> new Inimigo_Frente(x, y);
        };
    }

    /**
     * Move toda a frota. Se atingir o limite lateral, desce uma linha
     * e inverte a direção. A velocidade aumenta com as eliminações.
     */
    public void moverEmBloco() {
        List<Inimigo> vivos = getVivos();
        if (vivos.isEmpty()) return;

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
        List<Inimigo> atiradores = getFrenteColuna();
        if (atiradores.isEmpty()) return null;

        Inimigo atirador = atiradores.get(random.nextInt(atiradores.size()));
        double px = atirador.getX() + atirador.getLargura() / 2
                - Projetil_Inimigo.LARGURA / 2;
        double py = atirador.getY() + atirador.getAltura();

        Projetil_Inimigo.TipoTrajeto tipo = random.nextBoolean()
                ? Projetil_Inimigo.TipoTrajeto.ZIGZAG
                : Projetil_Inimigo.TipoTrajeto.ROLO_LENTO;

        return new Projetil_Inimigo(px, py, tipo);
    }

    /** Desce toda a frota uma linha (PASSO_DESCIDA pixels). */
    public void descerLinha() {
        for (Inimigo i : getVivos()) {
            i.setY(i.getY() + PASSO_DESCIDA);
        }
    }

    /**
     * Obtém o inimigo mais abaixo de cada coluna (o que dispara).
     */
    private List<Inimigo> getFrenteColuna() {
        List<Inimigo> frente = new ArrayList<>();
        // agrupa por coluna (posição X aproximada) e pega o mais baixo
        for (int col = 0; col < COLUNAS; col++) {
            final int c = col;
            frota.stream()
                    .filter(Inimigo::isVivo)
                    .filter(i -> Math.round(i.getX() / ESPACO_H) % COLUNAS == c % COLUNAS)
                    .max((a, b) -> Double.compare(a.getY(), b.getY()))
                    .ifPresent(frente::add);
        }
        // fallback: se agrupamento falhar, devolve todos os vivos
        return frente.isEmpty() ? getVivos() : frente;
    }

    /** Verifica se algum inimigo atingiu a linha base (y >= limiteBase). */
    public boolean atingiuBase(double limiteBase) {
        return getVivos().stream().anyMatch(i -> i.getY() + i.getAltura() >= limiteBase);
    }

    public List<Inimigo> getVivos() {
        return frota.stream().filter(Inimigo::isVivo).collect(Collectors.toList());
    }

    public List<Inimigo> getTodos() { return frota; }

    public boolean estaVazia() { return getVivos().isEmpty(); }

    public double getVelocidadeAtual() { return velocidadeAtual; }

    /** Reseta a velocidade (útil ao avançar de vaga). */
    public void resetarVelocidade() { this.velocidadeAtual = VELOCIDADE_BASE; }
}
