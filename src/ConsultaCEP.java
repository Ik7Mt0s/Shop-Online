import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ConsultaCEP {
    private HttpClient httpClient;
    
    public ConsultaCEP() {
        this.httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    }
    
    public String verificarCEP(String cep) {
        try {
            String urlString = "https://viacep.com.br/ws/" + cep + "/json/";
            
            System.out.println("DEBUG: Consultando URL: " + urlString);
            
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlString))
                .GET()
                .timeout(Duration.ofSeconds(10))
                .build();
            
            HttpResponse<String> response = httpClient.send(
                request, 
                HttpResponse.BodyHandlers.ofString()
            );
            
            System.out.println("DEBUG: Status Code: " + response.statusCode());
            System.out.println("DEBUG: Resposta Bruta: " + response.body());
            
            if (response.statusCode() == 200) {
                String json = response.body();
                
                // Verifica de formas diferentes
                boolean temErro = json.contains("\"erro\": true") || 
                                 json.contains("\"erro\":true") ||
                                 json.contains("\"erro\" : true");
                
                System.out.println("DEBUG: Tem erro? " + temErro);
                
                if (temErro) {
                    return "CEP inválido";
                }
                
                String endereco = formatarEndereco(json);
                System.out.println("DEBUG: Endereço formatado: " + endereco);
                return endereco;
            } else {
                return "Erro na consulta - Status: " + response.statusCode();
            }
            
        } catch (Exception e) {
            System.out.println("DEBUG: Exception: " + e.getMessage());
            return "Erro: " + e.getMessage();
        }
    }
    
    public boolean cepEhValido(String cep) {
        String resultado = verificarCEP(cep);
        return !resultado.contains("inválido") && !resultado.contains("Erro");
    }
    
    private String formatarEndereco(String json) {
        // Tenta diferentes abordagens para extrair os campos
        String logradouro = extrairCampoFlexivel(json, "logradouro");
        String bairro = extrairCampoFlexivel(json, "bairro");
        String localidade = extrairCampoFlexivel(json, "localidade");
        String uf = extrairCampoFlexivel(json, "uf");
        
        System.out.println("DEBUG: Campos extraídos - " +
                         "logradouro: '" + logradouro + "', " +
                         "bairro: '" + bairro + "', " +
                         "cidade: '" + localidade + "', " +
                         "uf: '" + uf + "'");
        
        // Se todos os campos principais estão vazios, considera inválido
        if (logradouro.isEmpty() && bairro.isEmpty() && localidade.isEmpty()) {
            return "CEP inválido";
        }
        
        return String.format("%s, %s - %s/%s", 
            logradouro.isEmpty() ? "S/N" : logradouro,
            bairro.isEmpty() ? "Centro" : bairro,
            localidade.isEmpty() ? "N/A" : localidade,
            uf.isEmpty() ? "N/A" : uf);
    }
    
    private String extrairCampoFlexivel(String json, String campo) {
        // Tenta diferentes padrões de busca
        String[] padroes = {
            "\"" + campo + "\":\"",
            "\"" + campo + "\" : \"",
            "\"" + campo + "\": \""
        };
        
        for (String padrao : padroes) {
            try {
                int startIndex = json.indexOf(padrao);
                if (startIndex != -1) {
                    startIndex += padrao.length();
                    int endIndex = json.indexOf("\"", startIndex);
                    if (endIndex != -1) {
                        String valor = json.substring(startIndex, endIndex);
                        if (!valor.isEmpty() && !valor.equals(" ")) {
                            return valor;
                        }
                    }
                }
            } catch (Exception e) {
                // Continua para o próximo padrão
            }
        }
        return "";
    }
}