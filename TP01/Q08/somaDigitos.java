package Q08;
import java.util.Scanner;

public class somaDigitos {

    public static int somarDigitos(int n){
        if(n == 0)
            return 0;
        return n % 10 + somarDigitos(n / 10);
    }
    public static void main(){
        Scanner sc = new Scanner(System.in);
        int num = sc.nextInt();
        System.out.println(somarDigitos(num));
        sc.close();
    }
}
