import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

class Show {

    public static final String FILE_PATH = "/tmp/disneyplus.csv";
    public static ArrayList<Show> todosFilmes = new ArrayList<Show>();
    
    private String Id;
    private String Tipo;
    private String Titulo;
    private String Diretor;
    private ArrayList<String> Cast;
    private String Pais;
    private Date Data;
    private int Ano;
    private String Rating;
    private String Duracao;
    private ArrayList<String> Listado;

    // Contrutor vazio
    public Show() {

        this.Id = "";
        this.Tipo = "";
        this.Titulo = "";
        this.Diretor = "";
        this.Cast = new ArrayList<String>();
        this.Pais = "";
        this.Data = null;
        this.Ano = 0;
        this.Rating = "";
        this.Duracao = "";
        this.Listado = new ArrayList<String>();
    }

    // Construtor
    public Show(String id, String tipo, String titulo, String diretor,
            ArrayList<String> cast, String pais, Date data, int ano, String rating, String duracao,
            ArrayList<String> listado) {

        Id = id;
        Tipo = tipo;
        Titulo = titulo;
        Diretor = diretor;
        Cast = cast;
        Pais = pais;
        Data = data;
        Ano = ano;
        Rating = rating;
        Duracao = duracao;
        Listado = listado;
    }

    // Gets
    public String getId() {
        return this.Id;
    }

    public String getTipo() {
        return this.Tipo;
    }

    public String getTitulo() {
        return this.Titulo;
    }

    public String getDiretor() {
        return this.Diretor;
    }

    public String getPais() {
        return this.Pais;
    }

    public Date getData() {
        return this.Data;
    }

    public int getAno() {
        return this.Ano;
    }

    public String getRating() {
        return this.Rating;
    }

    public String getDuracao() {
        return this.Duracao;
    }

    public String getListado() {

        this.Listado = OrdenarArraylist(this.Listado);
        String list = "[";

        for (int i = 0; i < this.Listado.size(); i++) {
            list += this.Listado.get(i);
            if (i < this.Listado.size() - 1)
                list += ", ";
        }

        list += "]";

        if (list == "[]")
            list = "[NaN]";

        return list;
    }

    public String getCast() {

        this.Cast = OrdenarArraylist(this.Cast);
        String cast = "[";

        for (int i = 0; i < this.Cast.size(); i++) {

            cast += this.Cast.get(i);
            if (i < this.Cast.size() - 1)
                cast += ", ";
        }

        cast += "]";

        if (cast == "[]")
            cast = "[NaN]";

        return cast;
    }

    // Bubble sort manual
    public ArrayList<String> OrdenarArraylist(ArrayList<String> lista) {

        for (int i = 0; i < lista.size() - 1; i++) {
            for (int j = 0; j < lista.size() - i - 1; j++) {
                if (lista.get(j).compareTo(lista.get(j + 1)) > 0) {
                    // Swap
                    String temp = lista.get(j);
                    lista.set(j, lista.get(j + 1));
                    lista.set(j + 1, temp);
                }
            }
        }
        return lista;
    }

    // Sets
    public void setId(String id) {
        this.Id = id;
    }

    public void setTipo(String tipo) {
        this.Tipo = tipo;
    }

    public void setTitulo(String titulo) {
        this.Titulo = titulo;
    }

    public void setDiretor(String diretor) {
        this.Diretor = diretor;
    }

    public void setPais(String pais) {
        this.Pais = pais;
    }

    public void setData(Date data) {
        this.Data = data;
    }

    public void setAno(int ano) {
        this.Ano = ano;
    }

    public void setRating(String rating) {
        this.Rating = rating;
    }

    public void setDuracao(String duracao) {
        this.Duracao = duracao;
    }

    public void setListado(ArrayList<String> listado) {
        this.Listado = listado;
    }

    public void setCast(ArrayList<String> cast) {
        this.Cast = cast;
    }

    // Clone
    public Show Clone() {
        return new Show(this.Id, this.Tipo, this.Titulo, this.Diretor, this.Cast,
                this.Pais, this.Data, this.Ano, this.Rating, this.Duracao, this.Listado);
    }

    public void read(String linhaCsv) {
        String[] lista = new String[11];
        int i = 0, j = 0;

        while (j < 11) { // 11 = Quantidade de atributos de cada Show

            String campo = "";
            // se o campo estiver entre aspas, processa o campo de maneira especial
            if (i < linhaCsv.length() && linhaCsv.charAt(i) == '"') {

                i++; // Pula aspa dupla
                // enquanto não encontrar a aspa final ou uma sequência de duas aspas duplas
                // (representando uma aspa literal)
                while (i < linhaCsv.length()) {
                    if (linhaCsv.charAt(i) == '"') { // se encontrar uma aspa dupla
                        // Verifica se são duas aspas duplas seguidas, que devem ser tratadas como uma
                        // aspa literal
                        if (i + 1 < linhaCsv.length() && linhaCsv.charAt(i + 1) == '"') {
                            i += 2; // Pula as duas aspas
                        } else { // Se for aspa final, sai do loop
                            i++; // Pula aspa final
                            break;
                        }
                    } else { // Se não for aspa dupla, adiciona o caractere ao campo
                        campo += linhaCsv.charAt(i);
                        i++;
                    }
                }
                // Pula a vírgula que separa os campos, se houver
                if (i < linhaCsv.length() && linhaCsv.charAt(i) == ',')
                    i++;
            } else {
                // Vai até a vírgula ou o final da linha
                while (i < linhaCsv.length() && linhaCsv.charAt(i) != ',') {
                    campo += linhaCsv.charAt(i);
                    i++;
                }
                // Pula a vírgula, se houver
                if (i < linhaCsv.length() && linhaCsv.charAt(i) == ',')
                    i++;
            }

            // se o campo estiver vazio, considera como "NaN"
            if (campo.isEmpty())
                campo = "NaN";

            // armazena o campo no array
            lista[j] = campo;
            j++;
        }

        // faz as atribuições dos valores
        this.Id = lista[0];
        this.Tipo = lista[1];
        this.Titulo = lista[2];
        this.Diretor = lista[3];

        if (lista[4].equals("NaN")) {
            ArrayList<String> list = new ArrayList<>();
            list.add("NaN");
            this.Cast = list; // Lista com único valor "NaN"
        } else {
            this.Cast = new ArrayList<>(Arrays.asList(lista[4].split(", "))); // Lista completa
        }

        this.Pais = lista[5];
        this.Data = safeParseDate(lista[6]); // converte em formato de data
        this.Ano = safeParseInt(lista[7]); // converte em inteiro
        this.Rating = lista[8];
        this.Duracao = lista[9];

        if (lista[10].equals("NaN")) {
            ArrayList<String> list = new ArrayList<>();
            list.add("NaN");
            this.Listado = list; // Lista com único valor "NaN"
        } else {
            this.Listado = new ArrayList<>(Arrays.asList(lista[10].split(", "))); // Lista completa
        }
    }

