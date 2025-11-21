public class CartaoDeCredito extends FormaPagamento {
    private String numero;
    private String titular;

    public CartaoDeCredito(String numero, String titular) {
        this.numero = numero;
        this.titular = titular;
    }
    @Override
    public boolean processarPagamento(double valor) {
        System.out.println("\n pagamento por CARTÃO DE CRÉDITO");

        if(numero == null || numero.length() < 13) {
            System.out.println(" Número do cartão inválido.");
            return false;
        }
        System.out.println("Transação autorizada para: " + titular);
        return true;
    }
}
