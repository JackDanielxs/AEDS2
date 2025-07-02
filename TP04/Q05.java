import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

class Show {

    public static final String FILE_PATH = "/tmp/disneyplus.csv";
    public static ArrayList<Show> todosFilmes = new ArrayList<>();

    private String id;
    private String tipo;
    private String titulo;
    private String diretor;
    private ArrayList<String> cast;
    private String pais;
    private Date data;
    private int ano;
    private String rating;
    private String duracao;
    private ArrayList<String> listado;

    public Show() {
        this.id = "";
        this.tipo = "";
        this.titulo = "";
        this.diretor = "";
        this.cast = new ArrayList<>();
        this.pais = "";
        this.data = null;
        this.ano = 0;
        this.rating = "";
        this.duracao = "";
        this.listado = new ArrayList<>();
    }

    // Getters e Setters para todos os atributos.
    public String getId() { return this.id; }
    public String getTipo() { return this.tipo; }
    public String getTitulo() { return this.titulo; }
    public String getDiretor() { return this.diretor; }
    public ArrayList<String> getCast() { return this.cast; }
    public String getPais() { return this.pais; }
    public Date getData() { return this.data; }
    public int getAno() { return this.ano; }
    public String getRating() { return this.rating; }
    public String getDuracao() { return this.duracao; }
    public ArrayList<String> getListado() { return this.listado; }

