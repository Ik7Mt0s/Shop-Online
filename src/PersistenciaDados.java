import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PersistenciaDados {
    private static final String ARQUIVO_CLIENTES = "clientes.txt";
    
    // Método para salvar um cliente apenas se o CEP for válido
    public static boolean salvarCliente(Cliente cliente) {
        // Verifica se o CEP é válido antes de salvar
        if (!cliente.cepEhValido()) {
            System.out.println("❌ Cliente " + cliente.getNomeUsuario() + " não salvo - CEP inválido: " + cliente.getCep());
            return false;
        }
        
        try (FileWriter fw = new FileWriter(ARQUIVO_CLIENTES, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            
            // Formato: nome;cep
            String linha = cliente.getNomeUsuario() + ";" + cliente.getCep();
            out.println(linha);
            System.out.println("✅ Cliente salvo: " + cliente.getNomeUsuario() + " - CEP: " + cliente.getCep());
            return true;
            
        } catch (IOException e) {
            System.out.println("❌ Erro ao salvar cliente: " + e.getMessage());
            return false;
        }
    }
    
    // Método para carregar todos os clientes do arquivo
    public static List<Cliente> carregarClientes() {
        List<Cliente> clientes = new ArrayList<>();
        File arquivo = new File(ARQUIVO_CLIENTES);
        
        if (!arquivo.exists()) {
            System.out.println("📁 Arquivo de clientes não encontrado. Será criado automaticamente.");
            return clientes;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_CLIENTES))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                // Divide a linha no formato: nome;cep
                String[] dados = linha.split(";");
                if (dados.length == 2) {
                    String nome = dados[0].trim();
                    String cep = dados[1].trim();
                    
                    // Cria o cliente e verifica se o CEP ainda é válido
                    Cliente cliente = new Cliente(nome, cep);
                    if (cliente.cepEhValido()) {
                        clientes.add(cliente);
                        System.out.println("✅ Cliente carregado: " + nome + " - CEP: " + cep);
                    } else {
                        System.out.println("⚠️  Cliente " + nome + " não carregado - CEP inválido: " + cep);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Erro ao carregar clientes: " + e.getMessage());
        }
        
        return clientes;
    }
    
    // Método para limpar o arquivo (útil para testes)
    public static void limparArquivo() {
        try (FileWriter fw = new FileWriter(ARQUIVO_CLIENTES, false)) {
            // Abre o arquivo em modo sobrescrita (limpa tudo)
            System.out.println("🗑️  Arquivo de clientes limpo");
        } catch (IOException e) {
            System.out.println("❌ Erro ao limpar arquivo: " + e.getMessage());
        }
    }
    
    // Método para exibir todos os clientes salvos
    public static void exibirClientesSalvos() {
        List<Cliente> clientes = carregarClientes();
        
        if (clientes.isEmpty()) {
            System.out.println("📭 Nenhum cliente salvo no arquivo.");
        } else {
            System.out.println("\n📋 CLIENTES SALVOS NO ARQUIVO:");
            System.out.println("================================");
            for (int i = 0; i < clientes.size(); i++) {
                Cliente cliente = clientes.get(i);
                System.out.println((i + 1) + ". " + cliente.getNomeUsuario() + " - CEP: " + cliente.getCep());
            }
            System.out.println("================================");
            System.out.println("Total: " + clientes.size() + " cliente(s) válido(s)");
        }
    }
}