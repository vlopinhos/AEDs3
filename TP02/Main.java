import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class Main extends CRUD {

    // --------------- Métodos ---------------

    public static boolean tranferencia(RandomAccessFile raf, int id1, int id2, float valor){ // Metodo para transferencia de valores entre duas contas
        Conta conta1 = readById(raf, id1);
        Conta conta2 = readById(raf, id2);

        if(conta1 == null) System.out.println("-> Conta de origem não encontrada!"); // Se a conta de origem não for encontrada
        else if(conta2 == null) System.out.println("-> Conta de destino não encontrada!"); // Se a conta de destino não for encontrada
        else if(conta1.getSaldoConta() < valor) System.out.println("-> Saldo insuficiente!"); // Se o saldo da conta de origem for menor que o valor a ser transferido
        else{ // Se a transferencia for realizada com sucesso
            conta1.setSaldoConta(conta1.getSaldoConta() - valor);
            conta2.setSaldoConta(conta2.getSaldoConta() + valor); 
            conta1.setTransferenciasRealizadas(conta1.getTransferenciasRealizadas() + 1);
            conta2.setTransferenciasRealizadas(conta2.getTransferenciasRealizadas() + 1); 
            update(raf, conta1); // Atualiza as contas no arquivo
            update(raf, conta2);
            return true;
        }
        return false;
    }

    // --------------- Main ---------------

    public static void main(String[] args) throws IOException { 
        RandomAccessFile raf = new RandomAccessFile("banco.bin", "rw"); // Cria o arquivo
        Scanner sc = new Scanner(System.in); 
        Conta conta = new Conta();
        
        int opcao = 0; // Variavel para armazenar a opcao dos menus
        boolean loop = true; // Variavel para controlar o loop do menu principal

        if (raf.length() == 0) raf.writeInt(0); // Se o arquivo estiver vazio escreve 0 no inicio do arquivo para indicar o ultimo id utilizado
        raf.seek(0); // Volta para o inicio do arquivo
        ultimoId = raf.readInt(); // Le o ultimo id utilizado

        while(loop) { // Loop do menu principal
            System.out.println("____________MENU____________");
            System.out.println("|                          |");
            System.out.println("|0 - Sair                  |");
            System.out.println("|1 - Criar conta           |");
            System.out.println("|2 - Transferência         |");
            System.out.println("|3 - Ler registro          |");
            System.out.println("|4 - Atualizar registro    |");
            System.out.println("|5 - Deletar registro      |");
            System.out.println("|6 - Intercalar            |");
            System.out.println("|7 - Árvore B+             |");
            System.out.println("|8 - Hashing Extendido     |");
            System.out.println("|9 - Lista Invertida       |");
            System.out.println("|10 - Huffman              |");
            System.out.println("|11 - LZW                  |");
            System.out.println("|__________________________|\n");
            System.out.print("-> ");
            do { 
                try {
                    opcao = sc.nextInt();
                    if(opcao < 0 || opcao > 11) System.out.println("-> Opção inválida!"); 
                } catch (Exception e) { // Se a opcao não for um numero
                    System.out.println("-> Digite um número!");
                    sc.nextLine();
                    break;
                }
            } while (opcao < 0 || opcao > 11); // Enquanto a opcao for invalida continua no loop

            switch (opcao) { // Menu principal
                case 1: // Criar conta
                    conta = new Conta();
                    System.out.println("\n____________CRIAR CONTA____________");

                    // Leitura dos dados da conta
                    System.out.print("-> Nome: ");
                    conta.setNomePessoa(sc.next());

                    String in = "";
                    do {
                        System.out.print("-> CPF: ");
                        in = sc.next();
                    } while (in.length() != 11); // Enquanto o cpf não tiver 11 digitos continua no loop
                    conta.setCpf(in);

                    System.out.print("-> Cidade: ");
                    conta.setCidade(sc.next());

                    System.out.print("-> Quantidade de emails: ");
                    conta.setQtdEmails(sc.nextInt());
                    String[] email = new String[conta.getQtdEmails()];
                    for(int i = 0; i < conta.getQtdEmails(); i++) {
                        System.out.print("-> Email " + (i + 1) + ": ");
                        email[i] = sc.next();
                    }
                    conta.setEmail(email);

                    if(raf.length() > 5) { // Se o arquivo estiver vazio 
                        do {
                            System.out.print("-> Usuário: ");
                            in = sc.next();
                        } while (readByUser(raf, in).getNomeUsuario().equals(in)); // Enquanto o usuario já existir continua no loop
                        conta.setNomeUsuario(in);
                    }else {
                        System.out.print("-> Usuário: ");
                        in = sc.next();
                        conta.setNomeUsuario(in);
                    }
                    
                    System.out.print("-> Senha: ");
                    conta.setSenha(sc.next());

                    System.out.print("-> Saldo: ");
                    conta.setSaldoConta(sc.nextFloat());

                    conta.setIdConta(++ultimoId); // Incrementa o ultimo id utilizado e atribui ao id da conta

                    // Cria a conta no arquivo
                    if(create(raf, conta)) System.out.println("\n-> Conta criada com sucesso!"); 
                    else System.out.println("\n-> Erro ao criar conta!");

                    break;
                case 2: // Transferencia
                    System.out.println("____________TRANSFERÊNCIA____________");
                    System.out.print("-> ID da conta de origem: ");
                    int id1 = sc.nextInt();

                    System.out.print("-> ID da conta de destino: ");
                    int id2 = sc.nextInt();

                    System.out.print("-> Valor da transferência: ");
                    float valor = sc.nextFloat();

                    // Realiza a transferencia
                    if(tranferencia(raf, id1, id2, valor)) System.out.println("\n-> Transferência realizada com sucesso!\n");
                    else System.out.println("\n-> Erro ao realizar transferência!\n");
                    
                    break;
                case 3: // Ler registro
                    System.out.println("____________LER REGISTRO____________");

                    System.out.print("-> ID: ");
                    int pesquisa = sc.nextInt();

                    conta = readById(raf, pesquisa); // Criar uma conta com os dados do registro lido

                    if(conta == null) System.out.println("-> Conta não encontrada!");
                    else { 
                        // Imprime os dados da conta
                        System.out.println("\n-> Conta encontrada!");
                        System.out.println("-> ID: " + conta.getIdConta());
                        System.out.println("-> Nome: " + conta.getNomePessoa());
                        System.out.println("-> CPF: " + conta.getCpf());
                        System.out.println("-> Cidade: " + conta.getCidade());
                        System.out.println("-> Emails: ");
                        for(int i = 0; i < conta.getQtdEmails(); i++) {
                            System.out.println("-> " + conta.getEmail()[i]);
                        }
                        System.out.println("-> Usuário: " + conta.getNomeUsuario());
                        System.out.println("-> Senha: " + conta.getSenha());
                        System.out.println("-> Saldo: " + conta.getSaldoConta());
                        System.out.println("-> Transferências realizadas: " + conta.getTransferenciasRealizadas() + "\n");
                    }
                    break;
                case 4: // Atualizar registro
                    System.out.println("____________ATUALIZAR REGISTRO____________");
                    System.out.print("-> User: ");
                    String user = sc.next();

                    conta = readByUser(raf, user); // Criar uma conta com os dados do registro lido

                    if(conta == null) System.out.println("-> Conta não encontrada!");
                    else {
                        // Imprime os dados da conta
                        System.out.println("\n-> Conta encontrada!");
                        System.out.println("-> ID: " + conta.getIdConta());
                        System.out.println("-> Nome: " + conta.getNomePessoa());
                        System.out.println("-> CPF: " + conta.getCpf());
                        System.out.println("-> Cidade: " + conta.getCidade());
                        System.out.println("-> Emails: ");
                        for(int i = 0; i < conta.getQtdEmails(); i++) {
                            System.out.println("-> " + conta.getEmail()[i]);
                        }
                        System.out.println("-> Usuário: " + conta.getNomeUsuario());
                        System.out.println("-> Senha: " + conta.getSenha());
                        System.out.println("-> Saldo: " + conta.getSaldoConta());
                        System.out.println("-> Transferências realizadas: " + conta.getTransferenciasRealizadas());
                        System.out.println("__________________________________________\n");

                        // Escolha do que atualizar
                        System.out.println("____________ATUALIZAR REGISTRO____________");
                        System.out.println("-> Qual campo deseja atualizar?");
                        System.out.println("-> 1 - Nome");
                        System.out.println("-> 2 - CPF");
                        System.out.println("-> 3 - Cidade");
                        System.out.println("-> 4 - Email");
                        System.out.println("-> 5 - Usuário");
                        System.out.println("-> 6 - Senha");
                        System.out.println("-> 7 - Saldo");
                        System.out.println("-> 8 - Cancelar");
                        System.out.print("-> ");

                        do {
                            try {
                                opcao = sc.nextInt();
                                if(opcao < 1 || opcao > 8) System.out.println("-> Opção inválida!");
                            } catch (Exception e) {
                                System.out.println("-> Digite um número!");
                                sc.next();
                                break;
                            }
                        } while (opcao < 1 || opcao > 8); // Enquanto a opção for inválida continua no loop

                        switch (opcao) { // Atualiza o campo escolhido
                            case 1: // Nome
                                System.out.print("-> Novo Nome: ");
                                conta.setNomePessoa(sc.next());
                                break;
                            case 2: // CPF
                                System.out.print("-> Novo CPF: ");
                                conta.setCpf(sc.next());
                                break;
                            case 3: // Cidade
                                System.out.print("-> Nova Cidade: ");
                                conta.setCidade(sc.next());
                                break;
                            case 4: // Email
                                System.out.print("-> Nova Quantidade de emails: ");
                                conta.setQtdEmails(sc.nextInt());
                                String[] email2 = new String[conta.getQtdEmails()];
                                for(int i = 0; i < conta.getQtdEmails(); i++) {
                                    System.out.print("-> Novo Email " + (i + 1) + ": ");
                                    email2[i] = sc.next();
                                }
                                conta.setEmail(email2);
                                break;
                            case 5: // Usuário
                                System.out.print("-> Novo Usuário: ");
                                conta.setNomeUsuario(sc.next());
                                break;
                            case 6: // Senha
                                System.out.print("-> Nova Senha: ");
                                conta.setSenha(sc.next());
                                break;
                            case 7: // Saldo
                                System.out.print("-> Novo Saldo: ");
                                conta.setSaldoConta(sc.nextFloat());
                                break;
                            case 8: // Cancelar
                                System.out.println("-> Cancelado!\n");
                                break;
                        }

                        if(opcao != 8) { // Se a opção for diferente de 8, atualiza o registro
                            if(update(raf, conta)) System.out.println("-> Atualizado com sucesso!");
                            else System.out.println("-> Erro ao atualizar!");
                        }
                    }
                    break;
                case 5: // Deletar registro
                    System.out.println("____________DELETAR REGISTRO____________");
                    System.out.print("-> ID: ");
                    pesquisa = sc.nextInt();

                    conta = readById(raf, pesquisa); // Criar uma conta com os dados do registro lido
                    if(conta == null) System.out.println("-> Conta não encontrada!");
                    else { // Se a conta for encontrada, imprime os dados
                        System.out.println("\n-> Conta encontrada!");
                        System.out.println("-> ID: " + conta.getIdConta());
                        System.out.println("-> Nome: " + conta.getNomePessoa());
                        System.out.println("-> CPF: " + conta.getCpf());
                        System.out.println("-> Cidade: " + conta.getCidade());
                        System.out.println("-> Emails: ");
                        for(int i = 0; i < conta.getQtdEmails(); i++) {
                            System.out.println("-> " + conta.getEmail()[i]);
                        }
                        System.out.println("-> Usuário: " + conta.getNomeUsuario());
                        System.out.println("-> Senha: " + conta.getSenha());
                        System.out.println("-> Saldo: " + conta.getSaldoConta());
                        System.out.println("-> Transferências realizadas: " + conta.getTransferenciasRealizadas());
                        System.out.println("________________________________________\n");

                        // Confirmação de exclusão
                        System.out.println("-> Deseja realmente deletar essa conta?");
                        System.out.println("-> 1 - Sim");
                        System.out.println("-> 2 - Não");
                        System.out.print("-> ");

                        do {
                            try {
                                opcao = sc.nextInt();
                                if(opcao < 1 || opcao > 2) System.out.println("-> Opção inválida!");
                            } catch (Exception e) {
                                System.out.println("-> Digite um número!");
                                sc.next();
                                break;
                            }
                        } while (opcao < 1 || opcao > 2); // Enquanto a opção for inválida continua no loop

                        if(opcao == 1) { // Se a opção for 1, deleta o registro
                            if(delete(raf, conta)) System.out.println("-> Deletado com sucesso!");
                            else System.out.println("-> Erro ao deletar!");
                        }
                        else System.out.println("-> Cancelado!\n");
                    }
                    break;
                case 6: // Intercalar registros
                    System.out.println("____________INTERCALAR____________");
                    System.out.println("Deseja realmente intercalar os registros?");
                    System.out.println("-> 1 - Sim");
                    System.out.println("-> 2 - Não");
                    System.out.print("-> ");

                    do {
                        try {
                            opcao = sc.nextInt();
                            if(opcao < 1 || opcao > 2) System.out.println("-> Opção inválida!");
                        } catch (Exception e) {
                            System.out.println("-> Digite um número!");
                            sc.next();
                            break;
                        }
                    } while (opcao < 1 || opcao > 2); // Enquanto a opção for inválida continua no loop

                    if(opcao == 1) { // Se a opção for 1, intercala os registros                        
                        if(Sort.intercalar(raf)) System.out.println("\n-> Intercalado com sucesso!");
                        else System.out.println("\n-> Erro ao intercalar!");
                        
                    }
                    else System.out.println("-> Cancelado!\n");

                    break;
                case 7: // Arvore B+
                    System.out.println("Arvore B+:");
                    break;
                case 8: // Hashing Extensível0
                    System.out.println("Hashing Estendido:");
                    break;
                case 9: // Lista Invertida
                    System.out.println("____________LISTAR____________");
                    System.out.println("-> 1 - Nome");
                    System.out.println("-> 2 - Cidade");
                    System.out.println("-> 3 - Cancelar");
                    System.out.print("-> ");

                    do {
                        try {
                            opcao = sc.nextInt();
                            if(opcao < 1 || opcao > 3) System.out.println("-> Opção inválida!");
                        } catch (Exception e) {
                            System.out.println("-> Digite um número!");
                            sc.next();
                            break;
                        }
                    } while (opcao < 1 || opcao > 3); // Enquanto a opção for inválida continua no loop

                    if(opcao == 1) { // Se a opção for 1, lista por nome   
                        System.out.print("\n-> Digite o nome: ");
                        String name = sc.next();
                        if(ListaInvertida.listarNome(raf, name)) System.out.println("\n-> Listado com sucesso!");
                        else System.out.println("\n-> Erro ao listar!");
                        
                    }else if(opcao == 2) { // Se a opção for 2, lista por cidade
                        System.out.print("\n-> Digite a cidade: ");
                        String city = sc.next();
                        if(ListaInvertida.listarCidade(raf, city)) System.out.println("\n-> Listado com sucesso!");
                        else System.out.println("\n-> Erro ao listar!");
                    }
                    else System.out.println("-> Cancelado!\n");
                    
                    break;
                case 10: // Huffman
                    System.out.println("____________HUFFMAN____________");
                    System.out.println("-> 1 - Compactar");
                    System.out.println("-> 2 - Descompactar");
                    System.out.println("-> 3 - Cancelar");
                    System.out.print("-> ");

                    do {
                        try {
                            opcao = sc.nextInt();
                            if(opcao < 1 || opcao > 3) System.out.println("-> Opção inválida!");
                        } catch (Exception e) {
                            System.out.println("-> Digite um número!");
                            sc.next();
                            break;
                        }
                    } while (opcao < 1 || opcao > 3); // Enquanto a opção for inválida continua no loop

                    if(opcao == 1) { // Se a opção for 1, compacta o arquivo
                        String name = "banco.bin";
                        if(HuffmanTree.compactar(name)) System.out.println("\n-> Compactado com sucesso!\n");
                        else System.out.println("\n-> Erro ao compactar!");
                    }else if(opcao == 2) { // Se a opção for 2, descompacta o arquivo
                        String name = "comprimido.bin";
                        if(HuffmanTree.descompactar(name)) System.out.println("\n-> Descompactado com sucesso!\n");
                        else System.out.println("\n-> Erro ao descompactar!");
                    }
                    else System.out.println("-> Cancelado!\n");
                    
                    break;
                case 11: // LZW
                    System.out.println("LZW:");
                    // TODO
                    break;
                case 0: // Sair
                    System.out.println("-> Saindo...");
                    loop = false;
                    loop = false;
                    break;
            }
        }

        sc.close();
        raf.close();
    }
}
