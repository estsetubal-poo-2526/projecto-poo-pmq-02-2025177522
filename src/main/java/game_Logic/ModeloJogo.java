package game_Logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Modelo central do jogo "Synthetic Oceans".
 *
 * <p>Gere o estado global, coordena todas as entidades e implementa
 * as regras fundamentais do jogo (colisões, condições de derrota, progressão de vagas,
 * sistema de pontuação e estatísticas).</p>
 *
 * <p>Esta classe <b>não tem qualquer dependência</b> de JavaFX ou de elementos de UI.
 * Funciona como o motor lógico que é invocado pelo controlador a cada frame do game loop.</p>
 */
public class ModeloJogo {

    // ---- Dimensões do ecrã de jogo ----
    /** Largura global da área de jogo lógica. */
    public static final double LARGURA_ECRA = 800;

    /** Altura global da área de jogo lógica. */
    public static final double ALTURA_ECRA = 600;

    // ---- Configuração de barricadas ----
    private static final int NUM_BARRICADAS = 3;
    private static final double Y_BARRICADAS = ALTURA_ECRA - 140;

    // ---- Configuração da frota ----
    private static final double X_INICIO_FROTA = 60;
    private static final double Y_INICIO_FROTA = 60;

    // ---- Configuração do jogador ----
    private static final double Y_JOGADOR = ALTURA_ECRA - 60;
    private static final double X_JOGADOR_INICIO = LARGURA_ECRA / 2 - Player.LARGURA / 2;

    // ---- Disparo dos inimigos ----
    private static final int INTERVALO_DISPARO_MIN = 40;  // frames
    private static final int INTERVALO_DISPARO_MAX = 100;
    private int framesSemDisparo = 0;
    private int proximoDisparo;

    // ---- Invasor aleatório ----
    private static final int INTERVALO_INVASOR_MIN = 400;
    private static final int INTERVALO_INVASOR_MAX = 900;
    private int framesSemInvasor = 0;
    private int proximoInvasor;

    // ---- Entidades ----
    private Player jogador;
    private FrotaInimigo frota;
    private List<Barricadas> barricadas;

    /** Lista de projéteis disparados pelo jogador (máximo 1 ativo por norma). */
    private List<Projetil_jogador> projetilJogador;

    /** Lista de projéteis disparados pela frota inimiga. */
    private List<Projetil_Inimigo> projetisInimigos;

    /** Referência para o Invasor Bónus (UFO). É {@code null} quando inativo. */
    private Inimigo_aleatorio invasorAleatorio;

    // ---- Estado ----
    private EstadoJogo estado;
    private int vaga;
    private int hiScore;
    private List<MelhoresPontuacoes> classificacoes;

    // ---- Estatísticas da vaga ----
    private int tirosTotais;
    private int tirosAcertados;
    private final Random random = new Random();

    /**
     * Construtor da classe {@code ModeloJogo}.
     * Inicializa a lista de classificações locais e arranca a primeira partida.
     */
    public ModeloJogo() {
        this.classificacoes = new ArrayList<>();
        this.hiScore = 0;
        iniciarNovoJogo();
    }

    /**
     * Reinicia completamente o estado do jogo.
     * <p>Repõe a vaga a 1, zera as estatísticas de tiro e reinicializa todas as entidades.</p>
     */
    public void iniciarNovoJogo() {
        this.vaga = 1;
        this.tirosTotais = 0;
        this.tirosAcertados = 0;
        this.estado = EstadoJogo.A_JOGAR;
        inicializarEntidades();
        sortearProximoDisparo();
        sortearProximoInvasor();
    }

    /**
     * Helper para inicializar e instanciar as entidades no arranque ou reinício do jogo.
     */
    private void inicializarEntidades() {
        jogador = new Player(X_JOGADOR_INICIO, Y_JOGADOR);
        frota = new FrotaInimigo(X_INICIO_FROTA, Y_INICIO_FROTA, 0, LARGURA_ECRA, this.vaga);
        barricadas = criarBarricadas();
        projetilJogador = new ArrayList<>();
        projetisInimigos = new ArrayList<>();
        invasorAleatorio = null;
        framesSemDisparo = 0;
        framesSemInvasor = 0;
    }

    /**
     * Gera e distribui as barricadas de proteção de forma uniforme ao longo do eixo X.
     * @return Uma lista com as barricadas geradas.
     */
    private List<Barricadas> criarBarricadas() {
        List<Barricadas> lista = new ArrayList<>();
        double espacamento = LARGURA_ECRA / (NUM_BARRICADAS + 1);
        for (int i = 1; i <= NUM_BARRICADAS; i++) {
            double x = i * espacamento - Barricadas.LARGURA / 2;
            lista.add(new Barricadas(x, Y_BARRICADAS));
        }
        return lista;
    }

