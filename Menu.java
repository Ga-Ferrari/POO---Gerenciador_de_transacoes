import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {

    // Armazena todas as contas criadas, já "adaptadas"
    private List<PortaPagamento> contas;
    private GerenciadorTransacoes gerenciador;
    private Scanner scanner;

    public Menu() {
        this.contas = new ArrayList<>();
        this.gerenciador = new GerenciadorTransacoes();
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.exibirMenuPrincipal();
    }

    public void exibirMenuPrincipal() {
        while (true) {
            System.out.println("\n--- SISTEMA DE TRANSAÇÕES ---");
            System.out.println("1. Adicionar Nova Conta");
            System.out.println("2. Realizar Transação");
            System.out.println("3. Listar Contas");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            int escolha = scanner.nextInt();
            scanner.nextLine(); // Consome a quebra de linha

            switch (escolha) {
                case 1:
                    adicionarConta();
                    break;
                case 2:
                    realizarTransacao();
                    break;
                case 3:
                    listarContas();
                    break;
                case 0:
                    System.out.println("Saindo do sistema...");
                    return; // Encerra o loop e o programa
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private void adicionarConta() {
        System.out.println("\n--- Adicionar Nova Conta ---");
        System.out.println("Qual o tipo de conta?");
        System.out.println("1. Conta Cliente (BRL)");
        System.out.println("2. PayPal (USD)");
        System.out.println("3. AliPay (CNY)");
        System.out.println("0. Voltar ao menu");
        System.out.print("Escolha uma opção: ");

        int escolha = scanner.nextInt();
        scanner.nextLine(); // Consome a quebra de linha

        // Variáveis para criar a conta
        String nome;

        switch (escolha) {
            case 1: // Conta Cliente
                System.out.print("Nome do titular: ");
                nome = scanner.nextLine();
                System.out.print("CPF: ");
                String cpf = scanner.nextLine();
                System.out.print("Saldo inicial (BRL): ");
                float saldoBRL = scanner.nextFloat();
                
                // Cria a conta real e o adaptador
                ContaCliente novaConta = new ContaCliente(nome, cpf, saldoBRL);
                PortaPagamento adaptadorConta = new AdaptadorConta(novaConta);
                contas.add(adaptadorConta);
                System.out.println(">>> Conta Cliente adicionada com sucesso!");
                break;

            case 2: // PayPal
                System.out.print("Nome do titular: ");
                nome = scanner.nextLine();
                System.out.print("Email: ");
                String email = scanner.nextLine();
                System.out.print("Saldo inicial (USD): $");
                float saldoUSD = scanner.nextFloat();
                
                // Cria a conta real e o adaptador
                PayPal novaContaPayPal = new PayPal(nome, email, saldoUSD);
                PortaPagamento adaptadorPayPal = new AdaptadorPayPal(novaContaPayPal);
                contas.add(adaptadorPayPal);
                System.out.println(">>> Conta PayPal adicionada com sucesso!");
                break;

            case 3: // AliPay
                System.out.print("Nome do titular: ");
                nome = scanner.nextLine();
                System.out.print("Telefone: ");
                String telefone = scanner.nextLine();
                System.out.print("Saldo inicial (CNY): ¥");
                float saldoCNY = scanner.nextFloat();
                
                // Cria a conta real e o adaptador
                AliPay novaContaAliPay = new AliPay(nome, telefone, saldoCNY);
                PortaPagamento adaptadorAliPay = new AdaptadorAliPay(novaContaAliPay);
                contas.add(adaptadorAliPay);
                System.out.println(">>> Conta AliPay adicionada com sucesso!");
                break;

            case 0:
                return; // Volta ao menu principal
                
            default:
                System.out.println("Tipo de conta inválido.");
        }
    }

    private void realizarTransacao() {
        System.out.println("\n--- Realizar Transação ---");

        if (contas.size() < 2) {
            System.out.println("Erro: Você precisa ter pelo menos duas contas cadastradas para realizar uma transação.");
            return;
        }

        // // Lista as contas para o usuário escolher
        // System.out.println("Contas disponíveis:");
        // listarContas();

        // Selecionar conta de ORIGEM
        System.out.print("\nDigite a chave da conta de ORIGEM: ");
        String chaveOrigem = scanner.nextLine();

        System.out.print("\nDigite a chave da conta de DESTINO: ");
        String chaveDestino = scanner.nextLine();
        // Selecionar conta de DESTINO
        
        if(chaveOrigem == chaveDestino) {
            System.out.println("Erro: A conta de origem e destino não podem ser a mesma.");
            return;
        }
        
        // Obter valor
        System.out.print("Digite o valor da transação (em BRL): R$");
        float valorBRL = scanner.nextFloat();

        // Executa a transação
        System.out.println("\n...Processando transação...");
        boolean sucesso = gerenciador.solicitarTransacao(chaveOrigem, chaveDestino,contas, valorBRL);

        if (sucesso) {
            System.out.println(">>> SUCESSO: Transação concluída!");
        } else {
            System.out.println(">>> Transação não realizada.");
        }
    }
    
    private void listarContas() {
        System.out.println("\n--- Contas Cadastradas ---");
        if (contas.isEmpty()) {
            System.out.println("Nenhuma conta cadastrada.");
            return;
        }
        
        for (int i = 0; i < contas.size(); i++) {
            PortaPagamento conta = contas.get(i);
            // Pega o nome da classe do Adaptador (ex: "AdaptadorPayPal")
            printConta(conta);
            System.out.println();
        }
    }

    private void printConta(PortaPagamento conta){
        String tipoConta = conta.getClass().getSimpleName(); 
            
            System.out.printf("%s. Tipo: %s\n", conta.pegaNome(), tipoConta);
    }

}