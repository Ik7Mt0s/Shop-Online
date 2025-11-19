public class PedidoVip extends Pedido {
    public PedidoVip(int Id, String nomeCliente, String Status) {
        super(Id, nomeCliente, Status);
    }
    @Override
    public double calcularTotal() {
        return calcularSubtotal() * 0.9;
    }
}
