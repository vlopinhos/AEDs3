import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Conta {

    // --------------- Atributos ---------------

    private int idConta; // Id da conta
    private String nomePessoa; // Nome da pessoa
    private int qtdEmails; // Quantidade de emails
    private String[] email; // Emails
    private String nomeUsuario; // Nome de usuario
    private String senha; // Senha
    private String cpf; // CPF 
    private String cidade; // Cidade
    private int transferenciasRealizadas; // Quantidade de transferencias realizadas
    private float saldoConta; // Saldo da conta

    // --------------- Construtores ---------------

    public Conta() { // Construtor padrao
        this.idConta = -1;
        this.nomePessoa = null;
        this.qtdEmails = 0;
        this.email = new String[qtdEmails];
        this.nomeUsuario = null;
        this.senha = null;
        this.cpf = null;
        this.cidade = null;
        this.transferenciasRealizadas = 0;
        this.saldoConta = -1;
    }

    public Conta(int idConta, String nomePessoa, int qtdEmails, String[] email, String nomeUsuario, String senha, String cpf, String cidade, int transferenciasRealizadas, float saldoConta) { // Construtor com parametros
        this.idConta = idConta;
        this.nomePessoa = nomePessoa;
        this.qtdEmails = qtdEmails;
        this.email = email;
        this.nomeUsuario = nomeUsuario;
        this.senha = senha;
        this.cpf = cpf;
        this.cidade = cidade;
        this.transferenciasRealizadas = transferenciasRealizadas;
        this.saldoConta = saldoConta;
    }

    // --------------- Getters e Setters ---------------

    // Id da conta
    public int getIdConta() {return idConta;}
    public void setIdConta(int idConta) {this.idConta = idConta;}

    // Nome da pessoa
    public String getNomePessoa() {return nomePessoa;}
    public void setNomePessoa(String nomePessoa) {this.nomePessoa = nomePessoa;}

    // Quantidade de emails
    public int getQtdEmails() {return qtdEmails;}
    public void setQtdEmails(int qtdEmails) {this.qtdEmails = qtdEmails;}

    // Emails
    public String[] getEmail() {return email;}
    public void setEmail(String[] email) {this.email = email;}

    // Nome de usuario
    public String getNomeUsuario() {return nomeUsuario;}
    public void setNomeUsuario(String nomeUsuario) {this.nomeUsuario = nomeUsuario;}

    // Senha
    public String getSenha() {return senha;}
    public void setSenha(String senha) {this.senha = senha;}

    // CPF
    public String getCpf() {return cpf;}
    public void setCpf(String cpf) {this.cpf = cpf;}

    // Cidade
    public String getCidade() {return cidade;}
    public void setCidade(String cidade) {this.cidade = cidade;}

    // Quantidade de transferencias realizadas
    public int getTransferenciasRealizadas() {return transferenciasRealizadas;}
    public void setTransferenciasRealizadas(int transferenciasRealizadas) {this.transferenciasRealizadas = transferenciasRealizadas;}

    // Saldo da conta
    public float getSaldoConta() {return saldoConta;}
    public void setSaldoConta(float saldoConta) {this.saldoConta = saldoConta;}

    // --------------- Métodos ---------------

    public byte[] toByteArray() throws IOException { // Converte objeto para array de bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream(); // Cria um array de bytes
        DataOutputStream dos = new DataOutputStream(baos); // Cria um fluxo de dados

        dos.writeInt(this.getIdConta()); // Escreve o id da conta no array de bytes
        dos.writeUTF(this.getNomePessoa()); // Escreve o nome da pessoa no array de bytes
        dos.writeInt(this.getQtdEmails()); // Escreve a quantidade de emails no array de bytes
        for(int i = 0; i < this.getQtdEmails(); i++){ // Escreve os emails no array de bytes
            dos.writeUTF(this.getEmail()[i]);
        }
        dos.writeUTF(this.getNomeUsuario()); // Escreve o nome de usuario no array de bytes
        dos.writeUTF(this.getSenha()); // Escreve a senha no array de bytes
        dos.writeUTF(this.getCpf()); // Escreve o CPF no array de bytes
        dos.writeUTF(this.getCidade()); // Escreve a cidade no array de bytes
        dos.writeInt(this.getTransferenciasRealizadas()); // Escreve a quantidade de transferencias realizadas no array de bytes
        dos.writeFloat(this.getSaldoConta()); // Escreve o saldo da conta no array de bytes

        dos.close();
        baos.close();

        return baos.toByteArray(); // Retorna o array de bytes
    }
}
