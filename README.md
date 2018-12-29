# Spring Boot Mutual Authentication Sample
A simple example of x509 mutual authentication with Spring Boot.

**Step 1: Create your server keystore**
`keytool -genkey -alias <domain> -keyalg RSA -keysize 2048 -keystore keystore.jks`

**Step 2: Generate a CSR**
`keytool -certreq -alias <domain> -file your_domain.csr -keystore keystore.jks`

**Step 3: Getting a signed certificate**
1.	**Through a Certificate Authority**
	Send the CSR file to a CA of your choice and get your certificate.
    
2. **Using EJBCA - RA Web**
	2.1. Click on "Make New Request"
	2.3. Choose "Server" in the Certificate Type/Subtype option
	3.4. Select key-pair generation "Provided by user"
	3.5. Upload your CSR file created on step 2
	3.6. Download your certificate

**Step 4: Import into server keystore**
`keytool -import -trustcacerts -alias <domain> -file <certificate> -keystore keystore.jks`

**Step 5: Crete and import root certificates into truststore**
`keytool -import -file <root_certificate_filename> -alias <alias> -keystore truststore.jks`

**Step 6: Configure Wildfly/JBoss EAP 7**
1. Paste this code into `<security-realms>` section:
```
<security-realm name="SSLRealm">
	<server-identities>
		<ssl>
			<keystore path="${jboss.server.config.dir}/keystore/keystore.jks" keystore-password="your_password"/>
		</ssl>
	</server-identities>
	<authentication>
		<truststore path="${jboss.server.config.dir}/keystore/truststore.jks" keystore-password="your_password"/>
	</authentication>
</security-realm>
````
2. Into server section, configure your **https-listener** like this:
```
<https-listener name="https" socket-binding="https" security-realm="SSLRealm" verify-client="REQUIRED"/>
```

### NOTES
- This project generates a WAR package that can be deployed into a Wildfly 9.x or JBoss EAP 7. With small changes, can be reverted to the default spring boot behavior.
- If you receive this error on chrome: Error: "Subject Alternative Name Missing" or **NET::ERR_CERT_COMMON_NAME_INVALID** or "Your connection is not private": See this link https://goo.gl/ZL5nKw
