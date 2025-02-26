#include <stdio.h>
#include <string.h>
#include <stdlib.h>

int Fim(char* s) {
    return strcmp(s, "FIM") == 0;
}

int somarDigitos(int n){
    if(n == 0)
        return 0;
    return n % 10 + somarDigitos(n / 10); // soma os algarismos de trás pra frente
}

int main(){
    char str[100];
    int num;
    do {
        fgets(str, sizeof(str), stdin);
        str[strcspn(str, "\n")] = 0; // Remove o caractere de nova linha

        if (!Fim(str)) {
            num = atoi(str); // Converte string para inteiro
            printf("%d\n", somarDigitos(num));
        }
    } while (!Fim(str));
    return 0;
}