package praktikum.data;

// JSON входных данных для метода авторизации
public class LoginData {
    private String email;
    private String password;

    public LoginData() {
    }

    public LoginData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public LoginData(RegisterData registerUserData) {
        this.email = registerUserData.getEmail();
        this.password = registerUserData.getPassword();
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
