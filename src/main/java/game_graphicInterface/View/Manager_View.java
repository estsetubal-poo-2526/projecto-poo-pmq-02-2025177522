package game_graphicInterface.View;

import game_Logic.ModeloJogo;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Manager_View {

    private final Stage stage;
    private final Scene scene;
    private final StackPane root;


    public Manager_View(Stage stage) {
        this.stage = stage;
        this.root = new StackPane();
        this.scene = new Scene(root, 800, 600);
        mostrarMenu();
    }

    public void mostrarMenu() {
        MainMenu menu = new MainMenu();
        menu.getBtnIniciar().setOnAction(e->Iniciar());
        menu.getBtnClassificacoes().setOnAction(e->classificacoes(null));
        menu.getBtnSair().setOnAction(e -> stage.close());
        root.getChildren().setAll(menu);
    }
    public void Iniciar()
    {
        ModeloJogo modelo = new ModeloJogo();
        Game_View gameView = new Game_View(modelo, this);
        root.getChildren().setAll(gameView);
        gameView.iniciar();
    }
    public void classificacoes(ModeloJogo modeloJogo){
        Tabela_Classificacao table = new Tabela_Classificacao(modeloJogo);
        table.getBtnVoltar().setOnAction(e->mostrarMenu());
        root.getChildren().setAll(table);
    }
    public void mostrarGameOver(ModeloJogo modeloJogo) {
        Game_Over ecraGameOver = new Game_Over(modeloJogo, this);
        root.getChildren().setAll(ecraGameOver);
    }
        // SE a tua Manager_View usa uma variável root (ex: rootPane), fazes assim:
    public Scene getScene() { return scene; }
}