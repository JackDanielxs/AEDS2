#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

#define MAX_SHOWS 1400
#define MAX_LINHA 1024

typedef struct {
    char show_id[20];
    char type[20];
    char title[100];
    char director[100];
    char cast[300];
    char country[50];
    char date_added[30];
    int release_year;
    char rating[10];
    char duration[20];
    char listed_in[200];
} Show;

Show filmes[MAX_SHOWS];
int totalFilmes = 0;

char* trim(char* str) {
    while (*str == ' ' || *str == '\t') str++;
    char* end = str + strlen(str) - 1;
    while (end > str && (*end == '\n' || *end == '\r' || *end == ' ')) *end-- = '\0';
    return str;
}

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

    removerAspasDuplas(campos[0]); strcpy(s->show_id, campos[0]);
    removerAspasDuplas(campos[1]); strcpy(s->type, campos[1]);
    removerAspasDuplas(campos[2]); strcpy(s->title, strlen(campos[2]) ? campos[2] : "NaN");
    removerAspasDuplas(campos[3]); strcpy(s->director, strlen(campos[3]) ? campos[3] : "NaN");
    removerAspasDuplas(campos[4]); strcpy(s->cast, strlen(campos[4]) ? campos[4] : "NaN");
    removerAspasDuplas(campos[5]); strcpy(s->country, strlen(campos[5]) ? campos[5] : "NaN");
    removerAspasDuplas(campos[6]); strcpy(s->date_added, strlen(campos[6]) ? campos[6] : "March 1, 1900");
    removerAspasDuplas(campos[7]); s->release_year = atoi(campos[7]);
    removerAspasDuplas(campos[8]); strcpy(s->rating, strlen(campos[8]) ? campos[8] : "NaN");
    removerAspasDuplas(campos[9]); strcpy(s->duration, strlen(campos[9]) ? campos[9] : "NaN");
    removerAspasDuplas(campos[10]); strcpy(s->listed_in, strlen(campos[10]) ? campos[10] : "NaN");
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

Show* find_show_by_id(const char* id) {
    for (int i = 0; i < totalFilmes; i++) {
        if (strcmp(filmes[i].show_id, id) == 0) {
            return &filmes[i];
        }
    }
    return NULL;
}

typedef struct DNode {
    Show* show;
    struct DNode* prev;
    struct DNode* next;
} DNode;

DNode* dhead = NULL;
DNode* dtail = NULL;
int tamanho_dlista = 0;

void dinserirFim(Show* show) {
    DNode* novo = (DNode*)malloc(sizeof(DNode));
    novo->show = show;
    novo->next = NULL;
    novo->prev = dtail;
    if (dtail) dtail->next = novo;
    dtail = novo;
    if (!dhead) dhead = novo;
    tamanho_dlista++;
}

void dliberar() {
    DNode* curr = dhead;
    while (curr) {
        DNode* temp = curr;
        curr = curr->next;
        free(temp);
    }
    dhead = dtail = NULL;
    tamanho_dlista = 0;
}

int date_to_int(const char* date) {
    int day = 0, year = 0;
    char month_str[20];
    int month = 0;
    if (sscanf(date, "%s %d, %d", month_str, &day, &year) != 3) return 0;
    if (strcmp(month_str, "January") == 0) month = 1;
    else if (strcmp(month_str, "February") == 0) month = 2;
    else if (strcmp(month_str, "March") == 0) month = 3;
    else if (strcmp(month_str, "April") == 0) month = 4;
    else if (strcmp(month_str, "May") == 0) month = 5;
    else if (strcmp(month_str, "June") == 0) month = 6;
    else if (strcmp(month_str, "July") == 0) month = 7;
    else if (strcmp(month_str, "August") == 0) month = 8;
    else if (strcmp(month_str, "September") == 0) month = 9;
    else if (strcmp(month_str, "October") == 0) month = 10;
    else if (strcmp(month_str, "November") == 0) month = 11;
    else if (strcmp(month_str, "December") == 0) month = 12;
    else return 0;
    return year * 10000 + month * 100 + day;
}

int cmp_show(Show* a, Show* b) {
    int da = date_to_int(a->date_added);
    int db = date_to_int(b->date_added);
    int cmp = da - db;
    return cmp == 0 ? strcmp(a->title, b->title) : cmp;
}

