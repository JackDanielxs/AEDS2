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
int movimentacoes = 0;

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

// Função para imprimir filme formatado
void printFilme(Filme *f) {
    char castFormatado[600], listadoFormatado[600];

    formatarListaOrdenada(f->cast, castFormatado);
    formatarListaOrdenada(f->listado, listadoFormatado);

    printf("=> %s ## %s ## %s ## %s ## %s ## %s ## %s ## %d ## %s ## %s ## %s ##\n",
        f->id,
        f->titulo,
        f->tipo,
        f->diretor,
        castFormatado,
        f->pais,
        f->data,
        f->ano,
        f->rating,
        f->duracao,
        listadoFormatado
    );
}

void ordenarStrings(char *itens[], int n) {
    for (int i = 0; i < n - 1; i++) {
        for (int j = 0; j < n - i - 1; j++) {
            if (strcmp(itens[j], itens[j + 1]) > 0) {
                char *temp = itens[j];
                itens[j] = itens[j + 1];
                itens[j + 1] = temp;
            }
        }
    }
}

void formatarListaOrdenada(const char *entrada, char *saida) {
    if (strcmp(entrada, "NaN") == 0) {
        strcpy(saida, "[NaN]");
        return;
    }

    // Copia da string original, porque strtok modifica
    char copia[512];
    strcpy(copia, entrada);

    // Array de ponteiros para os itens
    char *itens[100];
    int count = 0;

    char *token = strtok(copia, ",");
    while (token && count < 100) {
        // Remove espaços extras
        while (*token == ' ') token++;
        itens[count++] = token;
        token = strtok(NULL, ",");
    }

    // Ordena os itens
    ordenarStrings(itens, count);

    // Monta a string formatada
    strcpy(saida, "[");
    for (int i = 0; i < count; i++) {
        strcat(saida, itens[i]);
        if (i < count - 1) strcat(saida, ", ");
    }
    strcat(saida, "]");
}

// Função para buscar filme por ID
Filme* buscarFilmePorId(char* id) {
    for (int i = 0; i < totalFilmes; i++) {
        if (strcmp(id, filmes[i].id) == 0)
            return &filmes[i];
    }
    return NULL;
}

void swap(int a, int b){
    Filme tmp = filmesId[a];
    filmesId[a] = filmesId[b];
    filmesId[b] = tmp;
    movimentacoes+=3;
}

// Função auxiliar para encontrar o índice do menor elemento a partir de um índice dado
int indiceMenor(int inicio, int n) {
    if (inicio == n - 1)
        return inicio;

    int menorDosRestantes = indiceMenor(inicio + 1, n);
    comparacoes++;
    return strcasecmp(filmesId[inicio].titulo, filmesId[menorDosRestantes].titulo) < 0 ? inicio : menorDosRestantes;
}

// Função principal recursiva de ordenação
void selecaoRec(int inicio, int n) {
    if (inicio >= n - 1)
        return;

    int menor = indiceMenor(inicio, n);
    swap(inicio, menor);

    selecaoRec(inicio + 1, n);
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

    selecaoRec(0, index);

    for(int i = 0; i < index; i++){
        Filme *f = &filmesId[i];
        printFilme(f);
    }

    clock_t fimTempo = clock();  // Fim da medição
    double tempoExecucao = (double)(fimTempo - inicioTempo) / CLOCKS_PER_SEC;

    FILE* arquivo = fopen("800712\_selecaoRecursiva.txt", "w");
    fprintf(arquivo, "800712\t%d\t%.6f", comparacoes, tempoExecucao);
    fclose(arquivo);
    return 0;
}