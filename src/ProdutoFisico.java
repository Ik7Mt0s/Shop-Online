public class ProdutoFisico extends Produto {
    private int estoque;
    
    public ProdutoFisico(int id, String nome, double preco, int estoque) {
        super(id, nome, preco);
        this.estoque = estoque;
    }
    
    @Override
    public String getTipo() {
        return "FISICO";
    }
    
    @Override
    public double calcularDesconto() {
        return getPreco() * 0.10;
    }
    
    @Override
    public boolean temEstoqueSuficiente(int quantidade) {
        return estoque >= quantidade;
    }
    
    @Override
    public void reduzirEstoque(int quantidade) {
        if (temEstoqueSuficiente(quantidade)) {
            estoque -= quantidade;
        }
    }
    
    @Override
    public String getInfoEstoque() {
        return "Estoque: " + estoque;
    }
    
    public int getEstoque() {
        return estoque;
    }
    
    @Override
    public String salvar() {
        return super.salvar() + ";" + estoque;
    }
}