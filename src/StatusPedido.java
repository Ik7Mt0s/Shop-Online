public enum StatusPedido {
    Pendente("Pendente"),
    Confirmado("Confirmado"),
    Processando("Processando"),
    Enviado("Enviado"),
    Entregue("Entregue"),
    Cancelado("Cancelado");
    private final String descricao;
    StatusPedido(String descricao) {
        this.descricao = descricao;
    }
    public String getDescricao() {
        return descricao;
    }
}