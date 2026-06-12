package game_graphicInterface.View;

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

/**
 * Classe responsável pela interface visual do menu de controlos.
 * Estende {@link StackPane} e exibe uma lista de teclas e respetivas ações,
 * bem como um botão para regressar ao menu anterior.
 */
public class Controlos_View extends StackPane {

    private final Button btnVoltar;

    /**
     * Construtor da classe {@code Controlos_View}.
     * Inicializa os componentes visuais, definindo o tamanho da janela, a cor de fundo,
     * o título estilizado, a lista de controlos disponíveis e o botão de voltar.
     */
    public Controlos_View() {
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

    /**
     * Helper method para adicionar uma linha com a tecla e a sua respetiva descrição à lista.
     *
     * @param box       O contentor {@link VBox} onde a linha será adicionada.
     * @param tecla     A {@code String} que representa a tecla ou atalho (ex: "← / A").
     * @param descricao A {@code String} com a descrição da ação correspondente (ex: "Mover para a esquerda").
     */
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

    /**
     * Cria e estiliza um botão com um efeito visual estilo "neon".
     * O botão possui comportamentos interativos (hover) quando o rato passa por cima.
     *
     * @param texto   O texto a ser exibido no botão.
     * @param corNeon O código hexadecimal da cor (ex: "#00f5ff") a aplicar aos bordos e ao texto.
     * @return Um objeto {@link Button} configurado e estilizado.
     */
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

    /**
     * Obtém a referência do botão "Voltar".
     * Este método é utilizado pelo Controller associado para atribuir eventos (ActionListeners).
     *
     * @return O {@link Button} utilizado para regressar.
     */
    public Button getBtnVoltar() { return btnVoltar; }
}