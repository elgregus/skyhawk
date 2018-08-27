package blinkDemo;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class BLink_Network_example {
  
  private final ManagedChannel channel;
  private final BLink_Network NetworkService;

  private static final Logger logger = Logger.getLogger(BLink_SkyHawkPowerManager_example.class.getName());
  
  /** Construct client connecting to BLink server at {@code host:port}. */
  public BLink_Network_example(String host, int port) {
    this(ManagedChannelBuilder.forAddress(host, port)
        // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
        // needing certificates.
        .usePlaintext(true).build());
  }

  /** Construct client for accessing GPIO service using the existing channel. */
  public BLink_Network_example(ManagedChannel channel) {
    this.channel = channel;
    this.NetworkService = new BLink_Network(channel);
  }
  

  public void shutdown() throws InterruptedException {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }
  
  public static JsonArray CreateSettingVariant(String variantType, JsonElement value) {
      JsonArray variant =  new JsonArray();
      variant.add(variantType);
      variant.add(value);
      return variant;
  }

  public static void main(String[] args) throws Exception {
    final BLink_Network_example blink = new BLink_Network_example("localhost", 50051);
    try {
    
      int state = blink.NetworkService.GetState();
      
      logger.log(Level.INFO, "Network State: " + state);
      
      Gson gson = new Gson();
      JsonParser parser = new JsonParser();
      
      // Add a Wi-Fi connection:
      // Connection settings:
      // https://developer.gnome.org/NetworkManager/stable/nm-settings.html
      JsonObject wifi_packedSettings            = new JsonObject();
      JsonObject wifi_connectionSettings        = new JsonObject();
      JsonObject wifi_wirelessSettings          = new JsonObject();
      JsonObject wifi_wirelessSecuritySettings  = new JsonObject();
      JsonObject wifi_ipv4Settings              = new JsonObject();
      JsonObject wifi_ipv6Settings              = new JsonObject();

      wifi_connectionSettings.add("type", CreateSettingVariant("s", parser.parse("802-11-wireless")));
      wifi_connectionSettings.add("id",   CreateSettingVariant("s", parser.parse("wifiTest")));

      String ssid_str = "my_ssid"; 
      String psk = "password123";   
      byte[] ssid_array = ssid_str.getBytes();
    
      wifi_wirelessSettings.add("ssid", CreateSettingVariant("ay", parser.parse(gson.toJson(ssid_array))));
      wifi_wirelessSettings.add("mode", CreateSettingVariant("s", parser.parse("infrastructure")));

      wifi_wirelessSecuritySettings.add("key-mgmt", CreateSettingVariant("s", parser.parse("wpa-psk")));
      wifi_wirelessSecuritySettings.add("auth-alg", CreateSettingVariant("s", parser.parse("open")));
      wifi_wirelessSecuritySettings.add("psk",      CreateSettingVariant("s", parser.parse(psk)));

      wifi_ipv4Settings.add("method", CreateSettingVariant("s", parser.parse("auto")));
      wifi_ipv6Settings.add("method", CreateSettingVariant("s", parser.parse("ignore")));

      wifi_packedSettings.add("connection", wifi_connectionSettings);
      wifi_packedSettings.add("802-11-wireless", wifi_wirelessSettings);
      wifi_packedSettings.add("802-11-wireless-security", wifi_wirelessSecuritySettings);
      wifi_packedSettings.add("ipv4", wifi_ipv4Settings);
      wifi_packedSettings.add("ipv6", wifi_ipv6Settings);

      blink.NetworkService.AddConnection(wifi_packedSettings, true);
      
      // Add a static Ethernet Connection on interface "eth1":
      // Connection settings:
      // https://developer.gnome.org/NetworkManager/stable/nm-settings.html
      JsonObject eth_packedSettings            = new JsonObject();
      JsonObject eth_connectionSettings        = new JsonObject();
      JsonObject eth_addrSettings              = new JsonObject();
      JsonObject eth_ipv4Settings              = new JsonObject();
      JsonArray  eth_addrData                  = new JsonArray();


      eth_connectionSettings.add("type",           CreateSettingVariant("s", parser.parse("802-3-ethernet")));
      eth_connectionSettings.add("interface-name", CreateSettingVariant("s", parser.parse("eth1")));
      eth_connectionSettings.add("id",             CreateSettingVariant("s", parser.parse("ethernetTest")));

      eth_addrSettings.add("address", CreateSettingVariant("s", parser.parse("192.168.10.222")));
      eth_addrSettings.add("prefix", CreateSettingVariant("u", parser.parse("[24]")));
      eth_addrData.add(eth_addrSettings);
      
      eth_ipv4Settings.add("method",       CreateSettingVariant("s", parser.parse("manual")));
      eth_ipv4Settings.add("gateway",      CreateSettingVariant("s", parser.parse("192.168.10.1")));
      eth_ipv4Settings.add("address-data", CreateSettingVariant("aa{sv}", eth_addrData));

      eth_packedSettings.add("connection", eth_connectionSettings);
      eth_packedSettings.add("ipv4", eth_ipv4Settings);

      blink.NetworkService.AddConnection(eth_packedSettings, true);
      
      java.util.List<String> networkConnections = blink.NetworkService.GetConnections();
      for (String item : networkConnections) {
          logger.info("ID: " + item);
      }
      
      logger.info("Removed connection: " + blink.NetworkService.RemoveConnection("eth1_static_ip"));
      
    } finally {
      blink.shutdown();
    }
  }
}
