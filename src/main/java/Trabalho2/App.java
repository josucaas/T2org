package Trabalho2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class App 
{
    final static String arquivo = "assembly.asm";
    static HashMap<String, String> tipos;
    static HashMap<String, String> opcode;
    static HashMap<String, String> funcao;
    static HashMap<String, String> argumentos;
    public static void main( String[] args )
    {
        populaFuncao();
        populaTipos();
        populaOpcode();
        List<String> linhas = leArquivo();
        for(String s:linhas)
            System.out.println(s);
        escreveArquivo();
    }

    public static List<String> codifica(List<String> linhas) {
        List<String> linhasCodificadas = new ArrayList<String>();
        for(String l:linhas){
            String[] args = l.split(" ");
            int tamanho = args.length;
            String binarioOpcode = opcode.get(args[0]);
            String binarioFuncao = funcao.get(args[0]);
            String tipo = tipos.get(args[0]);
            switch(tipo){
                case "1":
                    break;
                case "2":
                    break;
                case "3":
                    break;
                case "4":
                    break;
            }
        } 
        return linhasCodificadas;
    }

    public static void populaTipos(){
        tipos = new HashMap<String,String>();
        tipos.put("xor", "1");
        tipos.put("lui", "2");
        tipos.put("addu", "1");
        tipos.put("addiu", "2");
        tipos.put("lw", "2");
        tipos.put("sw", "2");
        tipos.put("beq", "2");
        tipos.put("bne", "2");
        tipos.put("slt", "1");
        tipos.put("j", "3");
        tipos.put("jr", "4");
        tipos.put("ori", "2");
        tipos.put("and", "1");
        tipos.put("andi", "2");
        tipos.put("sll", "1");
        tipos.put("srl", "1");
    }

    public static void populaOpcode(){
        opcode = new HashMap<String,String>();
        opcode.put("xor", "000000");
        opcode.put("lui", "001111");
        opcode.put("addu", "000000");
        opcode.put("addiu", "001001");
        opcode.put("lw", "100011");
        opcode.put("sw", "101011"); 
        opcode.put("beq", "000100");       
        opcode.put("bne", "000101");
        opcode.put("slt", "000000");
        opcode.put("j", "000010");
        opcode.put("jr", "000000");
        opcode.put("ori", "001101");
        opcode.put("and", "000000");
        opcode.put("andi", "001100");
        opcode.put("sll", "000000");
        opcode.put("srl", "000000");    
    }

    public static void populaFuncao(){
        funcao = new HashMap<String,String>();
        funcao.put("xor", "100110"); //38
        funcao.put("lui", "001111"); // -
        funcao.put("addu", "100001"); //33
        funcao.put("addiu", null);// -
        funcao.put("lw", null);   // -
        funcao.put("sw", null);   // -
        funcao.put("beq", null);  // -  
        funcao.put("bne", null); // -
        funcao.put("slt", "101010"); //0x2a 42
        funcao.put("j", null);   // -
        funcao.put("jr", "001000");  // 0x8
        funcao.put("ori", null); // -
        funcao.put("and", "100100"); //0x24
        funcao.put("andi", null);// -
        funcao.put("sll", "000000"); //0
        funcao.put("srl", "000010"); //2
    }

    public static void populaArgumentos(){
        argumentos = new HashMap<String,String>();
        argumentos.put("$zero", "00000");
        argumentos.put("$at", "00001");
        argumentos.put("$", "");
    }

    public static List<String> leArquivo(){
        try {
            List<String> linhas = new ArrayList<>();
            FileReader arq = new FileReader(arquivo);
            BufferedReader lerArq = new BufferedReader(arq);
            String linha = lerArq.readLine();         
            while (linha != null) {
                linhas.add(linha.replace(",", "").replace("$", ""));
                linha = lerArq.readLine(); 
            }
            arq.close();
            return linhas;
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
            return null;
        }
    }

    public static void escreveArquivo(){
        try {
            FileWriter arq = new FileWriter("nomedoArquivo");
            PrintWriter gravarArq = new PrintWriter(arq);
            gravarArq.println("teste :o");
            gravarArq.close();
        } catch (IOException e) {
            System.err.printf("Erro ao escrever o arquivo: %s. \n", e.getMessage());
        }
    }
}
