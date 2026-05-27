package game_Logic;

public abstract class Inimigo extends EntidadeJogo {

        public static final double LARGURA = 36;
        public static final double ALTURA = 28;

        protected int valorPontos;
        protected boolean vivo;

        public Inimigo(double x, double y, int valorPontos) {
            super(x, y, LARGURA, ALTURA);
            this.valorPontos = valorPontos;
            this.vivo = true;
        }

        /**
         * Elimina o inimigo.
         */
        public void destruir() {
            this.vivo = false;
        }

        public boolean isVivo() {
            return vivo;
        }

        public int getValorPontos() {
            return valorPontos;
        }

        @Override
        public void mover() {
            // Movimento gerido pela FrotaInimigos
        }
}

