package com.risevision.gcslogs.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

public interface CredentialProvider {
  GoogleCredential getCredential();
}
