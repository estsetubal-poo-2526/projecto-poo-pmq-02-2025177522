package game_graphicInterface.View;

import game_Logic.*;
import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.HashSet;
import java.util.Set;

public class Game_View extends StackPane {

    private static final double LARGURA = 800;
    private static final double ALTURA  = 600;

    // Paleta neon
    private static final Color COR_JOGADOR    = Color.web("#00f5ff");
    private static final Color COR_PROJETIL_J = Color.web("#00ffcc");
    private static final Color COR_PROJETIL_I = Color.web("#ff4466");
    private static final Color COR_BARRICADA  = Color.web("#00ff88");
    private static final Color COR_INIMIGO_F  = Color.web("#00f5ff");
    private static final Color COR_INIMIGO_M  = Color.web("#bf00ff");
    private static final Color COR_INIMIGO_T  = Color.web("#ff8c00");
    private static final Color COR_INVASOR    = Color.web("#ffd700");
    private static final Color COR_HUD        = Color.web("#00f5ff");

    private final ModeloJogo modelo;
    private final Manager_View managerView;
    private final Canvas canvas;
    private final GraphicsContext gc;
    private AnimationTimer gameLoop;

    private final Set<KeyCode> teclasAtivas = new HashSet<>();

    private boolean emTransicao = false;
    private int framesTransicao = 0;
    private static final int DURACAO_TRANSICAO = 120;

    public Game_View(ModeloJogo modelo, Manager_View managerView) {
        this.modelo      = modelo;
        this.managerView = managerView;
        this.canvas      = new Canvas(LARGURA, ALTURA);
        this.gc          = canvas.getGraphicsContext2D();

        getChildren().add(canvas);
        setAlignment(Pos.CENTER);
        setFocusTraversable(true);

        configurarInput();
        construirGameLoop();
    }

    public void iniciar() {
        requestFocus();
        gameLoop.start();
    }

    public void parar() {
        if (gameLoop != null) gameLoop.stop();
    }

    // =========================================================
    //  INPUT
    // =========================================================

    private void configurarInput() {
        setOnKeyPressed(e -> {
            teclasAtivas.add(e.getCode());
            if (e.getCode() == KeyCode.ESCAPE || e.getCode() == KeyCode.P) {
                EstadoJogo est = modelo.getEstado();
                if (est == EstadoJogo.A_JOGAR)  modelo.pausar();
                else if (est == EstadoJogo.PAUSADO) modelo.retomar();
            }
        });
        setOnKeyReleased(e -> teclasAtivas.remove(e.getCode()));
    }

    private void processarInput() {
        if (modelo.getEstado() != EstadoJogo.A_JOGAR) return;
        if (teclasAtivas.contains(KeyCode.LEFT)  || teclasAtivas.contains(KeyCode.A))
            modelo.moverJogadorEsquerda();
        if (teclasAtivas.contains(KeyCode.RIGHT) || teclasAtivas.contains(KeyCode.D))
            modelo.moverJogadorDireita();
        if (teclasAtivas.contains(KeyCode.SPACE))
            modelo.jogadorDisparar();
    }

    // =========================================================
    //  GAME LOOP
    // =========================================================

    private void construirGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                processarInput();

                if (modelo.getEstado() == EstadoJogo.A_JOGAR)
                    modelo.atualizar();

