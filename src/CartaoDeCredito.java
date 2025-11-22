public class CartaoDeCredito extends FormaPagamento {
    private String numero;
    private String titular;

    public void setNumero(String numero) {
        this.numero = numero;
    }
    
    public void setTitular(String titular) {
        this.titular = titular;
    }

    @Override
    public boolean processarPagamento(double valor) {
        System.out.println("\n pagamento por CARTÃO DE CRÉDITO");

        if(numero == null || numero.length() < 13) {
            System.out.println(" Número do cartão inválido.");
            return false;
        }
        
        if(titular == null || titular.trim().isEmpty()) {
            System.out.println(" Nome do titular é obrigatório.");
            return false;
        }
        
        System.out.println("Transação autorizada para: " + titular);
        System.out.printf("Valor: R$ %.2f\n", valor);
        return true;
    }
}