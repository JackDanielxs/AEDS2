import java.util.Scanner;

public class ContaPalavras {

    public static boolean Fim(String s){
        return s.length() == 3 && s.charAt(0) == 'F' && s.charAt(1) == 'I' && s.charAt(2) == 'M';
    }

    public static int Contar(String str) {
        int cont = 0;
        boolean dentro = false;
        char c;
        if (str == null || str.length() == 0) { // não existe palavra
            return cont;
        }
        
        for (int i = 0; i < str.length(); i++) { // percorrer tudo
            c = str.charAt(i); 
            if (c != ' ') { // se o caractere não for um espaço, significa que está numa palavra
                if (!dentro) { // verifica início de palavra
                    cont++;
                    dentro = true;
                }
            } else {
                dentro = false; // falso = espaço
            }
        }
        return cont;
    }
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String str = "";
        boolean fim;
        
        do{
            str = sc.nextLine();
            fim = Fim(str);
            if(!fim){
               System.out.println(Contar(str));
            }  
       }while(!fim);
        
        sc.close();
    }
}