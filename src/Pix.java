import java.util.UUID;

public class Pix extends FormaPagamento {
    @Override
    public boolean processarPagamento(double valor) {
        System.out.println("\n pagamento por PIX ");
        String chave = UUID.randomUUID().toString();
        
        System.out.println("Chave gerada: " + chave);
        System.out.println("Valor a pagar: R$ " + String.format("%.2f", valor));
        
        System.out.println(" Pagamento realizado.");
        return true;
    }
}
