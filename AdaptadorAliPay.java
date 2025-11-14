
public class AdaptadorAliPay implements PortaPagamento {
    private AliPay contaAliPay;
    private float cambioCNY_BRL = 0.68f; // Taxa de câmbio (exemplo)
    private String chaveAliString; // Atributo do diagrama

    public AdaptadorAliPay(AliPay contaAliPay) {
        this.contaAliPay = contaAliPay;
        this.chaveAliString = contaAliPay.getTelefone();
    }

    // Métodos de conversão (conforme diagrama)
    public float conversaoCNY_BRL(float valorCNY) {
        return valorCNY * cambioCNY_BRL;
    }

    public float conversaoBRL_CNY(float valorBRL) {
        return valorBRL / cambioCNY_BRL;
    }

    @Override
    public boolean realizaDebito(float valorBRL) {
        float valorCNY = conversaoBRL_CNY(valorBRL);
        // "Traduz" a chamada
        return contaAliPay.debitar(valorCNY);
    }

    @Override
    public boolean realizaCredito(float valorBRL) {
        float valorCNY = conversaoBRL_CNY(valorBRL);
        // "Traduz" a chamada
        return contaAliPay.creditar(valorCNY);
    }

    @Override
    public boolean realizaEstorno(float valorBRL) {
        return this.realizaCredito(valorBRL);
    }

    @Override
    public float getSaldo() {
        // Pega o saldo em CNY e "traduz" de volta para BRL
        float saldoCNY = contaAliPay.getSaldoAtual();
        return conversaoCNY_BRL(saldoCNY);
    }

    @Override
    public String getChave(){
        return this.chaveAliString;
    }
    @Override
    public String pegaNome(){
        return this.contaAliPay.getNome();
    }

}