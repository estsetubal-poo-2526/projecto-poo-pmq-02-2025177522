package game_Logic;

public class Barricadas extends EntidadeJogo {

    public static final double LARGURA = 60;
    public static final double ALTURA = 40;
    public static final int INTEGRIDADE_MAX = 10; // 6 hits para destruir

    private int integridade;

    public Barricadas(double x, double y) {
        super(x, y, LARGURA, ALTURA);
        this.integridade = INTEGRIDADE_MAX;
    }


    public void receberDano() {
        if (integridade > 0) {
            integridade--;
        }
    }

    public boolean estaDestruida() {
        return integridade <= 0;
    }

    public int getIntegridade() { return integridade; }

    /** Percentagem de saúde restante (útil para renderização). */
    public double getPercentagemIntegridade() {
        return (double) integridade / INTEGRIDADE_MAX;
    }

    @Override
    public void mover() {
        // As barricadas são estáticas
    }
}
