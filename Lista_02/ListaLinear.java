import java.util.*;

class ListaLinear{
    public int tamanho;
    public int[] arr;

    public ListaLinear(int max){
        tamanho = 0;
        arr = new int [max];
    }

    public void inserirInicio(int elem){
        if(tamanho == arr.length){
            return; // overflow
        }
        for(int i = arr.length - 1; i > 0; i--){
            arr[i] = arr[i - 1]; // arreda tudo pra direita
        }
        arr[0] = elem;
        tamanho++;
    }

    public void inserirFim(int elem){
        if(tamanho == arr.length){
            return; // overflow
        }
        arr[tamanho++] = elem;
    }

    public void inserir(int elem, int pos){
        if(tamanho == arr.length){
            return; // overflow
        }
        for(int i = arr.length - 1; i < pos; i--){
            arr[i] = arr[i - 1]; // arreda tudo pra direita
        }
        arr[pos] = elem;
        tamanho++;
    }

    public int removerInicio(){
        if(tamanho == 0){
            return -1; // underflow
        }
        int removido = arr[0];
        for(int i = 0; i < arr.length - 1; i++){
            arr[i] = arr[i + 1]; // arreda tudo pra esquerda
        }
        tamanho--;
        return removido;
    }

    public int removerFim(){
        if(tamanho == 0){
            return -1; // underflow
        }
        int removido = arr[tamanho - 1];
        arr[tamanho - 1] = 0; // valor padrão em java
        tamanho--;
        return removido;
    }

    public int remover(int elem, int pos){
        if(tamanho == 0){
            return -1; // underflow
        }
        int removido = arr[pos];
        for(int i = pos; i < arr.length - 1; i++){
            arr[i] = arr[i + 1]; // arreda tudo depois de POS pra esquerda
        }
        tamanho--;
        return removido;
    }

    public void mostrar(){
        for(int i = 0; i < tamanho; i++){
            System.out.println(arr[i] + " ");
        }
    }

    public boolean pesquisar(int elem){
        boolean resp = false;
        for(int i = 0; i < tamanho; i++){
            if(arr[i] == elem){
                resp = true;
                i = tamanho;
            }
        }
        return resp;
    }
}