                renderizar();
                verificarTransicoes();
            }
        };
    }

    private void verificarTransicoes() {
        EstadoJogo estado = modelo.getEstado();

        if (estado == EstadoJogo.GAME_OVER) {
            parar();
            mostrarGameOver();
            return;
        }

        if (estado == EstadoJogo.TRANSICAO_VAGA) {
            if (!emTransicao) {
                emTransicao = true;
                framesTransicao = 0;
            }
            framesTransicao++;
            if (framesTransicao >= DURACAO_TRANSICAO) {
                emTransicao = false;
                modelo.avancarVaga();
            }
        }
    }

    // =========================================================
    //  RENDERIZAÇÃO
    // =========================================================

    private void renderizar() {
        desenharFundo();
        desenharBarricadas();
        desenharFrota();
        desenharInvasorAleatorio();
        desenharProjetisInimigos();
        desenharProjetisJogador();
        desenharJogador();
        desenharHUD();

        EstadoJogo estado = modelo.getEstado();
        if (estado == EstadoJogo.PAUSADO)        desenharOverlayPausa();
        if (estado == EstadoJogo.TRANSICAO_VAGA) desenharOverlayVaga();
    }

    private void desenharFundo() {
        LinearGradient grad = new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.web("#000814")),
                new Stop(0.5, Color.web("#001233")),
                new Stop(1.0, Color.web("#000814"))
        );
        gc.setFill(grad);
        gc.fillRect(0, 0, LARGURA, ALTURA);

        gc.setStroke(Color.web("#00f5ff22"));
        gc.setLineWidth(1);
        gc.strokeLine(0, ALTURA - 48, LARGURA, ALTURA - 48);
    }

    private void desenharJogador() {
        Player p = modelo.getJogador();
        if (!p.isAtivo()) return;

        double x = p.getX(), y = p.getY();
        double w = p.getLargura(), h = p.getAltura();

        gc.setFill(Color.web("#00f5ff18"));
        gc.fillOval(x - 4, y - 4, w + 8, h + 8);

        gc.setFill(COR_JOGADOR);
        gc.fillRoundRect(x, y + h * 0.3, w, h * 0.55, 10, 10);
        gc.fillRoundRect(x + w * 0.35, y + h * 0.05, w * 0.30, h * 0.35, 6, 6);

        gc.setFill(Color.web("#00ffcc"));
        gc.fillRect(x + w * 0.46, y - h * 0.05, w * 0.08, h * 0.18);

        gc.setFill(Color.web("#000814"));
        gc.fillOval(x + w * 0.38, y + h * 0.12, w * 0.12, h * 0.14);
        gc.setFill(Color.web("#00f5ff66"));
        gc.fillOval(x + w * 0.40, y + h * 0.14, w * 0.08, h * 0.10);
    }

    private void desenharFrota() {
        for (Inimigo inimigo : modelo.getFrota().getVivos()) {
            Color cor = corInimigo(inimigo);
            double x = inimigo.getX(), y = inimigo.getY();
            double w = inimigo.getLargura(), h = inimigo.getAltura();

            gc.setFill(Color.color(cor.getRed(), cor.getGreen(), cor.getBlue(), 0.10));
            gc.fillOval(x - 3, y - 3, w + 6, h + 6);

            gc.setFill(cor);
            gc.fillRoundRect(x + w * 0.1, y + h * 0.15, w * 0.8, h * 0.7, 8, 8);

            gc.setStroke(cor);
            gc.setLineWidth(1.5);
            gc.strokeLine(x, y + h * 0.35, x + w * 0.15, y + h * 0.5);
            gc.strokeLine(x + w, y + h * 0.35, x + w * 0.85, y + h * 0.5);
            gc.strokeLine(x + w * 0.05, y + h * 0.65, x + w * 0.18, y + h * 0.5);
            gc.strokeLine(x + w * 0.95, y + h * 0.65, x + w * 0.82, y + h * 0.5);

            gc.setFill(Color.web("#000814"));
            gc.fillOval(x + w * 0.28, y + h * 0.28, w * 0.14, h * 0.22);
            gc.fillOval(x + w * 0.58, y + h * 0.28, w * 0.14, h * 0.22);
            gc.setFill(cor.brighter());
            gc.fillOval(x + w * 0.30, y + h * 0.30, w * 0.09, h * 0.15);
            gc.fillOval(x + w * 0.60, y + h * 0.30, w * 0.09, h * 0.15);
        }
    }

    private Color corInimigo(Inimigo i) {
        if (i instanceof Inimigo_Tras) return COR_INIMIGO_T;
        if (i instanceof Inimigo_Meio) return COR_INIMIGO_M;
        return COR_INIMIGO_F;
    }

    private void desenharInvasorAleatorio() {
        Inimigo_aleatorio inv = modelo.getInvasorAleatorio();
        if (inv == null || !inv.isVivo()) return;

        double x = inv.getX(), y = inv.getY();
        double w = inv.getLargura(), h = inv.getAltura();

        gc.setFill(Color.web("#ffd70018"));
        gc.fillOval(x - 6, y - 6, w + 12, h + 12);

        gc.setFill(COR_INVASOR);
        gc.fillOval(x, y + h * 0.3, w, h * 0.5);
        gc.setFill(Color.web("#fffacd"));
        gc.fillOval(x + w * 0.2, y + h * 0.05, w * 0.6, h * 0.45);

        gc.setFill(Color.web("#00f5ff"));
        gc.fillOval(x + w * 0.18, y + h * 0.55, w * 0.08, h * 0.15);
        gc.fillOval(x + w * 0.45, y + h * 0.60, w * 0.08, h * 0.15);
        gc.fillOval(x + w * 0.72, y + h * 0.55, w * 0.08, h * 0.15);

        gc.setFill(COR_INVASOR);
        gc.setFont(Font.font("Monospace", FontWeight.BOLD, 11));
        gc.fillText("?", x + w * 0.44, y - 4);
    }

    private void desenharBarricadas() {
        for (Barricadas b : modelo.getBarricadas()) {
            double pct = b.getPercentagemIntegridade();
            Color cor = interpolarCor(pct);
            double x = b.getX(), y = b.getY();
            double w = b.getLargura(), h = b.getAltura();

            gc.setFill(Color.color(cor.getRed(), cor.getGreen(), cor.getBlue(), 0.12));
            gc.fillRoundRect(x - 3, y - 3, w + 6, h + 6, 10, 10);

            gc.setFill(cor);
            gc.fillRoundRect(x, y, w, h, 8, 8);

            if (pct < 0.66) {
                gc.setStroke(Color.color(0, 0, 0, 0.5));
                gc.setLineWidth(1.5);
                gc.strokeLine(x + w * 0.3, y, x + w * 0.15, y + h);
            }
            if (pct < 0.33) {
                gc.strokeLine(x + w * 0.65, y, x + w * 0.80, y + h);
            }
        }
    }

    private Color interpolarCor(double pct) {
        if (pct > 0.5) {
            double t = (pct - 0.5) * 2.0;
            return Color.color(1.0 - t, 1.0, 0.2 * t + 0.1, 1.0);
        } else {
            double t = pct * 2.0;
            return Color.color(1.0, t, 0.1, 1.0);
        }
    }

    private void desenharProjetisJogador() {
        for (Projetil_jogador p : modelo.getProjetisJogador()) {
            if (!p.isAtivo()) continue;
            double x = p.getX(), y = p.getY();
            double w = p.getLargura(), h = p.getAltura();

            gc.setFill(Color.web("#00ffcc44"));
            gc.fillRoundRect(x - 1, y + 4, w + 2, h, 3, 3);
            gc.setFill(COR_PROJETIL_J);
            gc.fillRoundRect(x, y, w, h * 0.7, 3, 3);
        }
    }

    private void desenharProjetisInimigos() {
        for (Projetil_Inimigo p : modelo.getProjetisInimigos()) {
            if (!p.isAtivo()) continue;
            double x = p.getX(), y = p.getY();
            double w = p.getLargura(), h = p.getAltura();

            if (p.getTipoTrajeto() == Projetil_Inimigo.TipoTrajeto.ZIGZAG) {
                gc.setFill(Color.web("#ff446644"));
                gc.fillOval(x - 2, y - 2, w + 4, h + 4);
                gc.setFill(COR_PROJETIL_I);
                double[] xs = {x + w / 2, x + w, x + w / 2, x};
                double[] ys = {y, y + h / 2, y + h, y + h / 2};
                gc.fillPolygon(xs, ys, 4);
            } else {
                gc.setFill(Color.web("#aaff0044"));
                gc.fillOval(x - 2, y - 2, w + 4, h + 4);
                gc.setFill(Color.web("#aaff00"));
                gc.fillOval(x, y, w, h);
            }
        }
    }

    // =========================================================
    //  HUD
    // =========================================================

    private void desenharHUD() {
        gc.setFont(Font.font("Monospace", FontWeight.BOLD, 14));

        gc.setFill(Color.web("#ffffff88"));
        gc.fillText("SCORE", 10, 20);
        gc.setFill(COR_HUD);
        gc.fillText(String.format("%06d", modelo.getJogador().getPontuacao()), 10, 38);

        gc.setFill(Color.web("#ffffff88"));
        gc.fillText("HI-SCORE", LARGURA / 2 - 50, 20);
        gc.setFill(Color.web("#ff8c00"));
        gc.fillText(String.format("%06d", modelo.getHiScore()), LARGURA / 2 - 36, 38);

        gc.setFill(Color.web("#ffffff88"));
        gc.fillText("VAGA", LARGURA - 90, 20);
        gc.setFill(COR_HUD);
        gc.fillText(String.valueOf(modelo.getVaga()), LARGURA - 90, 38);

        gc.setFill(Color.web("#ffffff88"));
        gc.setFont(Font.font("Monospace", 12));
        gc.fillText("VIDAS", 10, ALTURA - 10);
        int vidas = modelo.getJogador().getVidas();
        for (int i = 0; i < vidas; i++) {
            desenharIconeVida(60 + i * 28, ALTURA - 22);
        }
    }

    private void desenharIconeVida(double x, double y) {
        gc.setFill(COR_JOGADOR);
        gc.fillRoundRect(x, y + 6, 20, 10, 4, 4);
        gc.fillRoundRect(x + 6, y + 2, 8, 7, 3, 3);
        gc.fillRect(x + 9, y, 2, 3);
    }

    // =========================================================
    //  OVERLAYS
    // =========================================================

    private void desenharOverlayPausa() {
        gc.setFill(Color.color(0, 0, 0, 0.65));
        gc.fillRect(0, 0, LARGURA, ALTURA);

        gc.setFont(Font.font("Monospace", FontWeight.BOLD, 36));
        gc.setFill(Color.WHITE);
        centrarTexto("⏸  PAUSADO", ALTURA / 2 - 20);

        gc.setFont(Font.font("Monospace", 16));
        gc.setFill(Color.web("#aaaaaa"));
        centrarTexto("Prima ESC ou P para continuar", ALTURA / 2 + 20);
    }

    private void desenharOverlayVaga() {
        gc.setFill(Color.color(0, 0, 0, 0.55));
        gc.fillRect(0, 0, LARGURA, ALTURA);

        gc.setFont(Font.font("Monospace", FontWeight.BOLD, 32));
        gc.setFill(Color.web("#00f5ff"));
        centrarTexto("✦ VAGA CONCLUÍDA! ✦", ALTURA / 2 - 30);

        gc.setFont(Font.font("Monospace", 20));
        gc.setFill(Color.web("#00ff88"));
        centrarTexto("Precisão: " + modelo.getPrecisaoTiro() + "%", ALTURA / 2 + 10);

        gc.setFont(Font.font("Monospace", 14));
        gc.setFill(Color.web("#aaaaaa"));
        centrarTexto("A preparar vaga " + (modelo.getVaga() + 1) + "...", ALTURA / 2 + 44);
    }

    private void centrarTexto(String texto, double y) {
        double x = (LARGURA - texto.length() * 8.5) / 2;
        gc.fillText(texto, x, y);
    }

    // =========================================================
    //  GAME OVER
    // =========================================================

    private void mostrarGameOver() {
        Game_Over gameOver = new Game_Over(modelo, managerView);
        managerView.mostrarGameOver(modelo);
    }
}