public class ContaCliente {
    private float saldo; // Saldo em BRL
    private String nome;
    private String cpf;

    public ContaCliente(String nome, String cpf, float saldoInicialBRL) {
        this.nome = nome;
        this.cpf = cpf;
        this.saldo = saldoInicialBRL;
    }

    // Métodos "próprios" da ContaCliente
    public boolean debitar(float valorBRL) {
        if (this.saldo >= valorBRL) {
            this.saldo -= valorBRL;
            System.out.println("ContaCliente: Débito de R$" + valorBRL + ". Saldo atual: R$" + this.saldo);
            return true;
        }
        System.out.println("ContaCliente: Saldo insuficiente.");
        return false;
    }

    public boolean creditar(float valorBRL) {
        this.saldo += valorBRL;
        System.out.println("ContaCliente: Crédito de R$" + valorBRL + ". Saldo atual: R$" + this.saldo);
        return true;
    }
    
    public float getSaldo() { // Nome do método "próprio"
        return this.saldo;
    }

    public String getCpf(){
        return this.cpf;
    }

    public String getNome(){
        return this.nome;
    }
}   