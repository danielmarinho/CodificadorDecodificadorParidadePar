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

    public static void main(String[] args) throws FileNotFoundException, IOException {
        String nomeArq = "Untitled.png";
        File arquivo = new File(nomeArq);
        File saida = new File("saida." + nomeArq.split("\\.")[nomeArq.split("\\.").length - 1]);
        FileInputStream fis = new FileInputStream(arquivo);
        FileOutputStream fos = new FileOutputStream(saida);

        byte[] aux = new byte[8];
        byte[] aux2 = new byte[8];
        int bytesRead = fis.read(aux, 0, 8); //serão necessarios 2 bytes para o calculo

        BitSet  matrizBits = new BitSet(64);
        BitSet verificadorVerticalBits = new BitSet(8);
        BitSet verificadorHorizontalBits = new BitSet(8);
        
        int numBitSet = 0;
        int i = 0;
        while (bytesRead != -1) {
            
            matrizBits = BitSet.valueOf(aux);

            aux2 = matrizBits.toByteArray();
            fos.write(aux2);
            while(i<64){
                
            }
            bytesRead = fis.read(aux, 0, 8);
        }


        fos.close();
        fis.close();
    }
}
