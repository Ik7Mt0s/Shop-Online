public abstract class Produto {
    private int id;
    private String nome;
    private double preco;
    
    public Produto(int id, String nome, double preco) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
    }
    
    public abstract String getTipo();
    public abstract double calcularDesconto();
    public abstract boolean temEstoqueSuficiente(int quantidade);
    public abstract void reduzirEstoque(int quantidade);

    public int getId() { return id; }
    public String getNome() { return nome; }
    public double getPreco() { return preco; }
    
    public String salvar() {
        return id + ";" + nome + ";" + preco + ";" + getTipo();
    }
    
    @Override
    public String toString() {
        return "ID: " + id + " | " + getTipo() + " | " + nome + " | R$ " + preco + " | " + getInfoEstoque();
    }
    
    public abstract String getInfoEstoque();
}