package game_graphicInterface.Controlador;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Controlador_Jogo extends StackPane {

    private final Button btnVoltar;

    public Controlador_Jogo () {
        setPrefSize(800, 600);
        setStyle("-fx-background-color: #000814;");

        Text titulo = new Text("CONTROLOS");
        titulo.setFont(Font.font("Monospace", FontWeight.BOLD, 36));
        titulo.setFill(Color.web("#00f5ff"));
        titulo.setStyle("-fx-effect: dropshadow(gaussian, #00f5ff, 10, 0.2, 0, 0);");

        VBox listaControlos = new VBox(16);
        listaControlos.setAlignment(Pos.CENTER);
        listaControlos.setPadding(new Insets(20, 0, 20, 0));

        adicionarControlo(listaControlos, "← / A",      "Mover para a esquerda");
        adicionarControlo(listaControlos, "→ / D",      "Mover para a direita");
        adicionarControlo(listaControlos, "ESPAÇO",     "Disparar");
        adicionarControlo(listaControlos, "ESC / P",    "Pausar / Retomar");

        btnVoltar = criarBotao("◀  VOLTAR", "#00f5ff");

        VBox conteudo = new VBox(30, titulo, listaControlos, btnVoltar);
        conteudo.setAlignment(Pos.CENTER);
        conteudo.setMaxWidth(500);

        getChildren().add(conteudo);
    }

    private void adicionarControlo(VBox box, String tecla, String descricao) {
        HBox linha = new HBox(20);
        linha.setAlignment(Pos.CENTER);

        Text tTecla = new Text(tecla);
        tTecla.setFont(Font.font("Monospace", FontWeight.BOLD, 16));
        tTecla.setFill(Color.web("#ff8c00"));
        tTecla.setWrappingWidth(120);

        Text tDesc = new Text(descricao);
        tDesc.setFont(Font.font("Monospace", 16));
        tDesc.setFill(Color.web("#aaaaaa"));
        tDesc.setWrappingWidth(260);

        linha.getChildren().addAll(tTecla, tDesc);
        box.getChildren().add(linha);
    }

    private Button criarBotao(String texto, String corNeon) {
        Button btn = new Button(texto);
        btn.setPrefWidth(180);
        btn.setPrefHeight(40);
        String estilo = String.format(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: %s;" +
                        "-fx-font-family: Monospace;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-border-color: %s;" +
                        "-fx-border-width: 2;" +
                        "-fx-cursor: hand;",
                corNeon, corNeon);
        String hover = String.format(
                "-fx-background-color: %s22;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-family: Monospace;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-border-color: %s;" +
                        "-fx-border-width: 2;" +
                        "-fx-cursor: hand;",
                corNeon, corNeon);
        btn.setStyle(estilo);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e  -> btn.setStyle(estilo));
        return btn;
    }

    public Button getBtnVoltar() { return btnVoltar; }
}