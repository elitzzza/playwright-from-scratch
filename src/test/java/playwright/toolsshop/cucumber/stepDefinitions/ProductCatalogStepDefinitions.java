package playwright.toolsshop.cucumber.stepDefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.DataTableType;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.Assertions;
import playwright.toolsshop.catalog.pageobject.NavBar;
import playwright.toolsshop.catalog.pageobject.ProductDetails;
import playwright.toolsshop.catalog.pageobject.ProductList;
import playwright.toolsshop.catalog.pageobject.SearchComponent;
import playwright.toolsshop.domain.ProductSummary;

import java.util.List;
import java.util.Map;

public class ProductCatalogStepDefinitions {

    NavBar navBar;
    SearchComponent searchComponent;
    ProductList productList;
    ProductDetails productDetails;

    @Before
    public void setupPageObject() {
        navBar = new NavBar(PlaywrightCucumberRunConfiguration.getPage());
        searchComponent = new SearchComponent(PlaywrightCucumberRunConfiguration.getPage());
        productList = new ProductList(PlaywrightCucumberRunConfiguration.getPage());
        productDetails = new ProductDetails(PlaywrightCucumberRunConfiguration.getPage());
    }

    @Given("Sally is on the home page")
    public void sally_is_on_the_home_page() {
        navBar.openHomePage();

    }

    @When("she searches for {string}")
    public void sally_searches_for_adjust_able_wrench(String searchTerm) {
        searchComponent.searchBy(searchTerm);

    }

    @Then("the {string} should be displayed")
    public void the_adjustable_wrench_should_be_displayed(String productName) {
        var matchingProducts = productList.getProductNames();
        Assertions.assertThat(matchingProducts).contains(productName);
    }

    @Then("following items should be displayed")
    public void followingItemsShouldBeDisplayed(List<String> productNames) {
        var matchingProducts = productList.getProductNames();
        Assertions.assertThat(matchingProducts).containsAll(productNames);
    }

    @DataTableType
    public ProductSummary mapToProductSummary(Map<String, String> productData) {
        return new ProductSummary(productData.get("Product"), productData.get("Price"));
    }

    @Then("following list of items should be displayed")
    public void followingListOfItemsShouldBeDisplayed(List<ProductSummary> expectedProductsSummaries) {

        List<ProductSummary> matchingProducts = productList.getProductSummaries();
        Assertions.assertThat(matchingProducts).containsExactlyInAnyOrderElementsOf(expectedProductsSummaries);
    }

    @Then("no products should be found")
    public void noProductsShouldBeFound() {
        List<ProductSummary> matchingProducts = productList.getProductSummaries();
        Assertions.assertThat(matchingProducts).isEmpty();
    }

    @And("the message {string} should be displayed")
    public void theMessageShouldBeDisplayed(String message) {
        Assertions.assertThat(productList.getSearchCompleteMessage()).isEqualTo(message);

    }

    @And("she filters by {string}")
    public void sheFiltersBy(String category) {
        searchComponent.filterByCategory(category);

    }

    @When("she sorts by {string}")
    public void sheSortsBySortingCriterion(String sortingCriterion) {
        searchComponent.sortBy(sortingCriterion);
        ;
    }

    @Then("the first displayed product should be {string}")
    public void theFirstDisplayedProductShouldBeFirstProduct(String firstProduct) {

        List<String> matchingProducts = productList.getProductNames();
        Assertions.assertThat(matchingProducts).startsWith(firstProduct);

    }

//    @Then("following list of items should be displayed")
//    public void followingListOfItemsShouldBeDisplayed(DataTable dataTable) {
//
//        var matchingProducts = productList.getProductSummaries();
//        List<Map<String, String>> productMap = dataTable.asMaps();
//
//        List<ProductSummary> expectedProductsSummaries = productMap.stream()
//                .map(productData -> new ProductSummary(
//                                productData.get("Product"),
//                                productData.get("Price")
//                        )
//
//                ).toList();
//
//        Assertions.assertThat(matchingProducts).containsExactlyInAnyOrderElementsOf(expectedProductsSummaries);
//    }
}
