package game_Logic;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Projetil_InimigoTest {

    @Test
    void testInicializacaoVelocidadeReto() {
        // Criar um tiro com trajetória RETA
        Projetil_Inimigo tiroReto = new Projetil_Inimigo(100.0, 50.0, Projetil_Inimigo.TipoTrajeto.RETO);

        // A velocidade deve ser 60% da base (4.0 * 0.6 = 2.4)
        double velocidadeEsperada = Projetil_Inimigo.VELOCIDADE_BASE * 0.6;

        assertEquals(velocidadeEsperada, tiroReto.getVelocidadeY(), 0.001, "A velocidade do trajeto RETO deve ser 60% da base.");
        assertEquals(Projetil_Inimigo.TipoTrajeto.RETO, tiroReto.getTipoTrajeto(), "O tipo de trajeto guardado deve ser RETO.");
    }

    @Test
    void testInicializacaoVelocidadeZigzag() {
        // Criar um tiro com trajetória ZIGZAG
        Projetil_Inimigo tiroZigzag = new Projetil_Inimigo(100.0, 50.0, Projetil_Inimigo.TipoTrajeto.ZIGZAG);

        assertEquals(Projetil_Inimigo.VELOCIDADE_BASE, tiroZigzag.getVelocidadeY(), 0.001, "A velocidade do trajeto ZIGZAG deve ser igual à velocidade base (4.0).");
    }

    @Test
    void testMovimentoReto() {
        Projetil_Inimigo tiro = new Projetil_Inimigo(100.0, 50.0, Projetil_Inimigo.TipoTrajeto.RETO);

        tiro.moverVerticalmente();

        // O Y deve aumentar com a velocidade reduzida
        double yEsperado = 50.0 + (Projetil_Inimigo.VELOCIDADE_BASE * 0.6);
        assertEquals(yEsperado, tiro.getY(), 0.001, "A coordenada Y deve aumentar segundo a velocidade calculada para o trajeto RETO.");

        // O X não se pode mexer
        assertEquals(100.0, tiro.getX(), "No trajeto RETO, a coordenada X tem de permanecer estática.");
    }

    @Test
    void testMovimentoZigzag() {
        Projetil_Inimigo tiro = new Projetil_Inimigo(100.0, 50.0, Projetil_Inimigo.TipoTrajeto.ZIGZAG);

        // Simular o primeiro frame de movimento
        tiro.moverVerticalmente();

        double yEsperadoAposPrimeiroFrame = 50.0 + Projetil_Inimigo.VELOCIDADE_BASE;

        // O teu tick começa em 0. Ao mover, soma 0.3. Depois calcula: sin(0.3) * 3.0
        double xEsperadoAposPrimeiroFrame = 100.0 + (Math.sin(0.3) * 3.0);

        assertEquals(yEsperadoAposPrimeiroFrame, tiro.getY(), 0.001, "A coordenada Y deve descer à velocidade máxima.");
        assertEquals(xEsperadoAposPrimeiroFrame, tiro.getX(), 0.001, "A coordenada X deve oscilar exatamente com base na fórmula do seno programada.");
    }
}