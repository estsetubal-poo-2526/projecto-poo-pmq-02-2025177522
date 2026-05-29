package game_graphicInterface.View;

import game_Logic.MelhoresPontuacoes;
import game_Logic.ModeloJogo;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import java.util.List;
import java.util.Optional;

public class Tabela_Classificacao extends StackPane {
    private final Button btnVoltar;

    public Tabela_Classificacao(ModeloJogo jogo) {
        setPrefSize(800, 600);
        setStyle("-fx-background-color: #000814;");

        //Titulo
        Text titulo = new Text("MELHORES PONTUAÇÕES");
        titulo.setFont(Font.font("Monospace", FontWeight.BOLD, 32));
        titulo.setFill(Color.web("#00f5ff"));
        titulo.setStyle("-fx-effect: dropshadow(gaussian, #00f5ff, 10, 0.2, 0, 0);");

        //Cabeçalho
        HBox cabecalho = criarLinha("#", "NOME", "PONTUAÇÃO", "AGA", true);
        cabecalho.setStyle("-fx-border-color: #00f5ff44; -fx-border-width: 0 0 1 0;");
        cabecalho.setPadding(new Insets(0, 0, 8, 0));

        // Linhas da tabela
        VBox tabela = new VBox(6);
        tabela.setAlignment(Pos.CENTER);
        tabela.getChildren().add(cabecalho);


        java.util.List<MelhoresPontuacoes> lista = Optional.ofNullable(jogo)
                .map(ModeloJogo::getClassificacoes)
                .stream()                // Converte o Optional num Stream
                .flatMap(List::stream)   // Transforma a lista numa Stream de MelhoresPontuacoes
                .toList();               // Coleta de volta para uma nova lista

        if (lista.isEmpty()) {
            Text vazio = new Text("— Sem pontuações registadas —");
            vazio.setFont(Font.font("Monospace", 14));
            vazio.setFill(Color.web("#00f5ff66"));
            tabela.getChildren().add(vazio);
        } else {
            for (int i = 0; i < lista.size(); i++) {
                MelhoresPontuacoes mp = lista.get(i);
                String corStr = (i == 0) ? "#ff8c00" : (i < 3) ? "#00f5ff" : "#aaaaaa";
                HBox linha = criarLinha(
                        String.valueOf(i + 1),
                        mp.getNome(),
                        String.format("%06d", mp.getPontuacao()),
                        String.valueOf(mp.getVaga()),
                        false
                );
                for (javafx.scene.Node n : linha.getChildren()) {
                    if (n instanceof Text t) t.setFill(Color.web(corStr));
                }
                tabela.getChildren().add(linha);
            }
        }

        tabela.setMaxWidth(500);

        // Botão voltar
        btnVoltar = new javafx.scene.control.Button("◀  VOLTAR");
        btnVoltar.setPrefWidth(180);
        btnVoltar.setPrefHeight(40);
        String estilo =
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #00f5ff;" +
                        "-fx-font-family: Monospace;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-border-color: #00f5ff;" +
                        "-fx-border-width: 2;" +
                        "-fx-cursor: hand;";
        String hover =
                "-fx-background-color: #00f5ff22;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-family: Monospace;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-border-color: #00f5ff;" +
                        "-fx-border-width: 2;" +
                        "-fx-cursor: hand;";
        btnVoltar.setStyle(estilo);
        btnVoltar.setOnMouseEntered(e -> btnVoltar.setStyle(hover));
        btnVoltar.setOnMouseExited(e -> btnVoltar.setStyle(estilo));

        VBox conteudo = new VBox(24, titulo, tabela, btnVoltar);
        conteudo.setAlignment(Pos.CENTER);
        conteudo.setMaxWidth(520);

        getChildren().add(conteudo);
    }

    private HBox criarLinha(String pos, String nome, String pontos, String vaga, boolean header) {
        HBox linha = new HBox();
        linha.setAlignment(Pos.CENTER_LEFT);
        linha.setMinWidth(480);
        linha.setMaxWidth(480);

        Text tPos = texto(pos, 40, header);
        Text tNome = texto(nome, 120, header);
        Text tPontos = texto(pontos, 200, header);
        Text tVaga = texto("V" + vaga, 80, header);

        if (header) {
            for (Text t : new Text[]{tPos, tNome, tPontos, tVaga}) {
                t.setFill(Color.web("#00f5ffaa"));
                t.setFont(Font.font("Monospace", FontWeight.BOLD, 13));
            }
        }

        linha.getChildren().addAll(tPos, tNome, tPontos, tVaga);
        HBox.setHgrow(tPontos, Priority.ALWAYS);
        return linha;
    }

    private Text texto(String s, double minW, boolean header) {
        Text t = new Text(s);
        t.setFont(Font.font("Monospace", header ? FontWeight.BOLD : FontWeight.NORMAL, 15));
        t.setFill(Color.web("#00f5ff"));
        t.setWrappingWidth(minW);
        return t;
    }

    public javafx.scene.control.Button getBtnVoltar() {
        return btnVoltar;
    }
}

