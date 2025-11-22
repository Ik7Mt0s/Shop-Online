public class DescontoCupom implements DescontoStrategy {
    private double porcentagemDesconto;

    public DescontoCupom(double porcentagemDesconto) {
        this.porcentagemDesconto = porcentagemDesconto;
    }

    @Override
    public double aplicarDesconto(double valorTotal) {
        double fator = 1 - (porcentagemDesconto / 100.0);
        return valorTotal * fator;
    }
}