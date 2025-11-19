public class DescontoCupom implements DescontoStrategy {
    private double porcentagemDesconto;

    public DescontoCupom(double porcentagemDesconto) {
        this.porcentagemDesconto = porcentagemDesconto;
    }

    @Override
    public double aplicarDesconto(double valorTotal) {
        // Encapsulamento: A lógica matemática fica escondida aqui
        double fator = 1 - (porcentagemDesconto / 100.0);
        return valorTotal * fator;
    }
}