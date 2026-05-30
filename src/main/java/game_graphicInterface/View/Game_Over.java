package game_graphicInterface.View;

import game_Logic.MelhoresPontuacoes;
import game_Logic.ModeloJogo;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Ecrã apresentado no fim do jogo.
 *
 * <p>Mostra as estatísticas da partida (pontuação, vaga, precisão),
 * permite ao jogador inserir as suas iniciais e navegar para
 * jogar novamente, ver as classificações ou voltar ao menu.
 *
 * <p>O registo da pontuação é protegido por uma flag para garantir
 * que nunca é feito mais do que uma vez, independentemente do botão
 * que o jogador prime.
 */
public class Game_Over extends StackPane {

    private final Button btnJogarNovamente;
    private final Button btnMenu;
    private final Button btnGuardar;
    /** Impede que a pontuação seja registada mais do que uma vez. */
    private boolean jaRegistado = false;

    /**
     * @param modeloJogo modelo com as estatísticas da partida
     * @param manager    gestor de navegação
     */
    public Game_Over(ModeloJogo modeloJogo, Manager_View manager) {
        setPrefSize(800, 600);
        setStyle("-fx-background-color: #000814;");

        Text titulo = criarTitulo();
        VBox stats  = criarEstatisticas(modeloJogo);

        TextField campoIniciais = criarCampoIniciais(modeloJogo);
        VBox registoBox         = criarRegistoBox(modeloJogo, campoIniciais);

        btnJogarNovamente = criarBotao("▶  JOGAR NOVAMENTE", "#00f5ff");
        btnMenu           = criarBotao("⌂  MENU PRINCIPAL",  "#00f5ff");
        btnGuardar        = criarBotao("Guardar classificação" , "#FF0000");

        // Ações dos botões — registam a pontuação antes de navegar
        btnJogarNovamente.setOnAction(e -> {
            registar(modeloJogo, campoIniciais);
            manager.iniciar();
        });
        btnMenu.setOnAction(e -> {
            registar(modeloJogo, campoIniciais);
            manager.mostrarMenu();
        });
        btnGuardar.setOnAction(e -> {
            registar(modeloJogo, campoIniciais);
            btnGuardar.setText("✔ GUARDADO");
            btnGuardar.setDisable(true);
        });


        HBox botoes = new HBox(20, btnJogarNovamente, btnMenu);
        botoes.setAlignment(Pos.CENTER);

        VBox conteudo = new VBox(22, titulo, stats, registoBox,btnGuardar, botoes);
        conteudo.setAlignment(Pos.CENTER);
        conteudo.setMaxWidth(500);

        getChildren().add(conteudo);
    }

    // =========================================================
    //  CONSTRUÇÃO DOS ELEMENTOS
    // =========================================================

    private Text criarTitulo() {
        Text titulo = new Text("GAME OVER");
        titulo.setFont(Font.font("Monospace", FontWeight.BOLD, 32));
        titulo.setFill(Color.web("#00f5ff"));
        titulo.setStyle("-fx-effect: dropshadow(gaussian, #00f5ff, 10, 0.2, 0, 0);");
        return titulo;
    }

    private VBox criarEstatisticas(ModeloJogo modelo) {
        VBox stats = new VBox(8);
        stats.setAlignment(Pos.CENTER);
        adicionarStat(stats, "Pontuação Final:", String.format("%06d", modelo.getJogador().getPontuacao()), "#00f5ff");
        adicionarStat(stats, "Nível Alcançado:", "Vaga " + modelo.getVaga(),                               "#00f5ff");
        adicionarStat(stats, "Precisão de Tiro:", modelo.getPrecisaoTiro() + "%",                          "#00ff88");
        return stats;
    }

