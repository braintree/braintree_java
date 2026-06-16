package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.braintreegateway.ExternalVault;

public class ExternalVaultTest {

    @Test
    public void statusEnumToString() {
        assertAll("external vault status values",
                () -> assertEquals("will_vault", ExternalVault.Status.WILL_VAULT.toString()),
                () -> assertEquals("vaulted", ExternalVault.Status.VAULTED.toString()));
    }
}
