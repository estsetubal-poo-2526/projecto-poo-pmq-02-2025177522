package game_Logic;

import java.io.*;
import java.util.*;

// 1. Implementa a interface Serializable
public class MelhoresPontuacoes implements Comparable<MelhoresPontuacoes>, Serializable {

    // 2. ID para garantir compatibilidade entre versões do ficheiro
    private static final long serialVersionUID = 1L;
    private static final int MAX_SCORES = 10;
    private static final String ARQUIVO = "pontuacoes.dat";


    private static List<MelhoresPontuacoes> classificacoes = carregarDoDisco();

    private final String nome;
    private final int pontuacao;
    private final int vaga;

    public MelhoresPontuacoes(String nome, int pontuacao, int vaga) {
        this.nome = nome.substring(0, Math.min(3, nome.length())).toUpperCase();
        this.pontuacao = pontuacao;
        this.vaga = vaga;
    }

    public static void adicionarScore(String nome, int pontuacao, int vaga) {
        if (classificacoes.size() >= MAX_SCORES && pontuacao <= classificacoes.get(MAX_SCORES - 1).getPontuacao()) {
            return;
        }
        classificacoes.add(new MelhoresPontuacoes(nome, pontuacao, vaga));
        Collections.sort(classificacoes);
        if (classificacoes.size() > MAX_SCORES)
            classificacoes = new ArrayList<>(classificacoes.subList(0, MAX_SCORES));

        salvarNoDisco(); //  Salva sempre que houver alteração
    }

    private static void salvarNoDisco() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO))) {
            oos.writeObject(classificacoes);
        } catch (IOException e) {
            System.err.println("Erro ao guardar pontuações: " + e.getMessage());
        }
    }

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
    public static boolean ehNovoRecorde(int pontuacao) {
        if (classificacoes.size() < MAX_SCORES) return true;
        return pontuacao > classificacoes.get(classificacoes.size() - 1).getPontuacao();
    }

    public static List<MelhoresPontuacoes> getClassificacoes() {
        return Collections.unmodifiableList(classificacoes);
    }

    public static boolean existeNome(String nomeProcurado) {
        return getClassificacoes().stream()
                .anyMatch(score -> score.getNome().equalsIgnoreCase(nomeProcurado));
    }
    public static void limparTabela() {
        classificacoes.clear();
        salvarNoDisco();
    }

    @Override
    public int compareTo(MelhoresPontuacoes outro) {
        return Integer.compare(outro.pontuacao, this.pontuacao);
    }

    public String getNome() { return nome; }
    public int getPontuacao() { return pontuacao; }
    public int getVaga() { return vaga; }

    @Override
    public String toString() {
        return String.format("%s | %06d | Vaga %d", nome, pontuacao, vaga);
    }
}