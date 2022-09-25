package praktikum.data;

// // JSON выходных данных для метода обновления данных
public class UserData {
    private String email;
    private String name;

    public UserData() {
    }

    public UserData(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
