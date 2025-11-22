import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static GerenciadorProdutos gerenciadorProdutos = new GerenciadorProdutos();
    private static GerenciadorCupons gerenciadorCupons = new GerenciadorCupons();
    private static List<Cliente> clientes = new ArrayList<>();
    private static List<Pedido> pedidos = new ArrayList<>();
    private static int nextPedidoId = 1;
    private static String arquivoClientes = "clientes.txt";
    private static String arquivoPedidos = "pedidos.txt";

    public static void main(String[] args) {
        System.out.println("Sistema de Vendas Inicializado.");
        
        carregarClientes();
        carregarPedidos();

        if (gerenciadorProdutos.getProdutos().isEmpty()) {
            System.out.println("Adicionando produtos iniciais...");
            gerenciadorProdutos.adicionarProduto(new ProdutoFisico(1, "Camiseta", 50.00, 10));
            gerenciadorProdutos.adicionarProduto(new ProdutoDigital(2, "Ebook Java", 99.90));
            gerenciadorProdutos.adicionarProduto(new ProdutoFisico(3, "Mouse Gamer", 120.00, 5));
        }

        
        int opcao = -1;
        while (opcao != 0) {
            exibirMenuPrincipal();
            try {
                opcao = Integer.parseInt(scanner.nextLine());
                switch (opcao) {
                    case 1:
                        menuNovoPedido();
                        break;
                    case 2:
                        menuRelatorios();
                        break;
                    case 0:
                        System.out.println("Obrigado, saindo do sistema...");
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número.");
            } catch (Exception e) {
                System.out.println("Ocorreu um erro: " + e.getMessage());
            }
        }
        scanner.close();
    }

    public static void carregarClientes() {
    try {
        File file = new File(arquivoClientes);
        if (!file.exists()) return; 
        Scanner fileScanner = new Scanner(file);
        while (fileScanner.hasNextLine()) {
            String linha = fileScanner.nextLine();
            if (!linha.trim().isEmpty()) {
                String[] dados = linha.split(";");
                if (dados.length >= 4) {
                    int id = Integer.parseInt(dados[0]);
                    String nome = dados[1];
                    String email = dados[2];
                    String tipo = dados[3];
                    
                    Cliente cliente;
                    switch (tipo) {
                        case "OURO": cliente = new ClienteOuro(id, nome, email); break;
                        case "PRATA": cliente = new ClientePrata(id, nome, email); break;
                        default: cliente = new ClienteBronze(id, nome, email);
                    }
                    if (dados.length >= 6) {
                        try {
                            double gasto = Double.parseDouble(dados[4]);
                            double cashback = Double.parseDouble(dados[5]);
                            cliente.setTotalGasto(gasto);       
                            cliente.setCashbackAcumulado(cashback); 
                        } catch (NumberFormatException e) {
                            System.out.println("Erro ao ler valores financeiros do cliente " + id);
                        }
                    }

                    clientes.add(cliente);
                }
            }
        }
        fileScanner.close();
    
    } catch (Exception e) {
        System.out.println("Erro ao carregar clientes: " + e.getMessage());
    }
}

    private static void salvarClientes() {
    try {
        PrintWriter writer = new PrintWriter(arquivoClientes);
        for (Cliente c : clientes) {
            String tipo = "BRONZE";
            if (c instanceof ClienteOuro) tipo = "OURO";
            else if (c instanceof ClientePrata) tipo = "PRATA";
            writer.println(c.getId() + ";" + 
                           c.getNome() + ";" + 
                           c.getEmail() + ";" + 
                           tipo + ";" + 
                           c.getTotalGasto() + ";" + 
                           c.getCashbackAcumulado());
        }
        writer.close();
    } catch (Exception e) {
        System.out.println("Erro ao salvar clientes: " + e.getMessage());
    }
}

    private static void carregarPedidos() {
        try {
            File file = new File(arquivoPedidos);
            if (!file.exists()) return;
            
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                String linha = fileScanner.nextLine();
                if (linha.startsWith("//") || linha.trim().isEmpty()) continue;
                
                String[] dados = linha.split(";");
                if (dados.length >= 6) {
                    int id = Integer.parseInt(dados[0]);
                    String nomeCliente = dados[1];
                    String status = dados[2];
                    double total = Double.parseDouble(dados[3]);
                    String tipo = dados[4];
                    String itensStr = dados[5];
                                            
                    if (id >= nextPedidoId) {
                        nextPedidoId = id + 1;
                    }
                        
                    if (status.equals("CONFIRMADO")) {
                        Pedido pedido;
                        if (tipo.equals("(VIP)")) {
                            pedido = new PedidoVip(id, nomeCliente, status);
                        } else {
                            pedido = new PedidoNormal(id, nomeCliente, status);
                        }
                        pedido.setStatus(StatusPedido.Confirmado);

                        if (!itensStr.equals("VAZIO")) {
                            String[] itens = itensStr.split(",");
                            for (String item : itens) {
                                String[] itemData = item.split(":");
                                if (itemData.length == 2) {
                                    String nomeProduto = itemData[0];
                                    int quantidade = Integer.parseInt(itemData[1]);
                                
                                    Produto produto = gerenciadorProdutos.getProdutos().stream()
                                    .filter(p -> p.getNome().equals(nomeProduto))
                                    .findFirst()
                                    .orElse(null);
                                
                                    if (produto != null) {
                                        pedido.addItem(quantidade, produto);
                                    }
                                }
                            }
                        }
                        pedidos.add(pedido);
                    }
                }
            }
            
            fileScanner.close();
            System.out.println(pedidos.size() + " pedidos históricos carregados.");
        } catch (Exception e) {
            System.out.println("Erro ao carregar pedidos: " + e.getMessage());
        }
    }
    
    private static void salvarPedidos() {
        try {
            PrintWriter writer = new PrintWriter(arquivoPedidos);
            writer.println("// Formato: ID;CLIENTE;STATUS;TOTAL;TIPO;ITENS");

            for (Pedido p : pedidos) {
                if (p.getStatus() == StatusPedido.Confirmado) {
                    String tipo = (p instanceof PedidoVip) ? "VIP" : "NORMAL";

                    String itensStr = "VAZIO";
                    if (!p.getItens().isEmpty()) {
                        itensStr = p.getItens().stream()
                            .map(item -> item.getProduto().getNome() + ":" + item.getQuantidade())
                            .collect(Collectors.joining(","));
                    }

                    writer.printf("%d;%s;%s;%.2f;%s;%s%n", 
                                 p.getId(), 
                                 p.getNomeCliente(),
                                 p.getStatus().name(),
                                 p.calcularTotal(),
                                 tipo,
                                 itensStr);
                }
            }
            writer.close();
        } catch (Exception e) {
            System.out.println("Erro ao salvar pedidos: " + e.getMessage());
        }
    }
    private static void salvarCashbackClientes() {
    String arquivoCashback = "cashback_clientes.txt";
    try (java.io.FileWriter fw = new java.io.FileWriter(arquivoCashback, false);
         PrintWriter writer = new PrintWriter(fw)) {
        writer.println("RELATÓRIO DE CASHBACK - " + java.time.LocalDateTime.now());
        writer.println("==========================================");
        if (clientes.isEmpty()) {
            writer.println("Nenhum cliente cadastrado para exibir.");
        }
        for (Cliente c : clientes) {
            String nivel = getNivelCliente(c);
            writer.printf("Cliente: %s | Nível: %s | Email: %s | Cashback: R$ %.2f%n",
                        c.getNome(), nivel, c.getEmail(), c.getCashbackAcumulado());
        }
        System.out.println("SUCESSO: Relatório de cashback salvo em '" + arquivoCashback + "'"); 
    } catch (Exception e) {
        System.out.println("ERRO CRÍTICO ao salvar cashback: " + e.getMessage());
        e.printStackTrace(); 
    }
}
    private static Cliente menuLoginCadastro() {
        System.out.println("\n--- LOGIN / CADASTRO ---");
        System.out.println("1. Já tenho login");
        System.out.println("2. Criar novo cadastro");
        System.out.print("Escolha uma opção: ");
        
        try {
            int opcao = Integer.parseInt(scanner.nextLine());
            switch (opcao) {
                case 1:
                    return fazerLogin();
                case 2:
                    return criarNovoCadastro();
                default:
                    System.out.println("Opção inválida.");
                    return null;
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida.");
            return null;
        }
    }

    private static Cliente fazerLogin() {
        System.out.println("\n--- LOGIN ---");
        System.out.println("Clientes cadastrados:");
        
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado. Crie um novo cadastro.");
            return null;
        }
        
        for (Cliente c : clientes) {
            String nivel = getNivelCliente(c);
            System.out.printf("ID: %d | Nome: %s | Email: %s | Nível: %s\n", 
                            c.getId(), c.getNome(), c.getEmail(), nivel);
        }
        
        System.out.print("Digite o ID do seu cadastro: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Cliente cliente = clientes.stream()
                                    .filter(c -> c.getId() == id)
                                    .findFirst()
                                    .orElse(null);
            if (cliente != null) {
                System.out.println("Login realizado! Bem-vindo, " + cliente.getNome() + "!");
                return cliente;
            } else {
                System.out.println("ID não encontrado.");
                return null;
            }
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
            return null;
        }
    }

     private static Cliente criarNovoCadastro() {
        System.out.println("\n--- NOVO CADASTRO ---");
        
        int novoId = 1;
        for (Cliente c : clientes) {
            if (c.getId() >= novoId) {
                novoId = c.getId() + 1;
            }
        }
        
        System.out.print("Digite seu nome: ");
        String nome = scanner.nextLine();
        
        System.out.print("Digite seu email: ");
        String email = scanner.nextLine();
        
        Cliente novoCliente = new ClienteBronze(novoId, nome, email);
        clientes.add(novoCliente);
        salvarClientes();
        
        System.out.println("Cadastro realizado com sucesso! Bem-vindo, " + nome + "!");
        System.out.println("Seu nível inicial é: Bronze");
        return novoCliente;
    }

    private static String getNivelCliente(Cliente cliente) {
        if (cliente instanceof ClienteOuro) {
            return ((ClienteOuro) cliente).getNivel();
        } else if (cliente instanceof ClientePrata) {
            return ((ClientePrata) cliente).getNivel();
        } else {
            return ((ClienteBronze) cliente).getNivel();
        }
    }

    private static void exibirMenuPrincipal() {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("1. Novo Pedido");
        System.out.println("2. Relatórios");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static void menuNovoPedido() {
        System.out.println("\n--- INICIAR NOVO PEDIDO ---");
        
        Cliente cliente = menuLoginCadastro();
        if (cliente == null) {
            System.out.println("Não foi possível identificar o cliente. Operação cancelada.");
            return;
        }

        Pedido pedido;
        if (cliente instanceof ClienteOuro || cliente instanceof ClientePrata) {
            pedido = new PedidoVip(nextPedidoId++, cliente.getNome(), "PENDENTE");
            System.out.println("Pedido VIP criado para " + cliente.getNome() + ".");
        } else {
            pedido = new PedidoNormal(nextPedidoId++, cliente.getNome(), "PENDENTE");
            System.out.println("Pedido Normal criado para " + cliente.getNome() + ".");
        }
        
        adicionarItensAoPedido(pedido);
        if (pedido.getItens().isEmpty()) {
            System.out.println("Pedido cancelado: nenhum item adicionado.");
            nextPedidoId--;
            return;
        }

        aplicarCupom(pedido);
        
        double total = pedido.calcularTotal();

        System.out.println("\n--- RESUMO DO PEDIDO ---");
        System.out.println(pedido);
        System.out.printf("SUBTOTAL: R$ %.2f\n", pedido.calcularSubtotal());
        System.out.printf("TOTAL A PAGAR (c/ descontos/frete): R$ %.2f\n", total);
        
        double cashbackDisponivel = cliente.getCashbackAcumulado();
        double cashbackUsado = 0;

        if (cashbackDisponivel>0) {
            System.out.printf("\nVocê tem R$ %.2f de cashback acumulado!%n", cashbackDisponivel);
            System.out.print("Deseja usar cashback nesta compra? (s/n): ");
            
            if (scanner.nextLine().equalsIgnoreCase("s")) {
                System.out.println("Quanto deseja usar? R$ ");
                 try {
                    double cashbackUsar = Double.parseDouble(scanner.nextLine());

                    cashbackUsar = Math.min(cashbackUsar, cashbackDisponivel);
                    cashbackUsar = Math.min(cashbackUsar, total);

                    if (cashbackUsar > 0) {
                        cashbackUsado = cashbackUsar;
                        total -= cashbackUsado;
                        cliente.cashbackAcumulado -= cashbackUsado;  
                        
                        System.out.printf("Cashback de R$ %.2f aplicado com sucesso!%n", cashbackUsado);
                        System.out.printf("Novo total a pagar: R$ %.2f%n", total);
                        System.out.printf("Cashback restante: R$ %.2f%n", cliente.getCashbackAcumulado());
                    }
                    else{
                        System.out.println("Valor inválido. Cashback não aplicado.");
                    }
                    
                 } catch (Exception e) {
                    System.out.println("Valor inválido. Cashback não aplicado.");
                 }
            }
        }

        System.out.println("------------------------");

        FormaPagamento pagamento = menuSelecaoPagamento(total);
        if (pagamento != null && pagamento.processarPagamento(total)) {
            pedido.setStatus(StatusPedido.Confirmado);
            System.out.println("Pagamento realizado com sucesso! Status do Pedido: " + pedido.getStatus().getDescricao());
            
            double novoCashback = cliente.calcularCashback(total);
            cliente.adicionarGasto(total);

            System.out.printf("Novo cashback gerado: R$ %.2f%n", novoCashback);
            if (cashbackUsado > 0) {
                System.out.printf("Cashback usado nesta compra: R$ %.2f%n", cashbackUsado);
            }
            System.out.printf("Cashback total acumulado: R$ %.2f%n", cliente.getCashbackAcumulado());
            
            pedidos.add(pedido);

            salvarCashbackClientes();
            salvarPedidos();

            System.out.println("Pedido salvo no histórico!");

        } else {
            if (cashbackUsado>0) {
                cliente.cashbackAcumulado += cashbackUsado;
                System.out.printf("Cashback de R$ %.2f devolvido devido à falha no pagamento.%n", cashbackUsado);
            }
            pedido.setStatus(StatusPedido.Cancelado);
            System.out.println("Falha no pagamento. Pedido Cancelado.");
        }
    }


    private static void adicionarItensAoPedido(Pedido pedido) {
        
        if (gerenciadorProdutos == null || pedido == null) {
            System.out.println("Erro: Gerenciador de produtos ou pedido não inicializado.");
            return;
        }
        String continuar = null;

        do {    
            gerenciadorProdutos.listarProdutos();
            System.out.print("\nDigite o ID do produto para adicionar (ou 0 para terminar): ");

            try{
                int idProduto = Integer.parseInt(scanner.nextLine());

                if (idProduto == 0) break;

                Produto produto = gerenciadorProdutos.getProdutos().stream()
                                  .filter(p -> p.getId() == idProduto)
                                  .findFirst()
                                  .orElse(null);

                if (produto == null) {
                    System.out.println("Produto não encontrado.");
                    continue;
                }

                System.out.print("Quantidade: ");
                int quantidade = Integer.parseInt(scanner.nextLine());

                if (quantidade <= 0) {
                  System.out.println("Quantidade deve ser maior que zero. ");
                  continue;
                }

                if (!produto.temEstoqueSuficiente(quantidade)) {
                    if (produto instanceof ProdutoFisico) {
                        System.out.println("Estoque insuficiente para " + produto.getNome() + ". Disponível: " + ((ProdutoFisico)produto).getEstoque());
                    }
                    else{
                        System.out.println("Quantidade indisponível para " + produto.getNome());
                    }
                    continue;
                }
                pedido.addItem(quantidade, produto);
                produto.reduzirEstoque(quantidade);

                gerenciadorProdutos.salvarProdutos();

                System.out.println(quantidade + "x " + produto.getNome() + " adicionado ao carrinho.");

                System.out.print("Adicionar outro item? (s/n): ");
                continuar = scanner.nextLine();
            } catch(NumberFormatException e){
                System.out.println("Error: Por favor digite um número válido");
                continuar = "s";
            } catch(Exception e){
                System.out.println("Erro inesperado: " + e.getMessage());
                continuar = "s";
                break;
            }
        } while (continuar !=null && continuar.equalsIgnoreCase("s"));
    }

    private static void aplicarCupom(Pedido pedido) {
        System.out.print("Deseja aplicar um cupom de desconto? (s/n): ");
        if (scanner.nextLine().equalsIgnoreCase("s")) {
            System.out.print("Digite o código do cupom: ");
            String codigo = scanner.nextLine();
            
            DescontoStrategy cupom = gerenciadorCupons.buscarCupom(codigo);
            if (cupom != null) {
                pedido.setDescontoStrategy(cupom);
                System.out.println("Cupom '" + codigo + "' aplicado com sucesso!");
            } else {
                System.out.println("Cupom inválido ou expirado.");
            }
        }
    }

    private static FormaPagamento menuSelecaoPagamento(double valorTotal) {
        System.out.println("\n--- SELEÇÃO DE PAGAMENTO ---");
        System.out.printf("Valor Total: R$ %.2f\n", valorTotal);
        System.out.println("1. Cartão de Crédito");
        System.out.println("2. PIX");
        System.out.println("3. Boleto");
        System.out.print("Escolha a forma de pagamento: ");
        
        try {
            int opcao = Integer.parseInt(scanner.nextLine());
            switch (opcao) {
                case 1:
                    return criarCartaoCredito();
                case 2:
                    return new Pix();
                case 3:
                    return new Boleto();
                default:
                    System.out.println("Opção inválida de pagamento.");
                    return null;
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida.");
            return null;
        }
    }

    private static CartaoDeCredito criarCartaoCredito(){
        System.out.println("\n--- DADOS DO CARTÃO ---");
        
        System.out.print("Número do cartão: ");
        String numero = scanner.nextLine();
    
        System.out.print("Nome do titular: ");
        String titular = scanner.nextLine();
    
        CartaoDeCredito cartao = new CartaoDeCredito();
        cartao.setNumero(numero);
        cartao.setTitular(titular);
        
        return cartao;
    }

    private static void menuRelatorios() {
    System.out.println("\n--- RELATÓRIOS ---");
    System.out.println("1. Listar Produtos com Desconto (Tela)");
    System.out.println("2. Listar Pedidos Confirmados (Tela)");
    System.out.println("3. Listar Cashback na Tela");
    System.out.println("4. GERAR ARQUIVO TXT DE CASHBACK"); 
    System.out.println("0. Voltar");
    System.out.print("Escolha uma opção: ");

    try {
        int opcao = Integer.parseInt(scanner.nextLine());
        switch (opcao) {
            case 1:
                relatorioProdutosDesconto();
                break;
            case 2:
                relatorioPedidosConfirmados();
                break;
            case 3:
                relatorioCashback(); 
                break;
            case 4:
                salvarCashbackClientes(); 
                break;
            case 0:
                break;
            default:
                System.out.println("Opção inválida.");
        }
    } catch (NumberFormatException e) {
        System.out.println("Entrada inválida.");
    }
}

    private static void relatorioProdutosDesconto() {
        System.out.println("\n--- RELATÓRIO DE PRODUTOS E DESCONTOS ---");
        gerenciadorProdutos.getProdutos().forEach(p -> {
            double precoComDesconto = p.getPreco() - p.calcularDesconto();
            System.out.printf("Tipo: %s | Nome: %s | Preço Original: R$ %.2f | Desconto: R$ %.2f | Preço Final: R$ %.2f\n", 
                              p.getTipo(), p.getNome(), p.getPreco(), p.calcularDesconto(), precoComDesconto);
        });
    }

    private static void relatorioPedidosConfirmados() {
        System.out.println("\n--- RELATÓRIO DE PEDIDOS CONFIRMADOS ---");

        try {
            File file = new File(arquivoPedidos);
            if (!file.exists()) {
                System.out.println("Nenhum pedido confirmado encontrado.");
                return;
            }

            Scanner fileScanner = new Scanner(file);
            int count = 0;

            while (fileScanner.hasNextLine()) {
                String linha = fileScanner.nextLine();
                if (linha.startsWith("//") || linha.trim().isEmpty()) continue;
            
                String[] dados = linha.split(";");
                 if (dados.length >= 6 && dados[2].trim().equalsIgnoreCase("CONFIRMADO")) {
                    String id = dados[0];
                    String cliente = dados[1];
                    String total = dados[3];
                    String tipo = dados[4];
                    String itens = dados[5];
                
                    System.out.printf("ID: %s | Cliente: %s | Total: R$ %s | Tipo: %s%n", id, cliente, total, tipo);

                    System.out.println("   Itens: " + itens);
                    System.out.println("   ---");
                    count++;
                }
            }
            fileScanner.close();
        
            if (count == 0) {
                System.out.println("Nenhum pedido confirmado encontrado.");
            } else {
                System.out.println("Total de pedidos: " + count);
            }

        } catch (Exception e) {
            System.out.println("Erro ao ler pedidos: " + e.getMessage());
        }
    }
    
    private static void relatorioCashback() {
        System.out.println("\n--- RELATÓRIO DE CASHBACK POR CLIENTE ---");
        clientes.forEach(c -> {
            String nivel = getNivelCliente(c);
            System.out.printf("Cliente: %s (%s) | Total Gasto: R$ %.2f | Cashback Acumulado: R$ %.2f\n",
                              c.getNome(), nivel, c.getTotalGasto(), c.getCashbackAcumulado());
        });
    }
}
