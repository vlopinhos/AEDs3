import java.io.IOException;
import java.io.RandomAccessFile;

public class ListaInvertida {

    // ------------------ Lista Invertida ------------------

    public static boolean listarNome(RandomAccessFile raf, String nome) throws IOException {

        RandomAccessFile lista = new RandomAccessFile("listaInvertida.bin", "rw");
        if(lista.length() != 0){ // Se o arquivo não estiver vazio
            lista.setLength(0); // Zera o arquivo
        }

        System.out.println("\nCidade: " + nome);
        lista.writeUTF(nome); // Escreve o nome no arquivo

        raf.seek(4); // Pula o cabeçalho
        int contador = 0; // Contador de registros
        while (raf.length() != raf.getFilePointer()) { // Enquanto não chegar ao final do arquivo
            double pointer = raf.getFilePointer(); // Pega o ponteiro atual
            if(raf.readByte() == 0) { // Se o registro estiver ativo
                raf.readInt(); // tamanho 
                int id = raf.readInt(); // id
                String n = raf.readUTF(); // nome
                int qdt = raf.readInt(); // quantidade de emails
                for (int i = 0; i < qdt; i++) {
                    raf.readUTF(); // email
                }
                raf.readUTF(); // usuario
                raf.readUTF(); // senha
                raf.readUTF(); // cpf
                raf.readUTF(); // cidade
                raf.readInt(); // transacoes
                raf.readFloat(); // saldo
                if (n.equals(nome)) { // Se a cidade for igual a passada por parâmetro
                    System.out.println("ID: " + id + " - Posição: " + (int) pointer); // Imprime o ID e a posição do registro
                    lista.writeInt(id); // Escreve o ID no arquivo de lista invertida
                    lista.writeInt((int)pointer); // Escreve a posição do registro no arquivo de lista invertida
                    contador++;
                }
            }else { // Se o registro estiver inativo
                raf.skipBytes(raf.readInt()); // Pula o registro
            }
           
        }

        System.out.println("Quantidade de registros: " + contador);
        lista.writeInt(contador); // Escreve a quantidade de registros no arquivo de lista invertida

        lista.close();
        return true;
    }

    public static boolean listarCidade(RandomAccessFile raf, String cidade) throws IOException {

        RandomAccessFile lista = new RandomAccessFile("listaInvertida.bin", "rw");
        if(lista.length() != 0){ // Se o arquivo não estiver vazio
            lista.setLength(0); // Zera o arquivo
        }

        System.out.println("\nCidade: " + cidade);
        lista.writeUTF(cidade); // Escreve o nome da cidade no arquivo

        raf.seek(4); // Pula o cabeçalho
        int contador = 0; // Contador de registros
        while (raf.length() != raf.getFilePointer()) { // Enquanto não chegar ao final do arquivo
            double pointer = raf.getFilePointer(); // Pega o ponteiro atual
            if(raf.readByte() == 0) { // Se o registro estiver ativo
                raf.readInt(); // tamanho 
                int id = raf.readInt(); // id
                raf.readUTF(); // nome
                int qdt = raf.readInt(); // quantidade de emails
                for (int i = 0; i < qdt; i++) {
                    raf.readUTF(); // email
                }
                raf.readUTF(); // usuario
                raf.readUTF(); // senha
                raf.readUTF(); // cpf
                String c = raf.readUTF(); // cidade
                raf.readInt(); // transacoes
                raf.readFloat(); // saldo
                if (c.equals(cidade)) { // Se a cidade for igual a passada por parâmetro
                    System.out.println("ID: " + id + " - Posição: " + (int) pointer); // Imprime o ID e a posição do registro
                    lista.writeInt(id); // Escreve o ID no arquivo de lista invertida
                    lista.writeInt((int)pointer); // Escreve a posição do registro no arquivo de lista invertida
                    contador++;
                }
            }else { // Se o registro estiver inativo
                raf.skipBytes(raf.readInt()); // Pula o registro
            }
           
        }

        System.out.println("Quantidade de registros: " + contador);
        lista.writeInt(contador); // Escreve a quantidade de registros no arquivo de lista invertida

        lista.close();
        return true;
    }
}

