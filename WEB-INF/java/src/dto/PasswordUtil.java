package dto;

public class PasswordUtil {

    // Hacher un mot de passe avec une méthode simple (XOR par exemple)
    public static String hashPassword(String password) {
        int hash = 0;
        for (int i = 0; i < password.length(); i++) {
            hash = hash ^ password.charAt(i);
        }
        return Integer.toHexString(hash);
    }

    // Vérifier si un mot de passe correspond à un hachage
    public static boolean checkPassword(String password, String hashedPassword) {
        return hashPassword(password).equals(hashedPassword);
    }
}