    // =========================================================
    //  ATUALIZAÇÃO DO JOGO (chamado a cada frame pelo Controller)
    // =========================================================

    /**
     * Executa a lógica de um frame completo do jogo.
     * <p>Este método deve ser invocado pelo loop central de animação. Processa os movimentos,
     * colisões, limpezas de memória e verifica transições de estado (vitória de vaga ou derrota).</p>
     */
    public void atualizar() {
        if (estado != EstadoJogo.A_JOGAR) return;
        moverProjeteis();
        frota.moverEmBloco();
        moverInvasorAleatorio();
        tentarDisparoInimigo();
        tentarSpawnInvasorAleatorio();
        verificarColisoes();
        limparInativos();
        verificarCondicoesFimDeVaga();
    }

    // ---- Movimento ----

    /**
     * Atualiza a posição de todos os projéteis (jogador e inimigos) e desativa
     * aqueles que ultrapassaram os limites verticais lógicos do ecrã.
     */
    private void moverProjeteis() {
        projetilJogador.forEach(Projetil::mover);
        projetisInimigos.forEach(Projetil::mover);
        // Desativar projéteis fora dos limites
        projetilJogador.forEach(p -> { if (p.getY() < 0) p.desativar(); });
        projetisInimigos.forEach(p -> { if (p.getY() > ALTURA_ECRA) p.desativar(); });
    }

    /**
     * Desloca o invasor especial (UFO). Destrói a entidade se esta sair totalmente da área visível.
     */
    private void moverInvasorAleatorio() {
        if (invasorAleatorio == null || !invasorAleatorio.isVivo()) return;
        invasorAleatorio.mover();
        // Remove se sair do ecrã
        if (invasorAleatorio.getX() > LARGURA_ECRA + 100
                || invasorAleatorio.getX() < -100) {
            invasorAleatorio = null;
        }
    }

    // ---- Disparo do inimigo ----

    /**
     * Controla a cadência de tiro inimigo baseado em contagem de frames.
     */
    private void tentarDisparoInimigo() {
        framesSemDisparo++;
        if (framesSemDisparo >= proximoDisparo) {
            Projetil_Inimigo p = frota.atirarAleatoriamente();
            if (p != null) projetisInimigos.add(p);
            framesSemDisparo = 0;
            sortearProximoDisparo();
        }
    }

    private void sortearProximoDisparo() {
        proximoDisparo = INTERVALO_DISPARO_MIN
                + random.nextInt(INTERVALO_DISPARO_MAX - INTERVALO_DISPARO_MIN + 1);
    }

    // ---- Invasor aleatório ----

    /**
     * Verifica e executa, se apropriado, o surgimento (spawn) do invasor bónus (UFO).
     */
    private void tentarSpawnInvasorAleatorio() {
        if (invasorAleatorio != null && invasorAleatorio.isVivo()) return;
        framesSemInvasor++;
        if (framesSemInvasor >= proximoInvasor) {
            spawnInvasorAleatorio();
            framesSemInvasor = 0;
            sortearProximoInvasor();
        }
    }

    /**
     * Instancia o invasor bónus. A direção de surgimento (esquerda ou direita) é aleatória.
     */
    private void spawnInvasorAleatorio() {
        boolean daEsquerda = random.nextBoolean();
        double x;
        double direcao;
        if (daEsquerda) {
            x = -Inimigo_aleatorio.LARGURA;
            direcao = 1;
        } else {
            x = LARGURA_ECRA;
            direcao = -1;
        }
        invasorAleatorio = new Inimigo_aleatorio(x, 20, direcao);
    }

    private void sortearProximoInvasor() {
        proximoInvasor = INTERVALO_INVASOR_MIN
                + random.nextInt(INTERVALO_INVASOR_MAX - INTERVALO_INVASOR_MIN + 1);
    }

    // =========================================================
    //  COLISÕES
    // =========================================================

    /**
     * Executa a rotina completa de testes de colisão entre todas as entidades relevantes.
     */
    private void verificarColisoes() {
        verificarProjetilJogadorVsInimigos();
        verificarProjetilJogadorVsInvasorAleatorio();
        verificarProjetilJogadorVsBarricadas();
        verificarProjetisInimigosVsJogador();
        verificarProjetisInimigosVsBarricadas();
    }

