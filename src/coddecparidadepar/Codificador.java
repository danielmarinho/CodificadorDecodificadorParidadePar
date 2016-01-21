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
    
    public static int qtdZeros = 0;
    public static int qtdBlocos = 0;

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
                //System.out.println(bs.get(i+j));
            }
            if (paridade % 2 == 0) {
                verificadorHorizontal.set(i, false);
            } else {
                verificadorHorizontal.set(i, true);
            }
        }
        return verificadorHorizontal;
    }

    public static void codificar(String nomeArq) throws FileNotFoundException, IOException {

        File arquivo = new File(nomeArq);
        File saida = new File("saida." + nomeArq.split("\\.")[nomeArq.split("\\.").length - 1]);
        FileInputStream fis = new FileInputStream(arquivo);
        FileOutputStream fos = new FileOutputStream(saida);

        byte[] bytesIn = new byte[8];
        byte[] bytesOut = new byte[8];
        int bytesRead = fis.read(bytesIn, 0, 8); //serão necessarios 2 bytes para o calculo

        BitSet matrizBits = new BitSet(64);
        for (int i = 0; i < 64; i++) {
            matrizBits.set(i, false);
        }
        BitSet verificadorVerticalBits = new BitSet(8);
        BitSet verificadorHorizontalBits = new BitSet(8);

        while (bytesRead != -1) {

            matrizBits = BitSet.valueOf(bytesIn);
            System.out.println("\n");
            System.out.println("Bits convertidos para matriz\n");
            for (int i = 1; i <= 64; i++) {
                //for (int j = 0; j < 8; j++) {
                System.out.print(((matrizBits.get(i - 1)) ? 1 : 0));
                if ((i % 8 == 0) && (i != 0)) {
                    System.out.println("");
                }
                //}

            }

            verificadorVerticalBits = calculaVerificadorVertical(matrizBits);

            verificadorHorizontalBits = calculaVerificadorHorizontal(matrizBits);

            bytesOut = verificadorHorizontalBits.toByteArray();
            if(bytesOut.length==0){
                bytesOut = new byte[1];
                bytesOut[0] = 0;
            }
            fos.write(bytesOut);
            bytesOut = verificadorVerticalBits.toByteArray();
            if(bytesOut.length==0){
                bytesOut = new byte[1];
                bytesOut[0] = 0;
            }
            fos.write(bytesOut);

//            byte [] temp = matrizBits.toByteArray();
//            
//            for (int i = 0; i < bytesRead; i++) {
//                bytesOut[i] = temp[i];
//            }
            bytesOut = matrizBits.toByteArray();
            if(bytesOut.length != bytesRead){
                byte [] aux = new byte[bytesRead];
                for (int i = 0; i < bytesRead; i++) {
                    if(i>=bytesOut.length){
                      aux[i] = 0;  
                    }else{
                       aux[i] = bytesOut[i]; 
                    }
                    
                }
                bytesOut = aux;
            }
            if (bytesRead < 8 && bytesRead > 0) {
                byte[] aux = new byte[bytesRead];
                for (int i = 0; i < bytesRead; i++) {
                    aux[i] = bytesOut[i];
                }

//                for (int i = 0; i < bytesRead; i++) {
//                    aux[i] = bytesOut[i];
//                }
                bytesOut = aux;
                qtdZeros = (8-bytesRead)*8;
            }

//            if (bytesRead < 8) {
//                byte [] aux = new byte[8];
//                for (int k = 0; k < bytesOut.length; k++) {
//                    aux[k] = bytesOut[k];
//                }
//                for (int i = bytesRead-1; i < 8; i++) {
//                    aux[i] = 0;
//                }
//                bytesOut = aux;
//            }
            fos.write(bytesOut);
//            byte[] aux = new byte[8 - bytesRead];
//            if (bytesRead < 8 && bytesRead >0) {
//
//                for (int k = 0; k < aux.length; k++) {
//                    aux[k] = 0;
//                }
//                fos.write(aux);
//
//            }

            System.out.println("\n");
            System.out.println("bits de saida para arquivo:\n");
            for (int i = 0; i < bytesOut.length; i++) {
                System.out.println(String.format("%8s", Integer.toBinaryString(bytesOut[i] & 0xFF)).replace(' ', '0'));
            }
//            if (aux.length > 0) {
//                for (int i = 0; i < aux.length; i++) {
//                    System.out.println(String.format("%8s", Integer.toBinaryString(aux[i] & 0xFF)).replace(' ', '0'));
//                }
//            }

            bytesRead = fis.read(bytesIn, 0, 8);
            for (int i = 0; i < bytesIn.length; i++) {
                System.out.println(bytesIn[i]);
            }
        }

        fos.close();
        fis.close();
    }
}
