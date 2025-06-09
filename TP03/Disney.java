import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Disney {

    private static final String FILE_PATH = "/tmp/disneyplus.csv";
    private static final String LOG_FILE = "800712_quicksort.txt";
    private static final String MATRICULA = "800712";

    private NodeDuplo primeiro, ultimo;
    private int tamanho;

    private static long comparacoes = 0;
    private static long movimentacoes = 0;
    private static long tempoInicio = 0;

    private class NodeDuplo {
        Show elemento;
        NodeDuplo ant, prox;

        NodeDuplo(Show elemento) {
            this.elemento = elemento;
        }
    }

    public Disney() {
        primeiro = new NodeDuplo(null);
        ultimo = primeiro;
        tamanho = 0;
    }

    public void inserirFim(Show show) {
        NodeDuplo novo = new NodeDuplo(show);
        ultimo.prox = novo;
        novo.ant = ultimo;
        ultimo = novo;
        tamanho++;
    }

    private NodeDuplo getNode(int index) {
        NodeDuplo atual = primeiro.prox;
        for (int i = 0; i < index; i++) {
            atual = atual.prox;
        }
        return atual;
    }

    private void swap(NodeDuplo a, NodeDuplo b) {
        Show temp = a.elemento;
        a.elemento = b.elemento;
        b.elemento = temp;
        movimentacoes += 3;
    }

    public void quicksort() {
        tempoInicio = System.currentTimeMillis();
        quicksort(0, tamanho - 1);
        long tempoTotal = System.currentTimeMillis() - tempoInicio;
        gerarLog(tempoTotal);
    }

    private void quicksort(int esq, int dir) {
        if (esq < dir) {
            int p = partition(esq, dir);
            quicksort(esq, p - 1);
            quicksort(p + 1, dir);
        }
    }

    private int partition(int esq, int dir) {
        NodeDuplo pivoNode = getNode(dir);
        Show pivo = pivoNode.elemento;
        int i = esq - 1;

        for (int j = esq; j < dir; j++) {
            NodeDuplo atual = getNode(j);
            int cmp = compare(atual.elemento, pivo);
            comparacoes++;
            if (cmp < 0) {
                i++;
                swap(getNode(i), atual);
            }
        }

        swap(getNode(i + 1), pivoNode);
        return i + 1;
    }

    private int compare(Show a, Show b) {
        int cmp = a.getDate_added().compareTo(b.getDate_added());
        return (cmp != 0) ? cmp : a.getTitle().compareTo(b.getTitle());
    }

    public void mostrar() {
        NodeDuplo atual = primeiro.prox;
        int index = 0;
        while (atual != null) {
            imprimirShow(atual.elemento, index++);
            atual = atual.prox;
        }
    }

    private void imprimirShow(Show show, int idx) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        System.out.print("[" + idx + "] => ");
        System.out.print(show.getShow_id() + " ## ");
        System.out.print(show.getTitle() + " ## ");
        System.out.print(show.getType() + " ## ");
        System.out.print(show.getDirector() + " ## ");
        System.out.print(Arrays.toString(show.getCast()) + " ## ");
        System.out.print(show.getCountry() + " ## ");
        System.out.print(sdf.format(show.getDate_added()) + " ## ");
        System.out.print(show.getRelease_year() + " ## ");
        System.out.print(show.getRating() + " ## ");
        System.out.print(show.getDuration() + " ## ");
        System.out.println(Arrays.toString(show.getListed_in()) + " ##");
    }

    private void gerarLog(long tempo) {
        try (FileWriter fw = new FileWriter(LOG_FILE)) {
            fw.write(MATRICULA + "\t" + comparacoes + "\t" + movimentacoes + "\t" + tempo + "\n");
        } catch (IOException e) {
            System.err.println("Erro ao gerar log: " + e.getMessage());
        }
    }

    public class Show {
        private String show_id, type, title, director, country, rating, duration;
        private String[] cast, listed_in;
        private Date date_added;
        private int release_year;

        public Show() {}

        public Show(String show_id, String type, String title, String director, String[] cast,
                    String country, Date date_added, int release_year,
                    String rating, String duration, String[] listed_in) {
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

        public Show clone() {
            return new Show(
                show_id, type, title, director,
                cast != null ? cast.clone() : null,
                country,
                date_added != null ? new Date(date_added.getTime()) : null,
                release_year, rating, duration,
                listed_in != null ? listed_in.clone() : null
            );
        }

        public String getShow_id() { return show_id; }
        public String getType() { return type; }
        public String getTitle() { return title; }
        public String getDirector() { return director; }
        public String[] getCast() { return cast; }
        public String getCountry() { return country; }
        public Date getDate_added() { return date_added; }
        public int getRelease_year() { return release_year; }
        public String getRating() { return rating; }
        public String getDuration() { return duration; }
        public String[] getListed_in() { return listed_in; }
    }

    public static Map<String, Show> readAllShows() {
        Map<String, Show> shows = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            br.readLine(); // Ignora cabeçalho
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] campos = parseCSVLine(linha);
                String show_id = campos[0].isEmpty() ? "NaN" : campos[0];
                String type = campos[1].isEmpty() ? "NaN" : campos[1];
                String title = campos[2].isEmpty() ? "NaN" : campos[2];
                String director = campos[3].isEmpty() ? "NaN" : campos[3];
                String[] cast = campos[4].isEmpty() ? new String[]{"NaN"} : campos[4].split(", ");
                Arrays.sort(cast);
                String country = campos[5].isEmpty() ? "NaN" : campos[5];

                Date date_added;
                try {
                    String dateStr = campos[6].trim();
                    SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
                    date_added = dateStr.isEmpty() ? sdf.parse("March 1, 1900") : sdf.parse(dateStr);
                } catch (Exception e) {
                    date_added = new Date();
                }

                int release_year = campos[7].isEmpty() ? 0 : Integer.parseInt(campos[7]);
                String rating = campos[8].isEmpty() ? "NaN" : campos[8];
                String duration = campos[9].isEmpty() ? "NaN" : campos[9];
                String[] listed_in = campos[10].isEmpty() ? new String[]{"NaN"} : campos[10].split(", ");

                Show show = new Disney().new Show(show_id, type, title, director, cast, country,
                                                  date_added, release_year, rating, duration, listed_in);
                shows.put(show_id, show);
            }
        } catch (Exception ignored) {}
        return shows;
    }

    public static String[] parseCSVLine(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == '\"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                tokens.add(current.toString().replaceAll("^\"|\"$", ""));
                current.setLength(0);
            } else {
                current.append(c);
            }
        }

        tokens.add(current.toString().replaceAll("^\"|\"$", ""));
        return tokens.toArray(new String[0]);
    }

    public static void main(String[] args) {
        Map<String, Show> allShows = readAllShows();
        Disney lista = new Disney();
        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            if (input.equals("FIM")) break;
            Show show = allShows.get(input);
            if (show != null) lista.inserirFim(show.clone());
        }

        if (sc.hasNextLine()) {
            String qtdStr = sc.nextLine();
            if (!qtdStr.isEmpty()) {
                int qtd = Integer.parseInt(qtdStr);
                for (int i = 0; i < qtd && sc.hasNextLine(); i++) {
                    String linha = sc.nextLine();
                    String[] partes = linha.split(" ");
                    if ("I".equals(partes[0])) {
                        Show show = allShows.get(partes[1]);
                        if (show != null) lista.inserirFim(show.clone());
                    }
                }
            }
        }

        lista.quicksort();
        lista.mostrar();
        sc.close();
    }
}