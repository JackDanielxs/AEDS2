package Q12;
import java.util.*;

public class senhaValida {

    public static boolean validacao(String s){
        boolean resp = false, mai = false, min = false, num = false, esp = false;
        char c;
        for(int i = 0; i < s.length(); i++){
            c = s.charAt(i);
            if(c >= 48 && c <= 57)
                num = true;
            else if(c >= 65 && c <= 90)
                mai = true;
            else if(c >= 97 && c <= 122)
                min = true;
            else
                esp = true;
        }
        
        if(s.length() >= 8 && mai && min && num && esp)
            resp = true;
        return resp;
    }
    public static void main(){
        Scanner sc = new Scanner(System.in);
        String senha = sc.nextLine();
        System.out.println(validacao(senha) ? "SIM" : "NÃO");
        sc.close();
    }
}
