public class ProdutoFisico extends Produto {
    
    public ProdutoFisico(int id, String nome, double preco, int estoque) {
        super(id, nome, preco, estoque);
    }
    
    @Override
    public String getTipo() {
        return "FISICO";
    }
    
    @Override
    public double calcularDesconto() {
        return getPreco() * 0.10;
    }
}