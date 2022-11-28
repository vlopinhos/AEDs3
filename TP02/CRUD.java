import java.io.RandomAccessFile;

public class CRUD {

    // --------------- Atributos ---------------

    static int ultimoId;

    // --------------- Create ---------------

    public static boolean create(RandomAccessFile raf, Conta conta) { // Cria uma conta

        try {
            raf.seek(raf.length()); // Posiciona o ponteiro no final do arquivo
            raf.writeByte(0); // Escreve a lapide (0 = ativo)
            raf.writeInt(conta.toByteArray().length); // Escreve o tamanho do registro
            raf.writeInt(conta.getIdConta()); // Escreve o id da conta
            raf.writeUTF(conta.getNomePessoa()); // Escreve o nome da pessoa
            raf.writeInt(conta.getQtdEmails()); // Escreve a quantidade de emails
            for (int i = 0; i < conta.getQtdEmails(); i++) { // Escreve os emails
                raf.writeUTF(conta.getEmail()[i]); 
            }
            raf.writeUTF(conta.getNomeUsuario()); // Escreve o nome de usuario
            raf.writeUTF(conta.getSenha()); // Escreve a senha
            raf.writeUTF(conta.getCpf()); // Escreve o cpf
            raf.writeUTF(conta.getCidade()); // Escreve a cidade
            raf.writeInt(conta.getTransferenciasRealizadas()); // Escreve a quantidade de transferencias realizadas
            raf.writeFloat(conta.getSaldoConta()); // Escreve o saldo da conta

            raf.seek(0); // Posiciona o ponteiro no inicio do arquivo
            raf.writeInt(ultimoId); // Atualiza o ultimo id

            return true;
        } catch (Exception e) {
            System.out.println("-> Erro ao criar registro!");
            return false;
        }
    }

    // --------------- Read ---------------

    public static Conta readById(RandomAccessFile raf, int pesquisa) { // Pesquisa uma conta pelo id
        try {
            Conta conta = new Conta();

            raf.seek(4); // Posiciona o ponteiro no inicio do arquivo
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
            System.out.println("-> Erro ao ler registro!");
            return null;
        }
    }

    public static Conta readByUser(RandomAccessFile raf, String pesquisa) { // Pesquisa uma conta pelo nome de usuario
        try {
            Conta conta = null; 
            String[] emails; 
            boolean achado = false; // Variavel para verificar se o nome de usuario foi encontrado

            raf.seek(4); // Posiciona o ponteiro no inicio do arquivo
            while (raf.getFilePointer() < raf.length() && !achado) { // enquanto não chegar no final do arquivo
                if (raf.readByte() == 0) { // se o registro não estiver excluído
                    conta = new Conta(); 
                    raf.readInt();

                    conta.setIdConta(raf.readInt());
                    conta.setNomePessoa(raf.readUTF());
                    conta.setQtdEmails(raf.readInt());
                    emails = new String[conta.getQtdEmails()];
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

                    if(conta.getNomeUsuario().equals(pesquisa)){ // se o nome de usuario for igual ao nome de usuario pesquisado
                        achado = true;
                    }
                } else { // se o registro estiver excluído
                    raf.skipBytes(raf.readInt());
                }
            }

            return conta;
        } catch (Exception e) {
            System.out.println("-> Erro ao ler registro!");
            return null;
        }
    }

    // --------------- Update ---------------

    public static boolean update(RandomAccessFile raf, Conta conta) { // Atualiza uma conta
        try {
            raf.seek(4); // Posiciona o ponteiro no inicio do arquivo
            while (raf.getFilePointer() < raf.length()) { // Enquanto o ponteiro nao chegar no final do arquivo
                if (raf.readByte() == 0) { // Se a lapide for 0 (ativo)
                    int tam = raf.readInt();

                    if (raf.readInt() == conta.getIdConta()) { // Se o id da conta for igual ao id da conta a ser atualizada
                        if (tam >= conta.toByteArray().length) { // Se o tamanho do registro for maior ou igual ao tamanho do registro atualizado encaixar no mesmo registro

                            raf.writeUTF(conta.getNomePessoa());
                            raf.writeInt(conta.getQtdEmails());
                            for (int i = 0; i < conta.getQtdEmails(); i++) {
                                raf.writeUTF(conta.getEmail()[i]);
                            }
                            raf.writeUTF(conta.getNomeUsuario());
                            raf.writeUTF(conta.getSenha());
                            raf.writeUTF(conta.getCpf());
                            raf.writeUTF(conta.getCidade());
                            raf.writeInt(conta.getTransferenciasRealizadas());
                            raf.writeFloat(conta.getSaldoConta());

                            return true;
                        } else { // Se o tamanho do registro for menor que o tamanho do registro atualizado sera necessario criar um novo registro e excluir o antigo
                            raf.seek(raf.getFilePointer() - 9); // Volta o ponteiro para o inicio do registro
                            raf.writeByte(1); // Escreve a lapide 1 (excluido)
                            return create(raf, conta); // Cria um novo registro
                        }
                    } else {
                        raf.skipBytes(tam - 4); // Pula o resto do registro
                    }
                } else {
                    raf.skipBytes(raf.readInt()); // Pula o registro
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println("-> Erro ao atualizar registro!");
            return false;
        }
    }

    // --------------- Delete ---------------

    public static boolean delete(RandomAccessFile raf, Conta conta) { // Exclui uma conta
        try {
            raf.seek(4); // Posiciona o ponteiro no inicio do arquivo
            while(raf.getFilePointer() < raf.length()) { // Enquanto o ponteiro nao chegar no final do arquivo
                if(raf.readByte() == 0) { // Se a lapide for 0 (ativo)
                    int tam = raf.readInt();
                    int id = raf.readInt();

                    if(id == conta.getIdConta()) { // Se o id da conta for igual ao id da conta a ser excluida
                        raf.seek(raf.getFilePointer() - 9); // Volta o ponteiro para o inicio do registro
                        raf.writeByte(1); // Escreve a lapide 1 (excluido)
                        return true;
                    }else {
                        raf.skipBytes(tam - 4); // Pula o resto do registro
                    }
                }else {
                    raf.skipBytes(raf.readInt()); // Pula o registro
                }
            }
            return false;
        }catch (Exception e) {
            System.out.println("-> Erro ao deletar registro!");
            return false;
        }
    }
}
