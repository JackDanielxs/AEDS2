#include <stdlib.h>
#include <stdio.h>
#define bool   short
#define true   1
#define false  0

// def nó
typedef struct No {
      int elemento;
	struct No *esq, *dir;
} No;

// Criar nó
No* novoNo(int elemento) {
   No* novo = (No*) malloc(sizeof(No));
   novo->elemento = elemento;
   novo->esq = NULL;
   novo->dir = NULL;
   return novo;
}

No* raiz;

void start() {
   raiz = NULL;
}

// Metodo publico iterativo para pesquisar elemento.
bool pesquisar(int x) {
   return pesquisarRec(x, raiz);
}

// Metodo privado recursivo para pesquisar elemento.
bool pesquisarRec(int x, No* i) {
   bool resp;
   if (i == NULL) {
      resp = false;

   } else if (x == i->elemento) {
      resp = true;

   } else if (x < i->elemento) {
      resp = pesquisarRec(x, i->esq);

   } else {
      resp = pesquisarRec(x, i->dir);
   }
   return resp;
}


//Metodo publico iterativo para exibir elementos.
void caminharCentral() {
   printf("[ ");
   caminharCentralRec(raiz);
   printf("]\n");
}

/**
 * Metodo privado recursivo para exibir elementos.
 * @param i No em analise.
 */
void caminharCentralRec(No* i) {
   if (i != NULL) {
      caminharCentralRec(i->esq);
      printf("%d ", i->elemento);
      caminharCentralRec(i->dir);
   }
}

// preordem
void caminharPre() {
   printf("[ ");
   caminharPreRec(raiz);
   printf("]\n");
}
void caminharPreRec(No* i) {
   if (i != NULL) {
      printf("%d ", i->elemento);
      caminharPreRec(i->esq);
      caminharPreRec(i->dir);
   }
}

// posordem
void caminharPos() {
   printf("[ ");
   caminharPosRec(raiz);
   printf("]\n");
}
void caminharPosRec(No* i) {
   if (i != NULL) {
      caminharPosRec(i->esq);
      caminharPosRec(i->dir);
      printf("%d ", i->elemento);
   }
}

// Metodo publico iterativo para inserir elemento.
void inserir(int x) {
   inserirRec(x, &raiz);
}

// Metodo privado recursivo para inserir elemento.
void inserirRec(int x, No** i) {
   if (*i == NULL) {
      *i = novoNo(x);

   } else if (x < (*i)->elemento) {
      inserirRec(x, &((*i)->esq));

   } else if (x > (*i)->elemento) {
      inserirRec(x, &((*i)->dir));

   } else {
      errx(1, "Erro ao inserir!");
   }
}

//Remover elemento
void remover(int x) {
   removerRec(x, &raiz);
}
void removerRec(int x, No** i) {
   if (*i == NULL) {
      errx(1, "Erro ao remover!");

   } else if (x < (*i)->elemento) {
      removerRec(x, &((*i)->esq));

   } else if (x > (*i)->elemento) {
      removerRec(x, &((*i)->dir));

   } else if ((*i)->dir == NULL) {
      No* del = *i;
      *i = (*i)->esq;
      free(del);

   } else if ((*i)->esq == NULL) {
      No* del = *i;
      *i = (*i)->dir;
      free(del);

   } else {
      maiorEsq(i, &((*i)->esq));
   }
}

// Trocar no removido pelo maiorEsq.
void maiorEsq(No** i, No** j) {
   if ((*j)->dir != NULL) {
      maiorEsq(i, &((*j)->dir));

   } else {
      No* del = *j;
      (*i)->elemento = (*j)->elemento;
      (*j) = (*j)->esq;
      free(del);
   }
}