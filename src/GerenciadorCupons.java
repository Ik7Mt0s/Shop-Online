import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class GerenciadorCupons {
    private Map<String, Double> cuponsValidos;
    private String arquivo = "cupons.txt";

    public GerenciadorCupons() {
        cuponsValidos = new HashMap<>();
        carregarCupons();
    }

    // Persistência: Lê do arquivo cupons.txt
    private void carregarCupons() {
        try {
            File file = new File(arquivo);
            if (!file.exists()) {
                criarArquivoPadrao(); // Cria arquivo se não existir para teste
                return;
            }

            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                if (!linha.trim().isEmpty()) {
                    String[] dados = linha.split(";");
                    // Formato: CODIGO;PORCENTAGEM (ex: DESC10;10)
                    if (dados.length >= 2) {
                        cuponsValidos.put(dados[0].toUpperCase(), Double.parseDouble(dados[1]));
                    }
                }
            }
            scanner.close();
        } catch (Exception e) {
            System.out.println("Erro ao carregar cupons: " + e.getMessage());
        }
    }

    // Método auxiliar para criar arquivo de teste se não existir
    private void criarArquivoPadrao() {
        try (PrintWriter writer = new PrintWriter(arquivo)) {
            writer.println("PROMO10;10");
            writer.println("NATAL20;20");
            writer.println("BLACKFRIDAY;50");
            // Recarrega o mapa após criar
            cuponsValidos.put("PROMO10", 10.0);
            cuponsValidos.put("NATAL20", 20.0);
            cuponsValidos.put("BLACKFRIDAY", 50.0);
        } catch (Exception e) {
            System.out.println("Erro ao criar arquivo padrão: " + e.getMessage());
        }
    }

    public DescontoStrategy buscarCupom(String codigo) {
        String codigoNormalizado = codigo.toUpperCase();
        if (cuponsValidos.containsKey(codigoNormalizado)) {
            double porcentagem = cuponsValidos.get(codigoNormalizado);
            return new DescontoCupom(porcentagem);
        }
        return null; // Cupom inválido
    }
}
