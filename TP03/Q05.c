#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX_SHOWS 1400
#define MAX_LINE 1024

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

// Array with all shows loaded from CSV
Show shows[MAX_SHOWS];
int show_count = 0;

// Removes leading and trailing spaces
char* trim(char* str) {
    while (*str == ' ' || *str == '\t') str++;
    char* end = str + strlen(str) - 1;
    while (end > str && (*end == '\n' || *end == '\r' || *end == ' ')) *end-- = '\0';
    return str;
}

// Parses a CSV line into a Show struct
void parse_csv_line(char* line, Show* show) {
    char* ptr = line;
    char buffer[500];
    int field = 0, inQuotes = 0, buf_idx = 0;

    for (; *ptr; ptr++) {
        if (*ptr == '"') {
            inQuotes = !inQuotes;
        } else if (*ptr == ',' && !inQuotes) {
            buffer[buf_idx] = '\0';
            switch (field) {
                case 0: strcpy(show->show_id, trim(buffer)); break;
                case 1: strcpy(show->type, trim(buffer)); break;
                case 2: strcpy(show->title, trim(buffer)); break;
                case 3: strcpy(show->director, trim(buffer)); break;
                case 4: strcpy(show->cast, trim(buffer)); break;
                case 5: strcpy(show->country, trim(buffer)); break;
                case 6: strcpy(show->date_added, trim(buffer)); break;
                case 7: show->release_year = atoi(trim(buffer)); break;
                case 8: strcpy(show->rating, trim(buffer)); break;
                case 9: strcpy(show->duration, trim(buffer)); break;
            }
            buf_idx = 0;
            field++;
        } else {
            buffer[buf_idx++] = *ptr;
        }
    }

    buffer[buf_idx] = '\0';
    if (field == 10) {
        strcpy(show->listed_in, trim(buffer));
    }
}


// Reads the CSV file and populates the shows array
void read_csv(const char* path) {
    FILE* fp = fopen(path, "r");
    if (!fp) {
        printf("Error\n");
        exit(1);
    }

    char line[MAX_LINE];
    fgets(line, sizeof(line), fp); // Skip header

    while (fgets(line, sizeof(line), fp)) {
        if (show_count >= MAX_SHOWS) break;
        parse_csv_line(line, &shows[show_count]);
        show_count++;
    }

    fclose(fp);
}

// Find a show by show_id in the shows array
Show* find_show_by_id(const char* id) {
    for (int i = 0; i < show_count; i++) {
        if (strcmp(shows[i].show_id, id) == 0) {
            return &shows[i];
        }
    }
    return NULL;
}

typedef struct Node {
    Show* show;
    struct Node* next;
} Node;

Node* head = NULL;
Node* tail = NULL;
int tamanho_lista = 0;

void inserirInicio(Show* show) {
    Node* novo = (Node*)malloc(sizeof(Node));
    novo->show = show;
    novo->next = head;
    head = novo;
    if (tamanho_lista == 0) tail = novo;
    tamanho_lista++;
}

void inserirFim(Show* show) {
    Node* novo = (Node*)malloc(sizeof(Node));
    novo->show = show;
    novo->next = NULL;
    if (tamanho_lista == 0) {
        head = tail = novo;
    } else {
        tail->next = novo;
        tail = novo;
    }
    tamanho_lista++;
}

void inserir(Show* show, int pos) {
    if (pos < 0 || pos > tamanho_lista) return;
    if (pos == 0) {
        inserirInicio(show);
    } else if (pos == tamanho_lista) {
        inserirFim(show);
    } else {
        Node* novo = (Node*)malloc(sizeof(Node));
        novo->show = show;
        Node* ant = head;
        for (int i = 0; i < pos - 1; i++) ant = ant->next;
        novo->next = ant->next;
        ant->next = novo;
        tamanho_lista++;
    }
}

Show* removerInicio() {
    if (tamanho_lista == 0) return NULL;
    Node* temp = head;
    Show* show = temp->show;
    head = head->next;
    free(temp);
    tamanho_lista--;
    if (tamanho_lista == 0) tail = NULL;
    return show;
}

Show* removerFim() {
    if (tamanho_lista == 0) return NULL;
    if (tamanho_lista == 1) {
        Show* show = head->show;
        free(head);
        head = tail = NULL;
        tamanho_lista = 0;
        return show;
    }
    Node* ant = head;
    while (ant->next != tail) ant = ant->next;
    Show* show = tail->show;
    free(tail);
    tail = ant;
    tail->next = NULL;
    tamanho_lista--;
    return show;
}

Show* remover(int pos) {
    if (tamanho_lista == 0 || pos < 0 || pos >= tamanho_lista) return NULL;
    if (pos == 0) return removerInicio();
    if (pos == tamanho_lista - 1) return removerFim();
    Node* ant = head;
    for (int i = 0; i < pos - 1; i++) ant = ant->next;
    Node* temp = ant->next;
    Show* show = temp->show;
    ant->next = temp->next;
    free(temp);
    tamanho_lista--;
    return show;
}

