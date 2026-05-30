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
 * É criado uma única vez no Game_View e a sua visibilidade
 * é controlada com setVisible(true/false) conforme o estado do jogo.
 */
public class Pause_Menu_View extends StackPane {

    private final Button btnRetomar;
    private final Button btnDesistir;

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

    public Button getBtnRetomar()  { return btnRetomar;  }
    public Button getBtnDesistir() { return btnDesistir; }
}
