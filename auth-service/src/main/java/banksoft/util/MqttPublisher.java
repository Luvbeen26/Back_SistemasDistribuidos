package banksoft.util;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class MqttPublisher {
    private static final Logger logger = LoggerFactory.getLogger(MqttPublisher.class);

    // ── Configuración del broker ──────────────────────────────────────────────
    private static final String BROKER_URL = "ssl://localhost:8883";
    private static final String CLIENT_ID  = "auth-service";
    private static final String MQTT_USER  = "auth-service";
    private static final String MQTT_PASS  = "auth1234";
    // ca.crt va en src/main/resources/certs/ca.crt
    private static final String CA_CERT_CLASSPATH = "/certs/ca.crt";

    // ── Singleton ─────────────────────────────────────────────────────────────
    private static MqttPublisher instance;
    private MqttClient client;

    private MqttPublisher() {
        connect();
    }

    public static synchronized MqttPublisher getInstance() {
        if (instance == null) {
            instance = new MqttPublisher();
        }
        return instance;
    }

    // ── Conexión ──────────────────────────────────────────────────────────────
    private void connect() {
        try {
            this.client = new MqttClient(BROKER_URL, CLIENT_ID);

            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setAutomaticReconnect(true);
            options.setConnectionTimeout(10);
            options.setKeepAliveInterval(30);
            options.setUserName(MQTT_USER);
            options.setPassword(MQTT_PASS.toCharArray());
            options.setSocketFactory(buildSslContext().getSocketFactory());
            options.setHttpsHostnameVerificationEnabled(false);

            client.connect(options);
            logger.info("MQTT conectado a {} como {}", BROKER_URL, CLIENT_ID);

        } catch (MqttException e) {
            logger.error("Error al conectar MQTT: {}", e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error al configurar TLS para MQTT: {}", e.getMessage(), e);
        }
    }

    // ── SSL Context cargando ca.crt desde classpath ───────────────────────────
    private SSLContext buildSslContext() throws Exception {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate caCert;

        // Carga desde classpath (src/main/resources/certs/ca.crt)
        try (InputStream is = MqttPublisher.class.getResourceAsStream(CA_CERT_CLASSPATH)) {
            if (is == null) {
                throw new RuntimeException("No se encontró ca.crt en classpath: " + CA_CERT_CLASSPATH);
            }
            caCert = (X509Certificate) cf.generateCertificate(is);
        }

        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", caCert);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(
            TrustManagerFactory.getDefaultAlgorithm()
        );
        tmf.init(keyStore);

        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(null, tmf.getTrustManagers(), null);
        return sslContext;
    }

    // ── Publicar evento ───────────────────────────────────────────────────────
    public void publish(String topic, String payload, int qos) {
        try {
            if (client != null && client.isConnected()) {
                client.publish(topic, payload.getBytes(), qos, false);
                logger.debug("Publicado en [{}]: {}", topic, payload);
            } else {
                logger.warn("MQTT no conectado, reintentando...");
                connect();
            }
        } catch (MqttException e) {
            logger.error("Error al publicar en {}: {}", topic, e.getMessage(), e);
        }
    }

    public void publish(String topic, String payload) {
        publish(topic, payload, 1);
    }

    // ── Desconectar ───────────────────────────────────────────────────────────
    public void disconnect() {
        try {
            if (client != null && client.isConnected()) {
                client.disconnect();
                client.close();
                logger.info("MQTT desconectado");
            }
        } catch (MqttException e) {
            logger.error("Error al desconectar MQTT: {}", e.getMessage(), e);
        }
    }
}
