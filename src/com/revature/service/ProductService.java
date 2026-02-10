package com.revature.service;

import java.util.List;
import com.revature.dao.ProductDAO;
import com.revature.dao.impl.ProductDAOImpl;
import com.revature.dao.impl.SellerDAOImpl;
import com.revature.model.Product;
import com.revature.model.Seller;

public class ProductService {

    private ProductDAO productDAO = new ProductDAOImpl();
    private SellerDAOImpl sellerDAO = new SellerDAOImpl();

    public void addProduct(Product p) {
        productDAO.addProduct(p);
    }

    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }

    public Seller getSellerIdByUserId(int userId) {
        return sellerDAO.getSellerIdByUserId(userId);
    }

    public Product getProductById(int id){
        return productDAO.getProductById(id);
    }
    
    public List<Product> getLowStockProducts(int sellerId){
        return productDAO.getLowStockProducts(sellerId);
    }
    
    public List<Product> search(String keyword){
        return productDAO.searchProducts(keyword);
        
        
    }
    
    public List<Product> getSellerProducts(int sellerId){
        return productDAO.getProductsBySeller(sellerId);
    }

    public boolean deleteProduct(int productId){
        return productDAO.deleteProduct(productId);
    }

    public boolean updateStock(int productId, int stock){
        return productDAO.updateProductStock(productId, stock);
    }
    
    public boolean adjustStock(int productId, int change){
        return productDAO.adjustStock(productId, change);
    }

    public boolean updatePrice(int productId, double mrp, double discount){
        return productDAO.updatePrice(productId, mrp, discount);
    }


    public boolean setStock(int productId, int newStock){
        return productDAO.setStock(productId, newStock);
    }
    
    
 



}
