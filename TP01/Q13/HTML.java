package Q13;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTML {

    public static boolean Fim(String s){
        return s.length() == 3 && s.charAt(0) == 'F' && s.charAt(1) == 'I' && s.charAt(2) == 'M';
    }

    public static int Contar(String s, char c){
        int cont = 0;
        for(int i = 0; i < s.length(); i++){
            if(Character.toLowerCase(s.charAt(i)) == c)
                cont += 1;
        }
        return cont;
    }

    public static int contarTag(String html, String tag) {
        String regex = "<\\s*" + tag + "\\s*[^>]*>"; // Regex flexível para capturar a tag com ou sem atributos
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(html);

        int contador = 0;
        while (matcher.find()) {
            contador++;
        }
        return contador;
    }

    public static int contarConsoantes(String texto) {
        int contador = 0;
        texto = texto.toLowerCase(); // Converter para minúsculas para facilitar a verificação

        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);

            // Verifica se é uma letra e não uma vogal
            if (c >= 'a' && c <= 'z' && c != 'a' && c != 'e' && c != 'i' && c != 'o' && c != 'u')
                contador++;
        }
        return contador;
    }
    
    public static String baixarPagina(String endereco) {
        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endereco))
                    .GET()
                    .build();
    
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()); // Baixar HTML da página pela url
            return response.body();
        }catch(Exception e){
            return "";
        }
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        boolean fim;
        String nome = "", link = "", html = "", resultado = "";
        do{
            nome = sc.nextLine();
            fim = Fim(nome);
            if(!fim){
                try{
                    link = sc.nextLine();
                    html = baixarPagina(link);
                    resultado = "a(" + Contar(html, 'a') + ") ";
                    resultado += "e(" + Contar(html, 'e') + ") ";
                    resultado += "i(" + Contar(html, 'i') + ") ";
                    resultado += "o(" + Contar(html, 'o') + ") ";
                    resultado += "u(" + Contar(html, 'u') + ") ";
                    resultado += "á(" + Contar(html, 'á') + ") ";
                    resultado += "é(" + Contar(html, 'é') + ") ";
                    resultado += "í(" + Contar(html, 'í') + ") ";
                    resultado += "ó(" + Contar(html, 'ó') + ") ";
                    resultado += "ú(" + Contar(html, 'ú') + ") ";
                    resultado += "à(" + Contar(html, 'à') + ") ";
                    resultado += "è(" + Contar(html, 'è') + ") ";
                    resultado += "ì(" + Contar(html, 'ì') + ") ";
                    resultado += "ò(" + Contar(html, 'ò') + ") ";
                    resultado += "ù(" + Contar(html, 'ù') + ") ";
                    resultado += "ã(" + Contar(html, 'ã') + ") ";
                    resultado += "õ(" + Contar(html, 'õ') + ") ";
                    resultado += "â(" + Contar(html, 'â') + ") ";
                    resultado += "ê(" + Contar(html, 'ê') + ") ";
                    resultado += "î(" + Contar(html, 'î') + ") ";
                    resultado += "ô(" + Contar(html, 'ô') + ") ";
                    resultado += "û(" + Contar(html, 'û') + ") ";
                    resultado += "consoante(" + contarConsoantes(html) + ") ";
                    resultado += "<br>(" + contarTag(html, "br") + ") ";
                    resultado += "<table>(" + contarTag(html, "table") + ") ";
                    resultado += nome; // Concatena tudo
                    MyIO.println(resultado);
                    //System.out.println(resultado);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }while(!fim);
        sc.close();
    }
}