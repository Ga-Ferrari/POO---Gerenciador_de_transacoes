public class AdaptadorPayPal implements PortaPagamento {
    private PayPal contaPayPal;
    private float cambioUSD_BRL = 5.15f; // Taxa de câmbio (exemplo)
    private String chavePayPalString; // Atributo do diagrama

    public AdaptadorPayPal(PayPal contaPayPal) {
        this.contaPayPal = contaPayPal;
        this.chavePayPalString = contaPayPal.getEmail();
    }

    // Métodos de conversão (conforme diagrama)
    public float conversaoUSD_BRL(float valorUSD) {
        return valorUSD * cambioUSD_BRL;
    }

    public float conversaoBRL_USD(float valorBRL) {
        return valorBRL / cambioUSD_BRL;
    }

    @Override
    public boolean realizaDebito(float valorBRL) {
        float valorUSD = conversaoBRL_USD(valorBRL);
        // "Traduz" a chamada do nosso sistema (BRL) para o sistema do PayPal (USD)
        return contaPayPal.debitar(valorUSD);
    }

    @Override
    public boolean realizaCredito(float valorBRL) {
        float valorUSD = conversaoBRL_USD(valorBRL);
        // "Traduz" a chamada
        return contaPayPal.creditar(valorUSD);
    }

    @Override
    public boolean realizaEstorno(float valorBRL) {
        // Um estorno de débito é um crédito.
        return this.realizaCredito(valorBRL);
    }

    @Override
    public float getSaldo() {
        // Pega o saldo em USD e "traduz" de volta para BRL
        float saldoUSD = contaPayPal.getSaldoTotal();
        return conversaoUSD_BRL(saldoUSD);
    }

    @Override
    public String getChave(){
        return this.chavePayPalString;
    }
    @Override
    public String pegaNome(){
        return this.contaPayPal.getNome();
    }

}