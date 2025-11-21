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

    public static void main(String[] args) {
        System.out.println("Sistema de Vendas Inicializado.");
        
        clientes.add(new ClienteBronze(101, "Ana Silva", "ana@email.com"));
        clientes.add(new ClientePrata(102, "Bruno Costa", "bruno@email.com"));
        clientes.add(new ClienteOuro(103, "Carla Vieira", "carla@email.com"));

        if (gerenciadorProdutos.getProdutos().isEmpty()) {
            System.out.println("Adicionando produtos iniciais...");
            gerenciadorProdutos.adicionarProduto(new ProdutoFisico(0, "Camiseta", 50.00, 10));
            gerenciadorProdutos.adicionarProduto(new ProdutoDigital(0, "Ebook Java", 99.90, 100));
            gerenciadorProdutos.adicionarProduto(new ProdutoFisico(0, "Mouse Gamer", 120.00, 5));
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
    

    private static void exibirMenuPrincipal() {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("1. Novo Pedido");
        System.out.println("2. Relatórios (Etapa 5)");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static void menuNovoPedido() {
        System.out.println("\n--- INICIAR NOVO PEDIDO ---");
        
        Cliente cliente = selecionarCliente();
        if (cliente == null) return;

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
        System.out.println("------------------------");


        FormaPagamento pagamento = menuSelecaoPagamento(total);
        if (pagamento != null && pagamento.processarPagamento(total)) {
            pedido.setStatus(StatusPedido.Confirmado);
            System.out.println("Pagamento realizado com sucesso! Status do Pedido: " + pedido.getStatus().getDescricao());
            
            double cashback = cliente.calcularCashback(total);
            cliente.adicionarGasto(total);
            System.out.printf("🎉 Cashback de R$ %.2f acumulado para %s (Total acumulado: R$ %.2f)\n", 
                                cashback, cliente.getNome(), cliente.getCashbackAcumulado());
            
            pedidos.add(pedido);
        } else {
            pedido.setStatus(StatusPedido.Cancelado);
            System.out.println("Falha no pagamento. Pedido Cancelado.");
        }
    }

    private static Cliente selecionarCliente() {
        System.out.println("\nClientes disponíveis:");
        clientes.forEach(c -> System.out.printf("ID: %d | Nome: %s | Nível: %s\n", c.getId(), c.getNome(), ((ClienteBronze)c).getNivel()));
        System.out.print("Digite o ID do Cliente: ");
        
        try {
            int id = Integer.parseInt(scanner.nextLine());
            return clientes.stream()
                           .filter(c -> c.getId() == id)
                           .findFirst()
                           .orElseGet(() -> {
                               System.out.println("Cliente não encontrado.");
                               return null;
                           });
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
            return null;
        }
    }

    private static void adicionarItensAoPedido(Pedido pedido) {
        String continuar;
        do {
            gerenciadorProdutos.listarProdutos();
            System.out.print("\nDigite o ID do produto para adicionar (ou 0 para terminar): ");
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

            if (produto instanceof ProdutoFisico && produto.getEstoque() < quantidade) {
                 System.out.println("Estoque insuficiente para " + produto.getNome() + ". Disponível: " + produto.getEstoque());
                 continue;
            }

            pedido.addItem(quantidade, produto);
            System.out.println(quantidade + "x " + produto.getNome() + " adicionado ao carrinho.");

            System.out.print("Adicionar outro item? (s/n): ");
            continuar = scanner.nextLine();
        } while (continuar.equalsIgnoreCase("s"));
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
                    return lerDadosCartaoNaMain();
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

    private static CartaoDeCredito lerDadosCartaoNaMain() {
        System.out.println("\n[Leitura de Dados do Cartão na Main] - Digite os detalhes:");
        CartaoDeCredito cartao = new CartaoDeCredito();
        cartao.lerDadosCartao(); 
        
        return cartao;
    }
    

    private static void menuRelatorios() {
        System.out.println("\n--- RELATÓRIOS ---");
        System.out.println("1. Listar Produtos com Desconto (Polimorfismo)");
        System.out.println("2. Listar Todos os Pedidos Confirmados");
        System.out.println("3. Listar Cashback Acumulado por Cliente");
        System.out.println("0. Voltar");
        System.out.print("Escolha uma opção de relatório: ");

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
            System.out.printf("Tipo: %s | Nome: %s | Preço Original: R$ %.2f | Desconto (Polimorfismo): R$ %.2f | Preço Final: R$ %.2f\n", 
                              p.getTipo(), p.getNome(), p.getPreco(), p.calcularDesconto(), precoComDesconto);
        });
    }

    private static void relatorioPedidosConfirmados() {
        System.out.println("\n--- RELATÓRIO DE PEDIDOS CONFIRMADOS ---");
        List<Pedido> confirmados = pedidos.stream()
                                          .filter(p -> p.getStatus() == StatusPedido.Confirmado)
                                          .collect(Collectors.toList());

        if (confirmados.isEmpty()) {
            System.out.println("Nenhum pedido confirmado encontrado.");
            return;
        }

        confirmados.forEach(p -> {
            System.out.printf("ID: %d | Cliente: %s | Total: R$ %.2f | Itens: %d\n", 
                              p.getId(), p.getNomeCliente(), p.calcularTotal(), p.getItens().size());
        });
    }
    
    private static void relatorioCashback() {
        System.out.println("\n--- RELATÓRIO DE CASHBACK POR CLIENTE ---");
        clientes.forEach(c -> {
            String nivel = ((ClienteBronze)c).getNivel(); 
            System.out.printf("Cliente: %s (%s) | Total Gasto: R$ %.2f | Cashback Acumulado: R$ %.2f\n",
                              c.getNome(), nivel, c.getTotalGasto(), c.getCashbackAcumulado());
        });
    }
}
