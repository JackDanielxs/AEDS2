import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

public class Filme{

    static SimpleDateFormat ddf = new SimpleDateFormat("dd-MM-yyyy");
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
    private String Descricao;

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
        this.Descricao = "";
    }

    // Construtor
    public Filme(UUID id, String tipo, String titulo, String diretor, 
    ArrayList<String> cast, String pais, Date data, int ano, String rating, String duracao, String descricao){

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
        Descricao = descricao;
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
    public String getDescricao() { return this.Descricao; }
    public String getCast(){
        
        String cast = "{";

        for(int i = 0; i < this.Cast.size(); i++) {

            cast += this.Cast.get(i);
            if(i < this.Cast.size() - 1) 
                cast += ", ";
        }

        cast += "}";
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
    public void setDescricao(String descricao) { this.Descricao = descricao; }
    public void setCast(ArrayList<String> cast) { this.Cast = cast; }

    // Clone
    public Filme Clone(){
        return new Filme(this.Id, this.Tipo, this.Titulo, this.Diretor, this.Cast, 
        this.Pais, this.Data, this.Ano, this.Rating, this.Duracao, this.Descricao);
    }


    public static void LerFilmes() {

        // Initialize variables
        try {

            FileInputStream fstream = new FileInputStream(FILE_PATH);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            // ---------------------- //

            // Explode CSV file
            String linha = br.readLine();
  
            while((linha = br.readLine()) != null) {

                Filme filme = new Filme();
                filme.read(linha);
                todosFilmes.add(filme);
            }

            // Fecahr CSV
            fstream.close();
        }
        catch(IOException e) { e.printStackTrace(); }
    }

    public static void main(String[] args) {

        //Ler todos os filmes em CSV
        LerFilmes();
        Scanner sc = new Scanner(System.in);
        Filme filme = new Character();
        String linha = sc.nextLine();

        while(!linha.equals("FIM")) {

            // Get id
            UUID id = UUID.fromString(linha);

            // buscar filme
            filme = searchById(id, todosFilmes);

            // Printar filme
            if(filme != null) filme.print();
            else System.out.println("x filme não encontrado!");

            linha = sc.nextLine();
        }

        sc.close();
    }
}
