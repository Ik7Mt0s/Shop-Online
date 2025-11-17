import java.util.*;
import java.io.*;

public class GerenciadorProdutos {
    private ArrayList<Produto> produtos;
    private String arquivo = "produtos.txt";
    
    public GerenciadorProdutos() {
        produtos = new ArrayList<>();
        carregarProdutos();
    }
    
    private void carregarProdutos() {
        try {
            File file = new File(arquivo);
            if (!file.exists()) return;
            
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                String[] dados = linha.split(";");
                
                int id = Integer.parseInt(dados[0]);
                String nome = dados[1];
                double preco = Double.parseDouble(dados[2]);
                int estoque = Integer.parseInt(dados[3]);
                String tipo = dados[4];
                
                Produto p;
                if (tipo.equals("FISICO")) {
                    p = new ProdutoFisico(id, nome, preco, estoque);
                } else {
                    p = new ProdutoDigital(id, nome, preco, estoque);
                }
                
                produtos.add(p);
            }
            scanner.close();
        } catch (Exception e) {
            System.out.println("Erro ao carregar produtos: " + e.getMessage());
        }
    }
    
    private void salvarProdutos() {
        try {
            PrintWriter writer = new PrintWriter(arquivo);
            for (Produto p : produtos) {
                writer.println(p.salvar());
            }
            writer.close();
        } catch (Exception e) {
            System.out.println("Erro ao salvar produtos: " + e.getMessage());
        }
    }
    
    public void adicionarProduto(Produto produto) {
        int novoId = 1;
        for (Produto p : produtos) {
            if (p.getId() >= novoId) {
                novoId = p.getId() + 1;
            }
        }
        
        try {
            produtos.add(produto);
            salvarProdutos();
        } catch (Exception e) {
            System.out.println("Erro ao adicionar produto: " + e.getMessage());
        }
    }
    
    public void listarProdutos() {
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }
        
        for (Produto p : produtos) {
            System.out.println(p);
        }
    }
    
    public ArrayList<Produto> getProdutos() {
        return produtos;
    }
}