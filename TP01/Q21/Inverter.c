#include <stdio.h>
#include <string.h>

int Fim(char* s) {
    return strcmp(s, "FIM") == 0;
}

void Reverter(char *str, int inicio, int fim){
    if (inicio >= fim) {
        return;
    }
    char temp = str[inicio];
    str[inicio] = str[fim];
    str[fim] = temp;
    Reverter(str, inicio + 1, fim - 1);
}

int main(){

    char palavra[1000];
    int fim;
    do{
        scanf(" %[^\n]", palavra);
        fim = Fim(palavra);

        if(!fim){
            Reverter(palavra, 0, strlen(palavra) - 1);
            printf("%s\n", palavra);
        }
        
    }while(!fim);
    return 0;
}