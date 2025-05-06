import java.io.*;
import java.util.*;

class SortMetrics {
    public long comparacoes = 0;
    public long movimentacoes = 0;
    public long tempoExecucao = 0;
}

public class Ordenadores {
    public static void selectionSort(int[] array, SortMetrics metrics) {
        long inicio = System.currentTimeMillis();
        int n = array.length;

        for (int i = 0; i < n - 1; i++) {
            int min = i;
            for (int j = i + 1; j < n; j++) {
                metrics.comparacoes++;
                if (array[j] < array[min]) {
                    min = j;
                }
            }
            if (min != i) {
                int temp = array[i];
                array[i] = array[min];
                array[min] = temp;
                metrics.movimentacoes += 3;
            }
        }

        metrics.tempoExecucao = System.currentTimeMillis() - inicio;
    }

    public static void insertionSort(int[] array, SortMetrics metrics) {
        long inicio = System.currentTimeMillis();
        int n = array.length;

        for (int i = 1; i < n; i++) {
            int chave = array[i];
            int j = i - 1;

            while (j >= 0) {
                metrics.comparacoes++;
                if (array[j] > chave) {
                    array[j + 1] = array[j];
                    metrics.movimentacoes++;
                    j--;
                } else {
                    break;
                }
            }
            array[j + 1] = chave;
            metrics.movimentacoes++;
        }

        metrics.tempoExecucao = System.currentTimeMillis() - inicio;
    }

    public static void bubbleSort(int[] array, SortMetrics metrics) {
        long inicio = System.currentTimeMillis();
        int n = array.length;
        boolean trocou;

        for (int i = 0; i < n - 1; i++) {
            trocou = false;
            for (int j = 0; j < n - 1 - i; j++) {
                metrics.comparacoes++;
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                    metrics.movimentacoes += 3;
                    trocou = true;
                }
            }
            if (!trocou) break;
        }

        metrics.tempoExecucao = System.currentTimeMillis() - inicio;
    }

    public static void quicksort(int[] array, SortMetrics metrics) {
        long inicio = System.currentTimeMillis();
        quicksortRec(array, 0, array.length - 1, metrics);
        metrics.tempoExecucao = System.currentTimeMillis() - inicio;
    }

    private static void quicksortRec(int[] array, int low, int high, SortMetrics metrics) {
        if (low < high) {
            int p = particionar(array, low, high, metrics);
            quicksortRec(array, low, p - 1, metrics);
            quicksortRec(array, p + 1, high, metrics);
        }
    }

    private static int particionar(int[] array, int low, int high, SortMetrics metrics) {
        int pivot = array[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            metrics.comparacoes++;
            if (array[j] <= pivot) {
                i++;
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
                metrics.movimentacoes += 3;
            }
        }

        int temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;
        metrics.movimentacoes += 3;

        return i + 1;
    }

    public static void main(String[] args) {
        int[] tamanhos = {100, 1000, 10000, 100000};
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("dados_ordenacao.csv"))) {
            writer.write("Algoritmo,Tamanho,Tempo,Comparacoes,Movimentacoes\n");

            for (int tamanho : tamanhos) {
                int[] vetor = gerarVetorAleatorio(tamanho);

                executarAlgoritmo("Selection Sort", vetor.clone(), tamanho, writer, "selectionSort");
                executarAlgoritmo("Insertion Sort", vetor.clone(), tamanho, writer, "insertionSort");
                executarAlgoritmo("Bubble Sort", vetor.clone(), tamanho, writer, "bubbleSort");
                executarAlgoritmo("Quicksort", vetor.clone(), tamanho, writer, "quicksort");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int[] gerarVetorAleatorio(int tamanho) {
        Random rand = new Random();
        int[] vetor = new int[tamanho];
        for (int i = 0; i < tamanho; i++) {
            vetor[i] = rand.nextInt(10000);
        }
        return vetor;
    }

    public static void executarAlgoritmo(String nomeAlgoritmo, int[] vetor, int tamanho, BufferedWriter writer, String algoritmo) {
        SortMetrics metrics = new SortMetrics();

        switch (algoritmo) {
            case "selectionSort":
                Ordenadores.selectionSort(vetor, metrics);
                break;
            case "insertionSort":
                Ordenadores.insertionSort(vetor, metrics);
                break;
            case "bubbleSort":
                Ordenadores.bubbleSort(vetor, metrics);
                break;
            case "quicksort":
                Ordenadores.quicksort(vetor, metrics);
                break;
        }

        try {
            writer.write(String.format("%s,%d,%d,%d,%d\n", nomeAlgoritmo, tamanho, metrics.tempoExecucao, metrics.comparacoes, metrics.movimentacoes));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}