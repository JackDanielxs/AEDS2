#include <stdio.h>

int main(){
    int n, k;

    while(scanf("%d %d", &n, &k) == 2){

        int notas[n];

        for(int i = 0; i < n; i++){
            scanf("%d", &notas[i]);
        }

        // sort
        for(int i = 0; i < n - 1; i++){
            int menor = i;
            for(int j = i + 1; j < n; j++){
                if(notas[j] < notas[menor])
                    menor = j; 

                // swap
                int tmp = notas[i];
                notas[i] = notas[menor];
                notas[menor] = tmp;
            }
        }

        int soma = 0;

        for(int i = n - 1 ; i >= n - k; i--){
            soma += notas[i];
        }
        printf("%d", soma);
    }
    
    return 0;
}