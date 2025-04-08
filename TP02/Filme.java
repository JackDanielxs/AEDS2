import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

public class Filme{

    static SimpleDateFormat ddf = new SimpleDateFormat("dd-MM-yyyy");

    public static final String FILE_PATH = "/tmp/disneyplus.csv";
    public static ArrayList<Filme> todosFilmes = new ArrayList<Filme>();

    private UUID Id;
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

        this.Id = UUID.randomUUID();
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
    public Filme(UUID id, String tipo, String titulo, String diretor, 
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
    public UUID getId() { return this.Id; }
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
            list = this.Listado.get(i);
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
    public void setId(UUID id) { this.Id = id; }
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

    //Imprimindo
    public void print() {

        System.out.println("[=> "
            + this.getId() + " ## "
            + this.getTitulo() + " ## "
            + this.getTipo() + " ## "
            + this.getDiretor() == "" ? "NaN" : this.getDiretor() + " ## "
            + this.getCast() + " ## " //tratado no get
            + this.getPais() == "" ? "NaN" : this.getPais() + " ## " 
            + this.getData() == null ? "NaN" : this.getData() + " ## " 
            + this.getAno() == 0 ? "NaN"+ : this.getAno() + " ## " 
            + this.getRating() + " ## " 
            + this.getDuracao() + " ## " 
            + this.getListado() + " ##"); //tratado no get
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
        catch(IOException e) { e.printStackTrace(); }
    }

    public static Filme getById(UUID id, ArrayList<Filme> filmes) {

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
            UUID id = UUID.fromString(linha);

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
