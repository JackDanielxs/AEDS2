#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>

#define TAM_FILA 5
#define MAX_CAST 10
#define MAX_LISTED 10

typedef struct {
    char show_id[20];
    char type[50];
    char title[100];
    char director[100];
    char cast[MAX_CAST][100];
    char country[100];
    char date_added[40];
    int release_year;
    char rating[20];
    char duration[20];
    char listed_in[MAX_LISTED][100];
} Show;

typedef struct No {
    Show show;
    struct No *prox;
} No;

typedef struct {
    No *inicio;
    No *fim;
    double soma_anos;
    int tamanho;
} FilaFlexivel;

void iniciarFila(FilaFlexivel *fila) {
    fila->inicio = NULL;
    fila->fim = NULL;
    fila->soma_anos = 0.0;
    fila->tamanho = 0;
}

void dividirString(char *str, char lista[][100]) {
    int idx = 0;
    char *token = strtok(str, ",");
    while (token != NULL && idx < 10) {
        while (*token == ' ') token++; // remove espaços à esquerda
        strcpy(lista[idx++], token);
        token = strtok(NULL, ",");
    }
    if (idx == 0) strcpy(lista[0], "NaN");
    else if (idx < 10) strcpy(lista[idx], "NaN");
}

void lerLinha(char *linha, Show *show) {
    char temp[1000];
    size_t i = 0, campoIndex = 0, len = strlen(linha);
    char campo[500];
    
    while (campoIndex < 11 && i < len) {
        int k = 0;
        if (linha[i] == '"') {
            i++; // pula aspas inicial
            while (i < len) {
                if (linha[i] == '"') {
                    if (linha[i+1] == '"') {
                        i += 2; // aspas duplas internas
                    } else {
                        i++;
                        break;
                    }
                } else {
                    campo[k++] = linha[i++];
                }
            }
            if (linha[i] == ',') i++;
        } else {
            while (i < len && linha[i] != ',') {
                campo[k++] = linha[i++];
            }
            if (linha[i] == ',') i++;
        }
        campo[k] = '\0';

        if (strlen(campo) == 0) strcpy(campo, "NaN");

        switch (campoIndex) {
            case 0: strcpy(show->show_id, campo); break;
            case 1: strcpy(show->type, campo); break;
            case 2: strcpy(show->title, campo); break;
            case 3: strcpy(show->director, campo); break;
            case 4:
                if (strcmp(campo, "NaN") == 0) strcpy(show->cast[0], "NaN");
                else {
                    strcpy(temp, campo);
                    dividirString(temp, show->cast);
                }
                break;
            case 5: strcpy(show->country, campo); break;
            case 6: strcpy(show->date_added, campo); break;
            case 7: show->release_year = atoi(campo); break;
            case 8: strcpy(show->rating, campo); break;
            case 9: strcpy(show->duration, campo); break;
            case 10:
                if (strcmp(campo, "NaN") == 0) strcpy(show->listed_in[0], "NaN");
                else {
                    strcpy(temp, campo);
                    dividirString(temp, show->listed_in);
                }
                break;
        }
        campoIndex++;
    }
}

int compararStrings(const void *a, const void *b) {
    const char *s1 = (const char *)a;
    const char *s2 = (const char *)b;
    return strcasecmp(s1, s2);
}

void ordenarElenco(Show *show) {
    int cnt = 0;
    if (strcmp(show->cast[0], "NaN") != 0) {
        while (cnt < MAX_CAST && strcmp(show->cast[cnt], "NaN") != 0) cnt++;
        if (cnt > 1) {
            qsort(show->cast, cnt, sizeof(show->cast[0]), compararStrings);
        }
    }
}

