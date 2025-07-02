import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

class Show {
    private String show_id;
    private String type;
    private String title;
    private String director;
    private String[] cast;
    private String country;
    private Date date_added;
    private int release_year;
    private String rating;
    private String duration;
    private String[] listed_in;

    public Show() {
        this.show_id = "";
        this.type = "";
        this.title = "";
        this.director = "";
        this.cast = new String[0];
        this.country = "";
        this.date_added = new Date();
        this.release_year = 0;
        this.rating = "";
        this.duration = "";
        this.listed_in = new String[0];
    }

    public Show(String show_id, String type, String title, String director, String[] cast, String country,
                Date date_added, int release_year, String rating, String duration, String[] listed_in) {
        this.show_id = show_id;
        this.type = type;
        this.title = title;
        this.director = director;
        this.cast = cast;
        this.country = country;
        this.date_added = date_added;
        this.release_year = release_year;
        this.rating = rating;
        this.duration = duration;
        this.listed_in = listed_in;
    }

    public String getShowId() {
        return show_id;
    }

    public void setShowId(String show_id) {
        this.show_id = show_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String[] getCast() {
        return cast;
    }

    public void setCast(String[] cast) {
        this.cast = cast;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Date getDateAdded() {
        return this.date_added; 
    }

    public void setDateAdded(Date date_added) {
        this.date_added = date_added;
    }

    public int getReleaseYear() {
        return release_year;
    }

    public void setReleaseYear(int release_year) {
        this.release_year = release_year;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String[] getListedIn() {
        return listed_in;
    }

    public void setListedIn(String[] listed_in) {
        this.listed_in = listed_in;
    }

    public void ler(String linhaCsv) {
        String[] lista = new String[11];
        int i = 0, j = 0;
    
        while(j < 11) {
            String campo = "";
            
            if(i < linhaCsv.length() && linhaCsv.charAt(i) == '"') {
                i++;
                
                while(i < linhaCsv.length()) {
                    if(linhaCsv.charAt(i) == '"') {
                        if(i + 1 < linhaCsv.length() && linhaCsv.charAt(i + 1) == '"') {
                            i += 2;
                        }
                        else {
                            i++;
                            break;
                        }
                    }
                    else {
                        campo += linhaCsv.charAt(i);
                        i++;
                    }
                }
                if(i < linhaCsv.length() && linhaCsv.charAt(i) == ',') i++;
            }
            else {
                while(i < linhaCsv.length() && linhaCsv.charAt(i) != ',') {
                    campo += linhaCsv.charAt(i);
                    i++;
                }
                if(i < linhaCsv.length() && linhaCsv.charAt(i) == ',') i++;
            }
    
            if(campo.isEmpty()) campo = "NaN";
            
            lista[j] = campo;
            j++;
        }
        
        this.show_id = lista[0];
        this.type = lista[1];
        this.title = lista[2];
        this.director = lista[3];
        this.cast = lista[4].equals("NaN") ? new String[] {"NaN"} : lista[4].split(", ");
        this.country = lista[5];
        this.date_added = safeParseDate(lista[6]);
        this.release_year = safeParseInt(lista[7]);
        this.rating = lista[8];
        this.duration = lista[9];
        this.listed_in = lista[10].equals("NaN") ? new String[] {"NaN"} : lista[10].split(", ");
    }

    private Date safeParseDate(String valor) {
        try {
            if(valor.equals("NaN")) return null;

            SimpleDateFormat formato = new SimpleDateFormat("MMMM d,yyyy", Locale.ENGLISH);
            return formato.parse(valor.trim());
        }
        catch(Exception e) {
            return null;
        }
    }
    
    private int safeParseInt(String valor) {
        try {
            return Integer.parseInt(valor.trim());
        }
        catch(Exception e) {
            return 0;
        }
    }
    
    public Show clone() {
        return new Show(
            show_id,
            type,
            title,
            director,
            cast.clone(),
            country,
            (date_added != null) ? new Date(date_added.getTime()) : null,
            release_year,
            rating,
            duration,
            listed_in.clone()
        );
    }
}

// Nó para as árvores secundárias (armazena objetos Show, organizados por título)
class NoArvoreSecundaria {
    public Show elemento;
    public NoArvoreSecundaria esq, dir;

    public NoArvoreSecundaria(Show elemento) {
        this(elemento, null, null);
    }

    public NoArvoreSecundaria(Show elemento, NoArvoreSecundaria esq, NoArvoreSecundaria dir) {
        this.elemento = elemento;
        this.esq = esq;
        this.dir = dir;
    }
}

// Árvore Secundária: Gerencia objetos Show, com chave sendo o título
class ArvoreSecundaria {
    private NoArvoreSecundaria raiz;
    public int comparacoes; // Comparações específicas desta árvore secundária
    public StringBuilder path; // Para construir o caminho interno da secundária

    public ArvoreSecundaria() {
        this.raiz = null;
        this.comparacoes = 0;
        this.path = new StringBuilder(); // Inicializa o StringBuilder
    }

    public void inserir(Show s) {
        this.raiz = inserir(s, this.raiz);
    }

    private NoArvoreSecundaria inserir(Show s, NoArvoreSecundaria i) {
        if (i == null) {
            i = new NoArvoreSecundaria(s);
        }
        else if (s.getTitle().compareTo(i.elemento.getTitle()) < 0) {
            i.esq = inserir(s, i.esq);
        }
        else if (s.getTitle().compareTo(i.elemento.getTitle()) > 0) {
            i.dir = inserir(s, i.dir);
        }
        return i;
    }

    // Este método retorna um booleano e constrói o caminho no StringBuilder
    public boolean pesquisar(String titulo) {
        this.path.setLength(0); // Limpa o path para cada nova pesquisa
        return pesquisarRec(titulo, this.raiz);
    }

    private boolean pesquisarRec(String titulo, NoArvoreSecundaria i) {
        if (i == null) {
            comparacoes++; 
            return false;
        }

        comparacoes++; 
        if (titulo.equals(i.elemento.getTitle())) {
            return true;
        }

        comparacoes++; 
        if (titulo.compareTo(i.elemento.getTitle()) < 0) {
            path.append("esq "); // Adiciona ao caminho
            return pesquisarRec(titulo, i.esq);
        }
        else {
            path.append("dir "); // Adiciona ao caminho
            return pesquisarRec(titulo, i.dir);
        }
    }
}

// Wrapper para um booleano mutável, usado para passar o estado de 'encontrado' pela recursão
class BooleanWrapper {
    public boolean value;

    public BooleanWrapper(boolean value) {
        this.value = value;
    }
}

// Nó para a árvore primária (armazena uma chave inteira e uma ArvoreSecundaria)
class NoArvorePrimaria {
    public int chave;
    public ArvoreSecundaria arvoreSecundaria;
    public NoArvorePrimaria esq, dir;

    public NoArvorePrimaria(int chave) {
        this(chave, null, null);
    }

    public NoArvorePrimaria(int chave, NoArvorePrimaria esq, NoArvorePrimaria dir) {
        this.chave = chave;
        this.arvoreSecundaria = new ArvoreSecundaria(); // Cada nó primário tem sua própria árvore secundária
        this.esq = esq;
        this.dir = dir;
    }
}

// Árvore Principal: Gerencia os Nós da Árvore Primária, com chave sendo (releaseYear % 15)
class ArvorePrincipal {
    private NoArvorePrimaria raiz;
    public long comparacoesTotais; // Comparações totais em todas as árvores

    public ArvorePrincipal() {
        this.raiz = null;
        this.comparacoesTotais = 0;
    }

    // Método para inicializar a árvore primária com chaves específicas
    public void criarArvorePrimaria(int[] chaves) {
        for (int chave : chaves) {
            this.raiz = criarArvorePrimariaRec(chave, this.raiz);
        }
    }

    private NoArvorePrimaria criarArvorePrimariaRec(int chave, NoArvorePrimaria i) {
        if (i == null) {
            i = new NoArvorePrimaria(chave);
        } else if (chave < i.chave) {
            i.esq = criarArvorePrimariaRec(chave, i.esq);
        } else if (chave > i.chave) {
            i.dir = criarArvorePrimariaRec(chave, i.dir);
        }
        return i;
    }

    public void inserirShow(Show s) {
        int chave = s.getReleaseYear() % 15;
        NoArvorePrimaria no = encontrarNoPrimario(chave, this.raiz);
        if (no != null) {
            no.arvoreSecundaria.inserir(s);
        } else {
            return;
        }
    }

    private NoArvorePrimaria encontrarNoPrimario(int chave, NoArvorePrimaria i) {
        if (i == null) {
            return null;
        }
        if (chave == i.chave) {
            return i;
        }
        if (chave < i.chave) {
            return encontrarNoPrimario(chave, i.esq);
        } else {
            return encontrarNoPrimario(chave, i.dir);
        }
    }

    public void pesquisarShow(String tituloParaPesquisar) {
        StringBuilder fullPath = new StringBuilder("raiz ");
        BooleanWrapper foundOverall = new BooleanWrapper(false); 
        
        // A chamada recursiva agora retorna um boolean que indica se a impressão DEVE CONTINUAR
        // (true = continue, false = parar impressão de ESQ/DIR)
        pesquisarShowRec(tituloParaPesquisar, this.raiz, fullPath, foundOverall);
        
        fullPath.append(foundOverall.value ? " SIM" : " NAO"); 
        System.out.println(fullPath.toString()); 
    }

    private boolean pesquisarShowRec(String tituloParaPesquisar, NoArvorePrimaria i, StringBuilder currentPath, BooleanWrapper foundOverall) {
        // Caso base: Se o nó é nulo, não há mais caminhos de árvore primária a imprimir *a partir daqui*.
        if (i == null) {
            return true; // Indica que a recursão para o pai deve continuar, mas este ramo termina.
        }

        comparacoesTotais++; 

        // 1. Processa o nó primário atual: pesquisa na árvore secundária
        i.arvoreSecundaria.path.setLength(0); 
        boolean encontradoNestaSecundaria = i.arvoreSecundaria.pesquisar(tituloParaPesquisar);
        
        currentPath.append(i.arvoreSecundaria.path.toString()); 
        
        comparacoesTotais += i.arvoreSecundaria.comparacoes;
        i.arvoreSecundaria.comparacoes = 0; 

        if (encontradoNestaSecundaria) {
            foundOverall.value = true; // Atualiza a flag global
            return false;
        }
        
        boolean continuePrintingLeft = true;
        boolean continuePrintingRight = true;

        // Chamada para o filho esquerdo
        currentPath.append(" ESQ ");
        if (i.esq != null) {
            continuePrintingLeft = pesquisarShowRec(tituloParaPesquisar, i.esq, currentPath, foundOverall);
        }
        else {
            pesquisarShowRec(tituloParaPesquisar, null, currentPath, foundOverall); // Apenas para simular a chamada recursiva
            continuePrintingLeft = true; // O ramo 'null' não impede a continuidade da impressão do irmão
        }

        if (continuePrintingLeft) { // Só entra aqui se o ramo ESQ não cortou a linha
            currentPath.append(" DIR ");
            if (i.dir != null) {
                continuePrintingRight = pesquisarShowRec(tituloParaPesquisar, i.dir, currentPath, foundOverall);
            }
            else {
                pesquisarShowRec(tituloParaPesquisar, null, currentPath, foundOverall);
                continuePrintingRight = true;
            }
        }
        
        return continuePrintingLeft && continuePrintingRight; 
    }
}

public class Q02 {
    public static boolean fim(String str) {
        return str.equals("FIM");
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        String csvPath = "/tmp/disneyplus.csv"; 
        
        Show[] todosOsShows = new Show[1368];
        int totalShows = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            br.readLine(); 
            String linha;
            while ((linha = br.readLine()) != null && totalShows < todosOsShows.length) {
                Show s = new Show();
                s.ler(linha);
                todosOsShows[totalShows++] = s;
            }
        }

        ArvorePrincipal arvorePrincipal = new ArvorePrincipal();

        int[] chavesArvorePrimaria = {7, 3, 11, 1, 5, 9, 13, 0, 2, 4, 6, 8, 10, 12, 14};
        arvorePrincipal.criarArvorePrimaria(chavesArvorePrimaria);

        String entradaId = sc.nextLine();
        while (!fim(entradaId)) {
            boolean encontrado = false;
            for (int i = 0; i < totalShows && !encontrado; i++) {
                if (todosOsShows[i].getShowId().equals(entradaId)) {
                    arvorePrincipal.inserirShow(todosOsShows[i]);
                    encontrado = true;
                }
            }
            entradaId = sc.nextLine();
        }

        long inicio = System.nanoTime();
        String entradaTitulo = sc.nextLine();
        while (!fim(entradaTitulo)) {
            arvorePrincipal.pesquisarShow(entradaTitulo); 
            entradaTitulo = sc.nextLine();
        }

        long fimTempo = System.nanoTime();
        double tempoExecucao = (fimTempo - inicio) / 1e6;

        try (FileWriter log = new FileWriter("matricula_arvoreArvore.txt")) {
            log.write("800712\t" + tempoExecucao + "\t" + arvorePrincipal.comparacoesTotais);
        }
        catch (IOException e) {}

        sc.close();
    }
}