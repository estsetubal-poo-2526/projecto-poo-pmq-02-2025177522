package game_Logic;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MelhoresPontuacoesTest {

    @BeforeEach
    void setUp() {
        // Limpamos a tabela ANTES de cada teste para garantir que começamos do zero
        MelhoresPontuacoes.limparTabela();
    }

    @AfterEach
    void tearDown() {
        // Limpamos a tabela DEPOIS de cada teste para apagar o ficheiro pontuacoes.dat do disco
        MelhoresPontuacoes.limparTabela();
    }

    @Test
    void testTratamentoDoNome() {
        // Testar se corta nomes com mais de 10 letras e se põe tudo em maiúsculas
        MelhoresPontuacoes.adicionarScore("um_nome_muito_comprido", 1000, 1);

        List<MelhoresPontuacoes> scores = MelhoresPontuacoes.getClassificacoes();
        assertEquals(1, scores.size());

        // "um_nome_muito_comprido" tem de virar "UM_NOME_MU" (10 caracteres, maiúsculas)
        assertEquals("UM_NOME_MU", scores.get(0).getNome(), "O nome deve ser cortado nos 10 caracteres e convertido para maiúsculas.");
    }

    @Test
    void testAdicionarScoreEOrdenacao() {
        // Adicionamos fora de ordem
        MelhoresPontuacoes.adicionarScore("JOGADOR_C", 500, 1);
        MelhoresPontuacoes.adicionarScore("JOGADOR_A", 1500, 3);
        MelhoresPontuacoes.adicionarScore("JOGADOR_B", 1000, 2);

        List<MelhoresPontuacoes> scores = MelhoresPontuacoes.getClassificacoes();

        assertEquals(3, scores.size());

        // Como implementaste o Comparable de forma decrescente, o maior tem de estar em 1º
        assertEquals("JOGADOR_A", scores.get(0).getNome());
        assertEquals("JOGADOR_B", scores.get(1).getNome());
        assertEquals("JOGADOR_C", scores.get(2).getNome());
    }

    @Test
    void testLimiteMaximoDeDezScores() {
        // Adicionar 15 pontuações seguidas
        for (int i = 1; i <= 15; i++) {
            // Pontuações de 100 a 1500
            MelhoresPontuacoes.adicionarScore("TESTE_" + i, i * 100, 1);
        }

        List<MelhoresPontuacoes> scores = MelhoresPontuacoes.getClassificacoes();

        // A tabela nunca pode ter mais do que 10 registos
        assertEquals(10, scores.size(), "A lista não pode exceder o MAX_SCORES (10).");

        // O registo mais baixo dos 10 melhores tem de ser o 600 (pois os de 100 a 500 foram descartados)
        assertEquals(600, scores.get(9).getPontuacao(), "O último score da lista deve ser o mais alto possível dentro do Top 10.");
    }

    @Test
    void testEhNovoRecorde() {
        // Com a tabela vazia (menos de 10), qualquer score é recorde
        assertTrue(MelhoresPontuacoes.ehNovoRecorde(50), "Deve ser recorde se a tabela ainda tiver espaço.");

        // Encher a tabela (10 scores) com pontuação fixa de 500
        for (int i = 0; i < 10; i++) {
            MelhoresPontuacoes.adicionarScore("PLAYER", 500, 1);
        }

        // Testar com a tabela cheia
        assertTrue(MelhoresPontuacoes.ehNovoRecorde(501), "Deve ser recorde se a pontuação for maior que a última da lista.");
        assertFalse(MelhoresPontuacoes.ehNovoRecorde(500), "Não deve ser recorde se a pontuação for igual à última da lista.");
        assertFalse(MelhoresPontuacoes.ehNovoRecorde(499), "Não deve ser recorde se a pontuação for menor que a última da lista.");
    }

    @Test
    void testExisteNomeIgnoraMaiusculasEMinusculas() {
        MelhoresPontuacoes.adicionarScore("MARIA", 2000, 5);

        assertTrue(MelhoresPontuacoes.existeNome("MARIA"), "Deve encontrar o nome exato.");
        assertTrue(MelhoresPontuacoes.existeNome("maria"), "Deve encontrar o nome ignorando as minúsculas.");
        assertFalse(MelhoresPontuacoes.existeNome("JOAO"), "Não deve encontrar um nome que não existe.");
    }
}