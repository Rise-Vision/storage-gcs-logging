package com.risevision.gcslogs;

import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static com.risevision.gcslogs.BuildConfig.*;

public class BuildConfigTest {
  @Test public void itHasAConfig() {
    assertThat("it has email scope",
    EMAIL_SCOPE.toString(),
    is("https://www.googleapis.com/auth/userinfo.email"));
  }

  @Test public void itHandlesToString() {
    assertThat("it handles toString",
    "Test " + STORAGE_SCOPE,
    is("Test https://www.googleapis.com/auth/devstorage.full_control"));
  }
}
