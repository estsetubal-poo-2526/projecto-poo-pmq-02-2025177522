package game_Logic;

public abstract class Projetil extends EntidadeJogo {

    protected double velocidadeY;
    protected boolean ativo;

    public Projetil(double x, double y, double largura, double altura, double velocidadeY) {
        super(x, y, largura, altura);
        this.velocidadeY = velocidadeY;
        this.ativo = true;
    }

    /** Move o projétil verticalmente. */
    @Override
    public void mover() {
        moverVerticalmente();
    }

    /** Lógica de movimento vertical (pode ser override para comportamentos especiais). */
    public void moverVerticalmente() {
        this.y += velocidadeY;
    }

    /** Desativa o projétil (após colisão ou sair dos limites). */
    public void desativar() {
        this.ativo = false;
    }

    public boolean isAtivo() { return ativo; }
    public double getVelocidadeY() { return velocidadeY; }
}

