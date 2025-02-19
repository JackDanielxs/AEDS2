import java.util.*;

class Aleatorio {

    public static boolean Fim(String s){
        return s.length() == 3 && s.charAt(0) == 'F' && s.charAt(1) == 'I' && s.charAt(2) == 'M';
    }

    public static String Trocar(String s, char c1, char c2){
        char[] arr = new char[s.length()]; //array de char para manipulação, já que a string é imutável
        for(int i = 0; i < s.length(); i++){
            if(s.charAt(i) == c1)
                arr[i] = c2;
            else
                arr[i] = s.charAt(i);
        }
        return new String(arr); // retorna array de char em formato de string
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        Random gerador = new Random();
        gerador.setSeed(4);
        String str = "";
        boolean fim;
        char c1, c2;

        do{
             str = sc.nextLine();
             fim = Fim(str);
             if(!fim){
                c1 = (char) ('a' + (Math.abs(gerador.nextInt())) % 26); //caracter aleatorio 1
                c2 = (char) ('a' + (Math.abs(gerador.nextInt())) % 26); //caracter aleatorio 2
                System.out.println(Trocar(str, c1, c2));
             }  
        }while(!fim);

        sc.close();
    }
}
