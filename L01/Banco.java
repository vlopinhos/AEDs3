import java.util.ArrayList;
import java.util.Scanner;

public class Banco {
    static int id;
    private int idConta;
    private String nomePessoa;
    private ArrayList<String> email;
    private String nomeUsuario;
    private String senha;
    private String cpf;
    private String cidade;
    private int transferenciasRealizadas;
    private float saldoConta;

    public Banco() {
        this.idConta = 0;
        this.nomePessoa = "";
        this.email = new ArrayList<String>();
        this.nomeUsuario = "";
        this.senha = "";
        this.cpf = "";
        this.cidade = "";
        this.transferenciasRealizadas = 0;
        this.saldoConta = 0;
    }

    public Banco(int idConta, String nomePessoa, ArrayList<String> email, String nomeUsuario, String senha, String cpf,
            String cidade, int transferenciasRealizadas, float saldoConta) {
        this.idConta = idConta;
        this.nomePessoa = nomePessoa;
        this.email = email;
        this.nomeUsuario = nomeUsuario;
        this.senha = senha;
        this.cpf = cpf;
        this.cidade = cidade;
        this.transferenciasRealizadas = transferenciasRealizadas;
        this.saldoConta = saldoConta;
    }

    public int getIdConta() {
        return idConta;
    }

    public String getNomePessoa() {
        return nomePessoa;
    }

