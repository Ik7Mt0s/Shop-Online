import java.util.Scanner;

public class CartaoDeCredito extends FormaPagamento {
    private String numero;
    private String titular;

    public void lerDadosCartao() {
        Scanner leitor = new Scanner(System.in);
        System.out.print("Número do Cartão (somente números): ");
        this.numero = leitor.nextLine();
        System.out.print("Nome do Titular: ");
        this.titular = leitor.nextLine();
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
