package game_Logic;

public class Projetil_jogador extends Projetil {

    public static final double LARGURA = 4;
    public static final double ALTURA = 16;
    public static final double VELOCIDADE = -9.0; // sobe (y decresce)
    public static final String TIPO = "Laser Retilíneo";

    public Projetil_jogador(double x, double y) {
        super(x, y, LARGURA, ALTURA, VELOCIDADE);
    }

    public String getTipo() { return TIPO; }
}

