package Trabalho2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ConversorMIPS {

    public static List<String> leArquivo(String arquivo){
        try {
            List<String> linhas = new ArrayList<>();
            FileReader arq = new FileReader(arquivo);
            BufferedReader lerArq = new BufferedReader(arq);
            String linha = lerArq.readLine();         
            while (linha != null) {
                linhas.add(linha.replace(",", "").replace("$", "").replace("(", " ").replace(")", ""));
                linha = lerArq.readLine(); 
            }
            arq.close();
            return linhas;
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
            return null;
        }
    }

    public static void escreveArquivo(List<String> linhas){
        try {
            FileWriter arq = new FileWriter("output");
            PrintWriter gravarArq = new PrintWriter(arq);
            for(String l:linhas){
                gravarArq.println(l);
            }
            gravarArq.close();
        } catch (IOException e) {
            System.err.printf("Erro ao escrever o arquivo: %s. \n", e.getMessage());
        }
    }   

    public static String adicionarZeros(String s, int tam){
        for(int i = 0; i < s.length(); i++){
            if(s.length() < tam)
                s = "0" + s;
        }
        return s;
    }
}


