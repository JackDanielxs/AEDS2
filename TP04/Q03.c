#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

#define MAX_FILMES 1400
#define MAX_LINHA 1024
#define MATRICULA "800712"

typedef struct {
    char id[100];
    char tipo[100];
    char titulo[100];
    char diretor[100];
    char cast[512];
    char pais[100];
    char data[100];
    int ano;
    char rating[50];
    char duracao[50];
    char listado[512];
} Show;

typedef struct No {
    Show filme;
    struct No* esq;
    struct No* dir;
    int altura;
} No;

Show filmes[MAX_FILMES];
int totalFilmes = 0;
int comparacoes = 0;

void removerAspasDuplas(char *str) {
    char *src = str, *dst = str;
    while (*src) {
        if (*src == '"' && *(src + 1) == '"') {
            *dst++ = '"';
            src += 2;
        } else if (*src == '"') {
            src++;
        } else {
            *dst++ = *src++;
        }
    }
    *dst = '\0';
}

void parseLinha(char *linha, Show *f) {
    int campo = 0;
    int i = 0, j = 0;
    char temp[MAX_LINHA] = "";
    int len = strlen(linha);
    int dentroAspas = 0;
    char campos[11][MAX_LINHA] = {0};

    while (i <= len) {
        char c = linha[i];
        if (c == '"') {
            dentroAspas = !dentroAspas;
            i++;
        } else if ((c == ',' || c == '\0' || c == '\n') && !dentroAspas) {
            temp[j] = '\0';
            strcpy(campos[campo++], temp);
            j = 0;
            temp[0] = '\0';
            i++;
        } else {
            temp[j++] = c;
            i++;
        }
    }

    removerAspasDuplas(campos[0]); strcpy(f->id, campos[0]);
    removerAspasDuplas(campos[1]); strcpy(f->tipo, campos[1]);
    removerAspasDuplas(campos[2]); strcpy(f->titulo, strlen(campos[2]) ? campos[2] : "NaN");
    removerAspasDuplas(campos[3]); strcpy(f->diretor, strlen(campos[3]) ? campos[3] : "NaN");
    removerAspasDuplas(campos[4]); strcpy(f->cast, strlen(campos[4]) ? campos[4] : "NaN");
    removerAspasDuplas(campos[5]); strcpy(f->pais, strlen(campos[5]) ? campos[5] : "NaN");
    removerAspasDuplas(campos[6]); strcpy(f->data, strlen(campos[6]) ? campos[6] : "March 1, 1900");
    f->ano = atoi(campos[7]);
    removerAspasDuplas(campos[8]); strcpy(f->rating, strlen(campos[8]) ? campos[8] : "NaN");
    removerAspasDuplas(campos[9]); strcpy(f->duracao, strlen(campos[9]) ? campos[9] : "NaN");
    removerAspasDuplas(campos[10]); strcpy(f->listado, strlen(campos[10]) ? campos[10] : "NaN");
}

void lerCSV(const char *caminho) {
    FILE *fp = fopen(caminho, "r");
    if (!fp) {
        perror("Erro ao abrir o arquivo CSV");
        exit(1);
    }
    char linha[MAX_LINHA];
    fgets(linha, MAX_LINHA, fp);
    while (fgets(linha, MAX_LINHA, fp) && totalFilmes < MAX_FILMES) {
        Show f;
        memset(&f, 0, sizeof(Show));
        parseLinha(linha, &f);
        filmes[totalFilmes++] = f;
    }
    fclose(fp);
}

Show* buscarFilmePorId(const char* id) {
    for (int i = 0; i < totalFilmes; i++) {
        if (strcmp(id, filmes[i].id) == 0) {
            return &filmes[i];
        }
    }
    return NULL;
}

int altura(No* no) {
    return no == NULL ? -1 : no->altura;
}

int max(int a, int b) {
    return (a > b) ? a : b;
}

No* novoNo(Show filme) {
    No* no = (No*)malloc(sizeof(No));
    no->filme = filme;
    no->esq = NULL;
    no->dir = NULL;
    no->altura = 0;
    return no;
}

