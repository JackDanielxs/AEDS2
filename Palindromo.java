class Palindromo {

    public static boolean Fim(String s){
        return s.length() == 3 && s.charAt(0) == 'F' && s.charAt(1) == 'I' && s.charAt(2) == 'M';
    }

    public static String IsPalindromo(String s, int esq, int dir){
        while(esq <= dir){
            if(s.charAt(esq) != s.charAt(dir))
                return "NAO";
            else
                IsPalindromo(s, esq++, dir--);
        }
        return "SIM";
    } 

    public static void main(String[] args){
        String[] str = new String[1000];
        int n = 0;

        do{
            str[n] = MyIO.readLine();
        }while(!Fim(str[n++]));
        n--;

        for(int i = 0; i < n; i++){
            MyIO.println("%s\n", IsPalindromo(str[i], 0, str[i].length() - 1));
        }
    }

}