import java.util.LinkedList;
import java.util.Scanner;

public class TabelaHash {

    private static class NoHash {
        String chave; //idx slot
        String valor; // Neste exemplo, chave e valor são a mesma palavra

        NoHash(String chave, String valor) {
            this.chave = chave;
            this.valor = valor;
        }

        @Override
        public String toString() {
            return chave;
        }
    }

    private LinkedList<NoHash>[] tabela; //slots
    private int capacidade; //qtd slots
    private int tamanhoAtual;

    @SuppressWarnings("unchecked") // Para suprimir o aviso sobre a criação do array genérico
    public TabelaHash(int capacidadeInicial) {
        if (capacidadeInicial <= 0) {
            // erro
        }
        this.capacidade = capacidadeInicial;
        // Inicializa o array de LinkedLists
        tabela = new LinkedList[capacidade];
        for (int i = 0; i < capacidade; i++) {
            tabela[i] = new LinkedList<>();
        }
        this.tamanhoAtual = 0;
    }

    private int funcaoHash(String chave) {
        int hash = 0;
        for (char c : chave.toCharArray()) {
            hash = (hash * 31 + c);
        }
        return Math.abs(hash % capacidade); // Garante um índice positivo dentro da capacidade
    }

    public void inserir(String palavra) {
        String chave = palavra.toLowerCase(); // Padroniza para minúsculas
        String valor = palavra.toLowerCase();

        int indice = funcaoHash(chave);
        LinkedList<NoHash> listaNoSlot = tabela[indice];

        // Verifica se a palavra já existe no slot para evitar duplicatas
        for (NoHash no : listaNoSlot) {
            if (no.chave.equals(chave)) {
                no.valor = valor; // Atualiza o valor se a chave já existe
                return;
            }
        }

        // Se não encontrou, adiciona um novo nó
        listaNoSlot.add(new NoHash(chave, valor));
        tamanhoAtual++;
        System.out.println("'" + palavra + "' inserida na posição " + indice + ".");

        // Opcional: rehash se muito cheia
        if ((double) tamanhoAtual / capacidade > 0.75) { // Fator de carga de 0.75
            rehash();
        }
    }

    public boolean buscar(String palavra) {
        String chave = palavra.toLowerCase();
        int indice = funcaoHash(chave);
        LinkedList<NoHash> listaNoSlot = tabela[indice];

        for (NoHash no : listaNoSlot) {
            if (no.chave.equals(chave)) {
                return true; // Encontrou a palavra
            }
        }
        return false; // Não encontrou
    }

    public boolean remover(String palavra) {
        String chave = palavra.toLowerCase();
        int indice = funcaoHash(chave);
        LinkedList<NoHash> listaNoSlot = tabela[indice];

        NoHash noParaRemover = null;
        for (NoHash no : listaNoSlot) {
            if (no.chave.equals(chave)) {
                noParaRemover = no;
                break;
            }
        }

        if (noParaRemover != null) {
            listaNoSlot.remove(noParaRemover);
            tamanhoAtual--;
            System.out.println("'" + palavra + "' removida da posição " + indice + ".");
            return true;
        }
        return false; // Não encontrou para remover
    }

    @SuppressWarnings("unchecked")
    private void rehash() {
        int novaCapacidade = capacidade * 2; // Dobra a capacidade
        LinkedList<NoHash>[] novaTabela = new LinkedList[novaCapacidade];
        for (int i = 0; i < novaCapacidade; i++) {
            novaTabela[i] = new LinkedList<>();
        }

        // Re-hash todos os elementos da tabela antiga para a nova
        for (int i = 0; i < capacidade; i++) {
            for (NoHash no : tabela[i]) {
                int novoIndice = (funcaoHash(no.chave) * 2) % novaCapacidade; // A função de hash precisa se adaptar à nova capacidade
                novaTabela[novoIndice].add(no);
            }
        }

        this.capacidade = novaCapacidade;
        this.tabela = novaTabela;
    }

    public void exibirTodasAsPalavras() {
        if (tamanhoAtual == 0) {
            return; // Vazia
        }
        for (int i = 0; i < capacidade; i++) {
            if (!tabela[i].isEmpty()) {
                System.out.print("Slot " + i + ": ");
                for (NoHash no : tabela[i]) {
                    System.out.print(no.chave + " -> ");
                }
                System.out.println("NULL"); // Indica o fim da lista no slot
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TabelaHash minhaTabelaHash = new TabelaHash(5);
        String palavra = scanner.nextLine();
        minhaTabelaHash.inserir(palavra);
        minhaTabelaHash.buscar(palavra);
        minhaTabelaHash.remover(palavra);
        minhaTabelaHash.exibirTodasAsPalavras();

        scanner.close();
    }
}