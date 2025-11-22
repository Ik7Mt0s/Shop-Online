public abstract class Cliente {
    private int id;
    private String nome;
    private String email;
    private double totalGasto;
    protected double cashbackAcumulado;

    public Cliente(int id, String nome, String email) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.totalGasto = 0.0;
        this.cashbackAcumulado = 0.0;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public double getTotalGasto() { return totalGasto; }
    public double getCashbackAcumulado() { return cashbackAcumulado; }

    public void adicionarGasto(double valor) {
        this.totalGasto += valor;
    }

    public abstract double calcularCashback(double valorCompra);
    
    public void setTotalGasto(double totalGasto) {
        this.totalGasto = totalGasto;
    }
    public void setCashbackAcumulado(double cashbackAcumulado) {
        this.cashbackAcumulado = cashbackAcumulado;
    }
}
