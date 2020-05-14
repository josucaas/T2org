package Trabalho2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App 
{
    final static String arquivo = "assembly.asm";
    public static void main( String[] args )
    {
        List<String> linhas = leArquivo();
        for(String s:linhas)
            System.out.println(s);
    }
    

    public static List<String> leArquivo(){
        try {
            List<String> linhas = new ArrayList<>();
            FileReader arq = new FileReader(arquivo);
            BufferedReader lerArq = new BufferedReader(arq);
            String linha = lerArq.readLine();         
            while (linha != null) {
                linhas.add(linha);
                linha = lerArq.readLine(); 
            }
            arq.close();
            return linhas;
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
            return null;
        }
    }
}
