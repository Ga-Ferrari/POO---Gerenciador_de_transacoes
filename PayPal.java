public class PayPal {
    private String nome;
    private float saldo; // Saldo em USD
    private String email;

    public PayPal(String nome, String email, float saldoInicialUSD) {
        this.nome = nome;
        this.email = email;
        this.saldo = saldoInicialUSD;
    }

    // Métodos "próprios" do sistema PayPal
    public boolean debitar(float valorUSD) {
        if (this.saldo >= valorUSD) {
            this.saldo -= valorUSD;
            System.out.println("Conta do PayPal de" + this.nome + ": Debito de $" + valorUSD + " USD. Saldo atual: $"
                    + this.saldo);
            return true;
        }
        System.out.println("PayPal: Saldo insuficiente.");
        return false;
    }

    public boolean creditar(float valorUSD) {
        this.saldo += valorUSD;
        System.out.println("PayPal: Credito de $" + valorUSD + " USD. Saldo atual: $" + this.saldo);
        return true;
    }

    public float getSaldoTotal() { // Nome do método "próprio"
        return this.saldo;
    }

    public String getEmail() {
        return this.email;
    }

    public String getNome() {
        return this.nome;
    }

}