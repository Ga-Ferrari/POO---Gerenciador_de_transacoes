
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// O Gerenciador, que só depende da interface
public class GerenciadorTransacoes {
    public boolean solicitarTransacao(String chaveContaOrigem, String chaveContaDestino,List<PortaPagamento> contas, float valor) {
        Scanner scan = new Scanner(System.in);
        PortaPagamento contaOrigem,contaDestino;
        int indexOrigem = findConta(chaveContaOrigem, contas),indexDestino = findConta(chaveContaDestino, contas);

        if(indexDestino ==-1||indexOrigem==-1){
            return false;
        }

        contaOrigem = contas.get(indexOrigem);
        contaDestino = contas.get(indexDestino);

        // 1. Verifica o saldo usando a interface
        // (Adicionei um getSaldo() à interface para isso)
        if (saldoSuficiente(valor, contaOrigem.getSaldo())) {
            // Mostra os saldos atualizados
            System.out.printf("Enviar valor R$%.2f de %s para %s?\n",valor, contaOrigem.pegaNome(),contaDestino.pegaNome());
            System.out.println("1 - sim\n2- não");

            int escolha = scan.nextInt();
            scan.nextLine();
            // scan.close();
            if(escolha ==2){
                return false;
            }

            // 2. Executa o débito (Polimorfismo!)
            // Não importa se é PayPal, AliPay ou ContaCliente,
            // o adaptador correto fará a tradução.
            boolean debitoOk = contaOrigem.realizaDebito(valor);
            
            if (debitoOk) {
                // 3. Executa o crédito
                boolean creditoOk = contaDestino.realizaCredito(valor);
                
                if (!creditoOk) {
                    // Ops, falhou no crédito! Precisa estornar.
                    // Assumindo que a interface tenha um estorno
                    // contaOrigem.realizaEstorno(valor); 
                    return false; // Falha na transação
                }
                return true; // Sucesso!
            }
        }
        
        return false; // Saldo insuficiente ou falha no débito
    }

    private int findConta(String chave,List<PortaPagamento> contas){
        for(int i=0;i<contas.size();i++){
            if(contas.get(i).getChave().equals(chave)){
                return i;
            }
        }
        return -1;
    }

    private boolean saldoSuficiente(float valor, float saldo) {
        return saldo >= valor;
    }
}