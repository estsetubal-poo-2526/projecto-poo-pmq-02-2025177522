package game_Logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EntidadeJogoTest {

    // 1. CRIAMOS UMA CLASSE "DUMMY" PARA TESTAR A CLASSE ABSTRATA
    private static class EntidadeTeste extends EntidadeJogo {
        public EntidadeTeste(double x, double y, double largura, double altura) {
            super(x, y, largura, altura);
        }

        @Override
        public void mover() {
            // Implementação vazia, apenas para podermos instanciar
        }
    }

    private EntidadeJogo entidadePrincipal;

    @BeforeEach
    void setUp() {
        // Criamos uma entidade de 20x20 pixels na posição (10, 10)
        // Isso significa que ela ocupa de X:10 a X:30, e de Y:10 a Y:30
        entidadePrincipal = new EntidadeTeste(10.0, 10.0, 20.0, 20.0);
    }

    @Test
    void testConstrutorEGetters() {
        assertEquals(10.0, entidadePrincipal.getX());
        assertEquals(10.0, entidadePrincipal.getY());
        assertEquals(20.0, entidadePrincipal.getLargura());
        assertEquals(20.0, entidadePrincipal.getAltura());
    }

    @Test
    void testSetters() {
        entidadePrincipal.setX(50.0);
        entidadePrincipal.setY(60.0);

        assertEquals(50.0, entidadePrincipal.getX(), "A coordenada X deve atualizar.");
        assertEquals(60.0, entidadePrincipal.getY(), "A coordenada Y deve atualizar.");
    }

    // --- TESTES DE COLISÃO (AABB) ---

    @Test
    void testColideComSobreposicaoTotal() {
        // Entidade exatamente no mesmo sítio e com o mesmo tamanho
        EntidadeJogo outraEntidade = new EntidadeTeste(10.0, 10.0, 20.0, 20.0);

        assertTrue(entidadePrincipal.colideCom(outraEntidade), "Devem colidir quando estão sobrepostas totalmente.");
    }

    @Test
    void testColideComSobreposicaoParcial() {
        // Entidade deslocada mas ainda a sobrepor-se à entidade principal
        // Entidade Principal: X [10 a 30], Y [10 a 30]
        // Outra Entidade: X [20 a 40], Y [20 a 40] -> A zona entre 20 e 30 choca.
        EntidadeJogo outraEntidade = new EntidadeTeste(20.0, 20.0, 20.0, 20.0);

        assertTrue(entidadePrincipal.colideCom(outraEntidade), "Devem colidir quando se sobrepõem parcialmente.");
    }

    @Test
    void testNaoColideQuandoSeparadas() {
        // Entidade completamente fora da zona
        EntidadeJogo outraEntidade = new EntidadeTeste(100.0, 100.0, 20.0, 20.0);

        assertFalse(entidadePrincipal.colideCom(outraEntidade), "Não devem colidir quando estão separadas.");
    }

    @Test
    void testNaoColideQuandoApenasTocamAsBordas() {
        // Entidade Principal: termina no X = 30
        // Outra Entidade: começa no X = 30
        EntidadeJogo outraEntidade = new EntidadeTeste(30.0, 10.0, 20.0, 20.0);

        // Pela tua lógica (que usa '<' e '>' em vez de '<=' e '>='), tocar nas bordas
        // não conta como colisão. Este teste garante que esse limite funciona como programaste.
        assertFalse(entidadePrincipal.colideCom(outraEntidade), "Pela lógica atual, tocar nas bordas não deve causar colisão.");
    }
}