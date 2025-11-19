public class PedidoVip extends Pedido {
    public PedidoVip(int Id, String nomeCliente, String Status) {
        super(Id, nomeCliente, Status);

        this.setDescontoStrategy(new DescontoVIP());
    }
    @Override
    public double calcularTotal() {
        double subtotal = calcularSubtotal();

        return aplicarDescontoNoSubtotal(subtotal);
    }
}
