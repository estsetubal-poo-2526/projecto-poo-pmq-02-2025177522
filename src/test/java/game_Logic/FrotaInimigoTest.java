package game_Logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FrotaInimigoTest {

    private FrotaInimigo frota;

    @BeforeEach
    void setUp() {
        // Criamos uma frota no canto superior esquerdo (X=0, Y=0)
        // Limites do ecrã arbitrários: 0 na esquerda, 800 na direita. Vaga 1.
        frota = new FrotaInimigo(0.0, 0.0, 0.0, 800.0, 1);
    }

    @Test
    void testInicializacaoDaFrota() {
        List<Inimigo> todosInimigos = frota.getTodos();

        // 5 linhas * 11 colunas = 55 inimigos
        assertEquals(55, todosInimigos.size(), "A frota deve iniciar com exatamente 55 inimigos.");
        assertEquals(55, frota.getVivos().size(), "Todos os inimigos devem estar vivos inicialmente.");
        assertFalse(frota.estaVazia(), "A frota não pode estar vazia ao iniciar.");
    }

    @Test
    void testProgressaoDeDificuldadeDasVagas() {
        FrotaInimigo frotaVaga1 = new FrotaInimigo(0, 0, 0, 800, 1);
        FrotaInimigo frotaVaga2 = new FrotaInimigo(0, 0, 0, 800, 2);
        FrotaInimigo frotaVaga3 = new FrotaInimigo(0, 0, 0, 800, 3);

        // A velocidade tem de aumentar conforme a vaga
        assertTrue(frotaVaga2.getVelocidadeAtual() > frotaVaga1.getVelocidadeAtual(), "A vaga 2 tem de ser mais rápida que a vaga 1.");
        assertTrue(frotaVaga3.getVelocidadeAtual() > frotaVaga2.getVelocidadeAtual(), "A vaga 3 tem de ser mais rápida que a vaga 2.");
    }

    @Test
    void testMovimentoHorizontalLivre() {
        Inimigo primeiroInimigo = frota.getTodos().get(0);
        double xInicial = primeiroInimigo.getX();
        double yInicial = primeiroInimigo.getY();

        frota.moverEmBloco();

        // A direção inicial é 1 (direita), logo o X tem de aumentar
        assertTrue(primeiroInimigo.getX() > xInicial, "O inimigo deve mover-se para a direita.");
        assertEquals(yInicial, primeiroInimigo.getY(), "O inimigo não deve descer enquanto não tocar na borda.");
    }

    @Test
    void testToqueNaBordaEDescida() {
        // Para testar o toque na borda sem saber a largura exata dos inimigos,
        // vamos calcular qual é a ponta direita da frota atual
        double extremaDireita = frota.getVivos().stream()
                .mapToDouble(i -> i.getX() + i.getLargura())
                .max().orElse(0);

        // Criamos uma nova frota onde o limite direito está "colado" à nave da ponta
        // Somamos apenas +0.1 para que o próximo passo bata de certeza na borda
        FrotaInimigo frotaApertada = new FrotaInimigo(0, 0, 0, extremaDireita + 0.1, 1);

        Inimigo pontaDireita = frotaApertada.getTodos().get(10); // Último da primeira linha
        double yInicial = pontaDireita.getY();
        double xInicial = pontaDireita.getX();

        frotaApertada.moverEmBloco();

        // Ao bater na borda, a frota inteira tem de descer o Y
        assertTrue(pontaDireita.getY() > yInicial, "A frota tem de descer (aumentar Y) ao atingir a borda lateral.");

        // Como inverteu a direção (e só desceu, não andou para o lado neste frame ou andou no frame seguinte),
        // garantimos que o Y sofreu alteração, provando que o bloco de IF do "atingiuBorda" foi ativado.
    }

    @Test
    void testAtingiuBaseDaDerrota() {
        // O limite da base para o jogador perder será Y = 600
        double limiteDerrota = 600.0;

        assertFalse(frota.atingiuBase(limiteDerrota), "Ao iniciar (Y=0), a frota não deve estar na base.");

        // Forçamos a descida da frota até passarem o limite
        for (int i = 0; i < 50; i++) {
            frota.descerLinha();
        }

        assertTrue(frota.atingiuBase(limiteDerrota), "A frota deve reportar que atingiu a base após descer demasiado.");
    }
}