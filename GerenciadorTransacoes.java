
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// O Gerenciador
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
        if (saldoSuficiente(valor, contaOrigem.getSaldo())) {
            // Mostra os saldos atualizados
            System.out.printf("Enviar valor R$%.2f de %s para %s?\n",valor, contaOrigem.pegaNome(),contaDestino.pegaNome());
            System.out.println("1 - sim\n2- não");

            int escolha = Integer.parseInt(String.valueOf(lerValor()));
            scan.nextLine();
            // scan.close();
            if(escolha ==2){
                return false;
            }

            // 2. Executa o débito
            // Não importa se é PayPal, AliPay ou ContaCliente,
            // o adaptador correto fará a tradução.
            boolean debitoOk = contaOrigem.realizaDebito(valor);
            
            if (debitoOk) {
                // 3. Executa o crédito
                boolean creditoOk = contaDestino.realizaCredito(valor);
                
                if (!creditoOk) {
                    // Ops, falhou no crédito! Precisa estornar.
                    contaOrigem.realizaEstorno(valor);

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
    
    private float lerValor(){
        String val;
        Scanner scanner = new Scanner(System.in);
        do{
            val = scanner.nextLine();
            if(!verificaValorValido(val))System.out.println("Insira um valor válido (número)!");
            else return Integer.parseInt(val);
        }
        while(true);
    }

    private boolean verificaValorValido(String valor){
        try{
            Float.parseFloat(valor);
            return true;
        }
        catch(Exception e){
            return false;
        }
    }
}