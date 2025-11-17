public class AliPay {
    private String nome;
    private float saldo; // Saldo em CNY
    private String telefone;

    public AliPay(String nome, String telefone, float saldoInicialCNY) {
        this.nome = nome;
        this.telefone = telefone;
        this.saldo = saldoInicialCNY;
    }

    // Métodos "próprios" do sistema AliPay
    public boolean debitar(float valorCNY) {
        if (this.saldo >= valorCNY) {
            this.saldo -= valorCNY;
            System.out.println("AliPay: Debito de ¥" + valorCNY + " CNY. Saldo atual: ¥" + this.saldo);
            return true;
        }
        System.out.println("AliPay: Saldo insuficiente.");
        return false;
    }

    public boolean creditar(float valorCNY) {
        this.saldo += valorCNY;
        System.out.println("AliPay: Credito de ¥" + valorCNY + " CNY. Saldo atual: ¥" + this.saldo);
        return true;
    }

    public float getSaldoAtual() { // Nome do método "próprio"
        return this.saldo;
    }

    public String getTelefone() {
        return this.telefone;
    }

    public String getNome() {
        return this.nome;
    }

}