package com.risevision.gcslogs.auth;

import java.util.logging.Logger;
import java.util.List;
import java.util.Arrays;

import java.io.File;
import java.io.IOException;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential.Builder;
import com.google.api.client.googleapis.util.Utils;

import static com.risevision.gcslogs.BuildConfig.*;

public class GoogleCredentialProvider implements CredentialProvider{
  private static final Logger log =
  Logger.getLogger("gcslogs.GoogleCredentialProvider");
  private static GoogleCredential credential;
  private static File p12File = new File(RVMEDIA_P12_PATH.toString());
  private static GoogleCredential.Builder credentialBuilder =
  new GoogleCredential.Builder();

  static {
    credentialBuilder = new GoogleCredential.Builder();

    List<String> scopes = Arrays.asList
    (EMAIL_SCOPE.toString(), BQ_SCOPE.toString(), STORAGE_SCOPE.toString());

    credentialBuilder
    .setTransport(Utils.getDefaultTransport())
    .setJsonFactory(Utils.getDefaultJsonFactory())
    .setServiceAccountId(RVMEDIA_ID.toString())
    .setServiceAccountScopes(scopes);

    try {
      credentialBuilder.setServiceAccountPrivateKeyFromP12File(p12File);
    } catch (Exception e) {
      log.severe("Could not set p12 file.");
      e.printStackTrace();
    }
  }

  public GoogleCredential getCredential() {
    try {
      credential = credentialBuilder.build();
      credential.refreshToken();
      return credential;
    } catch (IOException e) {
      log.severe("Error: " + e.getMessage() + "\n" +
      "Could not refresh token.");
      e.printStackTrace();
    }
    return credential;
  }
}
