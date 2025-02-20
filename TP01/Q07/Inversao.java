package Q07;
import java.util.Scanner;

class Inversao{

    public static boolean Fim(String s){
        return s.length() == 3 && s.charAt(0) == 'F' && s.charAt(1) == 'I' && s.charAt(2) == 'M';
    }
    
    public static String Inverter(String s, int pos, char[] arr){
        if(pos == s.length())
            return new String(arr); // retorna array de char em formato de string
        arr[pos] = s.charAt(s.length() - (pos + 1));
        return Inverter(s, pos + 1, arr);
    }

    public static void main(){
        Scanner sc = new Scanner(System.in);
        String palavra = "";

        do{
            palavra = sc.nextLine();
            if (!Fim(palavra)){
                char[] arr = new char[palavra.length()]; // array de char para manipulação, já que a string é imutável
                System.out.println(Inverter(palavra, 0, arr));
            }
                
        }while(!Fim(palavra));
        sc.close();
    }
}