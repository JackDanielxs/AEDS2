#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define TAMANHO_TABELA 50

// elemento tabela hash
typedef struct No {
    char *chave; // palavra
    struct No *proximo;
} No;

// Tabela Hash
typedef struct TabelaHash {
    No **tabela;
    int tamanho;
} TabelaHash;

unsigned int funcaoHash(const char *chave, int tamanho) {
    unsigned long int hash = 0;
    int c;

    while ((c = *chave++)) {
        hash += c;
    }

    return hash % tamanho;
}

TabelaHash* criarTabelaHash(int tamanho) {
    // Aloca memória para a estrutura da tabela hash
    TabelaHash *th = (TabelaHash*) malloc(sizeof(TabelaHash));
    if (th == NULL) {
        return NULL;
    }

    th->tamanho = tamanho;

    th->tabela = (No**) calloc(tamanho, sizeof(No*));
    if (th->tabela == NULL) {
        free(th);
        return NULL;
    }

    return th;
}

void inserir(TabelaHash *th, const char *chave) {
    // 1. Calcula o índice da chave
    unsigned int indice = funcaoHash(chave, th->tamanho);

    // 2. Verifica se a chave já existe na lista deste índice
    No *atual = th->tabela[indice];
    while (atual != NULL) {
        if (strcmp(atual->chave, chave) == 0) {
            return; // Palavra já existe
        }
        atual = atual->proximo;
    }

    // 3. Cria um novo nó para a palavra
    No *novoNo = (No*) malloc(sizeof(No));
    if (novoNo == NULL) {
        return;
    }
    // strdup aloca memória para a string e a copia
    novoNo->chave = strdup(chave);
    if (novoNo->chave == NULL) {
        free(novoNo);
        return;
    }

    // 4. Insere o novo nó no início da lista encadeada naquele índice
    novoNo->proximo = th->tabela[indice];
    th->tabela[indice] = novoNo;
}

int buscar(TabelaHash *th, const char *chave) {
    // 1. Calcula o índice onde a palavra deveria estar
    unsigned int indice = funcaoHash(chave, th->tamanho);

    // 2. Percorre a lista encadeada naquele índice
    No *atual = th->tabela[indice];
    while (atual != NULL) {
        if (strcmp(atual->chave, chave) == 0) {
            return 1; // Encontrou
        }
        atual = atual->proximo;
    }

    return 0; // Não encontrou
}

void removerNo(TabelaHash *th, const char *chave) {
    // 1. Calcula o índice da chave
    unsigned int indice = funcaoHash(chave, th->tamanho);

    No *atual = th->tabela[indice];
    No *anterior = NULL;

    // 2. Percorre a lista para encontrar o nó a ser removido
    while (atual != NULL && strcmp(atual->chave, chave) != 0) {
        anterior = atual;
        atual = atual->proximo;
    }

    // palavra não encontrada
    if (atual == NULL) {
        return;
    }

    // 3. Remove o nó da lista
    if (anterior == NULL) {
        // O nó a ser removido é o primeiro da lista
        th->tabela[indice] = atual->proximo;
    } else {
        // O nó a ser removido está no meio ou no fim
        anterior->proximo = atual->proximo;
    }

    // 4. Libera a memória do nó removido
    free(atual->chave);
    free(atual);
}

void liberarTabelaHash(TabelaHash *th) {
    if (th == NULL) return;

    for (int i = 0; i < th->tamanho; i++) {
        No *atual = th->tabela[i];
        while (atual != NULL) {
            No *temp = atual;
            atual = atual->proximo;
            free(temp->chave);
            free(temp);
        }
    }

    free(th->tabela);
    free(th);
}


int main() {
    TabelaHash *th = criarTabelaHash(TAMANHO_TABELA);
    if (th == NULL) {
        return 1;
    }

    int escolha;
    char bufferChave[256]; // Buffer para ler a palavra do usuário

    inserir(th, bufferChave);
    removerNo(th, bufferChave);
    buscar(th, bufferChave);
    liberarTabelaHash(th);
    return 0;
}