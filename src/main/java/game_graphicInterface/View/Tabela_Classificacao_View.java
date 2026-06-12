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

/**
 * Classe responsável por exibir o ecrã das melhores pontuações (Leaderboard).
 *
 * <p>Esta View apresenta uma tabela formatada com o Top de pontuações registadas,
 * destacando visualmente o primeiro classificado e o restante pódio (top 3).
 * Inclui também funcionalidades para regressar ao menu anterior e para limpar
 * todo o registo de classificações do sistema.</p>
 */
public class Tabela_Classificacao_View extends StackPane {

    /** Botão para regressar ao menu anterior. */
    private final Button btnVoltar;

    /** Botão para eliminar todos os registos de pontuações guardados. */
    private final Button btnLimpar;

    /**
     * Construtor da classe {@code Tabela_Classificacao_View}.
     * <p>Inicializa a estrutura visual, o cabeçalho da tabela e tenta obter a lista
     * de pontuações a partir do {@code ModeloJogo} fornecido ou diretamente da
     * classe {@code MelhoresPontuacoes}. O top 3 é destacado com cores distintas.</p>
     *
     * @param jogo O modelo de jogo atual. Pode ser {@code null} caso a tabela
     * seja acedida diretamente a partir do Menu Principal.
     */
    public Tabela_Classificacao_View(ModeloJogo jogo) {
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

        List<MelhoresPontuacoes> lista = Optional.ofNullable(jogo)
                .map(ModeloJogo::getClassificacoes)
                .orElseGet(MelhoresPontuacoes::getClassificacoes)
                .stream()
                .toList();

        if (lista.isEmpty()) {
            Text vazio = new Text("— Sem pontuações registadas —");
            vazio.setFont(Font.font("Monospace", 14));
            vazio.setFill(Color.web("#00f5ff66"));
            tabela.getChildren().add(vazio);
        } else {
            for (int i = 0; i < lista.size(); i++) {
                MelhoresPontuacoes mp = lista.get(i);
                String corStr;
                // Formatação de cores: Laranja para 1º, Ciano para 2º e 3º, Cinzento para os restantes
                if (i == 0) {
                    corStr = "#ff8c00";
                } else if (i < 3) {
                    corStr = "#00f5ff";
                } else {
                    corStr = "#aaaaaa";
                }
                HBox linha = criarLinha(
                        String.valueOf(i + 1),
                        mp.getNome(),
                        String.format("%06d", mp.getPontuacao()),
                        String.valueOf(mp.getVaga()),
                        false
                );
                for (javafx.scene.Node n : linha.getChildren()) {
                    if (n instanceof Text) {
                        Text t = (Text) n;
                        t.setFill(Color.web(corStr));
                    }
                }
                tabela.getChildren().add(linha);
            }
        }

        tabela.setMaxWidth(500);

        // Botões
        btnVoltar = createButton("◀  VOLTAR " , "FF6666");
        btnLimpar = createButton("LIMPAR" , "FF0000");

        VBox conteudo = new VBox(24, titulo, tabela, btnVoltar, btnLimpar);
        conteudo.setAlignment(Pos.CENTER);
        conteudo.setMaxWidth(520);

        getChildren().add(conteudo);

        // Lógica para o botão de limpar a tabela
        btnLimpar.setOnAction(e -> {
            MelhoresPontuacoes.limparTabela();

            btnLimpar.setText("✔ TABELA LIMPA");
            btnLimpar.setDisable(true);

            tabela.getChildren().clear();
            tabela.getChildren().add(cabecalho);

            Text vazio = new Text("— Sem pontuações registadas —");
            vazio.setFont(Font.font("Monospace", 14));
            vazio.setFill(Color.web("#00f5ff66"));
            tabela.getChildren().add(vazio);
        });
    }

    /**
     * Cria e estiliza um botão com propriedades visuais e efeitos de hover (neon).
     *
     * @param texto   O texto a ser exibido no botão.
     * @param corNeon O código de cor hexadecimal para os bordos e texto do botão.
     * @return Um objeto {@link Button} formatado.
     */
    private Button createButton(String texto, String corNeon) {
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

    /**
     * Constrói uma linha horizontal (HBox) para ser inserida na tabela de classificações.
     *
     * @param pos    A posição (ranking) do jogador.
     * @param nome   O nome ou iniciais do jogador.
     * @param pontos A pontuação obtida.
     * @param vaga   O nível (vaga) alcançado.
     * @param header Booleano que indica se a linha a criar é o cabeçalho principal da tabela.
     * @return Um {@link HBox} contendo os elementos de texto alinhados em colunas.
     */
    private HBox criarLinha(String pos, String nome, String pontos, String vaga, boolean header) {
        HBox linha = new HBox();
        linha.setAlignment(Pos.CENTER_LEFT);
        linha.setMinWidth(480);
        linha.setMaxWidth(480);

        Text tPos = texto(pos, 40, header);
        Text tNome = texto(nome, 120, header);
        Text tPontos = texto(pontos, 200, header);
        Text tVaga = texto(header ? vaga : "V" + vaga, 80, header);

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

    /**
     * Método auxiliar para criar e formatar os nós de texto das células da tabela.
     *
     * @param s      A string (conteúdo) a exibir.
     * @param minW   A largura mínima que o texto deverá ocupar, garantindo o alinhamento das colunas.
     * @param header Booleano que define se o texto pertence ao cabeçalho (aplica estilo negrito).
     * @return O objeto {@link Text} devidamente formatado.
     */
    private Text texto(String s, double minW, boolean header) {
        Text t = new Text(s);
        t.setFont(Font.font("Monospace", header ? FontWeight.BOLD : FontWeight.NORMAL, 15));
        t.setFill(Color.web("#00f5ff"));
        t.setWrappingWidth(minW);
        return t;
    }

    /**
     * Obtém o botão "Voltar".
     *
     * @return O {@link Button} utilizado para regressar ao menu principal.
     */
    public javafx.scene.control.Button getBtnVolta() {
        return btnVoltar;
    }
}