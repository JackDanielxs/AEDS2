import java.util.*;

class Substring{

    public static int MaiorSub(String str) {
        int maior = 0;
        int esq = 0;
        int[] ultimo = new int[256]; // Armazenar última posição do caractere
        
        for (int i = 0; i < 256; i++) {
            ultimo[i] = -1; // Inicializa todas as posições como -1
        }

        for (int dir = 0; dir < str.length(); dir++) {
            char c = str.charAt(dir);
            if (ultimo[c] >= esq) {
                esq = ultimo[c] + 1; // Atualiza a posição da janela
            }
            ultimo[c] = dir; // Atualiza a última posição do caractere
            maior = Math.max(maior, dir - esq + 1);
        }
        
        return maior;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String str = "";

        while (sc.hasNextLine()) {
            str = sc.nextLine();
            System.out.println(MaiorSub(str));
        }
        sc.close();
    }
}