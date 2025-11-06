package model;



public class Receipt {
    private Product product;
    private double amount;
    
    public Receipt(Product product, double amount){
        this.product = product;
        this.amount = amount;
    }

    public void printReceipt(){
        System.out.println("----------RECEIPT----------");
        System.out.println("Item: " + product.getName());
        System.out.println("Price: $" + product.getPrice());
        System.out.println("Total Paid: $" + amount);
        System.out.println("---------------------------\n");


    }
}
