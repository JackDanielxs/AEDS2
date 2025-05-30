#include <stdio.h>

float qtdNotas(float val){
    int qtd100 = 0, qtd50 = 0, qtd20 = 0, qtd10 = 0, qtd5 = 0, qtd2 = 0;
    while(val >= 100.0){
        qtd100++;
        val-= 100.0;
    }
    while(val >= 50.0){
        qtd50++;
        val-= 50.0;
    }
    while(val >= 20.0){
        qtd20++;
        val-= 20.0;
    }
    while(val >= 10.0){
        qtd10++;
        val-= 10.0;
    }
    while(val >= 5.0){
        qtd5++;
        val-= 5.0;
    }
    while(val >= 2.0){
        qtd2++;
        val-= 2.0;
    }
    printf("NOTAS:\n");
    printf("%d nota(s) de R$ 100.00\n", qtd100);
    printf("%d nota(s) de R$ 50.00\n", qtd50);
    printf("%d nota(s) de R$ 20.00\n", qtd20);
    printf("%d nota(s) de R$ 10.00\n", qtd10);
    printf("%d nota(s) de R$ 5.00\n", qtd5);
    printf("%d nota(s) de R$ 2.00\n", qtd2);
    return val; // retornar o valor para atualizar na main
}

void qtdMoedas(float val){
    int m100 = 0, m50 = 0, m25 = 0, m10 = 0, m5 = 0, m1 = 0;

    while(val >= 1.0){
        m100++;
        val-= 1.0;
    }
    while(val >= 0.5){
        m50++;
        val-= 0.5;
    }
    while(val >= 0.25){
        m25++;
        val-= 0.25;
    }
    while(val >= 0.10){
        m10++;
        val-= 0.10;
    }
    while(val >= 0.05){
        m5++;
        val-= 0.05;
    }
    while(val > 0.00){
        m1++;
        val-= 0.01;
    }
    
    printf("MOEDAS:\n");
    printf("%d moeda(s) de R$ 1.00\n", m100);
    printf("%d moeda(s) de R$ 0.50\n", m50);
    printf("%d moeda(s) de R$ 0.25\n", m25);
    printf("%d moeda(s) de R$ 0.10\n", m10);
    printf("%d moeda(s) de R$ 0.05\n", m5);
    printf("%d moeda(s) de R$ 0.01", m1);
}

int main(){
    float valor;
    scanf("%f", &valor);
    valor = qtdNotas(valor);
    qtdMoedas(valor); // valor = somente os centavos
    return 0;
}