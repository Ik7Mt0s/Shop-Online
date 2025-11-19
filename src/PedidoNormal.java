public class PedidoNormal extends Pedido {
    public PedidoNormal(int id, String nomeCliente, String status) {
        super(id, nomeCliente, status);
    }

    @Override
    public double calcularTotal() {
        double subtotal = calcularSubtotal();

        subtotal = aplicarDescontoNoSubtotal(subtotal);
        
        double frete = 15.0;
        return subtotal + frete;
}
}
