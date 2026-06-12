package game_graphicInterface.View;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Ecrã de pausa sobreposto ao jogo.
 *
 * <p>Esta View é criada uma única vez na classe {@code Game_View} e a sua visibilidade
 * é controlada através do método {@code setVisible(true/false)} consoante o estado
 * atual do jogo (pausado ou em execução).</p>
 * * <p>Apresenta um fundo escurecido semitransparente, uma mensagem informativa e opções
 * para o jogador retomar a partida ou desistir e voltar ao menu principal.</p>
 */
public class Pause_Menu_View extends StackPane {

    /** Botão que permite ao jogador retomar a partida em curso. */
    private final Button btnRetomar;

    /** Botão que permite ao jogador abandonar a partida e voltar ao menu principal. */
    private final Button btnDesistir;

    /**
     * Construtor da classe {@code Pause_Menu_View}.
     * <p>Inicializa a interface do menu de pausa, definindo um fundo translúcido,
     * textos estilizados indicando o estado de pausa e posicionando os botões de ação.</p>
     */
    public Pause_Menu_View() {
        // Fundo semitransparente escuro
        setStyle("-fx-background-color: rgba(0, 0, 0, 0.65);");
        setPrefSize(800, 600);
        setVisible(false); // começa invisível

        // Título
        Text titulo = new Text("⏸  PAUSADO");
        titulo.setFont(Font.font("Monospace", FontWeight.BOLD, 36));
        titulo.setFill(Color.WHITE);
        titulo.setStyle("-fx-effect: dropshadow(gaussian, #00f5ff, 10, 0.2, 0, 0);");

        // Subtítulo
        Text subtitulo = new Text("Prima ESC ou P para continuar");
        subtitulo.setFont(Font.font("Monospace", 16));
        subtitulo.setFill(Color.web("#aaaaaa"));

        // Botões
        btnRetomar  = criarBotao("▶  RETOMAR",  "#00f5ff");
        btnDesistir = criarBotao("✕  DESISTIR", "#ff4466");

        VBox conteudo = new VBox(20, titulo, subtitulo, btnRetomar, btnDesistir);
        conteudo.setAlignment(Pos.CENTER);

        getChildren().add(conteudo);
        setAlignment(Pos.CENTER);
    }

    // =========================================================
    //  FÁBRICA DE BOTÕES
    // =========================================================

    /**
     * Cria e estiliza um botão com um efeito de neon, mantendo o padrão visual do jogo.
     * <p>Configura as propriedades de tamanho, margens, fontes e os eventos de interação
     * do rato (hover) para alterar dinamicamente o brilho e a cor de fundo do botão.</p>
     *
     * @param texto   O texto a ser exibido no botão.
     * @param corNeon O código hexadecimal da cor (ex: "#00f5ff") a aplicar ao texto, bordos e sombra.
     * @return Um objeto {@link Button} pronto a ser utilizado na interface.
     */
    private Button criarBotao(String texto, String corNeon) {
        Button btn = new Button(texto);
        btn.setPrefWidth(220);
        btn.setPrefHeight(44);

        String estilo = String.format(
                "-fx-background-color: transparent;"  +
                        "-fx-text-fill: %s;"                  +
                        "-fx-font-family: Monospace;"          +
                        "-fx-font-size: 15px;"                 +
                        "-fx-font-weight: bold;"               +
                        "-fx-border-color: %s;"                +
                        "-fx-border-width: 2;"                 +
                        "-fx-cursor: hand;"                    +
                        "-fx-effect: dropshadow(gaussian, %s, 10, 0.2, 0, 0);",
                corNeon, corNeon, corNeon);

        String hover = String.format(
                "-fx-background-color: %s22;"          +
                        "-fx-text-fill: white;"                +
                        "-fx-font-family: Monospace;"          +
                        "-fx-font-size: 15px;"                 +
                        "-fx-font-weight: bold;"               +
                        "-fx-border-color: %s;"                +
                        "-fx-border-width: 2;"                 +
                        "-fx-cursor: hand;"                    +
                        "-fx-effect: dropshadow(gaussian, %s, 10, 0.2, 0, 0);",
                corNeon, corNeon, corNeon);

        btn.setStyle(estilo);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e  -> btn.setStyle(estilo));
        return btn;
    }

    // =========================================================
    //  GETTERS
    // =========================================================

    /**
     * Obtém o botão responsável por retomar a partida.
     * * @return O {@link Button} "RETOMAR".
     */
    public Button getBtnRetomar()  { return btnRetomar;  }

    /**
     * Obtém o botão responsável por desistir da partida.
     * * @return O {@link Button} "DESISTIR".
     */
    public Button getBtnDesistir() { return btnDesisti; }
}