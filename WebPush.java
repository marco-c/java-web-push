import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class WebPush {
  private static final String GCM_API_KEY = "YOUR_GCM_API_KEY";
  private static final String GCM_REQUEST_URL = "https://android.googleapis.com/gcm/send";

  static public void sendNotification(String endpointURL) throws Exception {
    HttpURLConnection conn;
    String requestBody;
    int expectedStatusCode = 201;

    if (endpointURL.startsWith(GCM_REQUEST_URL)) {
      URL url = new URL(GCM_REQUEST_URL);
      conn = (HttpURLConnection)url.openConnection();
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setRequestProperty("Authorization", "key=" + GCM_API_KEY);
      // TODO: Set requestBody with a proper JSON library.
      requestBody = "{\"registration_ids\":[\"" + (endpointURL.substring(endpointURL.lastIndexOf('/') + 1)) + "\"]}";
      expectedStatusCode = 200;
    } else {
      URL url = new URL(endpointURL);
      conn = (HttpURLConnection)url.openConnection();
      requestBody = "";
    }

    byte[] bytes = requestBody.getBytes("UTF-8");

    conn.setDoOutput(true);
    conn.setUseCaches(false);
    conn.setFixedLengthStreamingMode(bytes.length);
    conn.setRequestMethod("POST");

    OutputStream out = conn.getOutputStream();

    try {
      out.write(bytes);
    } finally {
      out.close();
    }

    int statusCode = conn.getResponseCode();

    if (statusCode != expectedStatusCode) {
      // TODO: Throw custom exception with status code and response body.    
      throw new Exception();
    }

    String line;
    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    while ((line = reader.readLine()) != null) {
      System.out.println(line);
    }
    reader.close();
  }

  public static void main(String[] args) {
    try {
      sendNotification(args[0]);
    } catch (Exception e) {
      System.out.println(e);
    }
  }
}


