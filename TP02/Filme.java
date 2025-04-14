import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

public class Filme{

    public static final String FILE_PATH = "/tmp/disneyplus.csv";
    public static ArrayList<Filme> todosFilmes = new ArrayList<Filme>();

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
    public Filme(){

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
    public Filme(String id, String tipo, String titulo, String diretor, 
    ArrayList<String> cast, String pais, Date data, int ano, String rating, String duracao, ArrayList<String> listado){

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
    public String getId() { return this.Id; }
    public String getTipo() { return this.Tipo; }
    public String getTitulo() { return this.Titulo; }
    public String getDiretor() { return this.Diretor; }
    public String getPais() { return this.Pais; }
    public Date getData() { return this.Data; }
    public int getAno() { return this.Ano; }
    public String getRating() { return this.Rating; }
    public String getDuracao() { return this.Duracao; }
    public String getListado() {

        String list = "[";

        for(int i = 0; i < this.Listado.size(); i++){
            list += this.Listado.get(i);
            if(i < this.Listado.size() - 1)
                list += ", ";
        }

        list += "]";

        if(list == "[]")
            list = "[NaN]";

        return list;
    }
    public String getCast(){
        
        String cast = "[";

        for(int i = 0; i < this.Cast.size(); i++) {

            cast += this.Cast.get(i);
            if(i < this.Cast.size() - 1) 
                cast += ", ";
        }

        cast += "]";

        if(cast == "[]")
            cast = "[NaN]";

        return cast;
    }

    // Sets
    public void setId(String id) { this.Id = id; }
    public void setTipo(String tipo) { this.Tipo = tipo; }
    public void setTitulo(String titulo) { this.Titulo = titulo; }
    public void setDiretor(String diretor) { this.Diretor = diretor; }
    public void setPais(String pais) { this.Pais = pais; }
    public void setData(Date data) { this.Data = data; }
    public void setAno(int ano) { this.Ano = ano; }
    public void setRating(String rating) { this.Rating = rating; }
    public void setDuracao(String duracao) { this.Duracao = duracao; }
    public void setListado(ArrayList<String> listado) { this.Listado = listado; }
    public void setCast(ArrayList<String> cast) { this.Cast = cast; }

    // Clone
    public Filme Clone(){
        return new Filme(this.Id, this.Tipo, this.Titulo, this.Diretor, this.Cast, 
        this.Pais, this.Data, this.Ano, this.Rating, this.Duracao, this.Listado);
    }

    public void print() {
   
        //se a data não for nula, formata no padrão "MMMM d, yyyy" do arquivo csv
        //caso contrário, exibe "NaN" para indicar que não havia data
        String dataAdicionada = (Data != null) ?
        new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).format(Data) 
        : "March 1, 1900";

        //converte o ano de lançamento para string para exibição
        String anoLancamento = String.valueOf(Ano);
    
        //impressão das imformações fomatadas
        System.out.println(
            "=> " + Id +
            " ## " + (Titulo.equals("NaN") ? "NaN" : Titulo) +
            " ## " + (Tipo.equals("NaN") ? "NaN" : Tipo) +
            " ## " + (Diretor.equals("NaN") ? "NaN" : Diretor) +
            " ## " + getCast() +
            " ## " + (Pais.equals("NaN") ? "NaN" : Pais) +
            " ## " + dataAdicionada +
            " ## " + anoLancamento +
            " ## " + (Rating.equals("NaN") ? "NaN" : Rating) +
            " ## " + (Duracao.equals("NaN") ? "NaN" : Duracao) +
            " ## " + getListado() + " ##"
        );
    }

    public void read(String linhaCsv) {
        String[] lista = new String[11];
        int i = 0, j = 0;
    
        while(j < 11) {  //enquanto não tiver os 11 campos preenchidos
            String campo = "";  //variável para armazenar o valor de cada campo
            
            //se o campo estiver entre aspas, processa o campo de maneira especial
            if(i < linhaCsv.length() && linhaCsv.charAt(i) == '"') {
                i++;  //pula a primeira aspa dupla
                
                //enquanto não encontrar a aspa final ou uma sequência de duas aspas duplas (representando uma aspa literal)
                while(i < linhaCsv.length()) {
                    if(linhaCsv.charAt(i) == '"') {  //se encontrar uma aspa dupla
                        //verifica se são duas aspas duplas seguidas, que devem ser tratadas como uma aspa literal
                        if(i + 1 < linhaCsv.length() && linhaCsv.charAt(i + 1) == '"') {
                            i += 2;  //pula as duas aspas
                        }
                        else {  //se for a aspa final, sai do loop
                            i++;  //pula a aspa final
                            break;
                        }
                    }
                    else {  //se não for uma aspa dupla, adiciona o caractere ao campo
                        campo += linhaCsv.charAt(i);
                        i++;
                    }
                }
                //pula a vírgula que separa os campos, se houver
                if(i < linhaCsv.length() && linhaCsv.charAt(i) == ',') i++;
            }
            else {
                //caso o campo não esteja entre aspas, vai até a vírgula ou o final da linha
                while(i < linhaCsv.length() && linhaCsv.charAt(i) != ',') {
                    campo += linhaCsv.charAt(i);
                    i++;
                }
                //pula a vírgula, se houver
                if(i < linhaCsv.length() && linhaCsv.charAt(i) == ',') i++;
            }
    
            //se o campo estiver vazio, considera como "NaN"
            if(campo.isEmpty()) campo = "NaN";
            
            //armazena o campo no array e incrementa o índice
            lista[j] = campo;
            j++;
        }
        
        //faz as atribuições dos valores
        this.Id = lista[0];
        this.Tipo = lista[1];
        this.Titulo = lista[2];
        this.Diretor = lista[3];
        //se for "NaN", salva como "NaN", se não, quebra a string sempre que encontrar vígula seguida de espaço
        if(lista[4].equals("NaN")){
            ArrayList<String> list = new ArrayList<>();
            list.add("NaN");
            this.Cast = list;
        }
        else{
            this.Cast = new ArrayList<>(Arrays.asList(lista[4].split(", ")));
        }
        this.Pais = lista[5];
        this.Data = safeParseDate(lista[6]); //converte em formato de data
        this.Ano = safeParseInt(lista[7]); //converte em inteiro
        this.Rating = lista[8];
        this.Duracao = lista[9];
        //se for "NaN", salva como "NaN", se não, quebra a string sempre que encontrar vígula seguida de espaço
        if(lista[10].equals("NaN")){
            ArrayList<String> list = new ArrayList<>();
            list.add("NaN");
            this.Listado = list;
        }
        else{
            this.Listado = new ArrayList<>(Arrays.asList(lista[10].split(", ")));
        }
    }

    private Date safeParseDate(String valor) {
        try {
            if(valor.equals("NaN")) return null; //se data for "NaN", retorna null

            //se data possuir valor, converte no formato do arquivo csv
            SimpleDateFormat formato = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
            return formato.parse(valor.trim());
        }
        catch(Exception e) {
            return null; //em caso de excessão, retorna null
        }
    }
    
    private int safeParseInt(String valor) {
        try {
            //converte o ano de lançamento para inteiro e usa trim() para remover espaços inesperados
            return Integer.parseInt(valor.trim());
        }
        catch(Exception e) {
            return 0; //em caso de excessão, retorna 0
        }
    }

    public static void LerFilmes() {

        try {

            FileInputStream fstream = new FileInputStream(FILE_PATH);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            String linha = br.readLine();
  
            while((linha = br.readLine()) != null) {

                Filme filme = new Filme();
                filme.read(linha);
                todosFilmes.add(filme);
            }

            // Fechar CSV
            fstream.close();
        }
        catch(IOException e) { }
    }

    public static Filme getById(String id, ArrayList<Filme> filmes) {

        for(int i = 0; i < filmes.size(); i++) {

            if(filmes.get(i).getId().equals(id)) return filmes.get(i);
        }
        return null;
    }

    public static void main(String[] args) {

        //Ler todos os filmes em CSV
        LerFilmes();
        Scanner sc = new Scanner(System.in);
        Filme filme = new Filme();
        String linha = sc.nextLine();

        while(!linha.equals("FIM")) {

            // Get id
            String id = linha;

            // buscar filme
            filme = getById(id, todosFilmes);

            // Printar filme
            if(filme != null) 
                filme.print();

            linha = sc.nextLine();
        }

        sc.close();
    }
}
