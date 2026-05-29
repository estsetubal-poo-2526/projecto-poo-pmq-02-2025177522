package game_graphicInterface.View;

import javafx.scene.Scene;
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
        menu.getBtnClassificacoes().setOnAction(e->classificacoes());
        menu.getBtnSair().setOnAction(e -> stage.close());
        root.getChildren().setAll(menu);
    }
    public void Iniciar()
    {

    }
    public void classificacoes(){

    }
    public Scene getScene() { return scene; }
}