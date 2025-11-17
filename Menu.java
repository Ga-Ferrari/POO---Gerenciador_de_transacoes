import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {

    // Armazena todas as contas criadas, já "adaptadas"
    private List<PortaPagamento> contas;
    private GerenciadorTransacoes gerenciador;
    private Scanner scanner;

    public Menu() {
        this.gerenciador = new GerenciadorTransacoes();
        this.scanner = new Scanner(System.in);
        this.contas = new ArrayList<>();
    }

    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.exibirMenuPrincipal();
    }

    public void exibirMenuPrincipal() {
        while (true) {
            System.out.println("\n--- SISTEMA DE TRANSACOES ---");
            System.out.println("1. Adicionar Nova Conta");
            System.out.println("2. Realizar Transacao");
            System.out.println("3. Listar Contas");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opcao: ");
            int escolha = (int) lerValor();

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
                    System.out.println("Opcao invalida. Tente novamente.");
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
        System.out.print("Escolha uma opcao: ");

        int escolha = (int) lerValor();

        // Variáveis para criar a conta
        String nome;
        boolean valido = false;
        switch (escolha) {
            case 1: // Conta Cliente
                System.out.print("Nome do titular: ");
                nome = scanner.nextLine();
                String lida;
                do {
                    System.out.print("CPF: ");
                    lida = scanner.nextLine();
                    valido = verificaCPF(lida);
                    if (!valido) {
                        System.out.println("O CPF deve seguir este formato: ***.***.***-**");
                    }
                } while (lida.length() != 14 || !valido || !chaveValida(lida));
                String cpf = lida;
                System.out.print("Saldo inicial (BRL): ");
                float saldoBRL = lerValor();

                // Cria a conta real e o adaptador
                ContaCliente novaConta = new ContaCliente(nome, cpf, saldoBRL);
                PortaPagamento adaptadorConta = new AdaptadorConta(novaConta);
                this.contas.add(adaptadorConta);
                System.out.println(">>> Conta Cliente adicionada com sucesso!");
                break;

            case 2: // PayPal
                System.out.print("Nome do titular: ");
                nome = scanner.nextLine();

                do {
                    System.out.print("Email: ");
                    lida = scanner.nextLine();
                    valido = (lida.contains(".com") && lida.contains("@"));
                    if (!valido) {
                        System.out.println("Insira um e-mail valido (***@***.com)");
                    }
                } while (!valido || !chaveValida(lida));
                String email = lida;
                System.out.print("Saldo inicial (USD): $");

                float saldoUSD = lerValor();

                // Cria a conta real e o adaptador
                PayPal novaContaPayPal = new PayPal(nome, email, saldoUSD);
                PortaPagamento adaptadorPayPal = new AdaptadorPayPal(novaContaPayPal);
                contas.add(adaptadorPayPal);
                System.out.println(">>> Conta PayPal adicionada com sucesso!");
                break;

            case 3: // AliPay
                System.out.print("Nome do titular: ");
                nome = scanner.nextLine();
                do {
                    System.out.print("Telefone: ");
                    lida = scanner.nextLine();
                    valido = verificaValorValido(lida);
                    if (lida.length() != 11 || !valido) {
                        System.out.println(
                                "O numero de telefone deve possuir 11 dígitos e somente numeros (com DDD e sem codigo de pais)");
                    }
                } while (lida.length() != 11 || !valido || !chaveValida(lida));
                String telefone = lida;
                System.out.print("Saldo inicial (CNY): ¥");

                float saldoCNY = lerValor();

                // Cria a conta real e o adaptador
                AliPay novaContaAliPay = new AliPay(nome, telefone, saldoCNY);
                PortaPagamento adaptadorAliPay = new AdaptadorAliPay(novaContaAliPay);
                contas.add(adaptadorAliPay);
                System.out.println(">>> Conta AliPay adicionada com sucesso!");
                break;

            case 0:
                return; // Volta ao menu principal

            default:
                System.out.println("Tipo de conta invalido.");
        }
    }

    private void realizarTransacao() {
        System.out.println("\n--- Realizar Transacao ---");

        if (contas.size() < 2) {
            System.out
                    .println("Erro: Voce precisa ter pelo menos duas contas cadastradas para realizar uma transacao.");
            return;
        }

        // Selecionar conta de ORIGEM
        System.out.print("\nDigite a chave da conta de ORIGEM: ");
        String chaveOrigem = scanner.nextLine();

        System.out.print("\nDigite a chave da conta de DESTINO: ");
        String chaveDestino = scanner.nextLine();

        // Selecionar conta de DESTINO
        if (chaveOrigem.equals(chaveDestino)) {
            System.out.println("\nErro: A conta de origem e destino nao podem ser a mesma.");
            return;
        }

        // Obter valor
        System.out.print("Digite o valor da transacao (em BRL): R$");
        float valorBRL = lerValor();

        if(valorBRL<=0){
            System.out.println("Por favor digite um valor maior que 0!");
            return;
        }

        // Executa a transação
        System.out.println("\n...Processando transacao...");
        boolean sucesso = gerenciador.solicitarTransacao(chaveOrigem, chaveDestino, contas, valorBRL);

        if (sucesso) {
            System.out.println(">>> SUCESSO: Transacao concluida!");
        } else {
            System.out.println(">>> Transacao nao foi realizada! Verifique os dados e tente novamente");
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

    private void printConta(PortaPagamento conta) {
        String tipoConta = conta.getClass().getSimpleName();

        if (tipoConta.equals("AdaptadorConta"))
            tipoConta = "Conta Cliente";
        else if (tipoConta.equals("AdaptadorPayPal"))
            tipoConta = "PayPal";
        else if (tipoConta.equals("AdaptadorAliPay"))
            tipoConta = "AliPay";

        System.out.printf("%s. Tipo: %s\n", conta.pegaNome(), tipoConta);
    }

    private boolean verificaValorValido(String valor) {
        try {
            Float.parseFloat(valor);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private float lerValor() {
        String val;
        do {
            val = scanner.nextLine();
            if (!verificaValorValido(val))
                System.out.println("Insira um valor valido (numero)!");
            else
                return Float.parseFloat(val);
        } while (true);
    }

    private boolean verificaCPF(String cpf) {
        for (int i = 0; i < cpf.length(); i++) {
            char c = cpf.charAt(i);

            if ((i + 1) % 4 == 0) {
                if (i <= 9) {
                    if (c != '.')
                        return false;
                } else {
                    if (c != '-')
                        return false;
                }
            } else {
                if (!(c >= '0' && c <= '9'))
                    return false;
            }
        }
        return true;
    }

    private boolean chaveValida(String chave) {
        for (PortaPagamento p : this.contas) {
            if (p.getChave().equals(chave)) {
                System.out.println("Ja ha uma conta com essa chave! Insira outra");
                return false;
            }
        }
        return true;
    }

}