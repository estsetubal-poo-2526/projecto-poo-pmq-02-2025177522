package game_graphicInterface.View;

import game_Logic.ModeloJogo;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Game_Over extends StackPane {

    private final Button btnJogarNovamente;
    private final Button btnMenu;
    private final Button btnClassificacoes;

    public Game_Over(ModeloJogo modeloJogo) {
        setPrefSize(800, 600);
        setStyle("-fx-background-color: #000814;");
        //Titulo
        Text titulo = new Text("Game Over");
        titulo.setFont(Font.font("Monospace", FontWeight.BOLD, 32));
        titulo.setFill(Color.web("#00f5ff"));
        titulo.setStyle("-fx-effect: dropshadow(gaussian, #00f5ff, 10, 0.2, 0, 0);");

        // Estatísticas
        VBox stats = new VBox(8);
        stats.setAlignment(Pos.CENTER);
        adicionarStat(stats, "Pontuação Final:", String.format("%06d", modeloJogo.getJogador().getPontuacao()), "#00f5ff");
        adicionarStat(stats, "Nível Alcançado:", "Vaga " + modeloJogo.getVaga(), "#00f5ff");
        adicionarStat(stats, "Precisão de Tiro:", modeloJogo.getPrecisaoTiro() + "%", "#00ff88");

    }
    private void adicionarStat(VBox box, String label, String valor, String cor) {
        HBox linha = new HBox(10);
        linha.setAlignment(Pos.CENTER);
        Text lbl = new Text(label);
        lbl.setFont(Font.font("Monospace", 15));
        lbl.setFill(Color.web("#aaaaaa"));
        Text val = new Text(valor);
        val.setFont(Font.font("Monospace", FontWeight.BOLD, 15));
        val.setFill(Color.web(cor));
        linha.getChildren().addAll(lbl, val);
        box.getChildren().add(linha);
    }

}