void imprimirShow(Show *show, int idx) {
    ordenarElenco(show);
    printf("[%d] => %s ## %s ## %s ## %s ## [", idx, show->show_id, show->title, show->type, show->director);
    
    if (strcmp(show->cast[0], "NaN") == 0) {
        printf("NaN");
    } else {
        for (int i = 0; i < MAX_CAST && strcmp(show->cast[i], "NaN") != 0; i++) {
            printf("%s", show->cast[i]);
            if (i + 1 < MAX_CAST && strcmp(show->cast[i+1], "NaN") != 0) printf(", ");
        }
    }

    printf("] ## %s ## %s ## %d ## %s ## %s ## [",
        show->country,
        strcmp(show->date_added, "NaN") == 0 ? "March 1, 1900" : show->date_added,
        show->release_year,
        show->rating,
        show->duration
    );

    for (int j = 0; j < MAX_LISTED && strcmp(show->listed_in[j], "NaN") != 0; j++) {
        printf("%s", show->listed_in[j]);
        if (j + 1 < MAX_LISTED && strcmp(show->listed_in[j + 1], "NaN") != 0) printf(", ");
    }
    printf("] ##\n");
}

int procurarShowPorID(const char *id, Show db[], int n) {
    for (int i = 0; i < n; i++) {
        if (strcmp(db[i].show_id, id) == 0) return i;
    }
    return -1;
}

int isFim(const char *entrada) {
    return strcmp(entrada, "FIM") == 0;
}

Show removerFila(FilaFlexivel *fila);

void inserirFila(FilaFlexivel *fila, Show show) {
    No *novo = (No *)malloc(sizeof(No));
    novo->show = show;
    novo->prox = NULL;

    if (fila->tamanho == TAM_FILA) {
        removerFila(fila);
    }

    if (fila->inicio == NULL) {
        fila->inicio = fila->fim = novo;
    } else {
        fila->fim->prox = novo;
        fila->fim = novo;
    }

    fila->soma_anos += show.release_year;
    fila->tamanho++;

    double media = fila->soma_anos / fila->tamanho;
    printf("[Media] %d\n", (int)floor(media));
}

Show removerFila(FilaFlexivel *fila) {
    if (fila->inicio == NULL) {
        printf("Erro ao remover: Fila vazia!\n");
        exit(1);
    }

    No *removido = fila->inicio;
    Show showRemovido = removido->show;

    fila->inicio = removido->prox;
    if (fila->inicio == NULL) fila->fim = NULL;

    fila->soma_anos -= showRemovido.release_year;
    fila->tamanho--;

    free(removido);
    return showRemovido;
}

void mostrarFila(FilaFlexivel *fila) {
    No *atual = fila->inicio;
    int idx = 0;
    while (atual != NULL) {
        imprimirShow(&atual->show, idx++);
        atual = atual->prox;
    }
}

int main() {
    FILE *file = fopen("/tmp/disneyplus.csv", "r");
    if (!file) file = fopen("disneyplus.csv", "r");
    if (!file) return 1;

    Show shows[1400];
    char linha[2000];
    int total = 0;

    fgets(linha, sizeof(linha), file); // pula cabeçalho

    while (fgets(linha, sizeof(linha), file) != NULL && total < 1368) {
        linha[strcspn(linha, "\r\n")] = 0;
        lerLinha(linha, &shows[total]);
        total++;
    }
    fclose(file);

    FilaFlexivel fila;
    iniciarFila(&fila);


    char entrada[50];
    fgets(entrada, sizeof(entrada), stdin);
    entrada[strcspn(entrada, "\r\n")] = 0;

    while (!isFim(entrada)) {
        int idx = procurarShowPorID(entrada, shows, total);
        if (idx != -1) inserirFila(&fila, shows[idx]);
        fgets(entrada, sizeof(entrada), stdin);
        entrada[strcspn(entrada, "\r\n")] = 0;
    }

    int n;
    scanf("%d", &n);
    getchar();

    for (int i = 0; i < n; i++) {
        char linhaComando[100];
        fgets(linhaComando, sizeof(linhaComando), stdin);
        char *cmd = strtok(linhaComando, " \n\r");

        if (cmd) {
            if (strcasecmp(cmd, "I") == 0) {
                char *id = strtok(NULL, " \n\r");
                if (id) {
                    int idx = procurarShowPorID(id, shows, total);
                    if (idx != -1) inserirFila(&fila, shows[idx]);
                }
            } else if (strcasecmp(cmd, "R") == 0) {
                if (fila.tamanho > 0) {
                    Show rem = removerFila(&fila);
                    printf("(R) %s\n", rem.title);
                }
            }
        }
    }

    mostrarFila(&fila);
    return 0;
}