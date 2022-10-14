import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class Sort extends CRUD {

    // --------------- Método ---------------
    public static void printFile(RandomAccessFile raf) throws IOException {
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

    public static Conta readFile(RandomAccessFile raf, int pesquisa) {
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

    public static boolean intercalar(RandomAccessFile raf) throws IOException {

        System.out.println("\n-> Distribuição:");

        ArrayList<Conta> contas = new ArrayList<Conta>();
        Conta conta = new Conta();

        RandomAccessFile arq1 = new RandomAccessFile("arq1.bin", "rw");
        RandomAccessFile arq2 = new RandomAccessFile("arq2.bin", "rw");

        raf.seek(4);
        while (raf.getFilePointer() < raf.length()) {
            if (raf.readByte() == 0) {
                raf.readInt();
                conta = readById(raf, raf.readInt());
                contas.add(conta);
            } else {
                raf.skipBytes(raf.readInt());
            }
        }

        ArrayList<Conta> contasTmp = new ArrayList<Conta>();
        int contador = 0;
        while (contas.size() > 0) {
            for (int j = 0; j < 5; j++) {
                if (contas.size() > 0) {
                    contasTmp.add(contas.get(0));
                    contas.remove(0);
                }
            }

            contasTmp.sort((Conta c1, Conta c2) -> c1.getIdConta() - c2.getIdConta());

            contador++;

            if (contador % 2 != 0) {
                for (Conta c : contasTmp) {
                    arq1.writeByte(0);
                    arq1.writeInt(c.toByteArray().length);
                    arq1.write(c.toByteArray());
                }
            } else {
                for (Conta c : contasTmp) {
                    arq2.writeByte(0);
                    arq2.writeInt(c.toByteArray().length);
                    arq2.write(c.toByteArray());
                }
            }

            contasTmp.clear();
        }

        System.out.println("\nArquivo 1: ");
        printFile(arq1);
        System.out.println("\nArquivo 2: ");
        printFile(arq2);

        ArrayList<Conta> contas1 = new ArrayList<Conta>();
        ArrayList<Conta> contas2 = new ArrayList<Conta>();
        
        System.out.println("\n-> Intercalação 1:");

        arq1.seek(0);
        while (arq1.getFilePointer() < arq1.length()) {
            arq1.readByte();
            arq1.readInt();
            conta = readFile(arq1, arq1.readInt());
            contas1.add(conta);
        }

        arq2.seek(0);
        while (arq2.getFilePointer() < arq2.length()) {
            arq2.readByte();
            arq2.readInt();
            conta = readFile(arq2, arq2.readInt());
            contas2.add(conta);
        }

        RandomAccessFile arq3 = new RandomAccessFile("arq3.bin", "rw");
        RandomAccessFile arq4 = new RandomAccessFile("arq4.bin", "rw");

        contador = 0;
        contasTmp.clear();
        int m = 5;
        while (contas1.size() > 0 || contas2.size() > 0) {
            for(int i = 0; i < m; i++) {
                if (contas1.size() > 0) {
                    contasTmp.add(contas1.get(0));
                    contas1.remove(0);
                }
                if (contas2.size() > 0) {
                    contasTmp.add(contas2.get(0));
                    contas2.remove(0);
                }
            }
            
            contasTmp.sort((Conta c1, Conta c2) -> c1.getIdConta() - c2.getIdConta());

            contador++;

            if (contador % 2 != 0) {
                for (Conta c : contasTmp) {
                    arq3.writeByte(0);
                    arq3.writeInt(c.toByteArray().length);
                    arq3.write(c.toByteArray());
                }
            } else {
                for (Conta c : contasTmp) {
                    arq4.writeByte(0);
                    arq4.writeInt(c.toByteArray().length);
                    arq4.write(c.toByteArray());
                }
            }

            contasTmp.clear();
        }

        System.out.println("\nArquivo 3: ");
        printFile(arq3);
        System.out.println("\nArquivo 4: ");
        printFile(arq4);

        System.out.println("\n-> Intercalação 2:");

        arq3.seek(0);
        while (arq3.getFilePointer() < arq3.length()) {
            arq3.readByte();
            arq3.readInt();
            conta = readFile(arq3, arq3.readInt());
            contas1.add(conta);
        }

        arq4.seek(0);
        while (arq4.getFilePointer() < arq4.length()) {
            arq4.readByte();
            arq4.readInt();
            conta = readFile(arq4, arq4.readInt());
            contas2.add(conta);
        }

        arq1.seek(0); // Limpa o arquivo
        arq2.seek(0); // Limpa o arquivo

        contador = 0;
        contasTmp.clear();
        m *= 2;
        while (contas1.size() > 0 || contas2.size() > 0) {
            for(int i = 0; i < m; i++) {
                if (contas1.size() > 0) {
                    contasTmp.add(contas1.get(0));
                    contas1.remove(0);
                }
                if (contas2.size() > 0) {
                    contasTmp.add(contas2.get(0));
                    contas2.remove(0);
                }
            }
            
            contasTmp.sort((Conta c1, Conta c2) -> c1.getIdConta() - c2.getIdConta());

            contador++;

            if (contador % 2 != 0) {
                for (Conta c : contasTmp) {
                    arq1.writeByte(0);
                    arq1.writeInt(c.toByteArray().length);
                    arq1.write(c.toByteArray());
                }
            } else {
                for (Conta c : contasTmp) {
                    arq2.writeByte(0);
                    arq2.writeInt(c.toByteArray().length);
                    arq2.write(c.toByteArray());
                }
            }

            contasTmp.clear();
        }

        System.out.println("\nArquivo 1: ");
        printFile(arq1);
        System.out.println("\nArquivo 2: ");
        printFile(arq2);

        arq1.close();
        arq2.close();
        arq3.close();
        arq4.close();

        return true;
    }
}

/*
 * 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16
 * 
 * Distribuição
 * 
 * 01 02 03 04 05 | 11 12 13 14 15 -> arq1
 * 06 07 08 09 10 | 16 -> arq2
 * 
 * Intercalação 1
 * 
 * 01 02 03 04 05 06 07 08 09 10 -> arq3
 * 11 12 13 14 15 16 -> arq4
 * 
 * Intercalação 2
 * 
 * 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 -> arq1
 * 
 */