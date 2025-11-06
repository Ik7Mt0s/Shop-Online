public class Usuario {
    private String nomeUsuario;

    public Usuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public void exibirInfo(){
        System.out.println("Nome: " + getNomeUsuario());
    }
}