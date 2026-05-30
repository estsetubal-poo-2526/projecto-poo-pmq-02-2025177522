package game_graphicInterface.View;

import game_Logic.*;
import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.HashSet;
import java.util.Set;

/**
 * Vista principal do jogo.
 *
 * Usa um Pane JavaFX em vez de Canvas — cada entidade do jogo
 * é representada por uma forma JavaFX (Rectangle, Ellipse, etc.)
 * que é atualizada a cada frame com a posição do modelo.
 *
 * O menu de pausa (Pause_Menu_View) é sobreposto via StackPane
 * e a sua visibilidade é controlada pelo estado do jogo.
 */
public class Game_View extends StackPane {

    private static final double LARGURA = 800;
    private static final double ALTURA  = 600;

    // ---- Paleta neon ----
    private static final Color COR_JOGADOR    = Color.web("#00f5ff");
    private static final Color COR_PROJETIL_J = Color.web("#00ffcc");
    private static final Color COR_PROJETIL_I = Color.web("#ff4466");
    private static final Color COR_PROJETIL_I_RETO = Color.web("#aaff00");
    private static final Color COR_INIMIGO_F  = Color.web("#00f5ff");
    private static final Color COR_INIMIGO_M  = Color.web("#bf00ff");
    private static final Color COR_INIMIGO_T  = Color.web("#ff8c00");
    private static final Color COR_INVASOR    = Color.web("#ffd700");

    private final ModeloJogo modelo;
    private final Manager_View managerView;

    /** Pane principal onde todas as entidades do jogo são desenhadas. */
    private final Pane campoJogo;

    /** Menu de pausa sobreposto ao campoJogo. */
    private final Pause_Menu_View menuPausa;

    private AnimationTimer gameLoop;
    private final Set<KeyCode> teclasAtivas = new HashSet<>();

    // ---- Formas JavaFX que representam as entidades ----
    // Jogador
    private final Rectangle corpoJogador;
    private final Rectangle torreJogador;
    private final Rectangle canhaoJogador;
    private final Ellipse  aureolJogador;

    // HUD (textos)
    private final Text txtScore;
    private final Text txtHiScore;
    private final Text txtVaga;
    private final Text txtVidas;

    // Overlay de vaga concluída
    private final Text txtVagaConcluida;
    private final Text txtPrecisao;
    private final Text txtProximaVaga;
    private final Rectangle overlayVaga;

    // Controlo de transição
    private boolean emTransicao = false;
    private int framesTransicao = 0;
    private static final int DURACAO_TRANSICAO = 120;

    // =========================================================
    //  CONSTRUTOR
    // =========================================================

