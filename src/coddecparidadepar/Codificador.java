/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coddecparidadepar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.BitSet;
import java.util.Scanner;

/**
 *
 * @author danielmarinho
 */
public class Codificador {

    public static BitSet calculaVerificadorVertical(BitSet bs) {//Calcula as paridades das linhas da matriz, criando um vetor verificador de paridade "vertical"
        int paridade;
        BitSet verificadorVertical = new BitSet(8);
        for (int i = 0; i < 64; i = i + 8) {//Como usamos BitSet, que é um vetor linear, adaptamos a verificação para funcionar como funcionaria numa matriz
            paridade = 0;
            for (int j = 0; j < 8; j++) {
                paridade = paridade + ((bs.get(i + j)) ? 1 : 0);
            }
            if (paridade % 2 == 0) {
                verificadorVertical.set(i / 8, false);
            } else {
                verificadorVertical.set(i / 8, true);
            }
        }

        return verificadorVertical;
    }

    public static BitSet calculaVerificadorHorizontal(BitSet bs) {//Calcula as paridades das colunas da matriz, criando um vetor verificador de paridade "horizontal"
        int paridade;
        BitSet verificadorHorizontal = new BitSet(8);
        for (int i = 0; i < 8; i++) {//Como usamos BitSet, que é um vetor linear, adaptamos a verificação para funcionar como funcionaria numa matriz
            paridade = 0;
            for (int j = 0; j < 64; j = j + 8) {
                paridade = paridade + ((bs.get(i + j)) ? 1 : 0);
            }
            if (paridade % 2 == 0) {
                verificadorHorizontal.set(i, false);
            } else {
                verificadorHorizontal.set(i, true);
            }
        }
        return verificadorHorizontal;
    }

    public static void codificar(String nomeArq, String nomeArqSaida) throws FileNotFoundException, IOException {

        //Cria e abre o stream dos arquivos de entrada e saida
        File arquivo = new File(nomeArq);
        File saida = new File(nomeArqSaida);
        FileInputStream fis = new FileInputStream(arquivo);
        FileOutputStream fos = new FileOutputStream(saida);

        byte[] bytesIn = new byte[8];
        byte[] bytesOut = new byte[8];
        int bytesRead = fis.read(bytesIn, 0, 8);//Vamos ler de 10 em 10 bytes

        BitSet matrizBits = new BitSet(64);
        for (int i = 0; i < 64; i++) {//Preenche a matriz com zeros inicialmente
            matrizBits.set(i, false);
        }
        BitSet verificadorVerticalBits = new BitSet(8);//Vetores verificadores de paridade
        BitSet verificadorHorizontalBits = new BitSet(8);

        while (bytesRead != -1) {//Enquanto não terminar o arquivo

            matrizBits = BitSet.valueOf(bytesIn);//Passa os bytes lidos para a estrutura BitSet
            verificadorVerticalBits = calculaVerificadorVertical(matrizBits);
            verificadorHorizontalBits = calculaVerificadorHorizontal(matrizBits);

            bytesOut = verificadorHorizontalBits.toByteArray();//Caso o byte verificador seja zero, criar o vetor de byte correspondente, pois BitSet ignora bytes 0 finais
            if (bytesOut.length == 0) {
                bytesOut = new byte[1];
                bytesOut[0] = 0;
            }
            fos.write(bytesOut);
            bytesOut = verificadorVerticalBits.toByteArray();//Caso o byte verificador seja zero, criar o vetor de byte correspondente, pois BitSet ignora bytes 0 finais
            if (bytesOut.length == 0) {
                bytesOut = new byte[1];
                bytesOut[0] = 0;
            }
            fos.write(bytesOut);

            bytesOut = matrizBits.toByteArray();
            if (bytesOut.length != bytesRead) {//Trata a escrita final do arquivo, tirando o lixo
                byte[] aux = new byte[bytesRead];
                for (int i = 0; i < bytesRead; i++) {
                    if (i >= bytesOut.length) {
                        aux[i] = 0;
                    } else {
                        aux[i] = bytesOut[i];
                    }

                }
                bytesOut = aux;
            }
            if (bytesRead < 8 && bytesRead > 0) {//Trata a escrita final do arquivo, tirando o lixo
                byte[] aux = new byte[bytesRead];
                for (int i = 0; i < bytesRead; i++) {
                    aux[i] = bytesOut[i];
                }

                bytesOut = aux;
            }

            fos.write(bytesOut);

            bytesRead = fis.read(bytesIn, 0, 8);//Tenta le proximos 8 bytes

        }

        fos.close();//Fecha os arquivos
        fis.close();
    }
}
