public class ProdutoDigital extends Produto {
    
    public ProdutoDigital(int id, String nome, double preco, int estoque) {
        super(id, nome, preco, estoque);
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
    public boolean vender(int quantidade) {
        return true;
    }
}