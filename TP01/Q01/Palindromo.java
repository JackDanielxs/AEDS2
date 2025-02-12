import java.util.Scanner;
class Palindromo {

    public static boolean IsPalindromo(String s, int esq, int dir){
        boolean resp;
        if (esq >= dir) 
            resp = true;
        else if (s.charAt(esq) != s.charAt(dir)) 
            resp = false;
        else
            resp = IsPalindromo(s, esq + 1, dir - 1);

        return resp;
    } 

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        String palavra = "";

        while(sc.hasNext()){
            palavra = sc.nextLine();
            System.out.println(IsPalindromo(palavra, 0, palavra.length() - 1) ? "SIM" : "NAO");
        }

        sc.close();
    }
}