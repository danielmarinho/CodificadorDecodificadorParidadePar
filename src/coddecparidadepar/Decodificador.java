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

    public static BitSet[] separaVerificadorEDados(BitSet bs) {//Separa os verificadores dos dados do arquivo
        byte[] byteArrayfromBitSet;
        byteArrayfromBitSet = bs.toByteArray();
        if (byteArrayfromBitSet.length == 0) {//Trata o caso dos ultimos bytes serem zero, o que normalmente seria ignorado pelo BitSet
            byteArrayfromBitSet = new byte[10];
            for (int i = 0; i < 10; i++) {
                byteArrayfromBitSet[i] = 0;
            }
        }
        byte[] auxiliarVerificadorHorizontal = new byte[1];
        byte[] auxiliarVerificadorVertical = new byte[1];
        BitSet verificadorVertical = new BitSet(8);
        BitSet verificadorHorizontal = new BitSet(8);
        BitSet dadosBits = new BitSet(64);
        verificadorHorizontal = bs.get(0, 7);//Byte do verificador de paridade das colunas
        auxiliarVerificadorHorizontal = verificadorHorizontal.toByteArray();
        if (auxiliarVerificadorHorizontal.length == 0) {//Trata o caso dos ultimos bytes serem zero, o que normalmente seria ignorado pelo BitSet
            auxiliarVerificadorHorizontal = new byte[1];
            auxiliarVerificadorHorizontal[0] = 0;
        }

        if (auxiliarVerificadorHorizontal[0] != byteArrayfromBitSet[0]) {//Resolve o seguinte problema: BitSet inverte o ultimo bit do byte, caso o byte represente um número negativo, portanto invertemos de volta nesse caso para nao perder dados
            if (verificadorHorizontal.get(7)) {
                verificadorHorizontal.set(7, false);
            } else {
                verificadorHorizontal.set(7, true);
            }
        }
        verificadorVertical = bs.get(8, 15);//Byte do verificador de paridade das linhas
        auxiliarVerificadorVertical = verificadorVertical.toByteArray();
        if (auxiliarVerificadorVertical.length == 0) {//Trata o caso dos ultimos bytes serem zero, o que normalmente seria ignorado pelo BitSet
            auxiliarVerificadorVertical = new byte[1];
            auxiliarVerificadorVertical[0] = 0;
        }

        if (auxiliarVerificadorVertical[0] != byteArrayfromBitSet[1]) {//Resolve o seguinte problema: BitSet inverte o ultimo bit do byte, caso o byte represente um número negativo, portanto invertemos de volta nesse caso para nao perder dados
            if (verificadorVertical.get(7)) {
                verificadorVertical.set(7, false);
            } else {
                verificadorVertical.set(7, true);
            }
        }

        dadosBits = bs.get(16, 79);//Bytes de dados
        BitSet[] bitSets = {verificadorHorizontal, verificadorVertical, dadosBits};
        return bitSets;//retorna as estruturas de dados separadas corretamente
    }

    public static void decodificar(String nomeArq, String nomeArqSaida) throws FileNotFoundException, IOException {

        //Cria e abre o stream dos arquivos de entrada e saida
        File arquivo = new File(nomeArq);
        File saida = new File(nomeArqSaida);
        FileInputStream fis = new FileInputStream(arquivo);
        FileOutputStream fos = new FileOutputStream(saida);

        byte[] bytesIn = new byte[10];
        byte[] bytesOut = new byte[8];
        int bytesRead = fis.read(bytesIn, 0, 10);//Vamos ler de 10 em 10 bytes

        BitSet matrizBits = new BitSet(80);//Matriz de 8x10

        BitSet verificadorVerticalBits = new BitSet(8);
        BitSet verificadorHorizontalBits = new BitSet(8);

        BitSet matrizBitsRecebida = new BitSet(64);
        BitSet verificadorVerticalRecebido = new BitSet(8);
        BitSet verificadorHorizontalRecebido = new BitSet(8);

        while (bytesRead != -1) {//Enquanto nao terminar o arquivo

            matrizBits = BitSet.valueOf(bytesIn);//Passa os bytes lidos para a estrutura BitSet
//            if (bytesIn.length != matrizBits.toByteArray().length) {
//                int diferenca = bytesIn.length - matrizBits.toByteArray().length;
//                matrizBits.set((bytesIn.length * 8), (bytesIn.length * 8) + 8, false);//Completa o BitSet 
//            }

            BitSet[] separador = separaVerificadorEDados(matrizBits);//Pega as partes separadas
            verificadorHorizontalRecebido = separador[0];
            verificadorVerticalRecebido = separador[1];
            matrizBitsRecebida = separador[2];

            verificadorVerticalBits = Codificador.calculaVerificadorVertical(matrizBitsRecebida);//Recalcula os verificadores de paridade, para procurar erro
            verificadorHorizontalBits = Codificador.calculaVerificadorHorizontal(matrizBitsRecebida);

            //Variáveis para tratar erros
            int erroVertical = 0;//qtd de erros
            int erroHorizontal = 0;
            int posErroVertical = -1;//Pos dos erros
            int posErroHorizontal = -1;

            if (!(verificadorHorizontalRecebido.equals(verificadorHorizontalBits))) {//Caso os verificadores sejam diferentes
                for (int i = 0; i < 8; i++) {
                    if (verificadorHorizontalRecebido.get(i) != verificadorHorizontalBits.get(i)) {
                        erroHorizontal++;
                        posErroHorizontal = i;//Acho qual posição está errado, adiciono contador de erros
                    }
                }
                if (erroHorizontal > 1) {//Caso haja mais de 1 erro, não é possível recuperar com esta técnica
                    System.err.println("ARQUIVO CORROMPIDO E NÃO FOI POSSÍVEL CORRIGI-LO: Foram detectados 2 erros ou mais, portanto irrecuperável por este método");
                    fos.close();
                    fis.close();
                    System.exit(0);
                }
            }

            if (!(verificadorVerticalRecebido.equals(verificadorVerticalBits))) {//Caso os verificadores sejam diferentes
                for (int i = 0; i < 8; i++) {
                    if (verificadorVerticalRecebido.get(i) != verificadorVerticalBits.get(i)) {
                        erroVertical++;
                        posErroVertical = i;//Acho qual posição está errado, adiciono contador de erros
                    }
                }
                if (erroVertical > 1) {//Caso haja mais de 1 erro, não é possível recuperar com esta técnica
                    System.err.println("ARQUIVO CORROMPIDO E NÃO FOI POSSÍVEL CORRIGI-LO: Foram detectados 2 erros ou mais, portanto irrecuperável por este método");
                    fos.close();
                    fis.close();
                    System.exit(0);
                }
            }

            if ((erroVertical == 1) && (erroHorizontal == 1)) {//Caso ideal: 1 erro em cada, é possivel recuperar

                if (matrizBitsRecebida.get((posErroVertical * 8) + posErroHorizontal)) {
                    matrizBitsRecebida.set((posErroVertical * 8) + posErroHorizontal, false);//Inverte o bit errado
                } else {
                    matrizBitsRecebida.set((posErroVertical * 8) + posErroHorizontal, true);
                }
                System.out.println("ERRO PONTUAL DETECTADO E CORRIGIDO AUTOMATICAMENTE.");
            } else if ((erroVertical == 1) || (erroHorizontal == 1)) {
                if (erroVertical == 1) {//Verifica por erros nos verificadores, mas esses erros não influenciam no arquivo final
                    System.out.println("ERRO DETECTADO NO VERIFICADOR DE PARIDADE VERTICAL, MAS SEM DANOS NO ARQUIVO.");
                }
                if (erroHorizontal == 1) {
                    System.out.println("ERRO DETECTADO NO VERIFICADOR DE PARIDADE HORIZONTAL, MAS SEM DANOS NO ARQUIVO.");
                }
            }

            bytesOut = matrizBitsRecebida.toByteArray();//Prepara para escrever

            if (bytesOut.length == 0) {//Trata o caso dos ultimos bytes zeros do bloco, que de outra forma seriam ignorados
                bytesOut = new byte[8];
                for (int i = 0; i < 8; i++) {
                    bytesOut[i] = 0;
                }
            } else {
                if (bytesRead < 10 && bytesRead > 0) {//Trata o caso do bloco nao ter 8 bytes de dados certinho, então ingnoramos os bytes de verificação e salvamos os bytes de dados
                    byte[] aux = new byte[bytesRead - 2];
                    for (int i = 0; i < bytesRead - 2; i++) {
                        aux[i] = bytesOut[i];
                    }

                    bytesOut = aux;
                }
            }

            fos.write(bytesOut);//Salvar os bytes no arquivo de saida
            bytesRead = fis.read(bytesIn, 0, 10);//Tenta ler 10 bytes novamente

            if (bytesRead < 3 && bytesRead != -1) {//Verifica o caso de menos de 3 bytes (2 de verificação e 1 de dados) lidos
                System.err.println("FINAL DO ARQUIVO CORROMPIDO: ultimo bloco tem menos que 3 bytes, estrutura comprometida");
                System.exit(0);
            }
        }

        fos.close();//Fecha os arquivos
        fis.close();

    }
}
