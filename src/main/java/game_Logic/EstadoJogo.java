package game_Logic;

/**
 * Representa os diferentes estados possíveis do ciclo de vida do jogo.
 *
 * <p>Esta enumeração é utilizada pelo modelo lógico para controlar o fluxo da partida,
 * determinar se as entidades (jogador, inimigos, projéteis) devem ser atualizadas
 * e indicar à interface gráfica (View) qual o menu, overlay ou ecrã que deve ser
 * apresentado ao utilizador em cada momento.</p>
 */
public enum EstadoJogo {

    /**
     * O jogo encontra-se nos menus de navegação iniciais.
     * Nenhuma lógica de partida ou movimento de entidades está a ocorrer.
     */
    MENU,

    /**
     * O jogo está em execução ativa.
     * A lógica processa as colisões, atualiza o movimento de todas as entidades
     * e reage aos comandos (input) do jogador.
     */
    A_JOGAR,

    /**
     * O jogo está temporariamente suspenso.
     * O loop de atualização (Game Loop) ignora a movimentação das entidades e
     * o ecrã do menu de pausa é apresentado.
     */
    PAUSADO,

    /**
     * O jogador derrotou todos os inimigos da vaga atual.
     * Ocorre uma breve pausa temporal de transição onde o jogo exibe os
     * resultados de precisão e prepara a geração da próxima frota inimiga.
     */
    TRANSICAO_VAGA,

    /**
     * A partida terminou, normalmente porque o jogador perdeu todas as vidas.
     * Interrompe em definitivo a lógica do jogo e aciona a transição para
     * o ecrã final de submissão de pontuação.
     */
    GAME_OVER
}