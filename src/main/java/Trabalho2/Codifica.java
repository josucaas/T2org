package Trabalho2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Codifica extends ConversorMIPS {
    static HashMap<String, String> tipos;
    static HashMap<String, String> opcode;
    static HashMap<String, String> funcao;
    static HashMap<String, String> argumentos;

    public Codifica(){
        populaFuncao();
        populaTipos();
        populaOpcode();
    }

    

    public List<String> codifica(String arquivo) {
        List<String> linhas = leArquivo(arquivo);
        List<String> linhasCodificadas = new ArrayList<String>();
        for(String l:linhas){
            String[] args = l.split(" ");
            String linhaCodificada = "";
            String binarioOpcode = opcode.get(args[0]);
            linhaCodificada += binarioOpcode;
            String binarioFuncao = funcao.get(args[0]);
            String tipo = tipos.get(args[0]);
            switch(tipo){
                case "1": //and $8, $9, $11
                    linhaCodificada += pegarArgumento(args[2]);
                    linhaCodificada += pegarArgumento(args[3]);
                    linhaCodificada += pegarArgumento(args[1]);
                    linhaCodificada += "00000";
                    linhaCodificada += binarioFuncao;
                    break;

                case "2": //beq $8, $9, 0xfffffffd        
                    if(binarioOpcode == "001101"){ //no caso do ori, ele segue a mesma ideia dos tipo 2 porem o valor imm nao pode ser convertido pela funcao complementeDeDois                       
                        linhaCodificada += pegarArgumento(args[2]);
                        linhaCodificada += pegarArgumento(args[1]);
                        linhaCodificada += pegarHexadecimal(args[3]);
                    }
                    else{
                        linhaCodificada += adicionarZeros(Integer.toBinaryString(Integer.parseInt(args[1])), 5);
                        linhaCodificada += adicionarZeros(Integer.toBinaryString(Integer.parseInt(args[2])), 5);
                        linhaCodificada += pegarHexadecimalComplementoDe2(args[3]);
                    }
                    break;

                case "3": // j target
                    String offsetBinario = pegarHexadecimal(args[1], 32);
                    linhaCodificada += offsetBinario.substring(4, 30); // ignora os primeiros 4 e ultimos 2 bits kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk
                    break;

                case "4": // jr $31
                    linhaCodificada += pegarArgumento(args[1]);
                    linhaCodificada += "0000000000000000";
                    linhaCodificada += binarioFuncao;
                    break;

                case "5": // lw $8 0x00000000 $8 
                    linhaCodificada += pegarArgumento(args[3]);
                    linhaCodificada += pegarArgumento(args[1]);
                    linhaCodificada += pegarHexadecimal(args[2]);
                    break;

                case "6": // lui $1, 0x00001001
                    linhaCodificada += "00000";
                    linhaCodificada += pegarArgumento(args[1]);
                    linhaCodificada += pegarHexadecimal(args[2]);
                    break;

                case "7": //sll $8, $9, 0x0000000
                    linhaCodificada += "00000";
                    linhaCodificada += pegarArgumento(args[2]);
                    linhaCodificada += pegarArgumento(args[1]);
                    linhaCodificada += pegarHexadecimal(args[3], 5);
                    linhaCodificada += binarioFuncao;
                    break;
            }
            //System.out.println(linhaCodificada);
            Long linhaCodificadaDecimal = Long.parseLong(linhaCodificada, 2);
            linhasCodificadas.add("0x" + adicionarZeros(Long.toString(linhaCodificadaDecimal, 16), 8)); //readiciona um zero a esquerda que possa ter sido desconsiderado
            //linhasCodificadas.add("0x" + Long.toString(linhaCodificadaDecimal, 16));

        } 
        return linhasCodificadas;
    }

    //transforma o inteiro NEGATIVO em um binario de n digitos :o
    public static String complementoDeDois(int s, int n){
        String binario = adicionarZeros(Integer.toString(s, 2).replace("-", ""), n);
        binario = binario.replace("1", " ").replace("0", "1").replace(" ", "0");
        binario = Integer.toString(0b01 + Integer.parseInt(binario, 2), 2);
        if(binario.length() > 16){ //caso haja overflow descarta o bit mais significativo
            binario = binario.substring(1);
        }
        return adicionarZeros(binario, n); //adiciona zeros novamente caso a operacao de soma tenha desconsideraco zeros à esquerad, precisamos de uma plavra de 16 bits
    }

    

    private String pegarArgumento(String arg){
        return adicionarZeros(Integer.toBinaryString(Integer.parseInt(arg)), 5);
    }

    private String pegarHexadecimal(String hexa){
        int offsetDecimal = Long.valueOf(hexa.replace("0x", ""), 16).intValue();
        return adicionarZeros(Integer.toString(offsetDecimal, 2), 16);
    }

    private String pegarHexadecimal(String hexa, int zeros){
        int offsetDecimal = Long.valueOf(hexa.replace("0x", ""), 16).intValue();
        return adicionarZeros(Integer.toString(offsetDecimal, 2), zeros);
    }

    private String pegarHexadecimalComplementoDe2(String hexa){
        int offsetDecimal = Long.valueOf(hexa.replace("0x", ""), 16).intValue();
        return complementoDeDois(offsetDecimal, 16);
    }

    public static void populaTipos(){
        tipos = new HashMap<String,String>();
        //tipo 1: opcode rs rt rd shamt function
        tipos.put("and", "1");
        tipos.put("xor", "1");
        tipos.put("addu", "1");
        tipos.put("slt", "1");
        
        //tipo 2: 4 args - beq rs, rt, label - opcode rs rt imm
        //tipos.put("addiu", "2"); apenas decodificacao
        tipos.put("beq", "2");
        tipos.put("bne", "2");
        tipos.put("ori", "2");
        //tipos.put("andi", "2"); apenas deco

        //tipo 3: 1 arg - j target - opcode target
        tipos.put("j", "3");

        //tipo 4: 1 arg - jr rs - opcode rs 0 0x8
        tipos.put("jr", "4");


        //tipo 5: opcode rs rt Offset
        tipos.put("lw", "5");
        tipos.put("sw", "5");
        
        //tipo 6: 2 args - lui rt, imm - 0xf 0 rt imm
        tipos.put("lui", "6");

        //tipo 6: 3 args - igual o tipo 1 porem usa o shamt
        tipos.put("sll", "7");
        tipos.put("srl", "7");

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
        funcao.put("jr", "01000");  // 0x8
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
   
}