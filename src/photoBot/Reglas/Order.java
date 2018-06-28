package photoBot.Reglas;

import java.util.ArrayList;
import java.util.List;

public class Order {

	private Customer customer;
	
	private List<Product> products;
	
	private double totalPrice;

	public Order(Customer customer) {
		super();
		this.customer = customer;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
	public void addProduct(Product product) {
		if (products == null) {
			products = new ArrayList<Product>();
		}
		products.add(product);
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	
}
