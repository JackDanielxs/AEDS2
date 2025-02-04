class LAB01Q01Aquecimento {

    public static boolean Fim(String s){
        return s.length() == 3 && s.charAt(0) == 'F' && s.charAt(1) == 'I' && s.charAt(2) == 'M';
    }

    public static boolean Maiusculo(char c){
        return c >= 'A' && c <= 'Z';
    }

    public static int Contar(String s, int pos){
        int resp = 0; 
        if(pos < s.length()){
           if(Maiusculo(s.charAt(pos))){
              resp = 1 + Contar(s, pos + 1);
           } else {
              resp = Contar(s, pos + 1);
           }
        }
        return resp;
     }

    public static void main(String[] args){
        String[] str = new String[1000];
        int n = 0;

        do{
            str[n] = MyIO.readLine();
        }while(!Fim(str[n++]));
        n--;

        for(int i = 0; i < n; i++){
            MyIO.println(Contar(str[i], 0));
        }
    }
}