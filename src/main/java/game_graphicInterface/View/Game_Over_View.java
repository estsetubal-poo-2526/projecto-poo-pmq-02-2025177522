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
 * Ecrã apresentado no fim do jogo (Game Over).
 *
 * <p>Esta View mostra as estatísticas finais da partida (pontuação, vaga alcançada,
 * precisão de tiro), permite ao jogador inserir as suas iniciais em caso de novo recorde
 * e fornece opções de navegação para jogar novamente ou voltar ao menu principal.
 *
 * <p>O registo da pontuação é protegido por uma flag de controlo para garantir
 * que a gravação no sistema de ficheiros nunca é feita mais do que uma vez,
 * independentemente das interações do jogador.
 */
public class Game_Over_View extends StackPane {

    /** Botão para reiniciar a partida. */
    private final Button btnJogarNovamente;

    /** Botão para regressar ao menu principal. */
    private final Button btnMenu;

    /** Botão para confirmar e guardar a classificação do jogador. */
    private final Button btnGuardar;

    /** Impede que a pontuação seja registada mais do que uma vez na mesma sessão de Game Over. */
    private boolean jaRegistado = false;

    /**
     * Construtor da classe {@code Game_Over_View}.
     * Inicializa a interface visual do ecrã de fim de jogo e define os eventos de clique dos botões.
     *
     * @param modeloJogo O modelo que contém as estatísticas e os dados da partida recém-terminada.
     * @param manager    O gestor de navegação responsável por alternar entre as diferentes Views.
     */
    public Game_Over_View(ModeloJogo modeloJogo, Manager_View manager) {
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
            manager.iniciar();
        });
        btnMenu.setOnAction(e -> {
            manager.mostrarMenu();
        });
        btnGuardar.setOnAction(e -> {
            registar(modeloJogo, campoIniciais);
            MelhoresPontuacoes.salvarNoDisco();
            btnGuardar.setText("✔ GUARDADO");
            btnGuardar.setDisable(true);
        });

        HBox botoes = new HBox(20, btnJogarNovamente, btnMenu);
        botoes.setAlignment(Pos.CENTER);

        VBox conteudo = new VBox(22, titulo, stats, registoBox, btnGuardar, botoes);
        conteudo.setAlignment(Pos.CENTER);
        conteudo.setMaxWidth(500);

        getChildren().add(conteudo);
    }

    // =========================================================
    //  CONSTRUÇÃO DOS ELEMENTOS
    // =========================================================

    /**
     * Cria e estiliza o título principal do ecrã ("GAME OVER").
     *
     * @return Um objeto {@link Text} configurado com o estilo visual apropriado.
     */
    private Text criarTitulo() {
        Text titulo = new Text("GAME OVER");
        titulo.setFont(Font.font("Monospace", FontWeight.BOLD, 32));
        titulo.setFill(Color.web("#00f5ff"));
        titulo.setStyle("-fx-effect: dropshadow(gaussian, #00f5ff, 10, 0.2, 0, 0);");
        return titulo;
    }

    /**
     * Constrói o contentor visual com a listagem das estatísticas finais do jogador.
     *
     * @param modelo O modelo de jogo com os dados da partida.
     * @return Uma {@link VBox} contendo as linhas de estatísticas (pontuação, vaga, precisão).
     */
    private VBox criarEstatisticas(ModeloJogo modelo) {
        VBox stats = new VBox(8);
        stats.setAlignment(Pos.CENTER);
        adicionarStat(stats, "Pontuação Final:", String.format("%06d", modelo.getJogador().getPontuacao()), "#00f5ff");
        adicionarStat(stats, "Nível Alcançado:", "Vaga " + modelo.getVaga(),                               "#00f5ff");
        adicionarStat(stats, "Precisão de Tiro:", modelo.getPrecisaoTiro() + "%",                          "#00ff88");
        return stats;
    }

    /**
     * Cria o campo de texto para o jogador introduzir as suas iniciais.
     * Define o comportamento de registo ao premir a tecla Enter.
     *
     * @param modeloJogo O modelo de jogo com os dados da partida.
     * @return Um {@link TextField} estilizado e configurado para entrada de texto.
     */
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
     * Constrói a caixa de registo de iniciais, combinando a label indicativa
     * e o campo de texto. Inclui dinamicamente um banner de destaque caso o
     * jogador tenha batido um novo recorde.
     *
     * @param modeloJogo    O modelo de jogo para verificar se é um novo recorde.
     * @param campoIniciais O {@link TextField} previamente criado para as iniciais.
     * @return Uma {@link VBox} contendo os elementos de interface para o registo.
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

    /**
     * Helper method para adicionar uma única linha de estatística ao contentor principal.
     *
     * @param box    A {@link VBox} onde a linha de estatística será inserida.
     * @param label  O texto descritivo da estatística (ex: "Pontuação Final:").
     * @param valor  O valor atingido pelo jogador.
     * @param corHex A cor hexadecimal em formato String a aplicar ao valor.
     */
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
     * Regista a pontuação do jogador na tabela de classificações em memória.
     *
     * <p>Protegido pelo atributo {@link #jaRegistado} para garantir que a gravação
     * não é duplicada caso o utilizador clique várias vezes nos botões.
     * Se o campo de texto estiver vazio, gera automaticamente um nome genérico
     * sequencial (ex: "User 1", "User 2").
     * * @param modelo O modelo contendo a pontuação final e a vaga.
     * @param campo  O campo de texto de onde as iniciais do jogador serão extraídas.
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

    /**
     * Cria e estiliza um botão padronizado com efeito neon para a interface.
     * Inclui a configuração dos eventos de hover do rato.
     *
     * @param texto   O texto que será exibido no botão.
     * @param corNeon A cor hexadecimal que define o bordo e os detalhes de estado do botão.
     * @return Um {@link Button} pronto a ser adicionado à interface visual.
     */
    private Button criarBotao(String texto, String corNeo) {
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