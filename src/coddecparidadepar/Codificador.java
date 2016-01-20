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
public class Codificador {

    public static int[][] bitSetToIntMatrix(BitSet bs) {
        return null;
    }

    public static BitSet calculaVerificadorVertical(BitSet bs) {
        int paridade;
        BitSet verificadorVertical = new BitSet(8);
        for (int i = 0; i < 64; i = i + 8) {
            paridade = 0;
            for (int j = 0; j < 8; j++) {
                paridade = paridade + ((bs.get(i + j)) ? 1 : 0);
            }
            if (paridade % 2 == 0) {
                verificadorVertical.set(i, false);
            } else {
                verificadorVertical.set(i, true);
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
                //System.out.println(bs.get(i+j));
            }
            if (paridade % 2 == 0) {
                verificadorHorizontal.set(i, false);
            } else {
                verificadorHorizontal.set(i, true);
            }
            // System.out.println(paridade);
        }
        //System.out.println(verificadorHorizontal);
        return verificadorHorizontal;
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        String nomeArq = "Untitled.png";
        File arquivo = new File(nomeArq);
        File saida = new File("saida." + nomeArq.split("\\.")[nomeArq.split("\\.").length - 1]);
        FileInputStream fis = new FileInputStream(arquivo);
        FileOutputStream fos = new FileOutputStream(saida);

        byte[] bytesIn = new byte[8];
        byte[] bytesOut = new byte[8];
        int bytesRead = fis.read(bytesIn, 0, 8); //serão necessarios 2 bytes para o calculo

        BitSet matrizBits = new BitSet(64);
        BitSet verificadorVerticalBits = new BitSet(8);
        BitSet verificadorHorizontalBits = new BitSet(8);

        while (bytesRead != -1) {

            matrizBits = BitSet.valueOf(bytesIn);
            //System.out.println("Matriz Original");
            
            
            System.out.println("");
            verificadorVerticalBits = calculaVerificadorVertical(matrizBits);
            System.out.println("Vetor Vertical");
            System.out.print(verificadorVerticalBits);

            verificadorHorizontalBits = calculaVerificadorHorizontal(matrizBits);
            System.out.println("Vetor Horizontal");
            System.out.print(verificadorHorizontalBits);
            
            bytesOut = verificadorHorizontalBits.toByteArray();
            fos.write(bytesOut);
            bytesOut = verificadorVerticalBits.toByteArray();
            fos.write(bytesOut);
            bytesOut = matrizBits.toByteArray();
            fos.write(bytesOut);
//            for (int j = 0; j < 64; j++) {
//                //System.out.print(matrizBits.get(j) + " ");
//                if (j % 8 == 0) {
//                    //System.out.print("\n");
//                }
//            }
            

            System.out.println("");
            bytesRead = fis.read(bytesIn, 0, 8);
        }

        fos.close();
        fis.close();
    }
}
