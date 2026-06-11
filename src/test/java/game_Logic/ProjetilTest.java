package game_Logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProjetilTest {

    // Criamos uma subclasse concreta apenas para conseguirmos instanciar e testar
    private static class ProjetilDummy extends Projetil {
        public ProjetilDummy(double x, double y, double largura, double altura, double velocidadeY) {
            super(x, y, largura, altura, velocidadeY);
        }
    }

    private Projetil projetil;

    @BeforeEach
    void setUp() {
        // Inicializamos um projétil de teste na posição Y=100 com velocidade=15
        projetil = new ProjetilDummy(50.0, 100.0, 5.0, 15.0, 15.0);
    }

    @Test
    void testInicializacaoCorreta() {
        assertTrue(projetil.isAtivo(), "Qualquer projétil deve ser criado como ativo.");
        assertEquals(15.0, projetil.getVelocidadeY(), "A velocidade guardada deve ser a que foi passada no construtor.");
        assertEquals(100.0, projetil.getY(), "A coordenada Y inicial deve estar correta.");
    }

    @Test
    void testMoverSomaVelocidadeAoY() {
        // Ao chamar mover(), o Y (que é 100) deve somar a velocidadeY (que é 15)
        projetil.mover();

        assertEquals(115.0, projetil.getY(), "O projétil deve deslocar-se verticalmente no valor exato da sua velocidadeY.");

        // Chamar uma segunda vez para garantir a acumulação
        projetil.mover();
        assertEquals(130.0, projetil.getY(), "O movimento deve ser acumulativo a cada frame.");
    }

    @Test
    void testDesativar() {
        // Garantir que começa ativo
        assertTrue(projetil.isAtivo());

        // Desativar
        projetil.desativar();

        // Verificar o estado
        assertFalse(projetil.isAtivo(), "O estado ativo deve passar a false após a desativação.");
    }
}