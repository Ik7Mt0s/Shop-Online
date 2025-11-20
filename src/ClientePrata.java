public class ClientePrata extends Cliente {

    public ClientePrata(int id, String nome, String email) {
        super(id, nome, email);
    }

    @Override
    public double calcularCashback(double valorCompra) {
        double cashback = valorCompra * 0.05;
        cashbackAcumulado += cashback;
        return cashback;
    }

    public String getNivel() {
        return "Prata";
    }
}
