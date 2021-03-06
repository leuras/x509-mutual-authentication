# Spring Boot Mutual Authentication Sample
This is a simple example of X.509 TLS mutual authentication with Spring Boot.

## How to generate certificates

**Step 1: Create your server keystore**
```bash
keytool -genkey -alias <domain> -keyalg RSA -keysize 2048 -keystore keystore.jks
```

**Step 2: Generate a CSR**
```bash
keytool -certreq -alias <domain> -file your_domain.csr -keystore keystore.jks
```

**Step 3: Getting a signed certificate**
* Through a Certificate Authority
   * Send the CSR file to a CA of your choice and get your certificate.
   
* Using EJBCA - RA Web
   * Click on "Make New Request"
   * Choose "Server" in the Certificate Type/Subtype option
   * Select key-pair generation "Provided by user"
   * Upload your CSR file created on step 2
   * Download your signed certificate

**Step 4: Import into server keystore**
```bash
keytool -import -trustcacerts -alias <domain> -file <certificate> -keystore keystore.jks
```

**Step 5: Create and import root certificates into truststore**\
In this step, you will need two things: a client certificate (used to log in) and the root ca certificate that issued your client certificate. The last one, you will import into the server trust store.
```bash
keytool -import -file <root_certificate_filename> -alias <alias> -keystore truststore.jks
```

## Server Setup

### Configure Wildfly 9.x / JBoss EAP 7

**Step 1: Paste this code into "*security-realms*" section:**
```xml
<security-realm name="SSLRealm">
    <server-identities>
        <ssl>
            <keystore path="${jboss.server.config.dir}/keystore/keystore.jks" keystore-password="your_password"/>
        </ssl>
    </server-identities>
    <authentication>
        <truststore path="${jboss.server.config.dir}/keystore/truststore.jks" keystore password="your_password"/>
    </authentication>
</security-realm>
```

**Step 2: Into server section, configure your "*https-listener*" as described below:**
```xml
<https-listener name="https" socket-binding="https" security-realm="SSLRealm" verify-client="REQUIRED"/>
```

## NOTES

- This project generates a WAR package that can be deployed in a Wildfly 9.x or JBoss EAP 7. With minor changes, it can be reverted to the default spring boot behavior.
- If you are getting this error in chrome: *Error: "Subject Alternative Name Missing" or **NET::ERR_CERT_COMMON_NAME_INVALID** or "Your connection is not private"*, see this: https://goo.gl/ZL5nKw
