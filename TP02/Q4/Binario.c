#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

#define MAX_FILMES 1400 // Maior que a quantidade de registros no CSV
#define MAX_LINHA 1024

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
} Filme;

Filme filmes[MAX_FILMES];
Filme filmesId[MAX_FILMES];
int totalFilmes = 0;
int comparacoes = 0;

void removerAspasDuplas(char *str) {
    char *src = str, *dst = str;
    while (*src) {
        if (*src == '"' && *(src + 1) == '"') {
            *dst++ = '"'; // uma aspa literal
            src += 2;
        } else if (*src == '"') {
            src++; // pula aspas normais
        } else {
            *dst++ = *src++;
        }
    }
    *dst = '\0';
}

// Função para ler e parsear o CSV
void parseLinha(char *linha, Filme *f) {
    int campo = 0;
    int i = 0, j = 0;
    char temp[1024] = "";
    int len = strlen(linha);
    int dentroAspas = 0;

    char campos[11][1024] = {0};

    while (i <= len) {
        char c = linha[i];
        if (c == '"' && linha[i + 1] == '"') {
            temp[j++] = '"';
            i += 2;
        }
        else if (c == '"') {
            dentroAspas = !dentroAspas;
            i++;
        }
        else if ((c == ',' || c == '\0' || c == '\n') && !dentroAspas) {
            temp[j] = '\0';
            strcpy(campos[campo++], temp);
            j = 0;
            temp[0] = '\0';
            i++;
        }
        else {
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
    removerAspasDuplas(campos[7]); f->ano = atoi(campos[7]);
    removerAspasDuplas(campos[8]); strcpy(f->rating, strlen(campos[8]) ? campos[8] : "NaN");
    removerAspasDuplas(campos[9]); strcpy(f->duracao, strlen(campos[9]) ? campos[9] : "NaN");
    removerAspasDuplas(campos[10]); strcpy(f->listado, strlen(campos[10]) ? campos[10] : "NaN");
}

void lerCSV(const char *caminho) {
    FILE *fp = fopen(caminho, "r");
    if (!fp) {
        return;
    }

    char linha[MAX_LINHA];
    fgets(linha, MAX_LINHA, fp); // Pula o cabeçalho

    while (fgets(linha, MAX_LINHA, fp)) {
        if (totalFilmes >= MAX_FILMES) break;
        Filme f;
        memset(&f, 0, sizeof(Filme));
        parseLinha(linha, &f);
        filmes[totalFilmes] = f;
        totalFilmes++;
    }

    fclose(fp);
}

// Função para buscar filme por ID
Filme* buscarFilmePorId(char* id) {
    for (int i = 0; i < totalFilmes; i++) {
        if (strcmp(id, filmes[i].id) == 0)
            return &filmes[i];
    }
    return NULL;
}

Filme* buscarFilmePorTitulo(char* titulo, int n) {
    int inicio = 0;
    int fim = n - 1;

    while(inicio <= fim){
        int meio = (inicio + fim) / 2;
        comparacoes++;
        int cmp = strcmp(filmesId[meio].titulo, titulo);

        if(cmp == 0)
            return &filmesId[meio];
        else if(cmp < 0)
            inicio = meio + 1;
        else
            fim = meio - 1;
    }
    return NULL;
}

void OrdenarPorTitulo(int n){
    Filme tmp;
    for(int i = 0; i < n; i++){
        for(int j = 0; j < n - i - 1; j++){
            if(strcmp(filmesId[j].titulo, filmesId[j + 1].titulo) > 0){
                comparacoes++;
                tmp = filmesId[j];
                filmesId[j] = filmesId[j + 1];
                filmesId[j + 1] = tmp;
            }
        }
    }
}

int main() {
    char input[100];
    lerCSV("/tmp/disneyplus.csv");
    
    clock_t inicioTempo = clock();  // Início da medição do tempo

    // Primeira leitura
    fgets(input, sizeof(input), stdin);
    input[strcspn(input, "\n")] = '\0'; // Remove o \n 

    int index = 0;
    while (strcmp(input, "FIM") != 0) {
        Filme *f = buscarFilmePorId(input);
        if (f){
            filmesId[index] = *f;
            index++;
        }
            
        // Lê a próxima entrada
        fgets(input, sizeof(input), stdin);
        input[strcspn(input, "\n")] = '\0';
    }

    OrdenarPorTitulo(index + 1);

    fgets(input, sizeof(input), stdin);
    input[strcspn(input, "\n")] = '\0';

    while (strcmp(input, "FIM") != 0) {
        Filme *f = buscarFilmePorTitulo(input, index + 1);
        if (f)
            printf("SIM\n");
        else
            printf("NAO\n");
            
        // Lê a próxima entrada
        fgets(input, sizeof(input), stdin);
        input[strcspn(input, "\n")] = '\0';
    }

    clock_t fimTempo = clock();  // Fim da medição
    double tempoExecucao = (double)(fimTempo - inicioTempo) / CLOCKS_PER_SEC;

    FILE* arquivo = fopen("800712\_binaria.txt", "w");
    fprintf(arquivo, "800712\t%d\t%.6f", comparacoes, tempoExecucao);
    fclose(arquivo);
    return 0;
}