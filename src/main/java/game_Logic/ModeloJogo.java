package game_Logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
/**
 * Modelo central do jogo "Synthetic Oceans".
 *
 * Gere o estado global, coordena todas as entidades e implementa
 * as regras de jogo (colisões, condições de derrota, progressão de vagas).
 *
 * Esta classe NÃO tem qualquer dependência de JavaFX ou de UI.
 * É chamada pelo controlador a cada frame do game loop.
 */
public class ModeloJogo {
    // ---- Dimensões do ecrã de jogo ----
    public static final double LARGURA_ECRA = 800;
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
    private List<Projetil_jogador> projetilJogador; // máx 1 ativo
    private List<Projetil_Inimigo> projetisInimigos;
    private Inimigo_aleatorio invasorAleatorio;     // null se inativo
    // ---- Estado ----
    private EstadoJogo estado;
    private int vaga;
    private int hiScore;
    private List<MelhoresPontuacoes> classificacoes;
    // ---- Estatísticas da vaga ----
    private int tirosTotais;
    private int tirosAcertados;
    private final Random random = new Random();
    public ModeloJogo() {
        this.classificacoes = new ArrayList<>();
        this.hiScore = 0;
        iniciarNovoJogo();
    }
    /** Reinicia completamente o jogo. */
    public void iniciarNovoJogo() {
        this.vaga = 1;
        this.tirosTotais = 0;
        this.tirosAcertados = 0;
        this.estado = EstadoJogo.A_JOGAR;
        inicializarEntidades();
        sortearProximoDisparo();
        sortearProximoInvasor();
    }
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
     * Atualiza um frame completo do jogo.
     * Deve ser chamado pelo Controller a cada tick do AnimationTimer.
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
    private void moverProjeteis() {
        projetilJogador.forEach(Projetil::mover);
        projetisInimigos.forEach(Projetil::mover);
        // Desativar projéteis fora dos limites
        projetilJogador.forEach(p -> { if (p.getY() < 0) p.desativar(); });
        projetisInimigos.forEach(p -> { if (p.getY() > ALTURA_ECRA) p.desativar(); });
    }
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
    private void tentarSpawnInvasorAleatorio() {
        if (invasorAleatorio != null && invasorAleatorio.isVivo()) return;
        framesSemInvasor++;
        if (framesSemInvasor >= proximoInvasor) {
            spawnInvasorAleatorio();
            framesSemInvasor = 0;
            sortearProximoInvasor();
        }
    }
    private void spawnInvasorAleatorio() {
        boolean daEsquerda = random.nextBoolean();
        double x = daEsquerda ? -Inimigo_aleatorio.LARGURA : LARGURA_ECRA;
        double direcao = daEsquerda ? 1 : -1;
        invasorAleatorio = new Inimigo_aleatorio(x, 20, direcao);
    }
    private void sortearProximoInvasor() {
        proximoInvasor = INTERVALO_INVASOR_MIN
                + random.nextInt(INTERVALO_INVASOR_MAX - INTERVALO_INVASOR_MIN + 1);
    }
    // =========================================================
    //  COLISÕES
    // =========================================================
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
    /** Avança para a próxima vaga. Chamado pelo Controller após transição. */
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
    private void atualizarHiScore() {
        if (jogador.getPontuacao() > hiScore) {
            hiScore = jogador.getPontuacao();
        }
    }
    // =========================================================
    //  AÇÕES DO JOGADOR
    // =========================================================
    /**
     * Move o jogador para a esquerda, respeitando os limites do ecrã.
     */
    public void moverJogadorEsquerda() {
        if (estado != EstadoJogo.A_JOGAR) return;
        double novoX = jogador.getX() - Player.VELOCIDADE;
        jogador.setX(Math.max(0, novoX));
    }
    /**
     * Move o jogador para a direita, respeitando os limites do ecrã.
     */
    public void moverJogadorDireita() {
        if (estado != EstadoJogo.A_JOGAR) return;
        double novoX = jogador.getX() + Player.VELOCIDADE;
        jogador.setX(Math.min(LARGURA_ECRA - jogador.getLargura(), novoX));
    }
    /**
     * O jogador dispara. Só é permitido um projétil ativo de cada vez.
     */
    public void jogadorDisparar() {
        if (estado != EstadoJogo.A_JOGAR) return;
        boolean temProjetilAtivo = projetilJogador.stream().anyMatch(Projetil::isAtivo);
        if (!temProjetilAtivo) {
            projetilJogador.add(jogador.disparar());
            tirosTotais++;
        }
    }
    public void pausar() {
        if (estado == EstadoJogo.A_JOGAR) estado = EstadoJogo.PAUSADO;
    }
    public void retomar() {
        if (estado == EstadoJogo.PAUSADO) estado = EstadoJogo.A_JOGAR;
    }
    // =========================================================
    //  HIGH SCORES
    // =========================================================
    /**
     * Regista um high score no final do jogo.
     * @param iniciais até 3 letras do jogador
     */
    public void registarHighScore(String iniciais) {
        MelhoresPontuacoes.adicionarScore(iniciais, jogador.getPontuacao(), vaga);
        classificacoes = new ArrayList<>(MelhoresPontuacoes.getClassificacoes());
    }
    public boolean ehNovoRecorde() {
        return classificacoes.isEmpty()
                || jogador.getPontuacao() > classificacoes.get(0).getPontuacao();
    }
    // ---- Limpeza ----
    private void limparInativos() {
        projetilJogador.removeIf(p -> !p.isAtivo());
        projetisInimigos.removeIf(p -> !p.isAtivo());
        barricadas.removeIf(Barricadas::estaDestruida);
    }
    // =========================================================
    //  GETTERS (para a View / Controller)
    // =========================================================
    public Player getJogador() { return jogador; }
    public FrotaInimigo getFrota() { return frota; }
    public List<Barricadas> getBarricadas() { return barricadas; }
    public List<Projetil_jogador> getProjetisJogador() { return projetilJogador; }
    public List<Projetil_Inimigo> getProjetisInimigos() { return projetisInimigos; }
    public Inimigo_aleatorio getInvasorAleatorio() { return invasorAleatorio; }
    public EstadoJogo getEstado() { return estado; }
    public int getVaga() { return vaga; }
    public int getHiScore() { return hiScore; }
    public List<MelhoresPontuacoes> getClassificacoes() { return Collections.unmodifiableList(classificacoes); }
    public int getPrecisaoTiro() {
        if (tirosTotais == 0) return 0;
        return (int) Math.round((double) tirosAcertados / tirosTotais * 100);
    }
    public int getPontuacaoVaga() { return jogador.getPontuacao(); }
}

