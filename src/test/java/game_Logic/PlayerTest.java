package game_Logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player jogador;

    @BeforeEach
    void setUp() {
        // Criamos o jogador numa posição central e redonda (X=100, Y=500) para facilitar as contas
        jogador = new Player(100.0, 500.0);
    }

    @Test
    void testInicializacaoCorreta() {
        assertEquals(Player.VIDAS_INICIAIS, jogador.getVidas(), "O jogador deve começar com as vidas iniciais.");
        assertEquals(0, jogador.getPontuacao(), "O jogador deve começar com 0 pontos.");
        assertTrue(jogador.isAtivo(), "O jogador deve começar ativo.");
        assertEquals(100.0, jogador.getX(), "A coordenada X inicial está errada.");
        assertEquals(500.0, jogador.getY(), "A coordenada Y inicial está errada.");
    }

    @Test
    void testMovimentoHorizontal() {
        // Mover para a direita e para a esquerda
        jogador.moverHorizontal(10.0);
        assertEquals(110.0, jogador.getX(), "O X deve aumentar ao mover positivo na horizontal.");
        jogador.moverHorizontal(-20.0);
        assertEquals(90.0, jogador.getX(), "O X deve diminuir ao mover negativo na horizontal.");
    }

    @Test
    void testPerderVidaEDesativacao() {
        // Vamos forçar o jogador a perder todas as vidas (são 3)
        jogador.perderVida(); // Fica com 2
        assertEquals(2, jogador.getVidas());
        assertTrue(jogador.isAtivo());

        jogador.perderVida(); // Fica com 1
        jogador.perderVida(); // Fica com 0

        assertEquals(0, jogador.getVidas(), "As vidas devem chegar a zero.");
        assertFalse(jogador.isAtivo(), "O jogador tem de ser desativado quando as vidas chegam a 0.");

        // Edge case: Tentar perder mais vidas quando já está a 0
        jogador.perderVida();
        assertEquals(0, jogador.getVidas(), "As vidas não podem ficar negativas.");
    }

    @Test
    void testAdicionarPontosValidos() {
        jogador.adicionarPontos(100);
        assertEquals(100, jogador.getPontuacao());

        jogador.adicionarPontos(50);
        assertEquals(150, jogador.getPontuacao(), "A pontuação tem de ser acumulada.");
    }

    @Test
    void testExcecaoAoAdicionarPontosNegativos() {
        // O assertThrows verifica se o código lá dentro "rebenta" propositadamente com o erro esperado
        IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class, () -> {
            jogador.adicionarPontos(-20);
        });

        assertEquals("Pontos não podem ser negativos.", excecao.getMessage(), "A mensagem da exceção tem de corresponder à que programaste.");
        assertEquals(0, jogador.getPontuacao(), "A pontuação não deve ser alterada quando a exceção é lançada.");
    }

    @Test
    void testCalculoDaPosicaoDoDisparo() {
        Projetil_jogador tiro = jogador.disparar();

        assertNotNull(tiro, "O disparo não pode devolver nulo.");

        // O tiro tem de nascer exatamente no centro do jogador. A fórmula matemática é:
        // X Tiro = X Jogador + (Largura Jogador / 2) - (Largura Tiro / 2)
        double xEsperado = 100.0 + (Player.LARGURA / 2) - (Projetil_jogador.LARGURA / 2);

        // O tiro tem de nascer imediatamente acima do jogador.
        // Y Tiro = Y Jogador - Altura Tiro
        double yEsperado = 500.0 - Projetil_jogador.ALTURA;

        assertEquals(xEsperado, tiro.getX(), "O projétil não está alinhado no centro (Eixo X).");
        assertEquals(yEsperado, tiro.getY(), "O projétil não está encostado ao topo do submarino (Eixo Y).");
    }
}