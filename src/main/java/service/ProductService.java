package service;

import util.logManager;


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
        double oldPrice = product.getPrice();
        product.setPrice(newPrice);

        
    }
    
}