    public ArrayList<String> getEmail() {
        return email;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public String getSenha() {
        return senha;
    }

    public String getCpf() {
        return cpf;
    }

    public String getCidade() {
        return cidade;
    }

    public int getTransferenciasRealizadas() {
        return transferenciasRealizadas;
    }

    public float getSaldoConta() {
        return saldoConta;
    }

    public void setIdConta(int idConta) {
        this.idConta = idConta;
    }

    public void setNomePessoa(String nomePessoa) {
        this.nomePessoa = nomePessoa;
    }

    public void setEmail(ArrayList<String> email) {
        this.email = email;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public void setTransferenciasRealizadas(int transferenciasRealizadas) {
        this.transferenciasRealizadas = transferenciasRealizadas;
    }

    public void setSaldoConta(float saldoConta) {
        this.saldoConta = saldoConta;
    }

    public static void balanceadaComum(ArrayList<Banco> contas) {
        int i, j, k, n = contas.size();
        Banco[] aux = new Banco[n];
        for (int m = 1; m < n; m *= 2) {
            for (int l = 0; l < n - 1; l += 2 * m) {
                i = l;
                j = l + m;
                k = l;
                while (i < l + m && j < l + 2 * m && j < n) {
                    if (contas.get(i).getSaldoConta() < contas.get(j).getSaldoConta()) {
                        aux[k] = contas.get(i);
                        i++;
                    } else {
                        aux[k] = contas.get(j);
                        j++;
                    }
                    k++;
                }
                while (i < l + m && i < n) {
                    aux[k] = contas.get(i);
                    i++;
                    k++;
                }
                while (j < l + 2 * m && j < n) {
                    aux[k] = contas.get(j);
                    j++;
                    k++;
                }
                for (k = l; k < l + 2 * m && k < n; k++) {
                    contas.set(k, aux[k]);
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Banco> contas = new ArrayList<Banco>();
        int input = 0;
        boolean loop = true;

        String welcome = "\n\n\t----------MENU----------\n1) para criar uma conta.\n2) para realizar uma transferência.\n3) ler um registro.\n4) atualizar um registro.\n5) para deletar um registro.\n6) selecionar uma ordenação.\n7) para sair.\n\t------------------------\n";
        
        while (loop) {
            
            System.out.println(welcome);
            do {
                try {
                    System.out.println("Digite uma opção: ");
                    input = sc.nextInt();
                } catch (Exception e) {
                    System.out.println("Digite um número válido!");
                    input = 0;
                    sc.next();
                }

            } while (input < 1 || input > 7);

            switch (input) {
                case 1:
                    Banco conta = new Banco();
                    System.out.println("\nCriando conta ...");
                    conta.setIdConta(++id);
                    System.out.println("Digite o nome da pessoa: ");
                    conta.setNomePessoa(sc.next());
                    System.out.print("Digite o CPF da pessoa: \n");
                    conta.setCpf(sc.next());
                    System.out.println("Digite a cidade: ");
                    conta.setCidade(sc.next());
                    boolean resp = true;
                    while (resp) {
                        System.out.println("Digite o email: ");
                        conta.getEmail().add(sc.next());
                        System.out.println("Deseja adicionar mais um email? (s/n)");
                        if (sc.next().equals("n")) {
                            resp = false;
                        }
                    }
                    System.out.println("Digite o nome de usuário: ");
                    conta.setNomeUsuario(sc.next());
                    System.out.println("Digite a senha: ");
                    conta.setSenha(sc.next());
                    System.out.println("Digite o saldo da conta: ");
                    conta.setSaldoConta(Float.parseFloat(sc.next()));
                    conta.setTransferenciasRealizadas(0);
                    contas.add(conta);
                    System.out.println("Conta cadastrada com sucesso!\n");
                    break;
                case 2:
                    System.out.println("Realizando transferência ...");
                    System.out.println("Digite a sua conta: ");
                    String conta1 = sc.next();
                    System.out.println("Digite a conta que deseja transferir: ");
                    String conta2 = sc.next();
                    System.out.println("Digite o valor da transferência: ");
                    float valor = sc.nextFloat();
                    for (int i = 0; i < contas.size(); i++) {
                        if (contas.get(i).getNomeUsuario().equals(conta1)) {
                            if (contas.get(i).getSaldoConta() >= valor) {
                                contas.get(i).setSaldoConta(contas.get(i).getSaldoConta() - valor);
                                contas.get(i)
                                        .setTransferenciasRealizadas(contas.get(i).getTransferenciasRealizadas() + 1);
                                for (int j = 0; j < contas.size(); j++) {
                                    if (contas.get(j).getNomeUsuario().equals(conta2)) {
                                        contas.get(j).setSaldoConta(contas.get(j).getSaldoConta() + valor);
                                        contas.get(j).setTransferenciasRealizadas(
                                                contas.get(j).getTransferenciasRealizadas() + 1);
                                        System.out.println("Transferência realizada com sucesso!\n");
                                    }
                                }
                            } else {
                                System.out.println("Saldo insuficiente!\n");
                            }
                        }
                    }
                    break;
                case 3:
                    System.out.println("Lendo registro ...");
                    for (int i = 0; i < contas.size(); i++) {
                        System.out.println("\nId da conta: " + contas.get(i).getIdConta());
                        System.out.println("Nome da pessoa: " + contas.get(i).getNomePessoa());
                        System.out.println("Email: " + contas.get(i).getEmail());
                        System.out.println("Nome de usuario: " + contas.get(i).getNomeUsuario());
                        System.out.println("Senha: " + contas.get(i).getSenha());
                        System.out.println("CPF: " + contas.get(i).getCpf());
                        System.out.println("Cidade: " + contas.get(i).getCidade());
                        System.out.println("Transferencias realizadas: " + contas.get(i).getTransferenciasRealizadas());
                        System.out.println("Saldo da conta: " + contas.get(i).getSaldoConta());
                    }
                    System.out.println("Arquivo lido com sucesso!\n");
                    break;
                case 4:
                    System.out.println("Atualiando registro ...");
                    System.out.println("Digite a conta que gostaria de atualizar: ");
                    String conta3 = sc.next();
                    for (int i = 0; i < contas.size(); i++) {
                        if (contas.get(i).getNomeUsuario().equals(conta3)) {
                            System.out.println("Digite qual atributo gostaria de atualizar: ");
                            System.out.println("\n\n\t----------MENU----------\n1) Nome da pessoa\n2) CPF\n3) Cidade\n4) Email\n5) Nome de usuário\n6) Senha\n7) Saldo da conta\n8) Transferências realizadas\n9) para voltar");
                            int op = sc.nextInt();
                            switch (op) {
                                case 1:
                                    System.out.println("Digite o novo nome da pessoa: ");
                                    contas.get(i).setNomePessoa(sc.next());
                                    break;
                                case 2:
                                    System.out.println("Digite o novo CPF: ");
                                    contas.get(i).setCpf(sc.next());
                                    break;
                                case 3:
                                    System.out.println("Digite a nova cidade: ");
                                    contas.get(i).setCidade(sc.next());
                                    break;
                                case 4:
                                    System.out.println("Digite o novo email: ");
                                    contas.get(i).getEmail().add(sc.next());
                                    break;
                                case 5:
                                    System.out.println("Digite o novo nome de usuário: ");
                                    contas.get(i).setNomeUsuario(sc.next());
                                    break;
                                case 6:
                                    System.out.println("Digite a nova senha: ");
                                    contas.get(i).setSenha(sc.next());
                                    break;
                                case 7:
                                    System.out.println("Digite o novo saldo da conta: ");
                                    contas.get(i).setSaldoConta(Float.parseFloat(sc.next()));
                                    break;
                                case 8:
                                    System.out.println("Digite o novo número de transferências realizadas: ");
                                    contas.get(i).setTransferenciasRealizadas(sc.nextInt());
                                    break;
                                case 9:
                                    System.out.println("Voltando...\n");
                                    break;
                                default:
                                    System.out.println("Opção inválida!");
                                    break;
                            }
                        }
                    }
                    break;
                case 5:
                    System.out.println("Deleteando registro ...");
                    System.out.println("Digite a conta que gostaria de deletar: ");
                    String conta4 = sc.next();
                    for (int i = 0; i < contas.size(); i++) {
                        if (contas.get(i).getNomeUsuario().equals(conta4)) {
                            contas.remove(i);
                        }
                    }
                    System.out.println("Conta deletada com sucesso!\n");
                    break;
                case 6:
                    System.out.println(
                            "Escolha uma ordenação:\n\n\t----------MENU----------\n1) Intercalação balanceada comum.\n2) Intercalação balanceada com blocos de tamanho variável.\n3) Intercalação balanceada com seleção por substituição.\n4) Intercalação usando n+1 arquivos.\n5) Intercalação Polifásica.\n6) para voltar.\n\t------------------------\n");

                    do {
                        try {
                            input = sc.nextInt();
                        } catch (Exception e) {
                            System.out.println("Digite um número válido!");
                            input = 0;
                            sc.next();
                        }

                    } while (input < 1 || input > 6);

                    switch (input) {
                        case 1:
                            System.out.println("Intercalação balanceada comum:");
                            balanceadaComum(contas);
                            System.out.println("Ordenação realizada com sucesso!\n");
                            break;
                        case 2:
                            System.out.println("Intercalação balanceada com blocos de tamanho variável:");
                            break;
                        case 3:
                            System.out.println("Interclação balanceada com seleção por substituição:");
                            break;
                        case 4:
                            System.out.println("Intercalação usando n+1 arquivos:");
                            break;
                        case 5:
                            System.out.println("Intercalação Polifásica:");
                            break;
                        case 6:
                            System.out.println("Voltando...");
                            break;
                        default:
                            System.out.println("Digite um número válido!");
                            break;
                    }
                    break;
                case 7:
                    loop = false;
                    break;
                default:
                    System.out.println("Error!!!Tecla inválida!");
                    break;
            }
        }

        sc.close();
    }
}