    private TextField criarCampoIniciais(ModeloJogo modeloJogo) {
        TextField campo = new TextField();
        campo.setMaxWidth(100);
        campo.setStyle(
                "-fx-background-color: #001233;" +
                        "-fx-text-fill: #00f5ff;"        +
                        "-fx-font-family: Monospace;"    +
                        "-fx-font-size: 18px;"           +
                        "-fx-border-color: #00f5ff;"     +
                        "-fx-border-width: 2;"           +
                        "-fx-alignment: center;"
        );
        campo.setPromptText("AAAAAAAAAA");
        // Registar ao premir Enter no campo
        campo.setOnAction(e -> registar(modeloJogo, campo));
        return campo;
    }

    /**
     * Constrói a caixa de registo de iniciais.
     * Inclui o banner de novo recorde se aplicável.
     */
    private VBox criarRegistoBox(ModeloJogo modeloJogo, TextField campoIniciais) {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);

        if (modeloJogo.ehNovoRecorde()) {
            Text novoRec = new Text("★ NOVO RECORDE! ★");
            novoRec.setFont(Font.font("Monospace", FontWeight.BOLD, 22));
            novoRec.setFill(Color.web("#ff8c00"));
            novoRec.setStyle("-fx-effect: dropshadow(gaussian, #ff8c00, 10, 0.2, 0, 0);");
            box.getChildren().add(novoRec);
        }

        Text lblIniciais = new Text("Insira iniciais: ");
        lblIniciais.setFont(Font.font("Monospace", 15));
        lblIniciais.setFill(Color.web("#00f5ff"));

        HBox linhaIniciais = new HBox(10, lblIniciais, campoIniciais);
        linhaIniciais.setAlignment(Pos.CENTER);
        box.getChildren().add(linhaIniciais);

        return box;
    }

    private void adicionarStat(VBox box, String label, String valor, String corHex) {
        Text lbl = new Text(label);
        lbl.setFont(Font.font("Monospace", 15));
        lbl.setFill(Color.web("#aaaaaa"));

        Text val = new Text(valor);
        val.setFont(Font.font("Monospace", FontWeight.BOLD, 15));
        val.setFill(Color.web(corHex));

        HBox linha = new HBox(10, lbl, val);
        linha.setAlignment(Pos.CENTER);
        box.getChildren().add(linha);
    }

    // =========================================================
    //  REGISTO DE PONTUAÇÃO
    // =========================================================

    /**
     * Regista a pontuação do jogador na tabela de classificações.
     *
     * <p>Protegido por {@link #jaRegistado} para garantir que não é
     * chamado mais do que uma vez mesmo que vários botões sejam premidos.
     * Se o campo estiver vazio, gera automaticamente um nome "User N".
     */
    private void registar(ModeloJogo modelo, TextField campo) {
        if (jaRegistado) {
            return;
        }
        jaRegistado = true;

        String nome = campo.getText().trim();

        if (nome.isBlank()) {
            int contador = 1;
            while (MelhoresPontuacoes.existeNome("User " + contador)) {
                contador++;
            }
            nome = "User " + contador;
        }

        MelhoresPontuacoes.adicionarScore(nome, modelo.getPontuacaoVaga(), modelo.getVaga());
    }

    // =========================================================
    //  FÁBRICA DE BOTÕES
    // =========================================================

    private Button criarBotao(String texto, String corNeon) {
        Button btn = new Button(texto);
        btn.setPrefWidth(220);
        btn.setPrefHeight(40);

        String estilo = String.format(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: %s;"                 +
                        "-fx-font-family: Monospace;"         +
                        "-fx-font-size: 14px;"                +
                        "-fx-font-weight: bold;"              +
                        "-fx-border-color: %s;"               +
                        "-fx-border-width: 2;"                +
                        "-fx-cursor: hand;",
                corNeon, corNeon);

        String hover = String.format(
                "-fx-background-color: %s22;"         +
                        "-fx-text-fill: white;"               +
                        "-fx-font-family: Monospace;"         +
                        "-fx-font-size: 14px;"                +
                        "-fx-font-weight: bold;"              +
                        "-fx-border-color: %s;"               +
                        "-fx-border-width: 2;"                +
                        "-fx-cursor: hand;",
                corNeon, corNeon);


        btn.setStyle(estilo);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e -> btn.setStyle(estilo));
        return btn;
    }
}
