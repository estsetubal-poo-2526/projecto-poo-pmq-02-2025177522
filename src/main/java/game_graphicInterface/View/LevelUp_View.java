package game_graphicInterface.View;


import javafx.animation.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.util.Duration;
import game_Logic.ModeloJogo;


public class LevelUp_View extends StackPane {

    private Runnable onContinuar;

    public LevelUp_View(ModeloJogo modelo) {
        setPrefSize(800, 600);
        setStyle("-fx-background-color: #000814;");

        // Título
        Text titulo = new Text("★★ VAGA " + modelo.getVaga() + " SUPERADA ★★");
        titulo.setFont(Font.font("Monospace", FontWeight.BOLD, 36));
        titulo.setFill(Color.web("#00f5ff"));
        titulo.setStyle("-fx-effect: dropshadow(gaussian, #00f5ff, 18, 0.9, 0, 0);");

        // Estatísticas
        Text pontuacao = new Text("Pontuação do Nível: " + modelo.getPontuacaoVaga());
        pontuacao.setFont(Font.font("Monospace", 20));
        pontuacao.setFill(Color.web("#00ff88"));

        Text precisao = new Text("Precisão de Tiro: " + modelo.getPrecisaoTiro() + "%");
        precisao.setFont(Font.font("Monospace", 20));
        precisao.setFill(Color.web("#ff8c00"));

        // Countdown
        Text prepare = new Text("Prepare-se...");
        PauseTransition espera = new PauseTransition(Duration.seconds(3));
        espera.setOnFinished(e -> {
            FadeTransition sair = new FadeTransition(Duration.millis(500), this);
            sair.setToValue(0);
            sair.setOnFinished(ev -> {
                if (onContinuar != null) onContinuar.run();
            });
            sair.play();
        });
    }
}