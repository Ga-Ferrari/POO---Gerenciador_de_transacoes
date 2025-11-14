public class AdaptadorConta implements PortaPagamento {
    private ContaCliente contaCliente;
    private String chaveContaCliente; // Atributo do diagrama

    public AdaptadorConta(ContaCliente contaCliente) {
        this.contaCliente = contaCliente;
        this.chaveContaCliente = contaCliente.getCpf();
    }

    @Override
    public boolean realizaDebito(float valorBRL) {
        // Não precisa de tradução, pois já está em BRL
        return contaCliente.debitar(valorBRL);
    }

    @Override
    public boolean realizaCredito(float valorBRL) {
        // Não precisa de tradução
        return contaCliente.creditar(valorBRL);
    }

    @Override
    public boolean realizaEstorno(float valorBRL) {
        // Um estorno de débito é um crédito
        return contaCliente.creditar(valorBRL);
    }

    @Override
    public float getSaldo() {
        // Não precisa de tradução
        return contaCliente.getSaldo();
    }
    @Override
    public String getChave(){
        return this.chaveContaCliente;
    }
    @Override
    public String pegaNome(){
        return this.contaCliente.getNome();
    }

}