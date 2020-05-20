package Trabalho2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Decodifica extends ConversorMIPS{
    static HashMap<String, String> tipos;
    static HashMap<String, String> opcode;
    static HashMap<String, String> funcao;
    static HashMap<String, String> argumentos;
    private int label = 1;
    private int index = 0;
    private final int inicioPrograma = 4194304;


    public Decodifica(){
        populaFuncao();
        populaTipos();
        populaOpcode();
    }

    public List<String> decodifica(String arquivo){
        List<String> linhas = leArquivo(arquivo);
        List<String> linhasDecodificadas = new ArrayList<String>();
        for(String l:linhas){
            //000000 00000 01001 01000 01111 00000
            String linhaDecodificada = "";
            String linhaBinario = pegarHexadecimal(l, 32);
            String opcodeBinario = linhaBinario.substring(0,6);
            String funcaoBinario = linhaBinario.substring(26);
            if(opcodeBinario.equals("000000")){
                switch(funcaoBinario){
                    case "000000": //sll rd rt shamt
                        linhaDecodificada += funcao.get(funcaoBinario);
                        linhaDecodificada += decodificaTipo7(linhaBinario);
                    break;
                    case "000010":
                        linhaDecodificada += funcao.get(funcaoBinario);
                        linhaDecodificada += decodificaTipo7(linhaBinario);
                    break;
                    case "001000":
                        linhaDecodificada += funcao.get(funcaoBinario);
                        linhaDecodificada += " $" + Integer.parseInt(linhaBinario.substring(6,11), 2);
                    break;
                    default:
                        linhaDecodificada += funcao.get(funcaoBinario);
                        linhaDecodificada += decodificaTipo1(linhaBinario);
                    break;
                }
            }
            else if(opcodeBinario.equals("100011") || opcodeBinario.equals("101011")){
                linhaDecodificada += opcodeBinario.equals("100011") ? "lw" : "sw";
                linhaDecodificada += " $" + Integer.parseInt(linhaBinario.substring(11,16), 2);
                linhaDecodificada += ", 0x" + adicionarZeros(Integer.toString(Integer.parseInt(linhaBinario.substring(16,32), 2), 16), 8);
                linhaDecodificada += "($" + Integer.parseInt(linhaBinario.substring(6,11), 2) + ")";
            }
            else if(opcodeBinario.equals("001111")){
                linhaDecodificada += "lui ";
                linhaDecodificada += " $" + Integer.parseInt(linhaBinario.substring(11,16), 2);
                linhaDecodificada += ", 0x" + adicionarZeros(Integer.toString(Integer.parseInt(linhaBinario.substring(16,32), 2), 16), 8);
            }
            else if(opcodeBinario.equals("001101")){
                linhaDecodificada += "ori";
                linhaDecodificada += " $" + Integer.parseInt(linhaBinario.substring(6,11), 2);
                linhaDecodificada += ", $" + Integer.parseInt(linhaBinario.substring(11,16), 2);
                linhaDecodificada += ", 0x" + adicionarZeros(Integer.toString(Integer.parseInt(linhaBinario.substring(16,32), 2), 16), 8);
            }
            else if(opcodeBinario.equals("000010")){
                linhaDecodificada += "j ";
                String target = linhaBinario.substring(6);
                target = "0000" + target + "00";
                linhaDecodificada += adicionarLabel(target, linhasDecodificadas);//" 0x" + adicionarZeros(Integer.toString(Integer.parseInt(target, 2), 16), 8);
                
            }
            else if(opcodeBinario.equals("000100") || opcodeBinario.equals("000101")){
                linhaDecodificada += opcodeBinario.equals("000100") ? "beq" : "bne";
                linhaDecodificada += " $" + Integer.parseInt(linhaBinario.substring(6,11), 2);
                linhaDecodificada += ", $" + Integer.parseInt(linhaBinario.substring(11,16), 2);
                linhaDecodificada += ", " + adicionarLabelBranch(Integer.valueOf(linhaBinario.substring(16,32), 2).shortValue(), linhasDecodificadas);
            }
            if(index < linhasDecodificadas.size()){
                linhaDecodificada = linhasDecodificadas.get(index) + "" + linhaDecodificada;                
                linhasDecodificadas.remove(index);
            }
            linhasDecodificadas.add(index, linhaDecodificada);
            index++;
        }
        //escrever no array
        return linhasDecodificadas;

    }

    
    private String adicionarLabel(String target, List<String> arrayList){
        String labelString = "Label_" + label;
        label++;
        int targetDecimal = Long.valueOf(target, 2).intValue();
        int posicaoLabel = (targetDecimal - inicioPrograma)/4;
        // if(arrayList.size() < posicaoLabel){              maquina do tempo
        //     for(int i = arrayList.size(); i <= posicaoLabel; i++){
        //         arrayList.add("");
        //     }
        // }
        String targetLinha = arrayList.get(posicaoLabel);
        arrayList.remove(posicaoLabel);
        arrayList.add(posicaoLabel, labelString + ":" + targetLinha);
        return labelString;
    }

    private String adicionarLabelBranch(int target, List<String> arrayList){
        String labelString = "Label_" + label;
        label++;
        int posicaoLabel = arrayList.size() + target;
        if(target > 0){              //maquina do tempo
            for(int i = arrayList.size(); i <= posicaoLabel; i++){
                arrayList.add("");
            }
        }
        String targetLinha = arrayList.get(posicaoLabel);
        arrayList.remove(posicaoLabel);
        arrayList.add(posicaoLabel, labelString + ":" + targetLinha);
        return labelString;

    }

    private String decodificaTipo1(String linhaBinario){
        String linhaCodificada = " $" + Integer.parseInt(linhaBinario.substring(16,21), 2);
        linhaCodificada += ", $" + Integer.parseInt(linhaBinario.substring(6,11), 2);
        return linhaCodificada += ", $" + Integer.parseInt(linhaBinario.substring(11,16), 2);
    }

    private String decodificaTipo7(String linhaBinario){
        String linhaCodificada = " $" + Integer.parseInt(linhaBinario.substring(16,21), 2);
        linhaCodificada += ", $" + Integer.parseInt(linhaBinario.substring(11,16), 2);
        return linhaCodificada += ", 0x" + adicionarZeros(Integer.toString(Integer.parseInt(linhaBinario.substring(21,26), 2), 16), 8);
    }

    private String pegarHexadecimal(String hexa, int zeros){
        Long offsetDecimal = Long.parseLong(hexa.replace("0x", ""), 16);
        //int offsetDecimal = Long.valueOf(hexa.replace("0x", ""), 16).intValue();
        return adicionarZeros(Long.toString(offsetDecimal, 2), zeros);
    }

    public static void populaTipos(){
        tipos = new HashMap<String,String>();
        //tipo 1: opcode rs rt rd shamt function
        tipos.put("and", "1"); // todos feitos
        tipos.put("xor", "1");
        tipos.put("addu", "1");
        tipos.put("slt", "1");
        
        //tipo 2: 4 args - beq rs, rt, label - opcode rs rt imm
        //tipos.put("addiu", "2"); apenas decodificacao
        tipos.put("beq", "2");
        tipos.put("bne", "2");
        tipos.put("ori", "2");// .-.
        //tipos.put("andi", "2"); apenas deco

        //tipo 3: 1 arg - j target - opcode target
        tipos.put("j", "3");

        //tipo 4: 1 arg - jr rs - opcode rs 0 0x8
        tipos.put("jr", "4"); // '-'


        //tipo 5: opcode rs rt Offset
        tipos.put("lw", "5"); //ambos feitos
        tipos.put("sw", "5");
        
        //tipo 6: 2 args - lui rt, imm - 0xf 0 rt imm
        tipos.put("lui", "6"); // ja era '-'

        //tipo 6: 3 args - igual o tipo 1 porem usa o shamt
        tipos.put("sll", "7"); // ambos feitos
        tipos.put("srl", "7");

    }

    public static void populaOpcode(){
        opcode = new HashMap<String,String>();
        opcode.put("000000", "xor");
        opcode.put("001111", "lui");
        opcode.put("000000", "addu");
        opcode.put("001001", "addiu");
        opcode.put("100011", "lw");
        opcode.put("101011", "sw"); 
        opcode.put("000100", "beq");       
        opcode.put("000101", "bne");
        opcode.put("000000", "slt");
        opcode.put("000010", "j");
        opcode.put("000000", "jr");
        opcode.put("001101", "ori");
        opcode.put("000000", "and");
        opcode.put("001100", "andi");
        opcode.put("000000", "sll");
        opcode.put("000000", "srl");    
    }

    public static void populaFuncao(){
        funcao = new HashMap<String,String>();
        funcao.put("100110", "xor"); //38
        funcao.put("001111", "lui"); // -
        funcao.put("100001", "addu"); //33
        funcao.put("addiu", null);// -
        funcao.put("lw", null);   // -
        funcao.put("sw", null);   // -
        funcao.put("beq", null);  // -  
        funcao.put("bne", null); // -
        funcao.put("101010", "slt"); //0x2a 42
        funcao.put("j", null);   // -
        funcao.put("001000", "jr");  // 0x8
        funcao.put("ori", null); // -
        funcao.put("100100", "and"); //0x24
        funcao.put("andi", null);// -
        funcao.put("000000", "sll"); //0
        funcao.put("000010", "srl"); //2
    }
}