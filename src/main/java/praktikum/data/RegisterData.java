package praktikum.data;

// JSON выходных данных для метода регистрации
public class RegisterData {
    private String email;
    private String password;
    private String name;

    public RegisterData() {
    }

    public RegisterData(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }
}
