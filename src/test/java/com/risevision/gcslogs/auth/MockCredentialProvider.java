package com.risevision.gcslogs.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

public class MockCredentialProvider implements CredentialProvider {
  public GoogleCredential getCredential() {
    return new GoogleCredential();
  }
}
