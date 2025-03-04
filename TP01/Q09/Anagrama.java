import java.util.*;

public class Anagrama {

    public static boolean Fim(String s){
        return s.length() == 3 && s.charAt(0) == 'F' && s.charAt(1) == 'I' && s.charAt(2) == 'M';
    }

    public static boolean Verificar(String s1, String s2){
        boolean resp = true;
        char c1;
        char c2;
        int[] freq1 = new int[Character.MAX_VALUE]; // Suporta Unicode completo
        int[] freq2 = new int[Character.MAX_VALUE];        

        if (s1.length() != s2.length()) { // Se os tamanhos forem diferentes, não é anagrama
            resp = false;
        }
        else{
            // Contar a frequência de cada caractere nas duas strings
            for (int i = 0; i < s1.length(); i++) {
                c1 = Character.toLowerCase(s1.charAt(i));
                c2 = Character.toLowerCase(s2.charAt(i));
                freq1[c1]++;
                freq2[c2]++;
            }

            // Comparar as frequências
            for (int i = 0; i < 256; i++) {
                if (freq1[i] != freq2[i]) resp = false;
            }
        }
        return resp;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String frase = "";
        String s1 = "";
        String s2 = "";
        boolean fim;
        boolean hifen;

        do{
            frase = sc.nextLine();
            fim = Fim(frase);
            if(!fim){
                s1 = ""; s2 = "";
                hifen = false;

                for (int i = 0; i < frase.length(); i++) {
                    char c = frase.charAt(i);

                    if (c == '-') {
                        hifen = true;
                    } else if (!hifen && c  != ' ') {
                        s1 += c;
                    } else if(c  != ' '){
                        s2 += c;
                    }
                }

                System.out.println(Verificar(s1, s2) ? "SIM" : "NÃO");
            }
        }while(!fim);
        sc.close();
    }
}