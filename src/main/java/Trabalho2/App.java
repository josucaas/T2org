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
            String linhaCodificada;
            int tamanho = args.length;
            String binarioOpcode = opcode.get(args[0]);
            String binarioFuncao = funcao.get(args[0]);
            String tipo = tipos.get(args[0]);
            switch(tipo){
                case "1":

                    for(int i = 1; i < tamanho; i++){
                        linhaCodificada = "" + argumentos.get(args[i]);

                    }
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
        //tipo 1: opcode rs rt rd shamt function
        tipos.put("and", "1");
        tipos.put("sll", "1");
        tipos.put("srl", "1");
        tipos.put("xor", "1");
        tipos.put("addu", "1");
        tipos.put("slt", "1");
        //tipo 2: opcode rs rt imm
        tipos.put("lui", "2"); // 2 args - mudar tipo
        tipos.put("addiu", "2");
        tipos.put("beq", "2");
        tipos.put("bne", "2");
        tipos.put("ori", "2");
        tipos.put("andi", "2");
        tipos.put("j", "3");
        tipos.put("jr", "4");

        //tipo 4: opcode rs rt Offset
        tipos.put("lw", "4");
        tipos.put("sw", "4");
               
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
        argumentos.put("zero", "00000");
        argumentos.put("at", "00001");
        argumentos.put("v0", "00010");
        argumentos.put("a0", "00100");
        argumentos.put("a1", "00101");
        argumentos.put("a2", "00110");
        argumentos.put("a3", "00111");
        argumentos.put("t0", "01000");
        argumentos.put("t1", "01001");
        argumentos.put("t2", "01010");
        argumentos.put("t3", "01011");
        argumentos.put("t4", "01100");
        argumentos.put("t5", "01101");
        argumentos.put("t6", "01110");
        argumentos.put("t7", "01111");
        argumentos.put("s0", "10000");
        argumentos.put("s1", "10001");
        argumentos.put("s2", "10010");
        argumentos.put("s3", "10011");
        argumentos.put("s4", "10100");
        argumentos.put("s5", "10101");
        argumentos.put("s6", "10110");
        argumentos.put("s7", "10111");
        argumentos.put("t8", "11000");
        argumentos.put("t9", "11001");
        argumentos.put("k0", "11010");
        argumentos.put("k1", "11011");
        argumentos.put("gp", "11100");
        argumentos.put("sp", "11101");
        argumentos.put("fp", "11110");
        argumentos.put("ra", "11111");
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
