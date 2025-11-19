import java.util.ArrayList;
import java.util.List;

public abstract class Pedido {
protected int id;
protected String nomeCliente;
protected StatusPedido status;
protected List<ItemPedido> itens;
protected DescontoStrategy descontoStrategy;

public Pedido(int id, String nomeCliente, String status) {
    this. id = id ;
    this.nomeCliente = nomeCliente;
    this.status = StatusPedido.Pendente;
    this.itens = new ArrayList<>();
    this.descontoStrategy = null;
}
public int getId() {
    return id;
}
public String getNomeCliente() {
    return nomeCliente;
}
public StatusPedido getStatus() {
    return status;
}
public List<ItemPedido> getItens() {
    return itens;
}
public void setNomeCliente(String nomeCliente) {
    this.nomeCliente = nomeCliente;
}
public void setStatus(StatusPedido novoStatus) {
    if(novoStatus != null && isValidTransicaoStatus(this.status, novoStatus)){
        this.status = novoStatus;
    }else {
        throw  new IllegalArgumentException("Transição de Status inválida");
    }
}

public void setDescontoStrategy(DescontoStrategy descontoStrategy) {
        this.descontoStrategy = descontoStrategy;
    }  
public void addItem(int quantidade, Produto produto) {
    itens.add(new ItemPedido(quantidade, produto));
}
public void removeItem(int quantidade, Produto produto) {
    itens.remove(new ItemPedido(quantidade, produto));
}

 protected double aplicarDescontoNoSubtotal(double subtotal) {
        if (descontoStrategy != null) {
            return descontoStrategy.aplicarDesconto(subtotal);
        }
        return subtotal;
    }
public double calcularSubtotal() {
    double total = 0;
    for (ItemPedido item : itens) {
        total += item.getQuantidade()*item.getProduto().getPreco();
    }
    return total;
}
private boolean isValidTransicaoStatus(StatusPedido atual, StatusPedido novo) {
    return true;
}

@Override
public String toString() {
    return "Pedido:\n" +
            "Id: " + id + "\n" +
            ", Nome: " + nomeCliente + "\n" +
            ", Status: " + status + "\n" +
            "itens: " + itens;
        }
public abstract double calcularTotal();
}