    private void verificarProjetilJogadorVsInimigos() {
        for (Projetil_jogador p : projetilJogador) {
            if (!p.isAtivo()) continue;
            for (Inimigo inimigo : frota.getVivos()) {
                if (p.colideCom(inimigo)) {
                    p.desativar();
                    inimigo.destruir();
                    jogador.adicionarPontos(inimigo.getValorPontos());
                    tirosAcertados++;
                    break;
                }
            }
        }
    }

    private void verificarProjetilJogadorVsInvasorAleatorio() {
        if (invasorAleatorio == null || !invasorAleatorio.isVivo()) return;
        for (Projetil_jogador p : projetilJogador) {
            if (!p.isAtivo()) continue;
            if (p.colideCom(invasorAleatorio)) {
                p.desativar();
                jogador.adicionarPontos(invasorAleatorio.getPontosVariaveis());
                invasorAleatorio.destruir();
                tirosAcertados++;
                break;
            }
        }
    }

    private void verificarProjetilJogadorVsBarricadas() {
        for (Projetil_jogador p : projetilJogador) {
            if (!p.isAtivo()) continue;
            for (Barricadas b : barricadas) {
                if (!b.estaDestruida() && p.colideCom(b)) {
                    p.desativar();
                    b.receberDano();
                    break;
                }
            }
        }
    }

    private void verificarProjetisInimigosVsJogador() {
        for (Projetil_Inimigo p : projetisInimigos) {
            if (!p.isAtivo()) continue;
            if (p.colideCom(jogador)) {
                p.desativar();
                jogador.perderVida();
            }
        }
    }

    private void verificarProjetisInimigosVsBarricadas() {
        for (Projetil_Inimigo p : projetisInimigos) {
            if (!p.isAtivo()) continue;
            for (Barricadas b : barricadas) {
                if (!b.estaDestruida() && p.colideCom(b)) {
                    p.desativar();
                    b.receberDano();
                    break;
                }
            }
        }
    }

    // =========================================================
    //  CONDIÇÕES DE FIM
    // =========================================================

    /**
     * Avalia o estado atual do jogo para determinar se a partida foi perdida
     * ou se a vaga foi ganha.
     */
    private void verificarCondicoesFimDeVaga() {
        // Derrota: inimigos chegaram ao fundo
        if (frota.atingiuBase(Y_JOGADOR)) {
            estado = EstadoJogo.GAME_OVER;
            atualizarHiScore();
            return;
        }
        // Derrota: jogador sem vidas
        if (!jogador.isAtivo()) {
            estado = EstadoJogo.GAME_OVER;
            atualizarHiScore();
            return;
        }
        // Vitória de vaga: todos os inimigos eliminados
        if (frota.estaVazia()) {
            estado = EstadoJogo.TRANSICAO_VAGA;
        }
    }

    /**
     * Instancia a próxima vaga. Mantém a pontuação e vidas do jogador, mas
     * repõe e acelera os inimigos, restabelece as barricadas e limpa o ecrã de projéteis.
     */
    public void avancarVaga() {
        vaga++;
        tirosTotais = 0;
        tirosAcertados = 0;
        frota = new FrotaInimigo(X_INICIO_FROTA, Y_INICIO_FROTA, 0, LARGURA_ECRA, this.vaga);
        projetisInimigos.clear();
        projetilJogador.clear();
        invasorAleatorio = null;
        barricadas = criarBarricadas();
        jogador.setX(X_JOGADOR_INICIO);
        estado = EstadoJogo.A_JOGAR;
        sortearProximoDisparo();
        sortearProximoInvasor();
    }

    /**
     * Atualiza o High Score na memória corrente, se o jogador o tiver superado.
     */
    private void atualizarHiScore() {
        if (jogador.getPontuacao() > hiScore) {
            hiScore = jogador.getPontuacao();
        }
    }

    // =========================================================
    //  AÇÕES DO JOGADOR
    // =========================================================

    /**
     * Move o jogador para a esquerda, garantindo que não sai da fronteira esquerda (X = 0).
     */
    public void moverJogadorEsquerda() {
        if (estado != EstadoJogo.A_JOGAR) return;
        double novoX = jogador.getX() - Player.VELOCIDADE;
        jogador.setX(Math.max(0, novoX));
    }

    /**
     * Move o jogador para a direita, garantindo que não sai da fronteira direita (LARGURA_ECRA).
     */
    public void moverJogadorDireita() {
        if (estado != EstadoJogo.A_JOGAR) return;
        double novoX = jogador.getX() + Player.VELOCIDADE;
        jogador.setX(Math.min(LARGURA_ECRA - jogador.getLargura(), novoX));
    }