    private Date safeParseDate(String valor) {
        try {
            if (valor.equals("NaN"))
                return null;

            // Se data possuir valor, converte no formato do arquivo csv
            SimpleDateFormat formato = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
            return formato.parse(valor.trim());
        } catch (Exception e) {
            return null; // Em caso de excessão, retorna null
        }
    }

    private int safeParseInt(String valor) {
        try {
            // Converte para inteiro e usa trim() para remover espaços inesperados
            return Integer.parseInt(valor.trim());
        } catch (Exception e) {
            return 0; // Em caso de excessão, retorna 0
        }
    }

    public void LerFilmes() {

        try {

            FileInputStream fstream = new FileInputStream(FILE_PATH);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            String linha = br.readLine();

            while ((linha = br.readLine()) != null) {

                Show show = new Show();
                show.read(linha);
                todosFilmes.add(show);
            }

            // Fechar CSV
            fstream.close();
        } catch (IOException e) {
        }
    }

    public void print(int n) {

        // Formata no padrão "Mês dia, ano" - Exibe "March 1, 1900" (default) se for
        // nulo
        String dataAdd = (Data != null) ? new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).format(Data)
                : "March 1, 1900";

        // Converte o ano -> string para exibição
        String anoLancamento = String.valueOf(Ano);

        // Printando
        System.out.println(
                "[" + n + "] => " + Id +
                        " ## " + (Titulo.equals("NaN") ? "NaN" : Titulo) +
                        " ## " + (Tipo.equals("NaN") ? "NaN" : Tipo) +
                        " ## " + (Diretor.equals("NaN") ? "NaN" : Diretor) +
                        " ## " + getCast() +
                        " ## " + (Pais.equals("NaN") ? "NaN" : Pais) +
                        " ## " + dataAdd +
                        " ## " + anoLancamento +
                        " ## " + (Rating.equals("NaN") ? "NaN" : Rating) +
                        " ## " + (Duracao.equals("NaN") ? "NaN" : Duracao) +
                        " ## " + getListado() + " ##");
    }

    public static Show getById(String id, ArrayList<Show> filmes) {

        for (int i = 0; i < filmes.size(); i++) {
            if (filmes.get(i).getId().equals(id))
                return filmes.get(i);
        }
        return null;
    }

}

class Celula{
    
    public Show elemento;
    Celula prox;

    public Celula(Show s){
        elemento = s;
        prox = null;
    }
}

class Pilha {

    public static Pilha filmesIds = new Pilha();
    public Celula topo;
    public int n;

    public Pilha(){
        topo = null;
        n = 0;
    }

    public void inserir(Show s){
        Celula tmp = new Celula(s);
        tmp.prox = topo;
        topo = tmp;
        tmp = null;
        n++;
    }

    public Show remover() throws Exception{
        if(topo == null)
            throw new Exception();
        Show removido = topo.elemento;
        topo = topo.prox;
        n--;
        return removido;
    }

    public void manipularLinha(String linha) throws Exception {
        String tipo = linha.substring(0, 1);
        String id = "";

        if (tipo.equals("I")) {
            id = linha.substring(2);
            Show s = Show.getById(id, Show.todosFilmes);
            if (s != null)
                filmesIds.inserir(s);
        } else if (tipo.equals("R")) {
            Show s = filmesIds.remover();
            System.out.println("(R) " + s.getTitulo());
        }
    }

    public void manipular(int n, Scanner sc) throws Exception {
        for (int i = 0; i < n; i++) {
            String linha = sc.nextLine();
            manipularLinha(linha);
        }
    }
}

public class Q09 {
    public static void main(String[] args) throws Exception {

        Show show = new Show();
        Scanner sc = new Scanner(System.in);

        // Ler todos os filmes em CSV
        show.LerFilmes();

        String linha = sc.nextLine();

        while (!linha.equals("FIM")) {

            // Get id
            String id = linha;

            // Buscar Show
            show = Show.getById(id, Show.todosFilmes);

            // Colocar na pilha
            if (show != null)
                Pilha.filmesIds.inserir(show);

            linha = sc.nextLine();
        }

        int n = sc.nextInt();
        sc.nextLine(); // Consome o \n pendente após o nextInt
        Pilha.filmesIds.manipular(n, sc);
        
        Celula j = Pilha.filmesIds.topo;
        for(int i = 0; i < Pilha.filmesIds.n; i++){
            j.elemento.print(Pilha.filmesIds.n - i - 1);
            j = j.prox;
        }

        sc.close();
    }
}