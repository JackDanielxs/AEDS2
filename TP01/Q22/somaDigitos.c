#include <stdio.h>

int somarDigitos(int n){
    if(n == 0)
        return 0;
    return n % 10 + somarDigitos(n / 10);
}

int main(){
    int num;
    scanf("%d", &num);
    printf("%d", somarDigitos(num));
    return 0;
}