    /**
     * O jogador efetua um disparo. O jogo clássico apenas permite um projétil ativo de cada vez.
     */
    public void jogadorDisparar() {
        if (estado != EstadoJogo.A_JOGAR) return;
        boolean temProjetilAtivo = projetilJogador.stream().anyMatch(Projetil::isAtivo);
        if (!temProjetilAtivo) {
            projetilJogador.add(jogador.disparar());
            tirosTotais++;
        }
    }

    /**
     * Pausa temporariamente a partida se esta estiver em execução.
     */
    public void pausar() {
        if (estado == EstadoJogo.A_JOGAR) estado = EstadoJogo.PAUSADO;
    }

    /**
     * Retoma a partida se esta se encontrar em estado de pausa.
     */
    public void retomar() {
        if (estado == EstadoJogo.PAUSADO) estado = EstadoJogo.A_JOGAR;
    }

    // =========================================================
    //  HIGH SCORES
    // =========================================================

    /**
     * Regista a pontuação final na tabela de classificações.
     * @param iniciais String com até 3 letras introduzidas pelo jogador.
     */
    public void registarHighScore(String iniciais) {
        MelhoresPontuacoes.adicionarScore(iniciais, jogador.getPontuacao(), vaga);
        classificacoes = new ArrayList<>(MelhoresPontuacoes.getClassificacoes());
    }

    /**
     * Valida se a pontuação atual superou o top 1 histórico.
     * @return {@code true} se for novo recorde.
     */
    public boolean ehNovoRecorde() {
        List<MelhoresPontuacoes> rankAtual = MelhoresPontuacoes.getClassificacoes();
        return rankAtual.isEmpty()
                || jogador.getPontuacao() > rankAtual.get(0).getPontuacao();
    }

    // ---- Limpeza ----

    /**
     * Remove das listas lógicas as entidades que já foram destruídas ou desativadas,
     * impedindo vazamentos de memória (memory leaks) e otimizando o loop.
     */
    private void limparInativos() {
        projetilJogador.removeIf(p -> !p.isAtivo());
        projetisInimigos.removeIf(p -> !p.isAtivo());
        barricadas.removeIf(Barricadas::estaDestruida);
    }

    // =========================================================
    //  GETTERS (para a View / Controller)
    // =========================================================

    /** @return A instância do jogador. */
    public Player getJogador() { return jogador; }

    /** @return A entidade que gere o bloco de inimigos. */
    public FrotaInimigo getFrota() { return frota; }

    /** @return Lista atual de barricadas em jogo. */
    public List<Barricadas> getBarricadas() { return barricadas; }

    /** @return Lista (geralmente singular) do projétil do jogador. */
    public List<Projetil_jogador> getProjetisJogador() { return projetilJogador; }

    /** @return Lista com os disparos efetuados pela frota inimiga. */
    public List<Projetil_Inimigo> getProjetisInimigos() { return projetisInimigos; }

    /** @return A instância do invasor bónus, ou {@code null} caso não esteja ativo no ecrã. */
    public Inimigo_aleatorio getInvasorAleatorio() { return invasorAleatorio; }

    /** @return O estado em que a máquina lógica do jogo se encontra (menu, pausa, etc). */
    public EstadoJogo getEstado() { return estado; }

    /** @return O nível numérico atual. */
    public int getVaga() { return vaga; }

    /**
     * Calcula dinamicamente o High Score a apresentar no ecrã superior durante o jogo.
     * @return O valor máximo entre o primeiro classificado no disco e a pontuação atual.
     */
    public int gethiScore() {
        List<MelhoresPontuacoes> rankAtual = MelhoresPontuacoes.getClassificacoes();

        // Se a tabela estiver vazia, mostra a pontuação atual do jogador (ou 0)
        if (rankAtual.isEmpty()) {
            return hiScore;
        }

        // Se não estiver vazia, o High Score do ecrã é o valor mais alto entre:
        // O 1º lugar da tabela OU a pontuação atual do jogador nesta partida
        return Math.max(rankAtual.get(0).getPontuacao(), hiScore);
    }

    /** @return Lista com o Top 10 histórico, em modo read-only. */
    public List<MelhoresPontuacoes> getClassificacoes() { return Collections.unmodifiableList(classificacoes); }

    /**
     * Calcula a percentagem de precisão dos disparos efetuados nesta vaga.
     * @return Um valor inteiro (0 a 100) refletindo a eficácia do jogador.
     */
    public int getPrecisaoTiro() {
        if (tirosTotais == 0) return 0;
        return (int) Math.round((double) tirosAcertados / tirosTotais * 100);
    }

    /** @return A pontuação amealhada. Útil como atalho para o Controller. */
    public int getPontuacaoVaga() { return jogador.getPontuacao(); }
}