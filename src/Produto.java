public abstract class Produto {
    private int id;
    private String nome;
    private double preco;
    private int estoque;
    
    public Produto(int id, String nome, double preco, int estoque) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.estoque = estoque;
    }
    
    public abstract String getTipo();
    public abstract double calcularDesconto();
    
    public int getId() {
        return id; }
    public String getNome() {
        return nome; }
    public double getPreco() {
        return preco; }
    public int getEstoque() {
        return estoque; }
    
    public void setEstoque(int estoque) { this.estoque = estoque; }
    
    public boolean vender(int quantidade) {
        if (quantidade <= estoque) {
            estoque -= quantidade;
            return true;
        }
        return false;
    }
    
    public String salvar() {
        return id + ";" + nome + ";" + preco + ";" + estoque + ";" + getTipo();
    }
    
    @Override
    public String toString() {
        return "ID: " + id + " | " + nome + " | R$ " + preco + " | Estoque: " + estoque;
    }
}