No* rotacaoDireita(No* y) {
    No* x = y->esq;
    No* T2 = x->dir;
    x->dir = y;
    y->esq = T2;
    y->altura = max(altura(y->esq), altura(y->dir)) + 1;
    x->altura = max(altura(x->esq), altura(x->dir)) + 1;
    return x;
}

No* rotacaoEsquerda(No* x) {
    No* y = x->dir;
    No* T2 = y->esq;
    y->esq = x;
    x->dir = T2;
    x->altura = max(altura(x->esq), altura(x->dir)) + 1;
    y->altura = max(altura(y->esq), altura(y->dir)) + 1;
    return y;
}

int fatorBalanceamento(No* no) {
    return no == NULL ? 0 : altura(no->esq) - altura(no->dir);
}

No* inserir(No* no, Show filme) {
    if (no == NULL) {
        return novoNo(filme);
    }
    int cmp = strcmp(filme.titulo, no->filme.titulo);
    if (cmp < 0) {
        no->esq = inserir(no->esq, filme);
    } else if (cmp > 0) {
        no->dir = inserir(no->dir, filme);
    } else {
        return no;
    }
    no->altura = 1 + max(altura(no->esq), altura(no->dir));
    int fb = fatorBalanceamento(no);
    if (fb > 1 && strcmp(filme.titulo, no->esq->filme.titulo) < 0) {
        return rotacaoDireita(no);
    }
    if (fb < -1 && strcmp(filme.titulo, no->dir->filme.titulo) > 0) {
        return rotacaoEsquerda(no);
    }
    if (fb > 1 && strcmp(filme.titulo, no->esq->filme.titulo) > 0) {
        no->esq = rotacaoEsquerda(no->esq);
        return rotacaoDireita(no);
    }
    if (fb < -1 && strcmp(filme.titulo, no->dir->filme.titulo) < 0) {
        no->dir = rotacaoDireita(no->dir);
        return rotacaoEsquerda(no);
    }
    return no;
}

void pesquisar(No* no, char* titulo) {
    if (no == NULL) {
        printf("NAO\n");
        return;
    }
    printf("raiz");
    while (no != NULL) {
        comparacoes++;
        int cmp = strcmp(titulo, no->filme.titulo);
        if (cmp == 0) {
            printf(" SIM\n");
            return;
        }
        if (cmp < 0) {
            no = no->esq;
            if (no != NULL) printf(" esq");
        } else {
            no = no->dir;
            if (no != NULL) printf(" dir");
        }
    }
    printf(" NAO\n");
}

void freeTree(No* no) {
    if (no != NULL) {
        freeTree(no->esq);
        freeTree(no->dir);
        free(no);
    }
}

int main() {
    lerCSV("/tmp/disneyplus.csv");
    No* raiz = NULL;
    char input[MAX_LINHA];
    fgets(input, sizeof(input), stdin);
    input[strcspn(input, "\n")] = '\0';
    while (strcmp(input, "FIM") != 0) {
        Show *f = buscarFilmePorId(input);
        if (f) {
            raiz = inserir(raiz, *f);
        }
        fgets(input, sizeof(input), stdin);
        input[strcspn(input, "\n")] = '\0';
    }
    clock_t inicioTempo = clock();
    comparacoes = 0;
    fgets(input, sizeof(input), stdin);
    input[strcspn(input, "\n")] = '\0';
    while (strcmp(input, "FIM") != 0) {
        pesquisar(raiz, input);
        fgets(input, sizeof(input), stdin);
        input[strcspn(input, "\n")] = '\0';
    }
    clock_t fimTempo = clock();
    double tempoExecucao = (double)(fimTempo - inicioTempo) / CLOCKS_PER_SEC;
    char nomeArquivoLog[50];
    sprintf(nomeArquivoLog, "%s_avl.txt", MATRICULA);
    FILE* arquivoLog = fopen(nomeArquivoLog, "w");
    if (arquivoLog) {
        fprintf(arquivoLog, "%s\t%.6f\t%d", MATRICULA, tempoExecucao, comparacoes);
        fclose(arquivoLog);
    }
    freeTree(raiz);
    return 0;
}