DNode* dget_node(int idx) {
    DNode* curr = dhead;
    for (int i = 0; i < idx && curr; i++) curr = curr->next;
    return curr;
}

void dswap(DNode* a, DNode* b) {
    Show* tmp = a->show;
    a->show = b->show;
    b->show = tmp;
}

int dpartition(int left, int right) {
    DNode* pnode = dget_node(right);
    Show* pivot = pnode->show;
    int i = left - 1;
    for (int j = left; j < right; j++) {
        DNode* jnode = dget_node(j);
        if (cmp_show(jnode->show, pivot) < 0) {
            i++;
            dswap(dget_node(i), jnode);
        }
    }
    dswap(dget_node(i + 1), pnode);
    return i + 1;
}

void dquicksort(int left, int right) {
    if (left < right) {
        int pi = dpartition(left, right);
        dquicksort(left, pi - 1);
        dquicksort(pi + 1, right);
    }
}

int cmp_cast(const void* a, const void* b) {
    return strcmp(*(const char**)a, *(const char**)b);
}

int cmp_listed(const void* a, const void* b) {
    return strcmp(*(const char**)a, *(const char**)b);
}

void ordenarCastListed(char *lista, int ref){
    char* tokens[100];
    int count = 0;
    char* temp = strdup(lista);
    char* token = strtok(temp, ",");
    while (token && count < 100) {
        tokens[count++] = strdup(trim(token));
        token = strtok(NULL, ",");
    }

    if (count > 1) {
        if(ref == 1)
            qsort(tokens, count, sizeof(char*), cmp_cast);
        else
            qsort(tokens, count, sizeof(char*), cmp_listed);
    }

    lista[0] = '\0';
    for (int i = 0; i < count; i++) {
        strcat(lista, tokens[i]);
        if (i < count - 1) strcat(lista, ", ");
        free(tokens[i]);  // libera a cópia
    }
    free(temp);  // libera a string base
}

void print_show(Show* s) {
    char cast_sorted[300];
    char listed_sorted[300];
    strcpy(cast_sorted, s->cast);
    strcpy(listed_sorted, s->listed_in);

    ordenarCastListed(cast_sorted, 1);
    ordenarCastListed(listed_sorted, 2);

    printf("=> %s ## %s ## %s ## %s ## [%s] ## %s ## %s ## %d ## %s ## %s ## [",
        strlen(s->show_id) > 0 ? s->show_id : "NaN",
        strlen(s->title) > 0 ? s->title : "NaN",
        strlen(s->type) > 0 ? s->type : "NaN",
        strlen(s->director) > 0 ? s->director : "NaN",
        strlen(cast_sorted) > 0 ? cast_sorted : "NaN",
        strlen(s->country) > 0 ? s->country : "NaN",
        strlen(s->date_added) > 0 ? s->date_added : "March 1, 1900",
        s->release_year > 0 ? s->release_year : 0,
        strlen(s->rating) > 0 ? s->rating : "NaN",
        strlen(s->duration) > 0 ? s->duration : "NaN",
        strlen(listed_sorted) > 0 ? listed_sorted : "NaN"
    );
    printf("] ##\n");
}

void dmostrar_lista() {
    DNode* curr = dhead;
    while (curr) {
        print_show(curr->show);
        curr = curr->next;
    }
}

void log_quicksort2(int comps, int movs, double tempo) {
    FILE* f = fopen("800712_quicksort2.txt", "w");
    fprintf(f, "800712_quicksort2\t%d\t%d\t%lf\n", comps, movs, tempo);
    fclose(f);
}

int main() {
    lerCSV("/tmp/disneyplus.csv");

    char input[100];
    while (1) {
        if (!fgets(input, sizeof(input), stdin)) break;
        char* newline = strchr(input, '\n');
        if (newline) *newline = '\0';
        if (strcmp(input, "FIM") == 0) break;
        Show* show = find_show_by_id(input);
        if (show) dinserirFim(show);
    }

    int comps = 0, movs = 0;
    clock_t start = clock();
    dquicksort(0, tamanho_dlista - 1);
    clock_t end = clock();
    double tempo = ((double)(end - start)) / CLOCKS_PER_SEC;

    dmostrar_lista();
    log_quicksort2(comps, movs, tempo);
    dliberar();
    return 0;
}