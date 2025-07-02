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

    public String getId() { return this.id; }
    public String getTitulo() { return this.titulo; }
    public String getTipo() { return this.tipo; }
    public String getDiretor() { return this.diretor; } 
    public String getPais() { return this.pais; }
    public Date getData() { return this.data; }
    public int getAno() { return this.ano; }
    public String getRating() { return this.rating; }
    public String getDuracao() { return this.duracao; }
    public ArrayList<String> getListado() { return this.listado; }
    public ArrayList<String> getCast() { return this.cast; }

    public void setId(String id) { this.id = id; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setDiretor(String diretor) { this.diretor = diretor; }
    public void setPais(String pais) { this.pais = pais; }
    public void setData(Date data) { this.data = data; }
    public void setAno(int ano) { this.ano = ano; }
    public void setRating(String rating) { this.rating = rating; }
    public void setDuracao(String duracao) { this.duracao = duracao; }
    public void setListado(ArrayList<String> listado) { this.listado = listado; }
    public void setCast(ArrayList<String> cast) { this.cast = cast; }

    public void read(String linhaCsv) {
        String[] lista = new String[11];
        int i = 0, j = 0;

        while (j < 11) {
            String campo = "";
            if (i < linhaCsv.length() && linhaCsv.charAt(i) == '"') {
                i++;
                while (i < linhaCsv.length()) {
                    if (linhaCsv.charAt(i) == '"') {
                        if (i + 1 < linhaCsv.length() && linhaCsv.charAt(i + 1) == '"') {
                            campo += '"';
                            i += 2;
                        } else {
                            i++;
                            break;
                        }
                    } else {
                        campo += linhaCsv.charAt(i);
                        i++;
                    }
                }
                if (i < linhaCsv.length() && linhaCsv.charAt(i) == ',') i++;
            } else {
                while (i < linhaCsv.length() && linhaCsv.charAt(i) != ',') {
                    campo += linhaCsv.charAt(i);
                    i++;
                }
                if (i < linhaCsv.length() && linhaCsv.charAt(i) == ',') i++;
            }

            if (campo.isEmpty()) campo = "NaN";
            lista[j] = campo;
            j++;
        }

        this.id = lista[0];
        this.tipo = lista[1];
        this.titulo = lista[2];
        this.diretor = lista[3];
        this.cast = lista[4].equals("NaN") ? new ArrayList<>() : new ArrayList<>(Arrays.asList(lista[4].split(", ")));
        this.pais = lista[5];
        this.data = safeParseDate(lista[6]);
        this.ano = safeParseInt(lista[7]);
        this.rating = lista[8];
        this.duracao = lista[9];
        this.listado = lista[10].equals("NaN") ? new ArrayList<>() : new ArrayList<>(Arrays.asList(lista[10].split(", ")));
    }

    private Date safeParseDate(String valor) {
        try {
            if (valor.equals("NaN")) return null;
            SimpleDateFormat formato = new SimpleDateFormat("MMMM d,yyyy", Locale.ENGLISH);
            return formato.parse(valor.trim());
        } catch (Exception e) {
            return null;
        }
    }

    private int safeParseInt(String valor) {
        try {
            return Integer.parseInt(valor.trim());
        } catch (Exception e) {
            return 0;
        }
    }
    
    public void lerTodosFilmes() {
        try (FileInputStream fstream = new FileInputStream(FILE_PATH);
             BufferedReader br = new BufferedReader(new InputStreamReader(fstream))) {

            br.readLine();
            String linha;
            while ((linha = br.readLine()) != null) {
                Show show = new Show();
                show.read(linha);
                todosFilmes.add(show);
            }
        } catch (IOException e) {
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
        try (BufferedWriter esc = new BufferedWriter(new FileWriter("800712_avinegra.txt"))) {
            esc.write("800712\t" + tempo + "\t" + comparacoes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class NoRN {
    public Show elemento;
    public NoRN esq, dir;
    public boolean cor;

    public NoRN(Show elemento) {
        this(elemento, false, null, null);
    }

    public NoRN(Show elemento, boolean cor, NoRN esq, NoRN dir) {
        this.elemento = elemento;
        this.cor = cor;
        this.esq = esq;
        this.dir = dir;
    }
}

class ArvoreRN {
    private NoRN raiz;
    private int comparacoes;

    public ArvoreRN() {
        this.raiz = null;
        this.comparacoes = 0;
    }

    public int getComparacoes() {
        return this.comparacoes;
    }

    private boolean isNoVermelho(NoRN no) {
        return (no != null && !no.cor);
    }

    private NoRN rotacionarEsquerda(NoRN no) {
        NoRN noDir = no.dir;
        no.dir = noDir.esq;
        noDir.esq = no;
        noDir.cor = no.cor;
        no.cor = false;
        return noDir;
    }

    private NoRN rotacionarDireita(NoRN no) {
        NoRN noEsq = no.esq;
        no.esq = noEsq.dir;
        noEsq.dir = no;
        noEsq.cor = no.cor;
        no.cor = false;
        return noEsq;
    }

    private void inverterCores(NoRN no) {
        no.cor = !no.cor;
        no.esq.cor = !no.esq.cor;
        no.dir.cor = !no.dir.cor;
    }

    public void inserir(Show show) {
        this.raiz = inserir(show, this.raiz);
        this.raiz.cor = true;
    }

    private NoRN inserir(Show show, NoRN no) {
        if (no == null) {
            no = new NoRN(show);
        } else if (show.getTitulo().compareTo(no.elemento.getTitulo()) < 0) {
            no.esq = inserir(show, no.esq);
        } else if (show.getTitulo().compareTo(no.elemento.getTitulo()) > 0) {
            no.dir = inserir(show, no.dir);
        } else {}

        if (isNoVermelho(no.dir) && !isNoVermelho(no.esq)) {
            no = rotacionarEsquerda(no);
        }
        if (isNoVermelho(no.esq) && isNoVermelho(no.esq.esq)) {
            no = rotacionarDireita(no);
        }
        if (isNoVermelho(no.esq) && isNoVermelho(no.dir)) {
            inverterCores(no);
        }

        return no;
    }

    public void pesquisar(String titulo) {
        System.out.print("=>raiz ");
        boolean encontrado = pesquisarRec(titulo, this.raiz);
        System.out.println(encontrado ? " SIM" : " NAO");
    }

    private boolean pesquisarRec(String titulo, NoRN no) {
        if (no == null) {
            return false;
        }

        this.comparacoes++;
        if (titulo.equals(no.elemento.getTitulo())) {
            return true;
        }

        this.comparacoes++;
        if (titulo.compareTo(no.elemento.getTitulo()) < 0) {
            System.out.print(" esq");
            return pesquisarRec(titulo, no.esq);
        } else {
            System.out.print(" dir");
            return pesquisarRec(titulo, no.dir);
        }
    }
}

public class Q04 {
    public static void main(String[] args) {
        long inicio = System.currentTimeMillis();
        
        Show baseShow = new Show();
        baseShow.lerTodosFilmes();

        ArvoreRN arvore = new ArvoreRN();
        Scanner sc = new Scanner(System.in);

        String linha = sc.nextLine();
        while (!linha.equals("FIM")) {
            Show showParaInserir = Show.getById(linha, Show.todosFilmes);
            if (showParaInserir != null) {
                arvore.inserir(showParaInserir);
            }
            linha = sc.nextLine();
        }

        linha = sc.nextLine();
        while (!linha.equals("FIM")) {
            String tituloParaBuscar = linha;
            arvore.pesquisar(tituloParaBuscar);
            linha = sc.nextLine();
        }

        sc.close();
        long fim = System.currentTimeMillis();

        Show.log(fim - inicio, arvore.getComparacoes());
    }
}