#include <stdio.h>
#include <string.h>
#include <ctype.h>

typedef struct {
    int direcao;
    char id[10];
} Aviao;

int main() {
  
    Aviao oeste[100], norte[100], sul[100], leste[100];
    int idx_oeste = 0, idx_norte = 0, idx_sul = 0, idx_leste = 0;

    char entrada[20];

    // Lê a primeira linha (direção ou "0" para encerrar)
    while (fgets(entrada, sizeof(entrada), stdin)) {
        entrada[strcspn(entrada, "\n")] = '\0';

        // Se for "0", termina
        if (strcmp(entrada, "0") == 0) break;

        // Se for uma direção válida
        if (entrada[0] == '-' && isdigit(entrada[1])) {
            int dir = atoi(entrada);

            // Lê os aviões da direção atual até encontrar a próxima direção ou 0
            while (fgets(entrada, sizeof(entrada), stdin)) {
                entrada[strcspn(entrada, "\n")] = '\0';

                if (entrada[0] == '-' || strcmp(entrada, "0") == 0) {
                    // Volta a entrada para ser processada como nova direção
                    ungetc('\n', stdin);
                    for (int i = strlen(entrada) - 1; i >= 0; i--)
                        ungetc(entrada[i], stdin);
                    break;
                }

                Aviao a;
                a.direcao = dir;
                strcpy(a.id, entrada);

                switch (dir) {
                    case -1: oeste[idx_oeste++] = a; break;
                    case -2: sul[idx_sul++] = a; break;
                    case -3: norte[idx_norte++] = a; break;
                    case -4: leste[idx_leste++] = a; break;
                }
            }
        }
    }

    // Calcula o máximo entre os vetores
    int maior = idx_oeste;
    if (idx_norte > maior) maior = idx_norte;
    if (idx_sul > maior) maior = idx_sul;
    if (idx_leste > maior) maior = idx_leste;

    // Impressão intercalada
    for (int i = 0; i < maior; i++) {
        if (i < idx_oeste) printf("%s ", oeste[i].id);
        if (i < idx_norte) printf("%s ", norte[i].id);
        if (i < idx_sul)   printf("%s ", sul[i].id);
        if (i < idx_leste) printf("%s ", leste[i].id);
    }
    
    return 0;
}
