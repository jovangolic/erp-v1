package com.jovan.erp_v1.config;

import java.io.InputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;

@Configuration
public class KeyStoreConfig {

    @Value("${mpgs.keystore.path:classpath:certs/mpgs-keystore.p12}")
    private Resource keyStorePath;

    @Value("${mpgs.keystore.password:changeit}")
    private String keyStorePassword;

    @Value("${mpgs.key.alias:merchantKey}")
    private String keyAlias;

    @Value("${mpgs.key.password:changeit}")
    private String keyPassword;

    // Production keystore
    @Bean
    @Profile("prod")
    public KeyStore keyStore() throws Exception {
        KeyStore ks = KeyStore.getInstance("PKCS12");
        try (InputStream is = keyStorePath.getInputStream()) {
            ks.load(is, keyStorePassword.toCharArray());
        }
        return ks;
    }

    //Development stub keystore (nema fajl, samo prazan keystore)
    @Bean
    @Profile("dev")
    public KeyStore devKeyStore() throws Exception {
        return KeyStore.getInstance(KeyStore.getDefaultType());
    }

    //Production PrivateKey (koristi pravi keystore)
    @Bean
    @Profile("prod")
    public PrivateKey merchantPrivateKey(KeyStore keyStore) throws Exception {
        return loadPrivateKey(keyStore);
    }

    @Bean(name = "merchantPrivateKey")
    @Profile("dev")
    public PrivateKey devMerchantPrivateKey() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair pair = keyGen.generateKeyPair();
            return pair.getPrivate();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to generate dummy PrivateKey for dev profile", e);
        }
    }

    //Metoda koja iz keystore-a izdvaja privatni kljuc
    public PrivateKey loadPrivateKey(KeyStore keyStore) throws Exception {
        Key key = keyStore.getKey(keyAlias, keyPassword.toCharArray());
        if (key instanceof PrivateKey) {
            return (PrivateKey) key;
        }
        throw new IllegalStateException("Key with alias " + keyAlias + " is not a private key");
    }
}
