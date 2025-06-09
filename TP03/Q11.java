import java.util.Scanner;

public class Q11 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int numCasos = scanner.nextInt();
        
        for (int caso = 0; caso < numCasos; caso++) {
            int l = scanner.nextInt();
            int c = scanner.nextInt();
            
            Matriz matriz1 = new Matriz(l, c);
            for (int i = 0; i < l; i++) {
                for (int j = 0; j < c; j++) {
                    matriz1.inserir(i, j, scanner.nextInt());
                }
            }
            
            int l2 = scanner.nextInt();
            int c2 = scanner.nextInt();
            
            Matriz matriz2 = new Matriz(l2, c2);
            for (int i = 0; i < l2; i++) {
                for (int j = 0; j < c2; j++) {
                    matriz2.inserir(i, j, scanner.nextInt());
                }
            }
            
            // Mostrar diagonal principal e secundária da primeira matriz
            matriz1.mostrarDiagonalPrincipal();
            matriz1.mostrarDiagonalSecundaria();
            
            // Realizar e mostrar a soma
            try {
                Matriz soma = matriz1.soma(matriz2);
                soma.imprimir();
            } catch (RuntimeException e) {
                System.out.println("SOMA IMPOSSIVEL");
            }
            
            // Realizar e mostrar a multiplicação
            try {
                Matriz mult = matriz1.multiplicacao(matriz2);
                mult.imprimir();
            } catch (RuntimeException e) {
                System.out.println("MULTIPLICACAO IMPOSSIVEL");
            }
        }
        
        scanner.close();
    }
    
    static class Matriz {
        private int[][] matriz;
        private int linhas;
        private int colunas;
        
        public Matriz(int linhas, int colunas) {
            this.linhas = linhas;
            this.colunas = colunas;
            this.matriz = new int[linhas][colunas];
        }
        
        public void inserir(int i, int j, int valor) {
            matriz[i][j] = valor;
        }
        
        public int getValor(int i, int j) {
            return matriz[i][j];
        }
        
        public int getLinhas() {
            return linhas;
        }
        
        public int getColunas() {
            return colunas;
        }
        
        public Matriz soma(Matriz outra) {
            if (this.linhas != outra.getLinhas() || this.colunas != outra.getColunas()) {
                throw new RuntimeException("Matrizes com dimensões incompatíveis para soma");
            }
            
            Matriz resultado = new Matriz(linhas, colunas);
            for (int i = 0; i < linhas; i++) {
                for (int j = 0; j < colunas; j++) {
                    resultado.inserir(i, j, this.matriz[i][j] + outra.getValor(i, j));
                }
            }
            return resultado;
        }
        
        public Matriz multiplicacao(Matriz outra) {
            if (this.colunas != outra.getLinhas()) {
                throw new RuntimeException("Matrizes com dimensões incompatíveis para multiplicação");
            }
            
            Matriz resultado = new Matriz(this.linhas, outra.getColunas());
            for (int i = 0; i < this.linhas; i++) {
                for (int j = 0; j < outra.getColunas(); j++) {
                    int soma = 0;
                    for (int k = 0; k < this.colunas; k++) {
                        soma += this.matriz[i][k] * outra.getValor(k, j);
                    }
                    resultado.inserir(i, j, soma);
                }
            }
            return resultado;
        }
        
        public void mostrarDiagonalPrincipal() {
            int min = Math.min(linhas, colunas);
            for (int i = 0; i < min; i++) {
                System.out.print(matriz[i][i] + " ");
            }
            System.out.println();
        }
        
        public void mostrarDiagonalSecundaria() {
            int min = Math.min(linhas, colunas);
            for (int i = 0; i < min; i++) {
                System.out.print(matriz[i][colunas - i - 1] + " ");
            }
            System.out.println();
        }
        
        public void imprimir() {
            for (int i = 0; i < linhas; i++) {
                for (int j = 0; j < colunas; j++) {
                    System.out.print(matriz[i][j] + " ");
                }
                System.out.println();
            }
        }
    }
}
