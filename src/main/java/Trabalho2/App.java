package Trabalho2;

import java.util.List;
import java.util.Scanner;

public class App 
{
    final static String arquivoCode = "Code.asm";
    final static String arquivoDecode = "Decode.asm";
    public static void main( String[] args )
    {
        Scanner in = new Scanner(System.in);
        System.out.println("Escolha a operaçao.");
        System.out.println("Codifica = 1, Decodifica = 2");
        int codigo = in.nextInt();
        if(codigo == 1){
            Codifica c = new Codifica();
            List<String> linhas = c.codifica(arquivoCode);
            // for(String l:linhas)
            //     System.out.println(l);
            ConversorMIPS.escreveArquivo(linhas); 
            System.out.println("Codificaçao concluída.");
        }
        else if(codigo == 2){
            Decodifica d = new Decodifica();
            List<String> linhas = d.decodifica(arquivoDecode);
            // for(String l:linhas)
            //     System.out.println(l);
            ConversorMIPS.escreveArquivo(linhas);
            System.out.println("Decodificaçao concluída.");
        }
        else{
            System.out.println("Código inválido");
            System.exit(0);
        }
    }
}