    public void setId(String id) { this.id = id; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setDiretor(String diretor) { this.diretor = diretor; }
    public void setCast(ArrayList<String> cast) { this.cast = cast; }
    public void setPais(String pais) { this.pais = pais; }
    public void setData(Date data) { this.data = data; }
    public void setAno(int ano) { this.ano = ano; }
    public void setRating(String rating) { this.rating = rating; }
    public void setDuracao(String duracao) { this.duracao = duracao; }
    public void setListado(ArrayList<String> listado) { this.listado = listado; }

    public Show clone() {
        Show clonedShow = new Show();
        clonedShow.setId(this.id);
        clonedShow.setTipo(this.tipo);
        clonedShow.setTitulo(this.titulo);
        clonedShow.setDiretor(this.diretor);
        // Cria novas ArrayLists para cast e listado para evitar referências ao mesmo objeto
        clonedShow.setCast(new ArrayList<>(this.cast));
        clonedShow.setPais(this.pais);
        clonedShow.setData(this.data); // Date objects are immutable, so direct assignment is fine
        clonedShow.setAno(this.ano);
        clonedShow.setRating(this.rating);
        clonedShow.setDuracao(this.duracao);
        clonedShow.setListado(new ArrayList<>(this.listado));
        return clonedShow;
    }

    public void read(String linhaCsv) {
        String[] lista = new String[11];
        int i = 0, j = 0; // i: índice na linhaCsv, j: índice no array lista

        while (j < 11) {
            String campo = "";
            // Verifica se o campo começa com aspas duplas (indica um campo com vírgulas internas ou aspas escapadas)
            if (i < linhaCsv.length() && linhaCsv.charAt(i) == '"') {
                i++; // Avança para depois da primeira aspas duplas
                while (i < linhaCsv.length()) {
                    if (linhaCsv.charAt(i) == '"') {
                        // Verifica se é uma aspas duplas escapada ("")
                        if (i + 1 < linhaCsv.length() && linhaCsv.charAt(i + 1) == '"') {
                            campo += '"'; // Adiciona uma única aspas duplas ao campo
                            i += 2; // Avança duas posições (para as duas aspas)
                        } else {
                            i++; // Avança para depois da aspas de fechamento do campo
                            break; // Sai do loop interno, campo lido
                        }
                    } else {
                        campo += linhaCsv.charAt(i); // Adiciona caractere ao campo
                        i++; // Avança uma posição
                    }
                }
                // Se o campo for lido e houver uma vírgula em seguida, avança além dela
                if (i < linhaCsv.length() && linhaCsv.charAt(i) == ',') i++;
            } else {
                // Campo sem aspas duplas
                while (i < linhaCsv.length() && linhaCsv.charAt(i) != ',') {
                    campo += linhaCsv.charAt(i); // Adiciona caractere ao campo
                    i++; // Avança uma posição
                }
                // Se houver uma vírgula em seguida, avança além dela
                if (i < linhaCsv.length() && linhaCsv.charAt(i) == ',') i++;
            }

            // Se o campo estiver vazio, substitui por "NaN" para padronização
            if (campo.isEmpty()) campo = "NaN";
            lista[j] = campo; // Armazena o campo processado
            j++; // Avança para o próximo campo
        }

        // Atribui os valores parseados aos atributos da instância Show
        this.id = lista[0];
        this.tipo = lista[1];
        this.titulo = lista[2];
        this.diretor = lista[3];
        // Trata campos de lista (cast, listado) que podem ser "NaN" ou listas separadas por ", "
        this.cast = lista[4].equals("NaN") ? new ArrayList<>() : new ArrayList<>(Arrays.asList(lista[4].split(", ")));
        this.pais = lista[5];
        this.data = safeParseDate(lista[6]); // Parse com tratamento de exceções
        this.ano = safeParseInt(lista[7]);   // Parse com tratamento de exceções
        this.rating = lista[8];
        this.duracao = lista[9];
        this.listado = lista[10].equals("NaN") ? new ArrayList<>() : new ArrayList<>(Arrays.asList(lista[10].split(", ")));
    }

    private Date safeParseDate(String valor) {
        try {
            if (valor.equals("NaN")) return null;
            // Define o formato da data e o Locale para parsing (ex: "MMMM d, yyyy" para "January 1, 2020")
            SimpleDateFormat formato = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
            return formato.parse(valor.trim());
        } catch (Exception e) {
            // Se ocorrer qualquer exceção durante o parse, retorna null
            return null;
        }
    }

    private int safeParseInt(String valor) {
        try {
            return Integer.parseInt(valor.trim());
        } catch (Exception e) {
            // Se ocorrer qualquer exceção (ex: NumberFormatException), retorna 0
            return 0;
        }
    }
    
    public void lerTodosFilmes() {
        try (FileInputStream fstream = new FileInputStream(FILE_PATH);
             BufferedReader br = new BufferedReader(new InputStreamReader(fstream))) {

            br.readLine(); // Pula a linha do cabeçalho do CSV
            String linha;
            while ((linha = br.readLine()) != null) {
                Show show = new Show();
                show.read(linha); // Lê e parseia cada linha
                todosFilmes.add(show); // Adiciona o show à lista global
            }
        } catch (IOException e) {
            // Imprime o stack trace em caso de erro de I/O (ex: arquivo não encontrado)
            System.err.println("Erro ao ler o arquivo CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Show getById(String id, ArrayList<Show> filmes) {
        for (Show filme : filmes) {
            if (filme.getId().equals(id)) {
                return filme;
            }
        }
        return null;
    }

    public static void log(long tempo, int comparacoes) {
        try (BufferedWriter esc = new BufferedWriter(new FileWriter("800712_hashReserva.txt"))) {
            esc.write("800712\t" + tempo + "\t" + comparacoes);
        } catch (IOException e) {}
    }
}

class HashTable {
    private final int TAM_TAB = 21; // Tamanho da tabela principal
    private final int TAM_RESERVA = 9; // Tamanho da área de reserva
    private final int TAM_TOTAL = TAM_TAB + TAM_RESERVA; // Tamanho total da tabela
    private Show[] tabela;
    private int comparacoes;

    public HashTable() {
        this.tabela = new Show[TAM_TOTAL];
        for (int i = 0; i < TAM_TOTAL; i++) {
            this.tabela[i] = null;
        }
        this.comparacoes = 0;
    }

    public int getComparacoes() {
        return this.comparacoes;
    }

    private int getASCIISum(String titulo) {
        int sum = 0;
        for (char c : titulo.toCharArray()) {
            sum += (int) c;
        }
        return sum;
    }

    private int hash(String titulo) {
        return getASCIISum(titulo) % TAM_TAB;
    }

    public boolean inserir(Show show) {
        if (show == null || show.getTitulo() == null) {
            return false; // Não insere shows nulos ou sem título
        }

        int hashIndex = hash(show.getTitulo());

        // Tenta inserir na posição calculada pela hash
        if (tabela[hashIndex] == null) {
            tabela[hashIndex] = show;
            return true;
        } else {
            // Colisão: tenta inserir na área de reserva
            for (int i = TAM_TAB; i < TAM_TOTAL; i++) {
                if (tabela[i] == null) {
                    tabela[i] = show;
                    return true;
                }
            }
        }
        return false; // Tabela cheia (principal + reserva)
    }

    public void pesquisar(String titulo) {
        int hashIndex = hash(titulo);
        boolean encontrado = false;
        int finalPosition = hashIndex;

        // 1. Procura na posição hash principal
        comparacoes++; 
        if (tabela[hashIndex] != null && tabela[hashIndex].getTitulo().equals(titulo)) {
            finalPosition = hashIndex; // Item encontrado no hashIndex
            encontrado = true;
        }

        // 2. Se não encontrou na posição principal, procura na área de reserva
        if (!encontrado) { 
            for (int i = TAM_TAB; i < TAM_TOTAL; i++) {
                comparacoes++;
                if (tabela[i] != null && tabela[i].getTitulo().equals(titulo)) {
                    finalPosition = i; // Item encontrado na área de reserva
                    encontrado = true;
                    break;
                }
            }
        }

        if (encontrado)
            System.out.println(" (Posicao: " + hashIndex + ") SIM");
        else 
            System.out.println(" (Posicao: " + finalPosition + ") NAO");
    }
}

public class Q05 {
    public static void main(String[] args) {
        long inicio = System.currentTimeMillis();
        
        Show baseShow = new Show();
        baseShow.lerTodosFilmes();

        HashTable hashTable = new HashTable();
        Scanner sc = new Scanner(System.in);

        String linha = sc.nextLine();
        while (!linha.equals("FIM")) {
            Show showParaInserir = Show.getById(linha, Show.todosFilmes);
            if (showParaInserir != null) {
                hashTable.inserir(showParaInserir); // Insere o show na tabela hash
            }
            linha = sc.nextLine(); 
        }

        linha = sc.nextLine();
        while (!linha.equals("FIM")) {
            String tituloParaBuscar = linha;
            hashTable.pesquisar(tituloParaBuscar); // Realiza a pesquisa na tabela hash
            linha = sc.nextLine();
        }

        sc.close(); 
        long fim = System.currentTimeMillis();

        Show.log(fim - inicio, hashTable.getComparacoes());
    }
}

