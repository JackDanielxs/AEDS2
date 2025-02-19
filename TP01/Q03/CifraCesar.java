import java.util.*;

class CifraCesar{

    public static boolean Fim(String s){
        return s.length() == 3 && s.charAt(0) == 'F' && s.charAt(1) == 'I' && s.charAt(2) == 'M';
    }

    public static String Cripto(String s, int pos){
        return pos == s.length() ? "" : ((s.charAt(pos) >= 32 && s.charAt(pos) <= 126) ? (char)(s.charAt(pos) + 3) : s.charAt(pos)) + Cripto(s, pos + 1); // caso esteja no intervalo válido, faz o cálculo de avanço, senão deixa o caracter como está
    }
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        boolean fim;
        do{
            String str = sc.nextLine();
            fim = Fim(str); 
            if(!fim) //ignora a ultima linha (FIM)
                System.out.println(Cripto(str, 0));
        }while(!fim);
        sc.close();
    }
}