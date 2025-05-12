import java.util.*;

public class Paciente {
    
    public static ArrayList<Paciente> lista = new ArrayList<Paciente>();
    public static int tempoPassado = 420; // Começa 7h
    public static int criticos = 0;
    
    public int hora;
    public int minuto;
    public int saude;
    
    public Paciente(int h, int m, int s) {
        this.hora = h;
        this.minuto = m;
        this.saude = s;
    }
    
    public int calcularTempoRest() {
        int chegada = hora * 60 + minuto;
        int tempoRestante = chegada + saude - tempoPassado;
        return tempoRestante;
    }
    
    public void atendimento() {
        int chegada = hora * 60 + minuto;
        if (tempoPassado < chegada)
            tempoPassado = chegada; // Espera até o paciente chegar

        int tempo = calcularTempoRest();
        if (tempo < 0) 
            criticos += 1; // Entrou em estado crítico

        tempoPassado += 30; // Atendimento leva 30 min
    }
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        for(int i = 0; i < n; i++){
            int h = sc.nextInt();
            int m = sc.nextInt();
            int s = sc.nextInt();
            Paciente p = new Paciente(h, m, s);
            lista.add(p); 
        } // Preenche a lista
        
        for(Paciente p : lista){
            p.atendimento();
        }
        
        System.out.println(criticos);
    }
}