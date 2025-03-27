import java.util.*;
import java.lang.NullPointerException;

public class Jogador{
    int Num;
    int Freq = 0;

    public Jogador(int num){
        Num = num;
    }

    public static void Swap(Jogador[] vet, int i, int j){
        Jogador tmp = vet[j];
        vet[j] = vet[i];
        vet[i] = tmp;
    }

    public static void SwapFinal(int[] vet, int i, int j){
        int tmp = vet[j];
        vet[j] = vet[i];
        vet[i] = tmp;
    }
    
    public static void Aparicoes(Jogador[] vet, int num){ // Registra aparições de cada item do array
        for(int i = 0; i < vet.length; i++){
            if(vet[i].Num == num)
                vet[i].Freq += 1;
        }
    }
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int sem, jog;
        
        do{
            sem = sc.nextInt();
            if(sem != 0){ // condição de parada
                jog = sc.nextInt();
                int tam = jog * sem; // tamanho do array
                Jogador[] vet = new Jogador[tam];
                for(int i = 0; i < tam; i++){
                    vet[i] = new Jogador(sc.nextInt()); // preenche array de jogadores
                }

                for(int i = 0; i < tam; i++){
                    Aparicoes(vet, vet[i].Num);
                }

                Jogador[] semRepetir = new Jogador[tam];
                int k = 0;
                for(int i = 0; i < tam; i++){
                    boolean tem = false;
                    for(int j = 0; j < tam; j++){
                        if(semRepetir[j] != null && semRepetir[j].Num == vet[i].Num)
                            tem = true;
                    }
                    if(!tem){
                        semRepetir[k] = vet[i]; // array sem numeros repetidos
                        k += 1;
                    }
                }
                semRepetir = Arrays.copyOf(semRepetir, k); //corta o array na pos K

                for(int i = 0; i < semRepetir.length - 1; i++){
                    int menor = i;
                    for(int j = i + 1; j < semRepetir.length; j++){
                        if(semRepetir[j].Freq < semRepetir[menor].Freq){ // ordena pela frequencia
                            menor = j;
                        }
                    }
                    Swap(semRepetir, i, menor);
                }
                int freqSegundo = 0;
                for(int i = semRepetir.length - 1; i >= 1; i--){
                    int freqAux = semRepetir[i].Freq;
                    if(freqAux != semRepetir[i - 1].Freq){
                        freqSegundo = semRepetir[i - 1].Freq; // acha a frequencia do 2 colocado
                        i = 0; // sair do loop
                    }
                }

                int[] arrFinal = new int[semRepetir.length];
                k = 0;
                for(int i = 0; i < arrFinal.length; i++){
                    if(semRepetir[i].Freq == freqSegundo){
                        arrFinal[k] = semRepetir[i].Num; // array só com os numeros que tem a frequencia do 2 colocado
                        k += 1;
                    }
                }

                for(int i = 0; i < arrFinal.length - 1; i++){
                    int menor = i;
                    for(int j = i + 1; j < arrFinal.length; j++){
                        if(arrFinal[j] < arrFinal[menor]){
                            menor = j;
                        }
                    }
                    SwapFinal(arrFinal, i, menor); // ordena por ordem crescente
                }

                String res = "";
                for(int i = 0; i < arrFinal.length; i++){
                    if(arrFinal[i] != 0){ // nenhum jogador tem ID = 0
                        res += arrFinal[i] + i == arrFinal.length - 1 ? "" : " ";
                    }
                }
                System.out.println(res);

            }
        }while(sem != 0);     
        sc.close();
    }
}