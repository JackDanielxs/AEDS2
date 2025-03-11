import java.io.*;
import java.util.*;
import java.net.*;

public class HTML {

    public static boolean Fim(String s){
        return s.length() == 3 && s.charAt(0) == 'F' && s.charAt(1) == 'I' && s.charAt(2) == 'M';
    }

    public static void GravarPaginaArquivo(URL url){
        BufferedReader ler = new BufferedReader(new InputStreamReader(url.openStream()));
        String linha = "";

        try (RandomAccessFile abrir = new RandomAccessFile("pagina.txt", "rw")) {
            while(linha = ler.readLine() != null){
                abrir.writeChars(linha);
            }
        } catch(IOException e) {}   
        ler.close();     
    }

    public static int Contar(char c){
        int resp = 0;
        char agr;

        try (RandomAccessFile ler = new RandomAccessFile("pagina.txt", "r")) {
            long tamanho = ler.length();
            long pos = tamanho - 1; // char ocupa 1 byte
            while(pos >= 0){
                ler.seek(pos);
                agr = Character.toLowerCase(ler.readChar());
                if(agr == c)
                resp += 1;
                pos -= 1; // ir de char em char
            }
        } catch(IOException e) {}

        return resp;
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        String s1 = "";
        String result = "";
        boolean fim;

        do{
            s1 = sc.nextLine();
            fim = Fim(s1);
            if(!fim){
                try{
                    URI uri = new URI(sc.nextLine());
                    URL url = uri.toURL();
                    GravarPaginaArquivo(url);
                }catch(URISyntaxException e){}
                catch(MalformedURLException e){}
                
                result = "a(" + Contar('a') + ") e(" + Contar('e') + ") i(" + Contar('i') + ") o(" + Contar('o') + ") u(" + Contar('u') + ")";
                System.out.println(result);
            }
        }while(!fim);
        sc.close();
 
    }
}