    public Game_View(ModeloJogo modelo, Manager_View managerView) {
        this.modelo      = modelo;
        this.managerView = managerView;

        // --- Campo de jogo (Pane) ---
        campoJogo = new Pane();
        campoJogo.setPrefSize(LARGURA, ALTURA);
        campoJogo.setStyle("-fx-background-color: #000814;");

        // Fundo gradiente
        Rectangle fundo = new Rectangle(LARGURA, ALTURA);
        fundo.setFill(new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.web("#000814")),
                new Stop(0.5, Color.web("#001233")),
                new Stop(1.0, Color.web("#000814"))
        ));
        campoJogo.getChildren().add(fundo);

        // Linha divisória inferior
        Line linhaDivisoria = new Line(0, ALTURA - 48, LARGURA, ALTURA - 48);
        linhaDivisoria.setStroke(Color.web("#00f5ff22"));
        linhaDivisoria.setStrokeWidth(1);
        campoJogo.getChildren().add(linhaDivisoria);

        // --- Jogador ---
        aureolJogador = new Ellipse();
        aureolJogador.setFill(Color.web("#00f5ff18"));

        corpoJogador = new Rectangle();
        corpoJogador.setFill(COR_JOGADOR);
        corpoJogador.setArcWidth(10);
        corpoJogador.setArcHeight(10);

        torreJogador = new Rectangle();
        torreJogador.setFill(COR_JOGADOR);
        torreJogador.setArcWidth(6);
        torreJogador.setArcHeight(6);

        canhaoJogador = new Rectangle();
        canhaoJogador.setFill(Color.web("#00ffcc"));

        campoJogo.getChildren().addAll(aureolJogador, corpoJogador, torreJogador, canhaoJogador);

        // --- HUD ---
        txtScore = criarTextoHUD("000000", 10, 38, COR_JOGADOR, 14, true);
        Text lblScore = criarTextoHUD("SCORE", 10, 20, Color.web("#ffffff88"), 14, false);

        txtHiScore = criarTextoHUD("000000", LARGURA / 2 - 36, 38, Color.web("#ff8c00"), 14, true);
        Text lblHiScore = criarTextoHUD("HI-SCORE", LARGURA / 2 - 50, 20, Color.web("#ffffff88"), 14, false);

        txtVaga = criarTextoHUD("1", LARGURA - 90, 38, COR_JOGADOR, 14, true);
        Text lblVaga = criarTextoHUD("VAGA", LARGURA - 90, 20, Color.web("#ffffff88"), 14, false);

        txtVidas = criarTextoHUD("VIDAS", 10, ALTURA - 10, Color.web("#ffffff88"), 12, false);

        campoJogo.getChildren().addAll(
                lblScore, txtScore,
                lblHiScore, txtHiScore,
                lblVaga, txtVaga,
                txtVidas
        );

        // --- Overlay de vaga concluída ---
        overlayVaga = new Rectangle(LARGURA, ALTURA, Color.color(0, 0, 0, 0.55));
        overlayVaga.setVisible(false);

        txtVagaConcluida = criarTextoOverlay("✦ VAGA CONCLUÍDA! ✦", 32, "#00f5ff", ALTURA / 2 - 30);
        txtPrecisao      = criarTextoOverlay("Precisão: 0%",         20, "#00ff88", ALTURA / 2 + 10);
        txtProximaVaga   = criarTextoOverlay("A preparar vaga 2...", 14, "#aaaaaa", ALTURA / 2 + 44);

        campoJogo.getChildren().addAll(overlayVaga, txtVagaConcluida, txtPrecisao, txtProximaVaga);

        // --- Menu de pausa ---
        menuPausa = new Pause_Menu_View();
        menuPausa.getBtnRetomar().setOnAction(e -> {
            modelo.retomar();
            requestFocus();
        });
        menuPausa.getBtnDesistir().setOnAction(e -> {
            parar();
            managerView.mostrarMenu();
        });

        // --- Montar tudo no StackPane ---
        getChildren().addAll(campoJogo, menuPausa);
        setAlignment(Pos.CENTER);
        setFocusTraversable(true);

        configurarInput();
        construirGameLoop();
    }

    // =========================================================
    //  ARRANQUE E PARAGEM
    // =========================================================

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
                if (est == EstadoJogo.A_JOGAR)   modelo.pausar();
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

                atualizar();
                verificarTransicoes();
            }
        };
    }

    // =========================================================
    //  ATUALIZAÇÃO (substitui o renderizar() do Canvas)
    // =========================================================

    /**
     * Atualiza todas as formas JavaFX com as posições atuais do modelo.
     * O JavaFX redesenha automaticamente o que for necessário.
     */
    private void atualizar() {
        atualizarJogador();
        atualizarFrota();
        atualizarInvasorAleatorio();
        atualizarBarricadas();
        atualizarProjetisJogador();
        atualizarProjetisInimigos();
        atualizarHUD();
        atualizarOverlayVaga();

        // Menu de pausa — basta controlar a visibilidade
        menuPausa.setVisible(modelo.getEstado() == EstadoJogo.PAUSADO);
    }

    // =========================================================
    //  ATUALIZAÇÃO DE CADA ENTIDADE
    // =========================================================

    private void atualizarJogador() {
        Player p = modelo.getJogador();
        if (!p.isAtivo()) {
            corpoJogador.setVisible(false);
            torreJogador.setVisible(false);
            canhaoJogador.setVisible(false);
            aureolJogador.setVisible(false);
            return;
        }

        double x = p.getX(), y = p.getY();
        double w = p.getLargura(), h = p.getAltura();

        // Auréola
        aureolJogador.setCenterX(x + w / 2);
        aureolJogador.setCenterY(y + h / 2);
        aureolJogador.setRadiusX(w / 2 + 4);
        aureolJogador.setRadiusY(h / 2 + 4);

        // Corpo
        corpoJogador.setX(x);
        corpoJogador.setY(y + h * 0.3);
        corpoJogador.setWidth(w);
        corpoJogador.setHeight(h * 0.55);

        // Torre
        torreJogador.setX(x + w * 0.35);
        torreJogador.setY(y + h * 0.05);
        torreJogador.setWidth(w * 0.30);
        torreJogador.setHeight(h * 0.35);

        // Canhão
        canhaoJogador.setX(x + w * 0.46);
        canhaoJogador.setY(y - h * 0.05);
        canhaoJogador.setWidth(w * 0.08);
        canhaoJogador.setHeight(h * 0.18);
    }

    private void atualizarFrota() {
        // Remove formas de inimigos anteriores e redesenha os vivos
        // (estratégia simples: limpa e recria apenas os nós de inimigos)
        campoJogo.getChildren().removeIf(n -> Boolean.TRUE.equals(n.getUserData()) );

        for (Inimigo inimigo : modelo.getFrota().getVivos()) {
            Color cor = corInimigo(inimigo);
            double x = inimigo.getX(), y = inimigo.getY();
            double w = inimigo.getLargura(), h = inimigo.getAltura();

            // Auréola
            Ellipse aureola = new Ellipse(x + w / 2, y + h / 2, w / 2 + 3, h / 2 + 3);
            aureola.setFill(Color.color(cor.getRed(), cor.getGreen(), cor.getBlue(), 0.10));
            aureola.setUserData(true);

            // Corpo
            Rectangle corpo = new Rectangle(x + w * 0.1, y + h * 0.15, w * 0.8, h * 0.7);
            corpo.setFill(cor);
            corpo.setArcWidth(8);
            corpo.setArcHeight(8);
            corpo.setUserData(true);

            // Tentáculos
            Line t1 = criarTentaculo(x, y + h * 0.35, x + w * 0.15, y + h * 0.5, cor);
            Line t2 = criarTentaculo(x + w, y + h * 0.35, x + w * 0.85, y + h * 0.5, cor);
            Line t3 = criarTentaculo(x + w * 0.05, y + h * 0.65, x + w * 0.18, y + h * 0.5, cor);
            Line t4 = criarTentaculo(x + w * 0.95, y + h * 0.65, x + w * 0.82, y + h * 0.5, cor);

            // Olhos
            Ellipse olhoEsq = new Ellipse(x + w * 0.35, y + h * 0.39, w * 0.07, h * 0.11);
            olhoEsq.setFill(Color.web("#000814"));
            olhoEsq.setUserData(true);

            Ellipse olhoDir = new Ellipse(x + w * 0.65, y + h * 0.39, w * 0.07, h * 0.11);
            olhoDir.setFill(Color.web("#000814"));
            olhoDir.setUserData(true);

            Ellipse brilhoEsq = new Ellipse(x + w * 0.345, y + h * 0.375, w * 0.045, h * 0.075);
            brilhoEsq.setFill(cor.brighter());
            brilhoEsq.setUserData(true);

            Ellipse brilhoDir = new Ellipse(x + w * 0.645, y + h * 0.375, w * 0.045, h * 0.075);
            brilhoDir.setFill(cor.brighter());
            brilhoDir.setUserData(true);

            campoJogo.getChildren().addAll(
                    aureola, corpo, t1, t2, t3, t4,
                    olhoEsq, olhoDir, brilhoEsq, brilhoDir
            );
        }
    }

    private Line criarTentaculo(double x1, double y1, double x2, double y2, Color cor) {
        Line l = new Line(x1, y1, x2, y2);
        l.setStroke(cor);
        l.setStrokeWidth(1.5);
        l.setUserData(true);
        return l;
    }

    private Color corInimigo(Inimigo i) {
        if (i instanceof Inimigo_Tras) return COR_INIMIGO_T;
        if (i instanceof Inimigo_Meio) return COR_INIMIGO_M;
        return COR_INIMIGO_F;
    }

    private void atualizarInvasorAleatorio() {
        campoJogo.getChildren().removeIf(n -> "invasor".equals(n.getUserData()));

        Inimigo_aleatorio inv = modelo.getInvasorAleatorio();
        if (inv == null || !inv.isVivo()) return;

        double x = inv.getX(), y = inv.getY();
        double w = inv.getLargura(), h = inv.getAltura();

        Ellipse aureola = new Ellipse(x + w / 2, y + h / 2, w / 2 + 6, h / 2 + 6);
        aureola.setFill(Color.web("#ffd70018"));
        aureola.setUserData("invasor");

        Ellipse corpo = new Ellipse(x + w / 2, y + h * 0.55, w / 2, h * 0.25);
        corpo.setFill(COR_INVASOR);
        corpo.setUserData("invasor");

        Ellipse cabeca = new Ellipse(x + w * 0.5, y + h * 0.275, w * 0.3, h * 0.2);
        cabeca.setFill(Color.web("#fffacd"));
        cabeca.setUserData("invasor");

        Text pontos = new Text("?");
        pontos.setFont(Font.font("Monospace", FontWeight.BOLD, 11));
        pontos.setFill(COR_INVASOR);
        pontos.setX(x + w * 0.44);
        pontos.setY(y - 4);
        pontos.setUserData("invasor");

        campoJogo.getChildren().addAll(aureola, corpo, cabeca, pontos);
    }

    private void atualizarBarricadas() {
        campoJogo.getChildren().removeIf(n -> "barricada".equals(n.getUserData()));

        for (Barricadas b : modelo.getBarricadas()) {
            double pct = b.getPercentagemIntegridade();
            Color cor = interpolarCor(pct);
            double x = b.getX(), y = b.getY();
            double w = b.getLargura(), h = b.getAltura();

            // Auréola
            Rectangle aureola = new Rectangle(x - 3, y - 3, w + 6, h + 6);
            aureola.setFill(Color.color(cor.getRed(), cor.getGreen(), cor.getBlue(), 0.12));
            aureola.setArcWidth(10);
            aureola.setArcHeight(10);
            aureola.setUserData("barricada");

            // Corpo
            Rectangle corpo = new Rectangle(x, y, w, h);
            corpo.setFill(cor);
            corpo.setArcWidth(8);
            corpo.setArcHeight(8);
            corpo.setUserData("barricada");

            campoJogo.getChildren().addAll(aureola, corpo);

            // Rachas de dano
            if (pct < 0.66) {
                Line racha1 = new Line(x + w * 0.3, y, x + w * 0.15, y + h);
                racha1.setStroke(Color.color(0, 0, 0, 0.5));
                racha1.setStrokeWidth(1.5);
                racha1.setUserData("barricada");
                campoJogo.getChildren().add(racha1);
            }
            if (pct < 0.33) {
                Line racha2 = new Line(x + w * 0.65, y, x + w * 0.80, y + h);
                racha2.setStroke(Color.color(0, 0, 0, 0.5));
                racha2.setStrokeWidth(1.5);
                racha2.setUserData("barricada");
                campoJogo.getChildren().add(racha2);
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

    private void atualizarProjetisJogador() {
        campoJogo.getChildren().removeIf(n -> "projetil_j".equals(n.getUserData()));

        for (Projetil_jogador p : modelo.getProjetisJogador()) {
            if (!p.isAtivo()) continue;
            double x = p.getX(), y = p.getY();
            double w = p.getLargura(), h = p.getAltura();

            // Brilho
            Rectangle brilho = new Rectangle(x - 1, y + 4, w + 2, h);
            brilho.setFill(Color.web("#00ffcc44"));
            brilho.setArcWidth(3);
            brilho.setArcHeight(3);
            brilho.setUserData("projetil_j");

            // Projétil
            Rectangle projetil = new Rectangle(x, y, w, h * 0.7);
            projetil.setFill(COR_PROJETIL_J);
            projetil.setArcWidth(3);
            projetil.setArcHeight(3);
            projetil.setUserData("projetil_j");

            campoJogo.getChildren().addAll(brilho, projetil);
        }
    }

    private void atualizarProjetisInimigos() {
        campoJogo.getChildren().removeIf(n -> "projetil_i".equals(n.getUserData()));

        for (Projetil_Inimigo p : modelo.getProjetisInimigos()) {
            if (!p.isAtivo()) continue;
            double x = p.getX(), y = p.getY();
            double w = p.getLargura(), h = p.getAltura();

            if (p.getTipoTrajeto() == Projetil_Inimigo.TipoTrajeto.ZIGZAG) {
                Ellipse brilho = new Ellipse(x + w / 2, y + h / 2, w / 2 + 2, h / 2 + 2);
                brilho.setFill(Color.web("#ff446644"));
                brilho.setUserData("projetil_i");

                Polygon losango = new Polygon(
                        x + w / 2, y,
                        x + w,     y + h / 2,
                        x + w / 2, y + h,
                        x,         y + h / 2
                );
                losango.setFill(COR_PROJETIL_I);
                losango.setUserData("projetil_i");

                campoJogo.getChildren().addAll(brilho, losango);
            } else {
                Ellipse brilho = new Ellipse(x + w / 2, y + h / 2, w / 2 + 2, h / 2 + 2);
                brilho.setFill(Color.web("#aaff0044"));
                brilho.setUserData("projetil_i");

                Ellipse projetil = new Ellipse(x + w / 2, y + h / 2, w / 2, h / 2);
                projetil.setFill(COR_PROJETIL_I_RETO);
                projetil.setUserData("projetil_i");

                campoJogo.getChildren().addAll(brilho, projetil);
            }
        }
    }

    // =========================================================
    //  HUD
    // =========================================================

    private void atualizarHUD() {
        txtScore.setText(String.format("%06d", modelo.getJogador().getPontuacao()));
        txtHiScore.setText(String.format("%06d", modelo.gethiScore()));
        txtVaga.setText(String.valueOf(modelo.getVaga()));
        txtVidas.setText("VIDAS: " + modelo.getJogador().getVidas());
    }

    // =========================================================
    //  OVERLAY DE VAGA CONCLUÍDA
    // =========================================================

    private void atualizarOverlayVaga() {
        boolean mostrar = modelo.getEstado() == EstadoJogo.TRANSICAO_VAGA;
        overlayVaga.setVisible(mostrar);
        txtVagaConcluida.setVisible(mostrar);
        txtPrecisao.setVisible(mostrar);
        txtProximaVaga.setVisible(mostrar);

        if (mostrar) {
            txtPrecisao.setText("Precisão: " + modelo.getPrecisaoTiro() + "%");
            txtProximaVaga.setText("A preparar vaga " + (modelo.getVaga() + 1) + "...");
            // Centrar os textos
            centrarTexto(txtVagaConcluida, ALTURA / 2 - 30);
            centrarTexto(txtPrecisao,      ALTURA / 2 + 10);
            centrarTexto(txtProximaVaga,   ALTURA / 2 + 44);
        }
    }

    // =========================================================
    //  TRANSIÇÕES
    // =========================================================

    private void verificarTransicoes() {
        EstadoJogo estado = modelo.getEstado();

        if (estado == EstadoJogo.GAME_OVER) {
            parar();
            managerView.mostrarGameOver(modelo);
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
    //  UTILITÁRIOS
    // =========================================================

    private Text criarTextoHUD(String conteudo, double x, double y, Color cor, int tamanho, boolean bold) {
        Text t = new Text(conteudo);
        t.setFont(bold
                ? Font.font("Monospace", FontWeight.BOLD, tamanho)
                : Font.font("Monospace", tamanho));
        t.setFill(cor);
        t.setX(x);
        t.setY(y);
        return t;
    }

    private Text criarTextoOverlay(String conteudo, int tamanho, String corHex, double y) {
        Text t = new Text(conteudo);
        t.setFont(Font.font("Monospace", FontWeight.BOLD, tamanho));
        t.setFill(Color.web(corHex));
        t.setY(y);
        t.setVisible(false);
        // APAGAR A LINHA QUE ESTAVA AQUI!
        return t;
    }

    private void centrarTexto(Text t, double y) {
        t.setX((LARGURA - t.getText().length() * 8.5) / 2);
        t.setY(y);
    }
}
