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

    public static void main(String[] args) throws IOException {
        //Interface para o usuário
        Scanner in = new Scanner(System.in);
        String nomeArquivo = null;
        String nomeArquivoSaida = null;

        System.out.println("Implementação da Técnica de Correção de Erros: Paridade Par\n");
        System.out.println("Escolha o programa a ser executado:");
        System.out.println("1.Codificador\n2.Decodificador\n3.Sair\n");

        int opcao = in.nextInt();
        in.nextLine();

        while (opcao != 3) {

            switch (opcao) {
                case 1:
                    System.out.println("Codificador!");
                    System.out.println("Digite o nome do arquivo de entrada com sua extensão e pressione ENTER:");

                    try {

                        nomeArquivo = in.nextLine();
                        System.out.println("Digite o nome do arquivo de saída com sua extensão e pressione ENTER:");
                        nomeArquivoSaida = in.nextLine();
                        Codificador.codificar(nomeArquivo, nomeArquivoSaida);
                    } catch (FileNotFoundException e) {
                        System.err.println("Arquivo não encontrado!!");
                    }

                    break;

                case 2:
                    System.out.println("Decodificador!");
                    System.out.println("Digite o nome do arquivo de entrada com sua extensão e pressione ENTER:");

                    try {
                        nomeArquivo = in.nextLine();
                        System.out.println("Digite o nome do arquivo de saída com sua extensão e pressione ENTER:");
                        nomeArquivoSaida = in.nextLine();
                        Decodificador.decodificar(nomeArquivo, nomeArquivoSaida);
                    } catch (FileNotFoundException e) {
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

            System.out.println("Escolha o programa a ser executado:");
            System.out.println("1.Codificador\n2.Decodificador\n3.Sair\n");

            opcao = in.nextInt();
            in.nextLine();

        }

    }

}
