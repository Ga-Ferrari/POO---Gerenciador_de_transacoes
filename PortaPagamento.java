/**
 * A interface "PortaPagamento" define o contrato universal
 * para qualquer sistema de pagamento que o GerenciadorTransacoes
 * pode usar.
 */
public interface PortaPagamento {
    boolean realizaDebito(float valor);
    boolean realizaCredito(float valor);
    boolean realizaEstorno(float valor);
    String getChave(); //Necessário para achar qual a conta que irá receber a transação
    float getSaldo(); // Necessário para GerenciadorTransacoes.saldoSuficiente()
    String pegaNome();
}