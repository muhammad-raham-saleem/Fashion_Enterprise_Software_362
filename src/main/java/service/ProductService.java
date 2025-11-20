package service;

import util.LogManager;

import java.util.List;

import model.Product;
import model.ProductRepository;


public class ProductService {
    private final ProductRepository productRepo;
    private final LogManager logManager;

    public ProductService(ProductRepository productRepo, LogManager logManager){
        this.productRepo = productRepo;
        this.logManager = logManager;
    }

    public Product findProductByID(int id){
        return productRepo.findById(id);
    }

    public void updatePrice(Product product, double newPrice){
        List<Product> products = productRepo.getAll();
        for (Product p : products) {
            if (p.getId() == product.getId()) {
                double oldPrice = p.getPrice();
                p.setPrice(newPrice);

                productRepo.saveAll(products);
                logManager.logPriceChange(p.getId(), oldPrice, newPrice);
                return;
            }
        }
    }
    
}
