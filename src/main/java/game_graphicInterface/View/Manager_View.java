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

        // Chamar o método que mostra a pagina principal
        mostrarMenu();
    }

    public void mostrarMenu() {
        //método que mostra a pagina principal
    }
}