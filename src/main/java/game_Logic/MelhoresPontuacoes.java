package game_Logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MelhoresPontuacoes implements Comparable<MelhoresPontuacoes> {

    private static final int MAX_SCORES = 10;
    private static List<MelhoresPontuacoes> classificacoes = new ArrayList<>();

    private final String nome;
    private final int pontuacao;
    private final int vaga;

    public MelhoresPontuacoes(String nome, int pontuacao, int vaga) {
        this.nome = nome.substring(0, Math.min(3, nome.length())).toUpperCase();
        this.pontuacao = pontuacao;
        this.vaga = vaga;
    }

    public static void adicionarScore(String nome, int pontuacao, int vaga) {
        classificacoes.add(new MelhoresPontuacoes(nome, pontuacao, vaga));
        Collections.sort(classificacoes);
        if (classificacoes.size() > MAX_SCORES)
            classificacoes = new ArrayList<>(classificacoes.subList(0, MAX_SCORES));
    }

    public static boolean ehNovoRecorde(int pontuacao) {
        if (classificacoes.size() < MAX_SCORES) return true;
        return pontuacao > classificacoes.get(classificacoes.size() - 1).getPontuacao();
    }

    public static List<MelhoresPontuacoes> getClassificacoes() {
        return Collections.unmodifiableList(classificacoes);
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