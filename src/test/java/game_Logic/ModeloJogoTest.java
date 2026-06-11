package game_Logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ModeloJogoTest {

    private ModeloJogo jogo;

    @BeforeEach
    void setUp() {
        // Inicializa um jogo novo antes de cada teste
        jogo = new ModeloJogo();
    }

    @Test
    void testInicializacaoNovoJogo() {
        assertEquals(1, jogo.getVaga(), "O jogo deve começar na vaga 1.");
        assertNotNull(jogo.getJogador(), "O jogador tem de ser instanciado.");
        assertNotNull(jogo.getFrota(), "A frota inimiga tem de ser instanciada.");

        // Verifica se criou exatamente o número de barricadas definido (3)
        assertEquals(3, jogo.getBarricadas().size(), "Devem ser criadas exatamente 3 barricadas iniciais.");

        // Assume que o teu Enum tem o valor A_JOGAR
        assertEquals(EstadoJogo.A_JOGAR, jogo.getEstado(), "O estado inicial tem de ser A_JOGAR.");
    }

    @Test
    void testPausaERetomaDoJogo() {
        // Pausar
        jogo.pausar();
        assertEquals(EstadoJogo.PAUSADO, jogo.getEstado(), "O jogo deve transitar para o estado PAUSADO.");

        // Retomar
        jogo.retomar();
        assertEquals(EstadoJogo.A_JOGAR, jogo.getEstado(), "O jogo deve voltar ao estado A_JOGAR ao retomar.");
    }

    @Test
    void testMovimentoDoJogadorRespeitaLimitesLaterais() {
        // 1. Forçar o jogador todo para a ESQUERDA
        // Chamamos o método muitas vezes para garantir que bate na "parede"
        for (int i = 0; i < 200; i++) {
            jogo.moverJogadorEsquerda();
        }
        assertEquals(0, jogo.getJogador().getX(), "O jogador não pode passar do limite esquerdo do ecrã (X = 0).");

        // 2. Forçar o jogador todo para a DIREITA
        for (int i = 0; i < 200; i++) {
            jogo.moverJogadorDireita();
        }
        double limiteDireitoEsperado = ModeloJogo.LARGURA_ECRA - jogo.getJogador().getLargura();
        assertEquals(limiteDireitoEsperado, jogo.getJogador().getX(), "O jogador não pode ultrapassar o limite direito do ecrã.");
    }

    @Test
    void testJogadorSoPodeTerUmTiroAtivo() {
        // Primeiro disparo (deve ser permitido)
        jogo.jogadorDisparar();
        assertEquals(1, jogo.getProjetisJogador().size(), "A lista de projéteis deve ter 1 tiro após disparar.");

        // Tentar disparar uma segunda vez imediatamente a seguir
        jogo.jogadorDisparar();
        assertEquals(1, jogo.getProjetisJogador().size(), "O jogo não pode permitir um segundo tiro se o primeiro ainda estiver ativo.");
    }

    @Test
    void testAvancarVaga() {
        jogo.avancarVaga();

        assertEquals(2, jogo.getVaga(), "O número da vaga tem de incrementar para 2.");
        assertEquals(EstadoJogo.A_JOGAR, jogo.getEstado(), "Ao começar a nova vaga, o estado volta a A_JOGAR.");
        assertTrue(jogo.getProjetisJogador().isEmpty(), "Os tiros do jogador devem ser limpos na transição de vaga.");
        assertTrue(jogo.getProjetisInimigos().isEmpty(), "Os tiros inimigos devem ser limpos na transição de vaga.");
    }
}