package Trabalho2;
import java.util.List;

public class App 
{
    final static String arquivo = "assembly.asm";
    
    public static void main( String[] args )
    {
        Codifica c = new Codifica();
        List<String> linhas = c.codifica(arquivo);
        for(String l:linhas)
            System.out.println(l);
        ConversorMIPS.escreveArquivo(); 
    }
}
