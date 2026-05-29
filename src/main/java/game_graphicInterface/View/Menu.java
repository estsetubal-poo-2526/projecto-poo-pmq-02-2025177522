package game_graphicInterface.View;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Menu extends StackPane {
    private final Button btnIniciar;
    private final Button btnClassificacoes;
    private final Button btnSair;

    public Menu(Button btnIniciar, Button btnClassificacoes, Button btnSair) {
        this.btnIniciar = btnIniciar;
        this.btnClassificacoes = btnClassificacoes;
        this.btnSair = btnSair;

        // Título "SYNTHETIC OCEANS"
        VBox tituloBox = new VBox(2);
        tituloBox.setAlignment(Pos.CENTER);

        Text linha1 = new Text("SYNTHETIC");
        linha1.setFont(Font.font("Monospace", FontWeight.BOLD, 58));
        linha1.setFill(Color.web("#00f5ff"));
        linha1.setStyle("-fx-effect: dropshadow(gaussian, #00f5ff, 20, 0.8, 0, 0);");

        Text linha2 = new Text("OCEANS");
        linha2.setFont(Font.font("Monospace", FontWeight.BOLD, 58));
        linha2.setFill(Color.web("#bf00ff"));
        linha2.setStyle("-fx-effect: dropshadow(gaussian, #bf00ff, 20, 0.8, 0, 0);");

        // Botões
        btnIniciar = criarBotao("▶  INICIAR JOGO", "#00f5ff");
        btnClassificacoes = criarBotao("★  CLASSIFICAÇÕES", "#00f5ff");
        btnSair = criarBotao("✕  SAIR", "#ff4466");

        VBox menuBox = new VBox(16, tituloBox, btnIniciar, btnClassificacoes, btnSair);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setMaxWidth(340);
    }

    private Button criarBotao(String texto, String corNeon) {
        Button btn = new Button(texto);
        btn.setPrefWidth(280);
        btn.setPrefHeight(44);String estilo = String.format(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: %s;" +
                        "-fx-font-family: Monospace;" +
                        "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-border-color: %s;" +
                        "-fx-border-width: 2;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, %s, 8, 0.5, 0, 0);",
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
                        "-fx-effect: dropshadow(gaussian, %s, 18, 0.9, 0, 0);",
                corNeon, corNeon, corNeon);
        btn.setStyle(estilo);
        btn.setOnMouseEntered(e -> btn.setStyle(estiloHover));
        btn.setOnMouseExited(e -> btn.setStyle(estilo));
        return btn;
    }
    private void desenharFundo(GraphicsContext gc, long now) {
        // Gradiente oceano profundo
        gc.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#000814")),
                new Stop(0.5, Color.web("#001233")),
                new Stop(1, Color.web("#000814"))));
        gc.fillRect(0, 0, 800, 600);

        // Linha de fundo suave
        gc.setStroke(Color.web("#00f5ff33"));
        gc.setLineWidth(1);
        gc.strokeLine(0, 580, 800, 580);
    }

    public Button getBtnIniciar() { return btnIniciar; }
    public Button getBtnClassificacoes() { return btnClassificacoes; }
    public Button getBtnSair() { return btnSair; }
}
