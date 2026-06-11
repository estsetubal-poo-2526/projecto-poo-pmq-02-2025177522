package game_Logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InimigoTest {

    // Criamos uma subclasse concreta fictícia para testar a classe abstrata Inimigo
    private static class InimigoDummy extends Inimigo {
        public InimigoDummy(double x, double y, int valorPontos, int coluna) {
            super(x, y, valorPontos, coluna);
        }
    }

    private Inimigo inimigo;

    @BeforeEach
    void setUp() {
        // Inicializa um inimigo na posição (50, 50), a valer 20 pontos, na coluna 4
        inimigo = new InimigoDummy(50.0, 50.0, 20, 4);
    }

    @Test
    void testInicializacaoCorreta() {
        assertTrue(inimigo.isVivo(), "O inimigo deve começar o jogo no estado vivo.");
        assertEquals(20, inimigo.getValorPontos(), "O valor de pontos deve ser o que foi passado no construtor.");
        assertEquals(4, inimigo.getColuna(), "A coluna guardada deve ser a que foi definida na inicialização.");

        // Testar se as dimensões fixas herdadas da superclasse foram aplicadas
        assertEquals(Inimigo.LARGURA, inimigo.getLargura(), "A largura do inimigo não corresponde à constante.");
        assertEquals(Inimigo.ALTURA, inimigo.getAltura(), "A altura do inimigo não corresponde à constante.");
    }

    @Test
    void testTransicaoDeEstadoAoDestruir() {
        // Garantir o estado inicial
        assertTrue(inimigo.isVivo());

        // Executar a destruição
        inimigo.destruir();

        // Validar a mudança do estado
        assertFalse(inimigo.isVivo(), "O método isVivo() deve retornar false após o inimigo ser destruído.");
    }

    @Test
    void testMoverNaoAlteraAtributos() {
        // Como o movimento individual é nulo (gerido em bloco),
        // garantimos que o método não quebra o comportamento nem altera dados.
        inimigo.mover();

        assertTrue(inimigo.isVivo(), "Chamar mover() não deve afetar o estado de vida.");
        assertEquals(50.0, inimigo.getX(), "A coordenada X não deve mudar através do método mover individual.");
        assertEquals(50.0, inimigo.getY(), "A coordenada Y não deve mudar através do método mover individual.");
    }
}