public class ClienteBronze extends Cliente {

    public ClienteBronze(int id, String nome, String email) {
        super(id, nome, email);
    }

    @Override
    public double calcularCashback(double valorCompra) {
        double cashback = valorCompra * 0.02;
        cashbackAcumulado += cashback;
        return cashback;
    }

    public String getNivel() {
        return "Bronze";
    }
}
