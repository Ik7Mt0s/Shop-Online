public class DescontoVIP implements DescontoStrategy {
    @Override
    public double aplicarDesconto(double valorTotal) {
        // Regra: Cliente VIP ganha 10% de desconto sobre o total (excluindo frete se houver)
        return valorTotal * 0.90; 
    }
}