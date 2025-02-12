import java.util.Scanner;
class Palindromo {

    public static boolean Fim(String s){
        return s.length() == 3 && s.charAt(0) == 'F' && s.charAt(1) == 'I' && s.charAt(2) == 'M';
    }

    public static String IsPalindromo(String s, int esq, int dir){
        if (esq >= dir) 
            return "SIM";
        if (s.charAt(esq) != s.charAt(dir)) 
            return "NAO";

        return IsPalindromo(s, esq + 1, dir - 1);
    } 

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        String palavra = "";

        do{
            palavra = scanner.nextLine();
            if (!Fim(palavra))
                System.out.println(IsPalindromo(palavra, 0, palavra.length() - 1));
        }while(!Fim(palavra));

        scanner.close();
    }
}
