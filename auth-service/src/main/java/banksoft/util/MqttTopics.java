// MqttTopics.java
package banksoft.util;

public final class MqttTopics {
    private MqttTopics() {}

    // Auth Service publica
    public static final String AUTH_LOGIN_EXITOSO    = "bank/auth/login-success";
    public static final String AUTH_LOGIN_SOSPECHOSO = "bank/auth/suspicious-login";

    // Accounts Service publica
    public static final String CUENTA_BLOQUEADA      = "bank/accounts/blocked";
    public static final String CUENTA_DESBLOQUEADA   = "bank/accounts/unblocked";
    public static final String CUENTA_LIMITE_CAMBIO  = "bank/accounts/limit-changed";

    // Transfer Service publica
    public static final String TRANSFERENCIA_COMPLETADA = "bank/transfers/completed";
}