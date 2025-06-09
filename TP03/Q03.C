#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

#define MAX_SHOWS 1400 // Maior que a quantidade de registros no CSV
#define MAX_LINHA 1024
#define bool   short
#define true   1
#define false  0

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

Show filmes[MAX_SHOWS];
int totalFilmes = 0;
Show filmesId[MAX_SHOWS];
int nIDS = 0;

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
void parseLinha(char *linha, Show *s) {
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

    removerAspasDuplas(campos[0]); strcpy(s->id, campos[0]);
    removerAspasDuplas(campos[1]); strcpy(s->tipo, campos[1]);
    removerAspasDuplas(campos[2]); strcpy(s->titulo, strlen(campos[2]) ? campos[2] : "NaN");
    removerAspasDuplas(campos[3]); strcpy(s->diretor, strlen(campos[3]) ? campos[3] : "NaN");
    removerAspasDuplas(campos[4]); strcpy(s->cast, strlen(campos[4]) ? campos[4] : "NaN");
    removerAspasDuplas(campos[5]); strcpy(s->pais, strlen(campos[5]) ? campos[5] : "NaN");
    removerAspasDuplas(campos[6]); strcpy(s->data, strlen(campos[6]) ? campos[6] : "March 1, 1900");
    removerAspasDuplas(campos[7]); s->ano = atoi(campos[7]);
    removerAspasDuplas(campos[8]); strcpy(s->rating, strlen(campos[8]) ? campos[8] : "NaN");
    removerAspasDuplas(campos[9]); strcpy(s->duracao, strlen(campos[9]) ? campos[9] : "NaN");
    removerAspasDuplas(campos[10]); strcpy(s->listado, strlen(campos[10]) ? campos[10] : "NaN");
}

void lerCSV(const char *caminho) {
    FILE *fp = fopen(caminho, "r");
    if (!fp) {
        return;
    }

    char linha[MAX_LINHA];
    fgets(linha, MAX_LINHA, fp); // Pula o cabeçalho

    while (fgets(linha, MAX_LINHA, fp)) {
        if (totalFilmes >= MAX_SHOWS) break;
        Show f;
        memset(&f, 0, sizeof(Show));
        parseLinha(linha, &f);
        filmes[totalFilmes] = f;
        totalFilmes++;
    }

    fclose(fp);
}

// Função para buscar filme por ID
Show* buscarFilmePorId(char* id) {
    for (int i = 0; i < totalFilmes; i++) {
        if (strcmp(id, filmes[i].id) == 0)
            return &filmes[i];
    }
    return NULL;
}

void inserirFim(Show *x) {

   //validar insercao
   if(nIDS >= MAX_SHOWS){
      printf("Erro ao inserir!");
      exit(1);
   }

   filmesId[nIDS] = *x;
   nIDS++;
}

Show removerFim() {

   //validar remocao
   if (nIDS == 0) {
      printf("Erro ao remover!");
      exit(1);
   }

   return filmesId[--nIDS];
}

void manipularLinha(char* linha){
    char* id;
    Show *aux;

    if (linha[0] == 'I') {
        strtok(linha, " ");
        id = strtok(NULL, " ");
        aux = buscarFilmePorId(id);
        if(aux != NULL)
            inserirFim(aux);

    }
    else if (linha[0] == 'R') {
        Show s = removerFim();
        printf("(R) %s\n", s.titulo);
    }
}

void manipularShows(int interacoes){
    char input[100];
    fgets(input, sizeof(input), stdin);
    input[strcspn(input, "\n")] = '\0'; // pular entrada vazia

    for (int i = 0; i < interacoes; i++) {
        fgets(input, sizeof(input), stdin);
        input[strcspn(input, "\n")] = '\0'; // Remove o \n
        manipularLinha(input);
    }
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

// Função para imprimir filme formatado
void printFilme(Show s, int i) {
    char castFormatado[600], listadoFormatado[600];

    formatarListaOrdenada(s.cast, castFormatado);
    formatarListaOrdenada(s.listado, listadoFormatado);

    printf("[%d] => %s ## %s ## %s ## %s ## %s ## %s ## %s ## %d ## %s ## %s ## %s ##\n",
        i,
        s.id,
        s.titulo,
        s.tipo,
        s.diretor,
        castFormatado,
        s.pais,
        s.data,
        s.ano,
        s.rating,
        s.duracao,
        listadoFormatado
    );
}

int main() {
    char input[100];
    lerCSV("/tmp/disneyplus.csv");

    // Primeira leitura
    fgets(input, sizeof(input), stdin);
    input[strcspn(input, "\n")] = '\0'; // Remove o \n

    while (strcmp(input, "FIM") != 0) {
        Show *s = buscarFilmePorId(input);
        if (s){
            filmesId[nIDS] = *s;
            nIDS++;
        }
        
        // Lê a próxima entrada
        fgets(input, sizeof(input), stdin);
        input[strcspn(input, "\n")] = '\0';
    }

    int interacoes;
    scanf("%d", &interacoes);
    manipularShows(interacoes);

    for(int i = nIDS - 1; i >= 0; i--){
        Show s = filmesId[i];
        printFilme(s, i);
    }
    return 0;
}