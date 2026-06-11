package game_Logic;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Projetil_jogadorTest {

    @Test
    void testConstrutorEAtributosIniciais() {
        // Preparação: instanciar o projétil numa posição arbitrária (ex: no topo do submarino)
        double xInicial = 200.0;
        double yInicial = 500.0;

        Projetil_jogador tiro = new Projetil_jogador(xInicial, yInicial);

        // Validação: garantir que as coordenadas chegaram intactas à EntidadeJogo
        assertEquals(xInicial, tiro.getX(), "A coordenada X inicial está incorreta.");
        assertEquals(yInicial, tiro.getY(), "A coordenada Y inicial está incorreta.");

        // Validação: garantir que as dimensões corretas do laser foram passadas
        assertEquals(Projetil_jogador.LARGURA, tiro.getLargura(), "A largura do tiro não corresponde à constante definida.");
        assertEquals(Projetil_jogador.ALTURA, tiro.getAltura(), "A altura do tiro não corresponde à constante definida.");

        // Validação crucial: a velocidade tem de ser repassada e tem de ser negativa
        assertEquals(Projetil_jogador.VELOCIDADE, tiro.getVelocidadeY(), "A velocidade do projétil deve ser -9.0 para garantir que ele sobe no ecrã.");

        // Testar o método específico desta classe
        assertEquals(Projetil_jogador.TIPO, tiro.getTipo(), "O tipo retornado pelo getTipo() deve ser 'Laser Retilíneo'.");
    }
}