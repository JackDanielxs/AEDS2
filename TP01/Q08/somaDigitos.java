import java.util.Scanner;

public class somaDigitos {

    public static boolean Fim(String s){
        return s.length() == 3 && s.charAt(0) == 'F' && s.charAt(1) == 'I' && s.charAt(2) == 'M';
    }

    public static int somarDigitos(int n){
        if(n == 0)
            return 0;
        return n % 10 + somarDigitos(n / 10); // Soma os algarismos de trás pra frente
    }
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        String str = "";
        int num;

        do {
            str = sc.nextLine(); // Entrada completa

            if (!Fim(str)) {
                num = Integer.parseInt(str); // Converte a string para número
                System.out.println(somarDigitos(num));
            }
        }while(!Fim(str));

        sc.close();
    }
}
