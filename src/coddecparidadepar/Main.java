/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coddecparidadepar;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author danielmarinho
 */
public class Main {
    
    public static void main(String [] args) throws IOException{
        
        System.out.println("Escolha o programa a ser executado:");
        System.out.println("1.Codificador\n2.Decodificador\n3.Sair\n");
        
        Scanner in = new Scanner(System.in);
        String nomeArquivo = null; //arquivoCod = nomeArquivo[0] / arquivoDecod = nomeArquivo[1]
        
        int opcao = in.nextInt();
        
        switch(opcao){
            case 1:
                System.out.println("Codificador!");
                System.out.println("Digite o nome do arquivo com sua extensão e pressione ENTER:");
                
                try{
                    nomeArquivo = in.nextLine();
                    Codificador.codificar(nomeArquivo);
                }
                catch(FileNotFoundException e){
                    System.err.println("Arquivo não encontrado!!");
                }
            
                break;
                
            case 2:
                System.out.println("Decodificador!");
                System.out.println("Digite o nome do arquivo com sua extensão e pressione ENTER:");
                
                try{
                    nomeArquivo = in.nextLine();
                    Codificador.codificar(nomeArquivo);
                }
                catch(FileNotFoundException e){
                    System.err.println("Arquivo não encontrado!!");
                }
            
                break;
                
            case 3:
                System.out.println("Tchau!");
                System.exit(0);
                
                break;
                
            default:
                System.out.println("Opção inválida!");
                
                break;
        }
        
        
        
        String nomeArq = in.nextLine();
    }
    
}
