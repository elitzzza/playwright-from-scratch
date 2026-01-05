package playwright.toolsshop.domain;

public record CartLineItems(String title,
                            int quantity,
                            double price,
                            double totalPrice) {
}
