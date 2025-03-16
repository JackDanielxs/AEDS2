import java.util.Scanner;

public class AlgebraBooleanaRecursivo {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean continuarExecucao = true;

        while (continuarExecucao && sc.hasNext()) {
            String expressao = sc.nextLine();

            continuarExecucao = expressao.charAt(0) != '0';

            if (continuarExecucao) {
                if (avaliar(expressao)) {
                    System.out.println("1");
                } else {
                    System.out.println("0");
                }
            }
        }
        sc.close();
    }

    // Inicia a avaliação da expressão booleana e extrai os valores das variáveis
    public static boolean avaliar(String expressao) {
        int[] posicao = {0}; // Índice para acompanhar a posição na string
        boolean[] valoresVariaveis = extrairValores(expressao, posicao);
        return resolver(expressao, posicao, valoresVariaveis);
    }

    // Extrai os valores booleanos das variáveis da entrada
    public static boolean[] extrairValores(String expressao, int[] posicao) {
        int quantidadeVariaveis = 0;

        while (expressao.charAt(posicao[0]) != ' ') {
            quantidadeVariaveis = quantidadeVariaveis * 10 + (expressao.charAt(posicao[0]) - '0');
            posicao[0]++;
        }
        posicao[0]++; // Pulando espaço

        boolean[] valores = new boolean[quantidadeVariaveis];

        // Lê os valores das variáveis e armazena no array
        for (int indice = 0; indice < quantidadeVariaveis; indice++) {
            valores[indice] = expressao.charAt(posicao[0]) == '1';
            posicao[0] += 2; // Pulando número e espaço
        }

        return valores;
    }

    public static boolean resolver(String expressao, int[] posicao, boolean[] valoresVariaveis) {
        // Ignora espaços e vírgulas antes da avaliação
        while (posicao[0] < expressao.length() && 
              (expressao.charAt(posicao[0]) == ' ' || expressao.charAt(posicao[0]) == ',')) {
            posicao[0]++;
        }

        // Se encontrar um parêntese abrindo, pula ele
        if (posicao[0] < expressao.length() && expressao.charAt(posicao[0]) == '(') {
            posicao[0]++;
        }

        if (posicao[0] >= expressao.length()) return false;

        char caractereAtual = expressao.charAt(posicao[0]);

        // Se for uma variável, retorna seu valor correspondente
        if (caractereAtual >= 'A' && caractereAtual < 'A' + valoresVariaveis.length) {
            return valoresVariaveis[expressao.charAt(posicao[0]++) - 'A'];
        }

        // Se for NOT, inverte a avaliação da próxima expressão
        if (expressao.startsWith("not", posicao[0])) {
            posicao[0] += 3; // Avança além de "not"
            boolean resultado = !resolver(expressao, posicao, valoresVariaveis);

            // Se encontrar um parêntese fechando, pula ele
            if (posicao[0] < expressao.length() && expressao.charAt(posicao[0]) == ')') {
                posicao[0]++;
            }

            return resultado;
        }

        // Se for AND
        if (expressao.startsWith("and", posicao[0])) {
            posicao[0] += 3; // Avança além de "and"
            boolean resultado = true;

            // Processa todos os operandos até encontrar um ')'
            while (posicao[0] < expressao.length() && expressao.charAt(posicao[0]) != ')') {
                resultado &= resolver(expressao, posicao, valoresVariaveis);
            }

            // Se encontrar um parêntese fechando, pula ele
            if (posicao[0] < expressao.length() && expressao.charAt(posicao[0]) == ')') {
                posicao[0]++;
            }

            return resultado;
        }

        // Se for OR
        if (expressao.startsWith("or", posicao[0])) {
            posicao[0] += 2; // Avança além de "or"
            boolean resultado = false;

            // Processa todos os operandos até encontrar um ')'
            while (posicao[0] < expressao.length() && expressao.charAt(posicao[0]) != ')') {
                resultado |= resolver(expressao, posicao, valoresVariaveis);
            }

            // Se encontrar um parêntese fechando, pula ele
            if (posicao[0] < expressao.length() && expressao.charAt(posicao[0]) == ')') {
                posicao[0]++;
            }

            return resultado;
        }

        return false;
    }
}
