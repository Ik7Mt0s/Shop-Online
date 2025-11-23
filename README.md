🛒 Sistema de Vendas e Gerenciamento (Console App)
Um sistema completo de e-commerce via terminal desenvolvido em Java. Esta aplicação simula o ciclo de vendas, desde o cadastro de clientes e produtos até o processamento de pedidos com diferentes formas de pagamento e persistência de dados.

📋 Funcionalidades Principais
👤 Para o Cliente
Autenticação: Login e Cadastro de novos usuários.

Carrinho de Compras: Adição de itens e validação de estoque em tempo real.

Sistema de Cashback:

Acúmulo automático baseado no nível do cliente (Bronze, Prata, Ouro).

Opção de utilizar o saldo de cashback para abater o valor de novas compras.

Pagamentos: Múltiplas estratégias (Cartão de Crédito, PIX, Boleto).

Cupons: Aplicação de códigos promocionais via Strategy Pattern.

🛡️ Para o Administrador (Novo!)
Acesso Restrito: Menu protegido por senha.

Cadastro Dinâmico: Adição de novos produtos (Físicos ou Digitais) diretamente pelo console, sem necessidade de alterar o código-fonte.

Relatórios:

Listagem de produtos e descontos aplicados.

Histórico de pedidos confirmados.

Exportação de relatório de cashback para arquivo .txt.

⚙️ Configurações e Credenciais
Para acessar o menu de administrador (Opção 3 no Menu Principal), utilize a credencial padrão configurada no código:

Configuração	Valor Padrão	Local no Código
Senha Admin	1234567	private static final String SENHA_ADMIN

Exportar para as Planilhas

🛠️ Tecnologias e Conceitos Aplicados
Java (JDK 8+): Linguagem base.

Persistência em Arquivo: Uso de java.io (Scanner, PrintWriter, File) para salvar dados de clientes e pedidos, garantindo que os dados não sejam perdidos ao fechar o programa.

Programação Orientada a Objetos:

Herança: Produto -> ProdutoFisico/ProdutoDigital; Cliente -> ClienteOuro/Prata/Bronze.

Polimorfismo: Tratamento genérico de pagamentos e cálculo de descontos.

Java Streams API: Utilizada para filtrar produtos, buscar clientes e formatar strings de itens no pedido.

📂 Estrutura de Arquivos de Dados
O sistema gera e lê automaticamente os seguintes arquivos na raiz do projeto:

clientes.txt: Base de dados de usuários.

Formato: ID;NOME;EMAIL;NIVEL;GASTO_TOTAL;CASHBACK

pedidos.txt: Histórico de transações.

Formato: ID;NOME_CLIENTE;STATUS;TOTAL;TIPO;ITENS

cashback_clientes.txt: Relatório gerado sob demanda (Opção 2 -> 4).

🚀 Como Executar
Pré-requisitos
Certifique-se de ter as classes de modelo (Cliente, Produto, Pedido, etc.) e serviços (GerenciadorProdutos, etc.) no mesmo pacote ou estrutura de pastas, pois este arquivo Main.java depende delas.

Passo a Passo
Compile o projeto:

Bash

javac Main.java
Execute a aplicação:

Bash

java Main
Primeira Execução:

O sistema identificará que não há produtos e carregará 3 produtos iniciais (Camiseta, Ebook, Mouse).

Para adicionar mais, vá em "3. Cadastrar Produto" e use a senha 1234567.

📝 Exemplo de Uso (Fluxo Admin)
Plaintext

--- MENU PRINCIPAL ---
1. Novo Pedido
2. Relatórios
3. Cadastrar Produto (Admin)
0. Sair
Escolha uma opção: 3

Digite a senha de administrador: 1234567
Acesso autorizado!

--- CADASTRO DE NOVO PRODUTO ---
Nome do Produto: Teclado Mecânico
Preço: R$ 250.00
Tipo do Produto:
1. Físico (Requer estoque)
2. Digital
Escolha: 1
Quantidade inicial em estoque: 20
Produto Físico cadastrado com sucesso!


AUTORES:
Ícaro Matos,
Murilo Souza,
Kaik Araujo,
Guilherme Moraes,
Rhuan Douglas.
