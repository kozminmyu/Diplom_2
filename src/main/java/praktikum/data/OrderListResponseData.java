package praktikum.data;

import java.util.List;

// JSON выходных данных для метода получения списка заказов
public class OrderListResponseData {
    private boolean success;
    private List<OrdersResponseData> orders;
    private int total;
    private int totalToday;

    public boolean isSuccess() {
        return success;
    }

    public List<OrdersResponseData> getOrders() {
        return orders;
    }

    public int getTotal() {
        return total;
    }

    public int getTotalToday() {
        return totalToday;
    }
}
