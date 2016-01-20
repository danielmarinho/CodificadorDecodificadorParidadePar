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

/**
 *
 * @author danielmarinho
 */
public class Decodificador {

    public static BitSet calculaVerificadorVertical(BitSet bs) {
        int paridade;
        BitSet verificadorVertical = new BitSet(8);
        for (int i = 0; i < 64; i = i + 8) {
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

    public static BitSet calculaVerificadorHorizontal(BitSet bs) {
        int paridade;
        BitSet verificadorHorizontal = new BitSet(8);
        for (int i = 0; i < 8; i++) {
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

    public static BitSet[] separaVerificadorEDados(BitSet bs) {
        BitSet verificadorVertical = new BitSet(8);
        BitSet verificadorHorizontal = new BitSet(8);
        BitSet dadosBits = new BitSet(64);
        verificadorHorizontal = bs.get(0, 7);
        verificadorVertical = bs.get(8, 15);
        dadosBits = bs.get(16, 79);
        BitSet[] bitSets = {verificadorHorizontal, verificadorVertical, dadosBits};
        return bitSets;
    }

    public static void decodificar(String nomeArq) throws FileNotFoundException, IOException {
        
        File arquivo = new File(nomeArq);
        File saida = new File("arq_final." + nomeArq.split("\\.")[nomeArq.split("\\.").length - 1]);
        FileInputStream fis = new FileInputStream(arquivo);
        FileOutputStream fos = new FileOutputStream(saida);

        byte[] bytesIn = new byte[10];
        byte[] bytesOut = new byte[8];
        int bytesRead = fis.read(bytesIn, 0, 10); //serão necessarios 2 bytes para o calculo

        BitSet matrizBits = new BitSet(80);

        BitSet verificadorVerticalBits = new BitSet(8);
        BitSet verificadorHorizontalBits = new BitSet(8);

        BitSet matrizBitsRecebida = new BitSet(64);
        BitSet verificadorVerticalRecebido = new BitSet(8);
        BitSet verificadorHorizontalRecebido = new BitSet(8);

        while (bytesRead != -1) {

            matrizBits = BitSet.valueOf(bytesIn);

            BitSet[] separador = separaVerificadorEDados(matrizBits);
            verificadorHorizontalRecebido = separador[0];
            verificadorVerticalRecebido = separador[1];
            matrizBitsRecebida = separador[2];

            verificadorVerticalBits = calculaVerificadorVertical(matrizBitsRecebida);

            verificadorHorizontalBits = calculaVerificadorHorizontal(matrizBitsRecebida);

            
//            if (bytesRead < 8 && bytesRead > 0) {
//                byte[] aux = new byte[8];
//                for (int k = 0; k < bytesOut.length; k++) {
//                    aux[k] = bytesOut[k];
//                }
//                for (int i = bytesRead - 1; i < 8; i++) {
//                    aux[i] = 0;
//                }
//                bytesOut = aux;
//            }
            

            bytesRead = fis.read(bytesIn, 0, 10);

            int erroVertical = 0;
            int erroHorizontal = 0;
            int posErroVertical = -1;
            int posErroHorizontal = -1;

            if (!(verificadorHorizontalRecebido.equals(verificadorHorizontalBits))) {
                for (int i = 0; i < 8; i++) {
                    if (verificadorHorizontalRecebido.get(i) != verificadorHorizontalBits.get(i)) {
                        erroHorizontal++;
                        posErroHorizontal = i;
                    }
                }
                if (erroHorizontal > 1) {
                    System.err.println("ARQUIVO CORROMPIDO E NÃO FOI POSSÍVEL CORRIGI-LO");
                    fos.close();
                    fis.close();
                    System.exit(0);
                }
            }

            if (verificadorVerticalRecebido != verificadorVerticalBits) {
                for (int i = 0; i < 8; i++) {
                    if (verificadorVerticalRecebido.get(i) != verificadorVerticalBits.get(i)) {
                        erroVertical++;
                        posErroVertical = i;
                    }
                }
                if (erroVertical > 1) {
                    System.err.println("ARQUIVO CORROMPIDO E NÃO FOI POSSÍVEL CORRIGI-LO");
                    fos.close();
                    fis.close();
                    System.exit(0);
                }
            }

            if ((erroVertical == 1) && (erroHorizontal == 1)) {

                if (matrizBitsRecebida.get((posErroVertical * 8) + posErroHorizontal)) {
                    matrizBitsRecebida.set((posErroVertical * 8) + posErroHorizontal, false);
                } else {
                    matrizBitsRecebida.set((posErroVertical * 8) + posErroHorizontal, true);
                }
                System.out.println("ERRO DETECTADO E CORRIGIDO.");
            } else if ((erroVertical == 1) || (erroHorizontal == 1)) {

                System.out.println("ERRO DETECTADO NO VERIFICADOR DE PARIDADE, MAS SEM DANOS NO ARQUIVO.");
            }
            bytesOut = matrizBitsRecebida.toByteArray();
            fos.write(bytesOut);

        }

        fos.close();
        fis.close();

    }
}
