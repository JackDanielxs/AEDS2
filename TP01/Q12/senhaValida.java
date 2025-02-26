import java.util.*;

public class senhaValida {

    public static boolean Fim(String s){
        return s.length() == 3 && s.charAt(0) == 'F' && s.charAt(1) == 'I' && s.charAt(2) == 'M';
    }

    public static void validacao(String s){
        boolean mai = false, min = false, num = false, esp = false;
        char c;
        for(int i = 0; i < s.length(); i++){
            c = s.charAt(i);
            if(c >= 48 && c <= 57){
                num = true;
            }
            else if(c >= 65 && c <= 90){
                mai = true;
            }
            else if(c >= 97 && c <= 122){
                min = true;
            }
            else if((c >= 33 && c <= 47) || (c >= 58 && c <= 64) || (c >= 91 && c <= 96) || (c >= 123 && c <= 126)){
                esp = true;
            }
        }
        
        if(s.length() > 8 && mai && min && num && esp)
            System.out.println("SIM");
        else    
            System.out.println("NÃO");
    }
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        String senha = "";
        boolean fim;
        do{
            senha = sc.nextLine();
            fim = Fim(senha);
            if(!fim)               
                validacao(senha);
        }while(!fim);
        
        sc.close();
    }
}
