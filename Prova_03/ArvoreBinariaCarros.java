import java.util.*;

class Carro { 
    String placa, modelo, tipo, chassi;

    public void ler(String linha) { 
        String[] partes = linha.split(",");
        placa = partes[0]; 
        modelo = partes[1]; 
        tipo = partes[2]; 
        chassi = partes[3];
    }
    
    public void imprimir() {
        System.out.println(placa + " " + modelo + " " + tipo + " " + chassi);
    }
}

class No {
    Carro elemento;
    No esq, dir;

    public No(Carro c){
        elemento = c;
        esq = dir = null;
    }
}

public class ArvoreBinariaCarros{
    private No raiz;
    private int totalNos = 0;
    private int totalFolha = 0;

    public ArvoreBinariaCarros() {
		raiz = null;
	}

    public int getAltura(){
        return getAltura(raiz, 0);
    }

    public int getAltura(No i, int altura){
        if(i == null){
           altura--;
        } else {
           int alturaEsq = getAltura(i.esq, altura + 1);
           int alturaDir = getAltura(i.dir, altura + 1);
           altura = (alturaEsq > alturaDir) ? alturaEsq : alturaDir;
        }
        return altura;
    }

    public void inserir(Carro c) {
		raiz = inserir(c, raiz);
	}
	private No inserir(Carro c, No i) {
		if (i == null) {
            i = new No(c);
        } else if (c.placa.compareToIgnoreCase(i.elemento.placa) < 0) {
            i.esq = inserir(c, i.esq);
        } else if (c.placa.compareToIgnoreCase(i.elemento.placa) > 0) {
            i.dir = inserir(c, i.dir);
        }
		return i;
	}

    public void contaNos() {
        contaNos(raiz);
	}
	private void contaNos(No i) {
		if (i != null) {
			contaNos(i.esq);
			contaNos(i.dir);
            if(i.dir == null && i.esq == null)
                totalFolha += 1;
			totalNos += 1;
		}
	}

    public void print(){
        System.out.println("Altura da arvore: " + getAltura());
        System.out.println("Total de nos: " + totalNos);
        System.out.println("Total de folhas: " + totalFolha);
        System.out.println("");
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        String entrada = "";
        ArvoreBinariaCarros arv = new ArvoreBinariaCarros();
        do{
            entrada = sc.nextLine();

            if(entrada.compareToIgnoreCase("FIM") != 0){
                Carro c = new Carro();
                c.ler(entrada);
                arv.inserir(c);
            }

        }while(entrada.compareToIgnoreCase("FIM") != 0);

        arv.contaNos();
        arv.print();
        sc.close();
    }
}