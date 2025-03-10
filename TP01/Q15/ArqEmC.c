#include <stdio.h>

int main() {
    int n;
    double flo;
    FILE *file;
    scanf("%d", &n);
    
    file = fopen("teste.txt", "wb");
    
    for (int i = 0; i < n; i++) {
        scanf("%lf", &flo);
        fwrite(&flo, sizeof(double), 1, file);
    }
    fclose(file);
    
    file = fopen("teste.txt", "rb");
    
    fseek(file, 0, SEEK_END);
    long tamanho = ftell(file);
    long pos = tamanho - sizeof(double);
    
    while (pos >= 0) {
        fseek(file, pos, SEEK_SET);
        fread(&flo, sizeof(double), 1, file);
        
        if (flo == (long) flo) {
            printf("%.1f\n", flo); // Imprime 10.0 se for inteiro
        } else {
            printf("%g\n", flo); // Imprime o número original
        }
        pos -= sizeof(double);
    }
    
    fclose(file);
    return 0;
}
