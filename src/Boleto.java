import java.time.LocalDate;
import java.util.Random;

public class Boleto extends FormaPagamento {
    @Override
    public boolean processarPagamento(double valor) {
        System.out.println("\n pagamento por BOLETO");
        
        Random random = new Random();
        String codBarras = "34191." + random.nextInt(99999) + " " + random.nextInt(99999);
        
        System.out.println("Código de Barras: " + codBarras);
        System.out.println("Vencimento: " + LocalDate.now().plusDays(3));
        System.out.println("Valor: R$ " + String.format("%.2f", valor));
        
        System.out.println(" Boleto foi criado. Aguardando pagamento.");
        return true; 
    }
}
