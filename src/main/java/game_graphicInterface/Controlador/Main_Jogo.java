package game_graphicInterface.Controlador;

import game_graphicInterface.View.Manager_View;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class Main_Jogo extends Application {
    public static final double LARGURA = 800;
    public static final double ALTURA = 600;

    @Override
    public void start(Stage stage) {
        Manager_View managerView = new Manager_View(stage);
        Scene scene = managerView.getScene();

        stage.setTitle("Synthetic Oceans");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}