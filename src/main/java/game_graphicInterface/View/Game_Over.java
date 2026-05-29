package game_graphicInterface.View;


import game_Logic.MelhoresPontuacoes;
import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.util.Duration;
import game_Logic.ModeloJogo;


public class Game_Over extends StackPane {

    private final Button btnJogarNovamente;
    private final Button btnMenu;
    private final Button btnClassificacoes;
    private Manager_View managerView;

    public Game_Over(ModeloJogo modeloJogo , Manager_View manager) {
        this.managerView = manager;
        setPrefSize(800, 600);
        setStyle("-fx-background-color: #000814;");
        //Titulo
        Text titulo = new Text("Game Over");
        titulo.setFont(Font.font("Monospace", FontWeight.BOLD, 32));
        titulo.setFill(Color.web("#00f5ff"));
        titulo.setStyle("-fx-effect: dropshadow(gaussian, #00f5ff, 10, 0.2, 0, 0);");

        // Estatísticas
        VBox stats = new VBox(8);
        stats.setAlignment(Pos.CENTER);
        adicionarStat(stats, "Pontuação Final:", String.format("%06d", modeloJogo.getJogador().getPontuacao()), "#00f5ff");
        adicionarStat(stats, "Nível Alcançado:", "Vaga " + modeloJogo.getVaga(), "#00f5ff");
        adicionarStat(stats, "Precisão de Tiro:", modeloJogo.getPrecisaoTiro() + "%", "#00ff88");


        // Campo de iniciais (sempre visível para registo)
        VBox registoBox = new VBox(10);
        registoBox.setAlignment(Pos.CENTER);

        boolean novoRecorde = modeloJogo.ehNovoRecorde();
        if (novoRecorde) {
            Text novoRec = new Text("★ NOVO RECORDE! ★");
            novoRec.setFont(Font.font("Monospace", FontWeight.BOLD, 22));
            novoRec.setFill(Color.web("#ff8c00"));
            novoRec.setStyle("-fx-effect: dropshadow(gaussian, #ff8c00, 10, 0.2, 0, 0);");

            registoBox.getChildren().add(novoRec);
        }
        Text lblIniciais = new Text("Insira iniciais: ");
        lblIniciais.setFont(Font.font("Monospace", 15));
        lblIniciais.setFill(Color.web("#00f5ff"));

        TextField campoIniciais = new TextField();
        campoIniciais.setMaxWidth(100);
        campoIniciais.setStyle(
                "-fx-background-color: #001233;" +
                        "-fx-text-fill: #00f5ff;" +
                        "-fx-font-family: Monospace;" +
                        "-fx-font-size: 18px;" +
                        "-fx-border-color: #00f5ff;" +
                        "-fx-border-width: 2;" +
                        "-fx-alignment: center;"
        );
        campoIniciais.setPromptText("AAAAA");


        campoIniciais.setOnAction(e -> registar(modeloJogo, campoIniciais));

        HBox linhaIniciais = new HBox(10, lblIniciais, campoIniciais);
        linhaIniciais.setAlignment(Pos.CENTER);
        registoBox.getChildren().add(linhaIniciais);

        // Botões
        btnJogarNovamente = criarBotao("▶  JOGAR NOVAMENTE", "#00f5ff");
        btnClassificacoes = criarBotao("★  CLASSIFICAÇÕES", "#ff8c00");
        btnMenu = criarBotao("⌂  MENU PRINCIPAL", "#00f5ff");

        btnJogarNovamente.setOnAction(e -> {
            registar(modeloJogo, campoIniciais);
            manager.Iniciar();
        });
        btnMenu.setOnAction(e -> {
            registar(modeloJogo, campoIniciais);
            manager.mostrarMenu();
        });
        btnClassificacoes.setOnAction(e -> {
            registar(modeloJogo, campoIniciais);
            manager.classificacoes(modeloJogo);
        });

        HBox botoes = new HBox(20, btnJogarNovamente, btnMenu);
        botoes.setAlignment(Pos.CENTER);

        VBox conteudo = new VBox(22, titulo, stats, registoBox, btnClassificacoes, botoes);
        conteudo.setAlignment(Pos.CENTER);
        conteudo.setMaxWidth(500);

        getChildren().add(conteudo);
    }

    private void adicionarStat(VBox box, String label, String valor, String cor) {
        HBox linha = new HBox(10);
        linha.setAlignment(Pos.CENTER);
        Text lbl = new Text(label);
        lbl.setFont(Font.font("Monospace", 15));
        lbl.setFill(Color.web("#aaaaaa"));
        Text val = new Text(valor);
        val.setFont(Font.font("Monospace", FontWeight.BOLD, 15));
        val.setFill(Color.web(cor));
        linha.getChildren().addAll(lbl, val);
        box.getChildren().add(linha);
    }

    private void registar(ModeloJogo modelo, TextField campo) {
        String nome = campo.getText().trim();

        if (nome.isBlank()) {
            int contador = 1;
            while (MelhoresPontuacoes.existeNome("User " + contador)) {
                contador++;
            }
            nome = "User " + contador;
        }

        int pontuacao = modelo.getPontuacaoVaga();
        int vaga = modelo.getVaga();

        MelhoresPontuacoes.adicionarScore(nome, pontuacao, vaga);
    }

    private Button criarBotao(String texto, String corNeon) {
        Button btn = new Button(texto);
        btn.setPrefWidth(220);
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
        btn.setOnMouseExited(e -> btn.setStyle(estilo));
        return btn;
    }
}