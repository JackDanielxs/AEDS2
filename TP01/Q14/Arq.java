import java.io.*;
import java.util.*;

public class Arq {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        double flo;
        
        try (RandomAccessFile abrir = new RandomAccessFile("teste.txt", "rw")) {
            for(int i = 0; i < n; i++){
                flo = sc.nextDouble();
                abrir.writeDouble(flo);
            }
        } catch(IOException e) {}
        
        try (RandomAccessFile ler = new RandomAccessFile("teste.txt", "r")) {
            long tamanho = ler.length();
            long pos = tamanho - 8; // double ocupa 8 bytes
            while(pos >= 0){
                ler.seek(pos);
                flo = ler.readDouble();
                System.out.println(flo);
                pos -= 8; // ir de double em double
            }
        } catch(IOException e) {}
        
        sc.close();
    }
}
