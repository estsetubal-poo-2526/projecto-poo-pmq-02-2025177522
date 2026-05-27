package game_Logic;

public abstract class EntidadeJogo {

    protected double x;
    protected double y;
    protected double largura;
    protected double altura;

    public EntidadeJogo(double x, double y, double largura, double altura) {
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
    }

    public abstract void mover();

    /** Verifica colisão AABB entre duas entidades. Sem dependência de JavaFX. */
    public boolean colideCom(EntidadeJogo outra) {
        return this.x < outra.x + outra.largura
                && this.x + this.largura > outra.x
                && this.y < outra.y + outra.altura
                && this.y + this.altura > outra.y;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getLargura() { return largura; }
    public double getAltura() { return altura; }

    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
}
