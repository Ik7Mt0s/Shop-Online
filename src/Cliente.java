public class Cliente extends Usuario {
    private String cep;
    private ConsultaCEP consultor;

    public Cliente(String nomeUsuario, String cep) {
        super(nomeUsuario);
        this.consultor = new ConsultaCEP();
        this.cep = cep;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }
    
    // Método que verifica e retorna o resultado
    public void consultarEndereco() {
        System.out.println("Consultando CEP: " + cep);
        String resultado = consultor.verificarCEP(cep);
        
        if (resultado.contains("inválido") || resultado.contains("Erro")) {
            System.out.println("❌ CEP inválido");
        } else {
            System.out.println("✅ " + resultado);
        }
    }
    
    // Método booleano simples para verificar validade
    public boolean cepEhValido() {
        String resultado = consultor.verificarCEP(cep);
        return !resultado.contains("inválido") && !resultado.contains("Erro");
    }
    
    @Override
    public void exibirInfo() {
        super.exibirInfo();
        System.out.println("CEP: " + cep);
        System.out.println("Válido: " + (cepEhValido() ? "✅ SIM" : "❌ NÃO"));
    }
    
    // Método estático para criar e salvar automaticamente se for válido
    public static boolean criarESalvarCliente(String nome, String cep) {
        Cliente cliente = new Cliente(nome, cep);
        if (cliente.cepEhValido()) {
            return PersistenciaDados.salvarCliente(cliente);
        } else {
            System.out.println("❌ Não foi possível criar cliente - CEP inválido");
            return false;
        }
    }
}