int cmp_cast(const void* a, const void* b) {
    return strcmp(*(const char**)a, *(const char**)b);
}

void print_show(Show* s) {
    char cast_sorted[300];
    strcpy(cast_sorted, s->cast);

    char* tokens[100];
    int count = 0;
    char* temp = strdup(cast_sorted);
    char* token = strtok(temp, ",");
    while (token && count < 100) {
        tokens[count] = trim(token);
        token = strtok(NULL, ",");
        count++;
    }
    if (count > 1) {
        qsort(tokens, count, sizeof(char*), cmp_cast);
    }
    cast_sorted[0] = '\0';
    for (int i = 0; i < count; i++) {
        strcat(cast_sorted, tokens[i]);
        if (i < count - 1) strcat(cast_sorted, ", ");
    }
    free(temp);

    char listed_in_copy[200];
    strcpy(listed_in_copy, s->listed_in);
    char* listed_tokens[50];
    int listed_count = 0;
    char* listed_temp = strtok(listed_in_copy, ",");
    while (listed_temp && listed_count < 50) {
        listed_tokens[listed_count++] = trim(listed_temp);
        listed_temp = strtok(NULL, ",");
    }

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
        strlen(s->duration) > 0 ? s->duration : "NaN"
    );
    for (int i = 0; i < listed_count; i++) {
        printf("%s", listed_tokens[i]);
        if (i < listed_count - 1) printf(", ");
    }
    printf("] ##\n");
}

// Circular queue of Show pointers (size 5)
#define TAM_FILA 5
Show* fila[TAM_FILA];
int inicio = 0, fim = 0, tamanho = 0;

void fila_inserir(Show* show) {
    int temp_tamanho = tamanho < TAM_FILA ? tamanho + 1 : TAM_FILA;
    int soma = 0, count = 0;
    int idx = inicio;
    if (tamanho == TAM_FILA) {
        idx = (inicio + 1) % TAM_FILA;
    }
    for (int i = 0; i < temp_tamanho - 1; i++, idx = (idx + 1) % TAM_FILA) {
        soma += fila[idx]->release_year;
    }
    soma += show->release_year;
    int media = (int)((soma / (double)temp_tamanho) + 0.5);
    printf("[Media] %d\n", media);

    if (tamanho == TAM_FILA) {
        Show* removido = fila[inicio];
        printf("(R) %s\n", removido->title);
        inicio = (inicio + 1) % TAM_FILA;
        tamanho--;
    }
    fila[fim] = show;
    fim = (fim + 1) % TAM_FILA;
    tamanho++;
}

Show* fila_remover() {
    if (tamanho == 0) return NULL;
    Show* removido = fila[inicio];
    inicio = (inicio + 1) % TAM_FILA;
    tamanho--;
    return removido;
}

void mostrar_fila() {
    int count = 0, idx = inicio;
    for (; count < tamanho; count++, idx = (idx + 1) % TAM_FILA) {
        printf("[%d] ", count);
        print_show(fila[idx]);
    }
}

void mostrar_lista() {
    Node* curr = head;
    while (curr) {
        print_show(curr->show);
        curr = curr->next;
    }
}

int main() {
    read_csv("/tmp/disneyplus.csv");

    char input[100];
    while (1) {
        if (!fgets(input, sizeof(input), stdin)) break;
        char* newline = strchr(input, '\n');
        if (newline) *newline = '\0';
        if (strcmp(input, "FIM") == 0) break;
        Show* show = find_show_by_id(input);
        if (show) inserirFim(show);
    }

    int n_cmds;
    scanf("%d\n", &n_cmds);
    for (int i = 0; i < n_cmds; i++) {
        char linha[200];
        fgets(linha, sizeof(linha), stdin);
        char cmd[10], arg1[100], arg2[100];
        int pos;
        if (sscanf(linha, "%s", cmd) == 1) {
            if (strcmp(cmd, "II") == 0) {
                sscanf(linha, "%*s %s", arg1);
                Show* show = find_show_by_id(arg1);
                if (show) inserirInicio(show);
            } else if (strcmp(cmd, "IF") == 0) {
                sscanf(linha, "%*s %s", arg1);
                Show* show = find_show_by_id(arg1);
                if (show) inserirFim(show);
            } else if (strncmp(cmd, "I*", 2) == 0) {
                sscanf(linha, "%*s %d %s", &pos, arg2);
                Show* show = find_show_by_id(arg2);
                if (show) inserir(show, pos);
            } else if (strcmp(cmd, "RI") == 0) {
                Show* removido = removerInicio();
                if (removido) printf("(R) %s\n", removido->title);
            } else if (strcmp(cmd, "RF") == 0) {
                Show* removido = removerFim();
                if (removido) printf("(R) %s\n", removido->title);
            } else if (strncmp(cmd, "R*", 2) == 0) {
                sscanf(linha, "%*s %d", &pos);
                Show* removido = remover(pos);
                if (removido) printf("(R) %s\n", removido->title);
            }
        }
    }

    mostrar_lista();

    return 0;
}
