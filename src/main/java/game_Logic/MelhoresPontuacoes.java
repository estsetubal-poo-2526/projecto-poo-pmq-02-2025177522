package game_Logic;

import java.io.*;
import java.util.*;

/**
 * Gere a tabela de classificações (Leaderboard) e a persistência dos dados.
 *
 * <p>Esta classe implementa {@link Serializable} para permitir que o estado da tabela
 * seja guardado e carregado diretamente de um ficheiro local ({@code pontuacoes.dat}).
 * Implementa também {@link Comparable} para garantir que a lista é sempre ordenada
 * de forma decrescente (da pontuação mais alta para a mais baixa).</p>
 *
 * <p>A lista estática mantém em memória apenas o Top 10 das melhores prestações.</p>
 */
public class MelhoresPontuacoes implements Comparable<MelhoresPontuacoes>, Serializable {

    /** * Identificador de versão para a serialização.
     * Garante a compatibilidade estrutural ao ler o ficheiro em versões futuras.
     */
    private static final long serialVersionUID = 1L;

    /** Limite máximo de pontuações guardadas na tabela. */
    private static final int MAX_SCORES = 10;

    /** Nome do ficheiro local onde as classificações serão guardadas. */
    private static final String ARQUIVO = "pontuacoes.dat";

    /** Lista estática que mantém o Top 10 carregado em memória durante a execução do jogo. */
    private static List<MelhoresPontuacoes> classificacoes = carregarDoDisco();

    /** Nome ou iniciais do jogador (limitado a 10 caracteres). */
    private final String nome;

    /** Pontuação final atingida pelo jogador. */
    private final int pontuacao;

    /** Número da vaga em que o jogador terminou a partida. */
    private final int vaga;

    /**
     * Construtor da classe {@code MelhoresPontuacoes}.
     * <p>O nome introduzido é automaticamente convertido para maiúsculas e
     * truncado num máximo de 10 caracteres para garantir o alinhamento visual na UI.</p>
     *
     * @param nome      O nome introduzido pelo jogador.
     * @param pontuacao A pontuação total obtida.
     * @param vaga      A vaga alcançada antes do Game Over.
     */
    public MelhoresPontuacoes(String nome, int pontuacao, int vaga) {
        this.nome = nome.substring(0, Math.min(10, nome.length())).toUpperCase();
        this.pontuacao = pontuacao;
        this.vaga = vaga;
    }

    /**
     * Regista uma nova pontuação na tabela, caso esta tenha valor suficiente para entrar no Top 10.
     * <p>Se a nova pontuação for válida, é adicionada à lista, a lista é ordenada de forma
     * decrescente e redimensionada para o limite máximo. Por fim, o estado atualizado
     * é gravado no disco.</p>
     *
     * @param nome      O nome do jogador.
     * @param pontuacao A pontuação obtida.
     * @param vaga      A vaga atingida.
     */
    public static void adicionarScore(String nome, int pontuacao, int vaga) {
        if (classificacoes.size() >= MAX_SCORES && pontuacao <= classificacoes.get(MAX_SCORES - 1).getPontuacao()) {
            return; // A pontuação não é suficiente para entrar no Top 10
        }
        classificacoes.add(new MelhoresPontuacoes(nome, pontuacao, vaga));
        Collections.sort(classificacoes);

        if (classificacoes.size() > MAX_SCORES) {
            classificacoes = new ArrayList<>(classificacoes.subList(0, MAX_SCORES));
        }

        salvarNoDisco(); // Salva sempre que houver alteração
    }

    /**
     * Grava a lista atual de classificações num ficheiro local utilizando serialização de objetos.
     * Caso ocorra um erro de I/O, este é impresso no terminal de erros (System.err).
     */
    public static void salvarNoDisco() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO))) {
            oos.writeObject(classificacoes);
        } catch (IOException e) {
            System.err.println("Erro ao guardar pontuações: " + e.getMessage());
        }
    }

    /**
     * Carrega a lista de classificações a partir do ficheiro local associado.
     * <p>Se o ficheiro não existir ou ocorrer algum erro de leitura (ex: classe não encontrada
     * ou ficheiro corrompido), inicializa e devolve uma nova lista vazia.</p>
     *
     * @return Uma lista tipificada contendo os objetos {@code MelhoresPontuacoes} guardados,
     * ou uma lista vazia caso falhe.
     */
    @SuppressWarnings("unchecked")
    private static List<MelhoresPontuacoes> carregarDoDisco() {
        File file = new File(ARQUIVO);
        if (!file.exists()) return new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<MelhoresPontuacoes>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Verifica se uma determinada pontuação tem valor suficiente para entrar na tabela (Top 10).
     *
     * @param pontuacao O valor da pontuação a verificar.
     * @return {@code true} se a lista ainda não estiver cheia ou se a pontuação for estritamente
     * superior à pontuação mais baixa atualmente registada.
     */
    public static boolean ehNovoRecorde(int pontuacao) {
        if (classificacoes.size() < MAX_SCORES) return true;
        return pontuacao > classificacoes.get(classificacoes.size() - 1).getPontuacao();
    }

    /**
     * Obtém uma visualização apenas de leitura (read-only) da lista atual de pontuações.
     *
     * @return Uma {@link List} inalterável contendo as classificações ordenadas.
     */
    public static List<MelhoresPontuacoes> getClassificacoes() {
        return Collections.unmodifiableList(classificacoes);
    }

    /**
     * Verifica se um determinado nome já se encontra registado na tabela.
     * A procura ignora diferenças entre maiúsculas e minúsculas (case-insensitive).
     *
     * @param nomeProcurado O nome a verificar.
     * @return {@code true} se existir pelo menos um registo com o nome exato.
     */
    public static boolean existeNome(String nomeProcurado) {
        return getClassificacoes().stream()
                .anyMatch(score -> score.getNome().equalsIgnoreCase(nomeProcurado));
    }

    /**
     * Apaga todos os registos em memória e reflete essa limpeza no ficheiro guardado no disco.
     */
    public static void limparTabela() {
        classificacoes.clear();
        salvarNoDisco();
    }

    /**
     * Compara a pontuação desta entidade com outra para determinar a ordem da lista.
     * <p>A ordem estabelecida é <b>decrescente</b> (maior pontuação primeiro).</p>
     *
     * @param outro O objeto {@code MelhoresPontuacoes} a comparar.
     * @return Um número negativo, zero ou positivo se esta pontuação for respetivamente
     * maior, igual ou menor que a do objeto comparado.
     */
    @Override
    public int compareTo(MelhoresPontuacoes outro) {
        return Integer.compare(outro.pontuacao, this.pontuacao);
    }

    // =========================================================
    //  GETTERS & TOSTRING
    // =========================================================

    /**
     * Obtém o nome do jogador.
     * @return Uma {@code String} com as iniciais/nome do registo.
     */
    public String getNome() { return nome; }

    /**
     * Obtém a pontuação do jogador.
     * @return O valor inteiro da pontuação.
     */
    public int getPontuacao() { return pontuacao; }

    /**
     * Obtém a vaga alcançada pelo jogador.
     * @return O número da vaga.
     */
    public int getVaga() { return vaga; }

    /**
     * Retorna a representação em texto do registo.
     * @return Uma {@code String} formatada com o nome, pontuação preenchida com zeros à esquerda e a vaga.
     */
    @Override
    public String toString() {
        return String.format("%s | %06d | Vaga %d", nome, pontuacao, vaga);
    }
}