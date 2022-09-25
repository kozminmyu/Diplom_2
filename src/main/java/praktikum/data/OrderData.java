package praktikum.data;

import java.util.List;

// JSON входных данных для метода создания заказа
public class OrderData {
    private List<String> ingredients;

    public OrderData() {
    }

    public OrderData(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void addIngredient(String ingredient) {
        ingredients.add(ingredient);
    }

    public void removeIngredient(int index) {
        ingredients.remove(index);
    }
}
