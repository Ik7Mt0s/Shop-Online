public class ProdutoDigital extends Produto {
    
    public ProdutoDigital(int id, String nome, double preco) { 
        super(id, nome, preco);
    }
    
    @Override
    public String getTipo() {
        return "DIGITAL";
    }
    
    @Override
    public double calcularDesconto() {
        return getPreco() * 0.15;
    }
    
    @Override
    public boolean temEstoqueSuficiente(int quantidade) {
        return true; // Produtos digitais sempre têm "estoque"
    }
    
    @Override
    public void reduzirEstoque(int quantidade) {
        // Não faz nada - produtos digitais não têm estoque
    }
    
    @Override
    public String getInfoEstoque() {
        return "Estoque: Ilimitado";
    }
    
    @Override
    public String salvar() {
        return super.salvar() + ";0";
    }
}