package game_graphicInterface.View;

import game_Logic.ModeloJogo;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

// Novos imports necessários para o áudio
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

public class Manager_View {

    private final Stage stage;
    private final Scene scene;
    private final StackPane root;

    // Objeto que vai controlar a música de fundo
    private MediaPlayer mediaPlayerFundo;

    public Manager_View(Stage stage) {
        this.stage = stage;
        this.root = new StackPane();
        this.scene = new Scene(root, 800, 600);
        iniciarMusicaFundo();

        mostrarMenu();
    }

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

    public void mostrarMenu() {
        MainMenu_View menu = new MainMenu_View();
        menu.getBtnIniciar().setOnAction(e-> iniciar());
        menu.getBtnClassificacoes().setOnAction(e->classificacoes(null));
        menu.getBtnSair().setOnAction(e -> stage.close());
        menu.getBtnControlos().setOnAction(e -> mostrarControlos());
        root.getChildren().setAll(menu);
    }

    public void iniciar() {
        ModeloJogo modelo = new ModeloJogo();
        Game_View gameView = new Game_View(modelo, this);
        root.getChildren().setAll(gameView);
        gameView.iniciar();
    }

    public void classificacoes(ModeloJogo modeloJogo){
        Tabela_Classificacao_View table = new Tabela_Classificacao_View(modeloJogo);
        table.getBtnVoltar().setOnAction(e->mostrarMenu());
        root.getChildren().setAll(table);
    }

    public void mostrarGameOver(ModeloJogo modeloJogo) {
        Game_Over_View ecraGameOver = new Game_Over_View(modeloJogo, this);
        root.getChildren().setAll(ecraGameOver);
    }

    public void mostrarControlos() {
        Controlos_View ctrl = new Controlos_View();
        ctrl.getBtnVoltar().setOnAction(e -> mostrarMenu());
        root.getChildren().setAll(ctrl);
    }

    public Scene getScene() { return scene; }
}