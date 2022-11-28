import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class Sort extends CRUD {

    // --------------- Método ---------------
    public static void printFile(RandomAccessFile raf) throws IOException { // Imprime o arquivo
        raf.seek(0);
        while (raf.getFilePointer() < raf.length()) {
            raf.readByte();
            raf.readInt();
            int id = raf.readInt();
            String nome = raf.readUTF();
            int qdtEmails = raf.readInt();
            String[] emails = new String[qdtEmails];
            for (int i = 0; i < qdtEmails; i++) {
                emails[i] = raf.readUTF();
            }
            raf.readUTF();
            raf.readUTF();
            raf.readUTF();
            raf.readUTF();
            raf.readInt();
            raf.readFloat();

            System.out.println("ID: " + id + " \t| Nome: " + nome);
        }
    }

    public static Conta readFile(RandomAccessFile raf, int pesquisa) { // Lê o arquivo
        try {
            Conta conta = new Conta();

            raf.seek(0); // Posiciona o ponteiro no inicio do arquivo
            while (raf.getFilePointer() < raf.length()) { // Enquanto o ponteiro nao chegar no final do arquivo
                if (raf.readByte() == 0) { // Se a lapide for 0 (ativo)
                    int tam = raf.readInt();

                    conta.setIdConta(raf.readInt());

                    if (conta.getIdConta() == pesquisa) { // Se o id da conta for igual ao id pesquisado
                        conta.setNomePessoa(raf.readUTF());
                        conta.setQtdEmails(raf.readInt());
                        String[] emails = new String[conta.getQtdEmails()];
                        for (int i = 0; i < conta.getQtdEmails(); i++) {
                            emails[i] = raf.readUTF();
                        }
                        conta.setEmail(emails);
                        conta.setNomeUsuario(raf.readUTF());
                        conta.setSenha(raf.readUTF());
                        conta.setCpf(raf.readUTF());
                        conta.setCidade(raf.readUTF());
                        conta.setTransferenciasRealizadas(raf.readInt());
                        conta.setSaldoConta(raf.readFloat());

                        return conta;
                    } else {
                        raf.skipBytes(tam - 4); // Pula o resto do registro
                    }
                } else {
                    raf.skipBytes(raf.readInt()); // Pula o registro
                }
            }

            return null;
        } catch (Exception e) {
            System.out.println("-> Erro ao ler o arquivo!");
            return null;
        }
    }

    public static void deleteFiles() { // Deleta os arquivos já existentes
        File file1 = new File("arq1.bin");
        File file2 = new File("arq2.bin");
        File file3 = new File("arq3.bin");
        File file4 = new File("arq4.bin");
        File file5 = new File("arqFinal.bin");

        file1.delete();
        file2.delete();
        file3.delete();
        file4.delete();
        file5.delete();
    }

    // --------------- Intercalação ---------------

    public static boolean intercalar(RandomAccessFile raf) throws IOException {

        deleteFiles(); // Deleta os arquivos já existentes

        System.out.println("\n-> Distribuindo ...");

        ArrayList<Conta> contas = new ArrayList<Conta>();
        Conta conta = new Conta();

        RandomAccessFile arq1 = new RandomAccessFile("arq1.bin", "rw");
        RandomAccessFile arq2 = new RandomAccessFile("arq2.bin", "rw");

        raf.seek(4); // Posiciona o ponteiro no inicio do arquivo
        while (raf.getFilePointer() < raf.length()) { // Enquanto o ponteiro nao chegar no final do arquivo
            if (raf.readByte() == 0) {
                raf.readInt();
                conta = readById(raf, raf.readInt());
                contas.add(conta);
            } else {
                raf.skipBytes(raf.readInt());
            }
        }

        ArrayList<Conta> contasTmp = new ArrayList<Conta>(); // Array temporário para armazenar as contas
        int contador = 0; // Contador para saber quantas contas foram adicionadas no arquivo
        while (contas.size() > 0) { // Enquanto o array contas nao estiver vazio
            for (int j = 0; j < 5; j++) { // Adiciona 5 contas no array temporário
                if (contas.size() > 0) { // Se o array contas nao estiver vazio
                    contasTmp.add(contas.get(0)); // Adiciona a primeira conta do array contas no array temporário
                    contas.remove(0); // Remove a primeira conta do array contas
                }
            }

            contasTmp.sort((Conta c1, Conta c2) -> c1.getIdConta() - c2.getIdConta()); // Ordena o array temporário

            contador++;

            if (contador % 2 != 0) { // Se o contador for impar adiciona no arquivo 1
                for (Conta c : contasTmp) {
                    arq1.writeByte(0);
                    arq1.writeInt(c.toByteArray().length);
                    arq1.write(c.toByteArray());
                }
            } else { // Se o contador for par adiciona no arquivo 2
                for (Conta c : contasTmp) {
                    arq2.writeByte(0);
                    arq2.writeInt(c.toByteArray().length);
                    arq2.write(c.toByteArray());
                }
            }

            contasTmp.clear(); // Limpa o array temporário
        }

        /*
         * System.out.println("\nArquivo 1: ");
         * printFile(arq1);
         * System.out.println("\nArquivo 2: ");
         * printFile(arq2);
         */

        // ------------------------------------------------------------------- //

        ArrayList<Conta> contas1 = new ArrayList<Conta>();
        ArrayList<Conta> contas2 = new ArrayList<Conta>();

        System.out.println("\n-> Intercalação 1 ...");

        arq1.seek(0); // Posiciona o ponteiro no inicio do arquivo 1
        while (arq1.getFilePointer() < arq1.length()) { // Enquanto o ponteiro nao chegar no final do arquivo
            arq1.readByte();
            arq1.readInt();
            conta = readFile(arq1, arq1.readInt()); // Le o registro
            contas1.add(conta); // Adiciona o registro no array contas1
        }

        arq2.seek(0); // Posiciona o ponteiro no inicio do arquivo 2
        while (arq2.getFilePointer() < arq2.length()) { // Enquanto o ponteiro nao chegar no final do arquivo
            arq2.readByte();
            arq2.readInt();
            conta = readFile(arq2, arq2.readInt()); // Le o registro
            contas2.add(conta); // Adiciona o registro no array contas2
        }

        RandomAccessFile arq3 = new RandomAccessFile("arq3.bin", "rw");
        RandomAccessFile arq4 = new RandomAccessFile("arq4.bin", "rw");

        contador = 0; // Contador para saber quantas contas foram adicionadas no arquivo
        contasTmp.clear(); // Limpa o array temporário
        int m = 5; // Tamanho do array temporário

        while (contas1.size() > 0 || contas2.size() > 0) { // Enquanto o array contas1 ou o array contas2 nao estiverem
                                                           // vazios
            for (int i = 0; i < m; i++) {
                if (contas1.size() > 0) { // Se o array contas1 nao estiver vazio adiciona a primeira conta no array
                                          // temporário e remove do array contas1
                    contasTmp.add(contas1.get(0));
                    contas1.remove(0);
                }
                if (contas2.size() > 0) { // Se o array contas2 nao estiver vazio adiciona a primeira conta no array
                                          // temporário e remove do array contas2
                    contasTmp.add(contas2.get(0));
                    contas2.remove(0);
                }
            }

            contasTmp.sort((Conta c1, Conta c2) -> c1.getIdConta() - c2.getIdConta()); // Ordena o array temporário

            contador++;

            if (contador % 2 != 0) { // Se o contador for impar adiciona no arquivo 3
                for (Conta c : contasTmp) {
                    arq3.writeByte(0);
                    arq3.writeInt(c.toByteArray().length);
                    arq3.write(c.toByteArray());
                }
            } else { // Se o contador for par adiciona no arquivo 4
                for (Conta c : contasTmp) {
                    arq4.writeByte(0);
                    arq4.writeInt(c.toByteArray().length);
                    arq4.write(c.toByteArray());
                }
            }

            contasTmp.clear(); // Limpa o array temporário
        }

        /*
         * System.out.println("\nArquivo 3: ");
         * printFile(arq3);
         * System.out.println("\nArquivo 4: ");
         * printFile(arq4);
         */

        // ------------------------------------------------------------------- //

        int qdt = 2; // Numero inicial da intercalação
        while (arq2.length() > 0) { // Enquanto o arquivo 2 nao estiver vazio

            System.out.println("\n-> Intercalação " + qdt + " ..."); // Imprime o numero da intercalação
            arq3.seek(0);
            while (arq3.getFilePointer() < arq3.length()) { // Enquanto o ponteiro nao chegar no final do arquivo 3 le o
                                                            // registro e adiciona no array contas1
                arq3.readByte();
                arq3.readInt();
                conta = readFile(arq3, arq3.readInt());
                contas1.add(conta);
            }

            arq4.seek(0);
            while (arq4.getFilePointer() < arq4.length()) { // Enquanto o ponteiro nao chegar no final do arquivo 4 le o
                                                            // registro e adiciona no array contas2
                arq4.readByte();
                arq4.readInt();
                conta = readFile(arq4, arq4.readInt());
                contas2.add(conta);
            }

            arq1.setLength(0);// Limpa o arquivo
            arq2.setLength(0); // Limpa o arquivo

            contador = 0;
            contasTmp.clear();
            m *= 2; // Aumenta o tamanho do array temporário
            while (contas1.size() > 0 || contas2.size() > 0) { // Enquanto o array contas1 ou o array contas2 nao
                                                               // estiverem vazios
                for (int i = 0; i < m; i++) { // Adiciona as contas nos arrays temporários
                    if (contas1.size() > 0) { // Se o array contas1 nao estiver vazio adiciona a primeira conta no array
                                              // temporário e remove do array contas1
                        contasTmp.add(contas1.get(0));
                        contas1.remove(0);
                    }
                    if (contas2.size() > 0) { // Se o array contas2 nao estiver vazio adiciona a primeira conta no array
                                              // temporário e remove do array contas2
                        contasTmp.add(contas2.get(0));
                        contas2.remove(0);
                    }
                }

                contasTmp.sort((Conta c1, Conta c2) -> c1.getIdConta() - c2.getIdConta()); // Ordena o array temporário

                contador++;

                if (contador % 2 != 0) { // Se o contador for impar adiciona no arquivo 1
                    for (Conta c : contasTmp) {
                        arq1.writeByte(0);
                        arq1.writeInt(c.toByteArray().length);
                        arq1.write(c.toByteArray());
                    }
                } else { // Se o contador for par adiciona no arquivo 2
                    for (Conta c : contasTmp) {
                        arq2.writeByte(0);
                        arq2.writeInt(c.toByteArray().length);
                        arq2.write(c.toByteArray());
                    }
                }

                contasTmp.clear(); // Limpa o array temporário
            }

            /* 
            System.out.println("\nArquivo 1: ");
            printFile(arq1);
            System.out.println("\nArquivo 2: ");
            printFile(arq2);
            */

            qdt++;
        }

        // ------------------------------------------------------------------- //
        
        RandomAccessFile arqFinal = new RandomAccessFile("arqFinal.bin", "rw");
        arqFinal.seek(0); // Limpa o arquivo
        arqFinal.writeInt(ultimoId); // Grava o último id

        arqFinal.seek(4); // Pula o último id
        arq1.seek(0); // Volta para o início do arquivo1
        while (arq1.getFilePointer() < arq1.length()) { // Copia o arquivo 1 para o arquivo final
            arq1.readByte(); // Lê o byte de lapide
            arq1.readInt(); // Lê o tamanho do registro
            conta = readFile(arq1, arq1.readInt()); // Lê o registro
            arqFinal.writeByte(0); // Escreve o byte de lapide
            arqFinal.writeInt(conta.toByteArray().length); // Escreve o tamanho do registro
            arqFinal.write(conta.toByteArray()); // Escreve o registro
        }

        // Fecha os arquivos
        arq1.close(); 
        arq2.close();
        arq3.close();
        arq4.close();
        arqFinal.close();

        return true;
    }
}
