package game_graphicInterface.View;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.shape.SVGPath;

/**
 * Classe responsável pela interface visual do Menu Principal do jogo.
 *
 * <p>Estende {@link StackPane} e exibe o título do jogo ("SYNTHETIC OCEANS"),
 * elementos decorativos (submarinos com efeito neon desenhados via SVG) e os
 * botões de navegação principais (Iniciar Jogo, Classificações, Opções e Sair).</p>
 */
public class MainMenu_View extends StackPane {

    /** Botão para transitar para o ecrã de jogo e iniciar uma nova partida. */
    private final Button btnIniciar;

    /** Botão para aceder ao ecrã de melhores pontuações. */
    private final Button btnClassificacoes;

    /** Botão para encerrar a aplicação. */
    private final Button btnSair;

    /** Botão para aceder ao menu de opções/controlos. */
    private final Button btnControlos;

    /**
     * Construtor da classe {@code MainMenu_View}.
     * <p>Inicializa a cor de fundo, compõe a estrutura visual com o título
     * estilizado, posiciona os submarinos decorativos nos cantos do ecrã
     * e organiza os botões num contentor centralizado.</p>
     */
    public MainMenu_View() {
        this.setStyle("-fx-background-color: #000814;");

        // --- SUBMARINOS DECORATIVOS ---
        Group subEsquerdo = criarSubmarinoNeon();
        StackPane.setAlignment(subEsquerdo, Pos.TOP_LEFT);
        StackPane.setMargin(subEsquerdo, new Insets(40, 0, 0, 40));

        Group subDireito = criarSubmarinoNeon();
        subDireito.setScaleX(-1); // Inverte o submarino para virar para a esquerda
        StackPane.setAlignment(subDireito, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(subDireito, new Insets(0, 40, 40, 0));

        // Título "SYNTHETIC OCEANS"
        VBox tituloBox = new VBox(2);
        tituloBox.setAlignment(Pos.CENTER);

        Text linha1 = new Text("SYNTHETIC");
        linha1.setFont(Font.font("Monospace", 58));
        linha1.setFill(Color.web("#00f5ff"));
        linha1.setStyle("-fx-effect: dropshadow(gaussian, #00f5ff, 10, 0.2, 0, 0);");

        Text linha2 = new Text("OCEANS");
        linha2.setFont(Font.font("Monospace", 58));
        linha2.setFill(Color.web("#bf00ff"));
        linha2.setStyle("-fx-effect: dropshadow(gaussian, #bf00ff, 10, 0.2, 0, 0);");

        tituloBox.getChildren().addAll(linha1, linha2);

        // Botões
        btnIniciar = criarBotao("▶  INICIAR JOGO", "#00f5ff");
        btnClassificacoes = criarBotao("★  CLASSIFICAÇÕES", "#00f5ff");
        btnSair = criarBotao("✕  SAIR", "#ff4466");
        btnControlos = criarBotao("⚙  OPÇÕES", "#00f5ff");

        VBox menuBox = new VBox(16, tituloBox, btnIniciar, btnClassificacoes, btnControlos, btnSair);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setMaxWidth(340);

        // Adiciona os submarinos primeiro (para ficarem no fundo) e o menu por cima
        this.getChildren().addAll(subEsquerdo, subDireito, menuBox);
    }

    /**
     * Constrói e desenha um elemento decorativo em forma de submarino.
     * <p>Utiliza um {@link SVGPath} para traçar os contornos baseados em
     * coordenadas matemáticas vetoriais, resultando num design angular e "Sci-Fi".
     * Aplica também um efeito de sombra projetada (DropShadow) para simular o brilho neon.</p>
     *
     * @return Um {@link Group} contendo a forma do submarino estilizada.
     */
    private Group criarSubmarinoNeon() {
        Group sub = new Group();
        Color corNeon = Color.web("#00f5ff");

        // SVGPath permite desenhar vetores complexos (como num software de design)
        SVGPath formatoSub = new SVGPath();

        // Coordenadas matemáticas para um design "Stealth / Sci-Fi" angular
        String corpo       = "M 30,40 L 50,25 L 110,25 L 140,40 L 110,55 L 50,55 Z ";
        String torre       = "M 65,25 L 70,10 L 95,10 L 100,25 ";
        String periscopio  = "M 80,10 L 80,-5 L 95,-5 L 95,0 L 85,0 L 85,10 ";
        String barbatanas  = "M 30,40 L 10,20 L 20,40 L 10,60 Z ";
        String helice      = "M 20,40 L -5,40 M -5,30 L -15,40 L -5,50 Z ";
        String detalhes    = "M 110,32 L 90,32 L 90,48 L 110,48 M 50,25 L 50,55 M 75,25 L 75,55 ";


        formatoSub.setContent(corpo + torre + periscopio + barbatanas + helice + detalhes);


        formatoSub.setFill(Color.TRANSPARENT);
        formatoSub.setStroke(corNeon);
        formatoSub.setStrokeWidth(2.5);


        formatoSub.setStrokeLineJoin(javafx.scene.shape.StrokeLineJoin.ROUND);
        formatoSub.setStrokeLineCap(javafx.scene.shape.StrokeLineCap.ROUND);

        sub.getChildren().add(formatoSub);

        // Efeito Neon aprimorado (Brilho mais intenso e espalhado)
        javafx.scene.effect.DropShadow neon = new javafx.scene.effect.DropShadow();
        neon.setColor(corNeon);
        neon.setRadius(20);
        neon.setSpread(0.35);
        sub.setEffect(neon);

        // Ajusta o tamanho no ecrã
        sub.setScaleX(0.75);
        sub.setScaleY(0.75);

        return sub;
    }

    /**
     * Cria e estiliza um botão padronizado com efeito visual neon para o menu principal.
     * Inclui a configuração de transição visual quando o cursor passa sobre o botão.
     *
     * @param texto   O texto visível a apresentar no botão.
     * @param corNeon A cor hexadecimal (ex: "#00f5ff") aplicada ao bordo e texto.
     * @return Uma instância de {@link Button} totalmente configurada e com interatividade.
     */
    private Button criarBotao(String texto, String corNeon) {
        Button btn = new Button(texto);
        btn.setPrefWidth(280);
        btn.setPrefHeight(44);
        String estilo = String.format(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: %s;" +
                        "-fx-font-family: Monospace;" +
                        "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-border-color: %s;" +
                        "-fx-border-width: 2;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, %s, 10, 0.2, 0, 0);",
                corNeon, corNeon, corNeon);
        String estiloHover = String.format(
                "-fx-background-color: %s22;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-family: Monospace;" +
                        "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-border-color: %s;" +
                        "-fx-border-width: 2;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, %s, 10, 0.2, 0, 0);",
                corNeon, corNeon, corNeon);
        btn.setStyle(estilo);
        btn.setOnMouseEntered(e -> btn.setStyle(estiloHover));
        btn.setOnMouseExited(e -> btn.setStyle(estilo));
        return btn;
    }

    /**
     * Obtém o botão responsável por iniciar o jogo.
     * @return O {@link Button} "INICIAR JOGO".
     */
    public Button getBtnIniciar() { return btnIniciar; }

    /**
     * Obtém o botão responsável por aceder às classificações.
     * @return O {@link Button} "CLASSIFICAÇÕES".
     */
    public Button getBtnClassificacoes() { return btnClassificacoes; }

    /**
     * Obtém o botão responsável por encerrar a aplicação.
     * @return O {@link Button} "SAIR".
     */
    public Button getBtnSair() { return btnSair; }

    /**
     * Obtém o botão responsável por aceder ao menu de opções/controlos.
     * @return O {@link Button} "OPÇÕES".
     */
    public Button getBtnControlos() { return btnControlos; }
}