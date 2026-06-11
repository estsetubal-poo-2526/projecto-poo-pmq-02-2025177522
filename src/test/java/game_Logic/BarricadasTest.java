package game_Logic;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BarricadasTest {

    private Barricadas barricada;

    @BeforeEach
    void setUp() {
        // Inicializamos uma nova barricada antes de cada teste
        barricada = new Barricadas(100.0, 100.0);
    }

    @Test
    void testInicializacaoCorreta() {
        assertEquals(Barricadas.INTEGRIDADE_MAX, barricada.getIntegridade(), "A integridade inicial deve ser igual à INTEGRIDADE_MAX.");
        assertFalse(barricada.estaDestruida(), "A barricada não deve estar destruída ao ser criada.");
        assertEquals(1.0, barricada.getPercentagemIntegridade(), "A percentagem inicial deve ser 1.0 (100%).");
    }

    @Test
    void testReceberDanoUnico() {
        barricada.receberDano();
        assertEquals(Barricadas.INTEGRIDADE_MAX - 1, barricada.getIntegridade(), "A integridade deve diminuir em 1 após receber dano.");
    }

    @Test
    void testPercentagemIntegridadeAposDano() {
        barricada.receberDano();
        double percentagemEsperada = (double) (Barricadas.INTEGRIDADE_MAX - 1) / Barricadas.INTEGRIDADE_MAX;
        assertEquals(percentagemEsperada, barricada.getPercentagemIntegridade(), "A percentagem de integridade deve refletir o dano recebido.");
    }

    @Test
    void testDestruicaoDaBarricada() {
        // Aplicar dano até atingir 0
        for (int i = 0; i < Barricadas.INTEGRIDADE_MAX; i++) {
            barricada.receberDano();
        }

        assertEquals(0, barricada.getIntegridade(), "A integridade deve ser 0 após receber dano máximo.");
        assertTrue(barricada.estaDestruida(), "A barricada deve reportar que está destruída.");
    }

    @Test
    void testNaoPermitirIntegridadeNegativa() {
        // Aplicar mais dano do que a integridade máxima
        for (int i = 0; i < Barricadas.INTEGRIDADE_MAX + 5; i++) {
            barricada.receberDano();
        }

        assertEquals(0, barricada.getIntegridade(), "A integridade não deve descer abaixo de 0.");
        assertTrue(barricada.estaDestruida(), "A barricada deve continuar destruída.");
    }

    @Test
    void testMoverNaoAlteraEstado() {
        barricada.mover();
        assertEquals(Barricadas.INTEGRIDADE_MAX, barricada.getIntegridade(), "Chamar o método mover não deve alterar a integridade.");
    }
}