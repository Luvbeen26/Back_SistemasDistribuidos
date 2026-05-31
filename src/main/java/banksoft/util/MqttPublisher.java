package banksoft.util;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cliente MQTT para publicar eventos del sistema bancario
 */
public class MqttPublisher {
    private static final Logger logger = LoggerFactory.getLogger(MqttPublisher.class);
    private static final String BROKER_URL = "tcp://localhost:1883";
    private static final String CLIENT_ID = "banco-backend";
    
    private MqttClient client;

    public MqttPublisher() {
        try {
            this.client = new MqttClient(BROKER_URL, CLIENT_ID);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setAutomaticReconnect(true);
            client.connect(options);
            logger.info("MQTT Client conectado a " + BROKER_URL);
        } catch (MqttException e) {
            logger.error("Error al conectar MQTT", e);
        }
    }

    public void publish(String topic, String message) {
        try {
            if (client != null && client.isConnected()) {
                client.publish(topic, message.getBytes(), 1, false);
                logger.debug("Mensaje publicado en " + topic);
            }
        } catch (MqttException e) {
            logger.error("Error al publicar en MQTT", e);
        }
    }

    public void disconnect() {
        try {
            if (client != null && client.isConnected()) {
                client.disconnect();
                client.close();
            }
        } catch (MqttException e) {
            logger.error("Error al desconectar MQTT", e);
        }
    }
}
