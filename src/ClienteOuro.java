public class ClienteOuro extends Cliente {

    public ClienteOuro(int id, String nome, String email) {
        super(id, nome, email);
    }

    @Override
    public double calcularCashback(double valorCompra) {
        double cashback = valorCompra * 0.09;
        cashbackAcumulado += cashback;
        return cashback;
    }

    public String getNivel() {
        return "Ouro";
    }
}
