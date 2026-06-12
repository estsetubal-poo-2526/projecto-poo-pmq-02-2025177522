package game_graphicInterface.View;

import game_Logic.ModeloJogo;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

// Novos imports necessários para o áudio
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

/**
 * Gestor principal das Views da aplicação (Scene Manager).
 *
 * <p>Esta classe atua como o controlador central da interface gráfica, gerindo a
 * transição entre os diferentes ecrãs do jogo (Menu Principal, Jogo, Game Over,
 * Classificações e Controlos). Além disso, é responsável por inicializar e
 * manter a música de fundo a tocar em loop contínuo.</p>
 */
public class Manager_View {

    /** A janela (Stage) principal da aplicação JavaFX. */
    private final Stage stage;

    /** A cena (Scene) principal onde os diferentes ecrãs serão sobrepostos/substituídos. */
    private final Scene scene;

    /** O contentor raiz (StackPane) que alberga a View atualmente ativa. */
    private final StackPane root;

    /** Objeto responsável pela reprodução e controlo da música de fundo. */
    private MediaPlayer mediaPlayerFundo;

    /**
     * Construtor da classe {@code Manager_View}.
     * <p>Inicializa o contentor raiz, a cena principal com as dimensões padrão (800x600),
     * arranca a música de fundo e apresenta imediatamente o Menu Principal.</p>
     *
     * @param stage O {@link Stage} principal fornecido pelo método start da aplicação JavaFX.
     */
    public Manager_View(Stage stage) {
        this.stage = stage;
        this.root = new StackPane();
        this.scene = new Scene(root, 800, 600);
        iniciarMusicaFundo();

        mostrarMenu();
    }

    /**
     * Carrega e inicia a reprodução da música de fundo do jogo.
     * <p>Procura o ficheiro de áudio nos recursos do projeto. Se o ficheiro for
     * encontrado, configura o {@link MediaPlayer} para tocar em loop infinito
     * com um volume ajustado. Caso ocorra um erro, emite um aviso na consola.</p>
     */
    private void iniciarMusicaFundo() {
        try {
            URL caminhoMusica = getClass().getResource("/SpaceInvaders.mp3");

            if (caminhoMusica != null) {

                Media media = new Media(caminhoMusica.toString());
                mediaPlayerFundo = new MediaPlayer(media);

                // Configura para tocar em loop infinito
                mediaPlayerFundo.setCycleCount(MediaPlayer.INDEFINITE);

                // Ajusta o volume (0.0 a 1.0)
                mediaPlayerFundo.setVolume(0.3);

                // Dá play na música
                mediaPlayerFundo.play();
            } else {
                System.err.println("Aviso: Ficheiro de música não encontrado na pasta resources!");
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar o áudio: " + e.getMessage());
        }
    }

    /**
     * Transita a interface gráfica para o Menu Principal.
     * <p>Instancia o {@link MainMenu_View}, define as ações dos botões (lambdas)
     * e substitui o conteúdo do root pela nova View.</p>
     */
    public void mostrarMenu() {
        MainMenu_View menu = new MainMenu_View();
        menu.getBtnIniciar().setOnAction(e -> iniciar());
        menu.getBtnClassificacoes().setOnAction(e -> classificacoes(null));
        menu.getBtnSair().setOnAction(e -> stage.close());
        menu.getBtnControlos().setOnAction(e -> mostrarControlos());
        root.getChildren().setAll(menu);
    }

    /**
     * Inicia uma nova partida.
     * <p>Cria um novo modelo de jogo ({@link ModeloJogo}), instancia o ecrã de jogo
     * ({@link Game_View}) passando o modelo e o gestor atual, substitui a View
     * e dá ordem de arranque ao loop do jogo.</p>
     */
    public void iniciar() {
        ModeloJogo modelo = new ModeloJogo();
        Game_View gameView = new Game_View(modelo, this);
        root.getChildren().setAll(gameView);
        gameView.iniciar();
    }

    /**
     * Transita a interface gráfica para o ecrã de Classificações (Leaderboard).
     *
     * @param modeloJogo Opcional. O modelo do jogo mais recente. Pode ser {@code null}
     * se for acedido diretamente pelo Menu Principal.
     */
    public void classificacoes(ModeloJogo modeloJogo) {
        Tabela_Classificacao_View table = new Tabela_Classificacao_View(modeloJogo);
        table.getBtnVoltar().setOnAction(e -> mostrarMenu());
        root.getChildren().setAll(table);
    }

    /**
     * Transita a interface gráfica para o ecrã de Fim de Jogo (Game Over).
     *
     * @param modeloJogo O modelo do jogo que acabou de terminar, contendo as
     * estatísticas finais a apresentar ao jogador.
     */
    public void mostrarGameOver(ModeloJogo modeloJogo) {
        Game_Over_View ecraGameOver = new Game_Over_View(modeloJogo, this);
        root.getChildren().setAll(ecraGameOver);
    }

    /**
     * Transita a interface gráfica para o ecrã de Opções / Controlos.
     * <p>Mostra aos jogadores as teclas de atalho configuradas e providencia
     * um botão para regressar ao Menu Principal.</p>
     */
    public void mostrarControlos() {
        Controlos_View ctrl = new Controlos_View();
        ctrl.getBtnVoltar().setOnAction(e -> mostrarMenu());
        root.getChildren().setAll(ctrl);
    }

    /**
     * Retorna a cena (Scene) principal gerida por esta classe.
     * Útil para ser atribuída ao {@link Stage} principal da aplicação na classe Main.
     *
     * @return O objeto {@link Scene} em uso.
     */
    public Scene getScene() {
        return scene;
    }
}