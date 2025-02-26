import java.util.*;

public class Ls{

    public static boolean Fim(String s){
        return s.length() == 3 && s.charAt(0) == 'F' && s.charAt(1) == 'I' && s.charAt(2) == 'M';
    }

    public static Boolean Vogais(String s, int pos, Boolean resp){
        if(!resp)
            return resp;
        else if(pos >= s.length())
            return true;

        char c = s.charAt(pos);
        if(c != 'A' && c != 'E' && c != 'I' && c != 'O' && c != 'U'){
            resp = false;
        }

        return Vogais(s, pos + 1, resp);
    }

    public static Boolean Consoantes(String s, int pos, Boolean resp){
        if(!resp)
            return resp;
        else if(pos >= s.length())
            return true;

        char c = s.charAt(pos);
        if((!(c >= 65 && c <= 90) && !(c >= 97 && c <= 122)) || c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U'){
            resp = false;
        }

        return Consoantes(s, pos + 1, resp);
    }

    public static Boolean Inteiro(String s, int pos, Boolean resp){
        if(!resp)
            return resp;
        else if(pos >= s.length())
            return true;

        char c = s.charAt(pos);
        if(!(c >= 48 && c <= 57)){
            resp = false;
        }
        return Inteiro(s, pos + 1, resp);
    }

    public static Boolean Real(String s, int pos, Boolean resp, int cont){
        if(!resp)
            return resp;
        else if(pos >= s.length()){
            return cont < 2;
        } 

        char c = s.charAt(pos);
        if(c == ',' || c == '.'){
            cont += 1;
        }
        else if(!(c >= 48 && c <= 57)){
            resp = false;
        }

        return Real(s, pos + 1, resp, cont);
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        String palavra = "";
        Boolean fim = false;
        String out = "";

        do{
            palavra = sc.nextLine();
            fim = Fim(palavra);
            palavra = palavra.toUpperCase();
            out = "";
            if(!fim){
                out = Vogais(palavra, 0, true) ? "SIM" : "NAO";
                out += Consoantes(palavra, 0, true) ? " SIM" : " NAO";
                out += Inteiro(palavra, 0, true) ? " SIM" : " NAO";
                out += Real(palavra, 0, true, 0) ? " SIM" : " NAO";
                System.out.println(out);
            }
        }while(!fim);

        sc.close();
    }
}