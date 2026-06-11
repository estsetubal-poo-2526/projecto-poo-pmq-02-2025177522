package game_Logic;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

class Inimigo_aleatorioTest {

    @Test
    void testSorteioDePontosValidos() {
        // Os pontos permitidos pela regra de negócio
        int[] pontosPossiveis = {50, 100, 150, 200, 300};

        // Criamos um OVNI
        Inimigo_aleatorio ovni = new Inimigo_aleatorio(0, 10, 1);

        // Verificamos se os pontos atribuídos existem na nossa lista
        boolean pontoValido = false;
        for (int p : pontosPossiveis) {
            if (ovni.getPontosVariaveis() == p) {
                pontoValido = true;
                break;
            }
        }

        assertTrue(pontoValido, "Os pontos gerados (" + ovni.getPontosVariaveis() + ") não estão na lista de pontos permitidos.");
    }

    @Test
    void testMoverParaADireita() {
        // Instancia com direção +1 (direita) e X inicial de 10
        Inimigo_aleatorio ovni = new Inimigo_aleatorio(10.0, 50.0, 1.0);

        ovni.mover();

        double xEsperado = 10.0 + Inimigo_aleatorio.VELOCIDADE;
        assertEquals(xEsperado, ovni.getX(), "O OVNI deve somar a velocidade ao X quando a direção é +1 (direita).");
    }

    @Test
    void testMoverParaAEsquerda() {
        // Instancia com direção -1 (esquerda) e X inicial de 100
        Inimigo_aleatorio ovni = new Inimigo_aleatorio(100.0, 50.0, -1.0);

        ovni.mover();

        double xEsperado = 100.0 - Inimigo_aleatorio.VELOCIDADE;
        assertEquals(xEsperado, ovni.getX(), "O OVNI deve subtrair a velocidade ao X quando a direção é -1 (esquerda).");
    }
}