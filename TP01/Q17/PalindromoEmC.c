#include <stdio.h>
#include <string.h>

int Fim(char* s) {
    return strcmp(s, "FIM") == 0;
}

int IsPalindromo(char* s, int esq, int dir){
    int resp;
    if(esq >= dir)
        resp = 1;
    else if(s[esq] != s[dir])
        resp = 0;
    else
        resp = IsPalindromo(s, esq + 1, dir - 1);
    return resp;
}

int main(){
    char palavra[1000];
    int fim;
    do{
        scanf(" %[^\n]", palavra);
        fim = Fim(palavra);

        if(!fim)
            printf(IsPalindromo(palavra, 0, strlen(palavra) - 1) ? "SIM\n" : "NAO\n");
    }while(!fim);
    return 0;
}