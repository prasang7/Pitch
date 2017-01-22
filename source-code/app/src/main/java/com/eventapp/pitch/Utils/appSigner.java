package com.eventapp.pitch.Utils;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.DigestOutputStream;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.TreeMap;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.EncryptedPrivateKeyInfo;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import kellinwood.logging.LoggerInterface;
import kellinwood.logging.LoggerManager;
import kellinwood.security.zipsigner.AutoKeyException;
import kellinwood.security.zipsigner.Base64;
import kellinwood.security.zipsigner.DefaultResourceAdapter;
import kellinwood.security.zipsigner.HexDumpEncoder;
import kellinwood.security.zipsigner.KeySet;
import kellinwood.security.zipsigner.ProgressHelper;
import kellinwood.security.zipsigner.ProgressListener;
import kellinwood.security.zipsigner.ResourceAdapter;
import kellinwood.security.zipsigner.ZipSignature;
import kellinwood.zipio.ZioEntry;
import kellinwood.zipio.ZipInput;
import kellinwood.zipio.ZipOutput;

/**
 * Created by Sid on 1/11/17.
 */

public class appSigner {
    private boolean canceled = false;
    private ProgressHelper progressHelper = new ProgressHelper();
    private ResourceAdapter resourceAdapter = new DefaultResourceAdapter();
    static LoggerInterface log = null;
    private static final String CERT_SF_NAME = "META-INF/CERT.SF";
    private static final String CERT_RSA_NAME = "META-INF/CERT.RSA";
    private static Pattern stripPattern = Pattern.compile("^META-INF/(.*)[.](SF|RSA|DSA)$");
    Map<String, KeySet> loadedKeys = new HashMap();
    KeySet keySet = null;
    public static final String MODE_AUTO_TESTKEY = "auto-testkey";
    public static final String MODE_AUTO_NONE = "auto-none";
    public static final String MODE_AUTO = "auto";
    public static final String KEY_NONE = "none";
    public static final String KEY_TESTKEY = "testkey";
    public static final String[] SUPPORTED_KEY_MODES = new String[]{"auto-testkey", "auto", "auto-none", "media", "platform", "shared", "testkey", "none"};
    String keymode = "testkey";
    Map<String, String> autoKeyDetect = new HashMap();
    kellinwood.security.zipsigner.ZipSigner.AutoKeyObservable autoKeyObservable = new kellinwood.security.zipsigner.ZipSigner.AutoKeyObservable();

    public static LoggerInterface getLogger() {
        if (log == null) {
            log = LoggerManager.getLogger(kellinwood.security.zipsigner.ZipSigner.class.getName());
        }

        return log;
    }

    public appSigner() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        this.autoKeyDetect.put("aa9852bc5a53272ac8031d49b65e4b0e", "media");
        this.autoKeyDetect.put("e60418c4b638f20d0721e115674ca11f", "platform");
        this.autoKeyDetect.put("3e24e49741b60c215c010dc6048fca7d", "shared");
        this.autoKeyDetect.put("dab2cead827ef5313f28e22b6fa8479f", "testkey");
    }

    public ResourceAdapter getResourceAdapter() {
        return this.resourceAdapter;
    }

    public void setResourceAdapter(ResourceAdapter resourceAdapter) {
        this.resourceAdapter = resourceAdapter;
    }

    public void addAutoKeyObserver(Observer o) {
        this.autoKeyObservable.addObserver(o);
    }

    public String getKeymode() {
        return this.keymode;
    }

    public void setKeymode(String km) throws IOException, GeneralSecurityException {
        if (getLogger().isDebugEnabled()) {
            getLogger().debug("setKeymode: " + km);
        }

        this.keymode = km;
        if (this.keymode.startsWith("auto")) {
            this.keySet = null;
        } else {
            this.progressHelper.initProgress();
            this.loadKeys(this.keymode);
        }

    }

    public static String[] getSupportedKeyModes() {
        return SUPPORTED_KEY_MODES;
    }

    protected String autoDetectKey(String mode, Map<String, ZioEntry> zioEntries) throws NoSuchAlgorithmException, IOException {
        boolean debug = getLogger().isDebugEnabled();
        if (!mode.startsWith("auto")) {
            return mode;
        } else {
            String keyName = null;
            Iterator i$ = zioEntries.entrySet().iterator();

            while (i$.hasNext()) {
                Map.Entry entry = (Map.Entry) i$.next();
                String entryName = (String) entry.getKey();
                if (entryName.startsWith("META-INF/") && entryName.endsWith(".RSA")) {
                    MessageDigest md5 = MessageDigest.getInstance("MD5");
                    byte[] entryData = ((ZioEntry) entry.getValue()).getData();
                    if (entryData.length < 1458) {
                        break;
                    }

                    md5.update(entryData, 0, 1458);
                    byte[] rawDigest = md5.digest();
                    StringBuilder builder = new StringBuilder();
                    byte[] md5String = rawDigest;
                    int len$ = rawDigest.length;

                    for (int i$1 = 0; i$1 < len$; ++i$1) {
                        byte b = md5String[i$1];
                        builder.append(String.format("%02x", new Object[]{Byte.valueOf(b)}));
                    }

                    String var16 = builder.toString();
                    keyName = (String) this.autoKeyDetect.get(var16);
                    if (debug) {
                        if (keyName != null) {
                            getLogger().debug(String.format("Auto-determined key=%s using md5=%s", new Object[]{keyName, var16}));
                        } else {
                            getLogger().debug(String.format("Auto key determination failed for md5=%s", new Object[]{var16}));
                        }
                    }

                    if (keyName != null) {
                        return keyName;
                    }
                }
            }

            if (mode.equals("auto-testkey")) {
                if (debug) {
                    getLogger().debug("Falling back to key=" + keyName);
                }

                return "testkey";
            } else if (mode.equals("auto-none")) {
                if (debug) {
                    getLogger().debug("Unable to determine key, returning: none");
                }

                return "none";
            } else {
                return null;
            }
        }
    }

    public void issueLoadingCertAndKeysProgressEvent() {
        this.progressHelper.progress(1, this.resourceAdapter.getString(ResourceAdapter.Item.LOADING_CERTIFICATE_AND_KEY, new Object[0]));
    }

    public void loadKeys(String name) throws IOException, GeneralSecurityException {
        this.keySet = (KeySet) this.loadedKeys.get(name);
        if (this.keySet == null) {
            this.keySet = new KeySet();
            this.keySet.setName(name);
            this.loadedKeys.put(name, this.keySet);
            if (!"none".equals(name)) {
                this.issueLoadingCertAndKeysProgressEvent();
                URL privateKeyUrl = this.getClass().getResource("/keys/" + name + ".pk8");
                this.keySet.setPrivateKey(this.readPrivateKey(privateKeyUrl, (String) null));
                URL publicKeyUrl = this.getClass().getResource("/keys/" + name + ".x509.pem");
                this.keySet.setPublicKey(this.readPublicKey(publicKeyUrl));
                URL sigBlockTemplateUrl = this.getClass().getResource("/keys/" + name + ".sbt");
                if (sigBlockTemplateUrl != null) {
                    this.keySet.setSigBlockTemplate(this.readContentAsBytes(sigBlockTemplateUrl));
                }

            }
        }
    }

    public void setKeys(String name, X509Certificate publicKey, PrivateKey privateKey, byte[] signatureBlockTemplate) {
        this.keySet = new KeySet(name, publicKey, privateKey, signatureBlockTemplate);
    }

    public void setKeys(String name, X509Certificate publicKey, PrivateKey privateKey, String signatureAlgorithm, byte[] signatureBlockTemplate) {
        this.keySet = new KeySet(name, publicKey, privateKey, signatureAlgorithm, signatureBlockTemplate);
    }

    public KeySet getKeySet() {
        return this.keySet;
    }

    public void cancel() {
        this.canceled = true;
    }

    public void resetCanceled() {
        this.canceled = false;
    }

    public boolean isCanceled() {
        return this.canceled;
    }

    public void loadProvider(String providerClassName) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class providerClass = Class.forName(providerClassName);
        Provider provider = (Provider) providerClass.newInstance();
        Security.insertProviderAt(provider, 1);
    }

    public X509Certificate readPublicKey(URL publicKeyUrl) throws IOException, GeneralSecurityException {
        InputStream input = publicKeyUrl.openStream();

        X509Certificate var4;
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            var4 = (X509Certificate) cf.generateCertificate(input);
        } finally {
            input.close();
        }

        return var4;
    }

    private KeySpec decryptPrivateKey(byte[] encryptedPrivateKey, String keyPassword) throws GeneralSecurityException {
        EncryptedPrivateKeyInfo epkInfo;
        try {
            epkInfo = new EncryptedPrivateKeyInfo(encryptedPrivateKey);
        } catch (IOException var10) {
            return null;
        }

        char[] keyPasswd = keyPassword.toCharArray();
        SecretKeyFactory skFactory = SecretKeyFactory.getInstance(epkInfo.getAlgName());
        SecretKey key = skFactory.generateSecret(new PBEKeySpec(keyPasswd));
        Cipher cipher = Cipher.getInstance(epkInfo.getAlgName());
        cipher.init(2, key, epkInfo.getAlgParameters());

        try {
            return epkInfo.getKeySpec(cipher);
        } catch (InvalidKeySpecException var9) {
            getLogger().error("signapk: Password for private key may be bad.");
            throw var9;
        }
    }

    public byte[] readContentAsBytes(URL contentUrl) throws IOException {
        return this.readContentAsBytes(contentUrl.openStream());
    }

    public byte[] readContentAsBytes(InputStream input) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];

        for (int numRead = input.read(buffer); numRead != -1; numRead = input.read(buffer)) {
            baos.write(buffer, 0, numRead);
        }

        byte[] bytes = baos.toByteArray();
        return bytes;
    }

    public PrivateKey readPrivateKey(URL privateKeyUrl, String keyPassword) throws IOException, GeneralSecurityException {
        DataInputStream input = new DataInputStream(privateKeyUrl.openStream());

        PrivateKey var7;
        try {
            byte[] bytes = this.readContentAsBytes((InputStream) input);
            Object spec = this.decryptPrivateKey(bytes, keyPassword);
            if (spec == null) {
                spec = new PKCS8EncodedKeySpec(bytes);
            }

            try {
                PrivateKey ex = KeyFactory.getInstance("RSA").generatePrivate((KeySpec) spec);
                return ex;
            } catch (InvalidKeySpecException var11) {
                var7 = KeyFactory.getInstance("DSA").generatePrivate((KeySpec) spec);
            }
        } finally {
            input.close();
        }

        return var7;
    }

    private Manifest addDigestsToManifest(Map<String, ZioEntry> entries) throws IOException, GeneralSecurityException {
        Manifest input = null;
        ZioEntry manifestEntry = (ZioEntry) entries.get("META-INF/MANIFEST.MF");
        if (manifestEntry != null) {
            input = new Manifest();
            input.read(manifestEntry.getInputStream());
        }

        Manifest output = new Manifest();
        Attributes main = output.getMainAttributes();
        if (input != null) {
            main.putAll(input.getMainAttributes());
        } else {
            main.putValue("Manifest-Version", "1.0");
            main.putValue("Created-By", "1.0 (Android SignApk)");
        }

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] buffer = new byte[512];
        TreeMap byName = new TreeMap();
        byName.putAll(entries);
        boolean debug = getLogger().isDebugEnabled();
        if (debug) {
            getLogger().debug("Manifest entries:");
        }

        Iterator i$ = byName.values().iterator();

        while (i$.hasNext()) {
            ZioEntry entry = (ZioEntry) i$.next();
            if (this.canceled) {
                break;
            }

            String name = entry.getName();
            if (debug) {
                getLogger().debug(name);
            }

            if (!entry.isDirectory() && !name.equals("META-INF/MANIFEST.MF") && !name.equals("META-INF/CERT.SF") && !name.equals("META-INF/CERT.RSA") && (stripPattern == null || !stripPattern.matcher(name).matches())) {
                this.progressHelper.progress(0, this.resourceAdapter.getString(ResourceAdapter.Item.GENERATING_MANIFEST, new Object[0]));
                InputStream data = entry.getInputStream();

                int num;
                while ((num = data.read(buffer)) > 0) {
                    md.update(buffer, 0, num);
                }

                Attributes attr = null;
                if (input != null) {
                    Attributes inAttr = input.getAttributes(name);
                    if (inAttr != null) {
                        attr = new Attributes(inAttr);
                    }
                }

                if (attr == null) {
                    attr = new Attributes();
                }

                attr.putValue("SHA-256-Digest", Base64.encode(md.digest()));
                output.getEntries().put(name, attr);
            }
        }

        return output;
    }

    private void generateSignatureFile(Manifest manifest, OutputStream out) throws IOException, GeneralSecurityException {
        out.write("Signature-Version: 1.0\r\n".getBytes());
        out.write("Created-By: 1.0 (Android SignApk)\r\n".getBytes());
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        PrintStream print = new PrintStream(new DigestOutputStream(new ByteArrayOutputStream(), md), true, "UTF-8");
        manifest.write(print);
        print.flush();
        out.write(("SHA-256-Digest-Manifest: " + Base64.encode(md.digest()) + "\r\n\r\n").getBytes());
        Map entries = manifest.getEntries();
        Iterator i$ = entries.entrySet().iterator();

        while (i$.hasNext()) {
            Map.Entry entry = (Map.Entry) i$.next();
            if (this.canceled) {
                break;
            }

            this.progressHelper.progress(0, this.resourceAdapter.getString(ResourceAdapter.Item.GENERATING_SIGNATURE_FILE, new Object[0]));
            String nameEntry = "Name: " + (String) entry.getKey() + "\r\n";
            print.print(nameEntry);
            Iterator i$1 = ((Attributes) entry.getValue()).entrySet().iterator();

            while (i$1.hasNext()) {
                Map.Entry att = (Map.Entry) i$1.next();
                print.print(att.getKey() + ": " + att.getValue() + "\r\n");
            }

            print.print("\r\n");
            print.flush();
            out.write(nameEntry.getBytes());
            out.write(("SHA-256-Digest: " + Base64.encode(md.digest()) + "\r\n\r\n").getBytes());
        }

    }

    private void writeSignatureBlock(KeySet keySet, byte[] signatureFileBytes, OutputStream out) throws IOException, GeneralSecurityException {
        ZipSignature x;
        if (keySet.getSigBlockTemplate() != null) {
            x = new ZipSignature();
            x.initSign(keySet.getPrivateKey());
            x.update(signatureFileBytes);
            byte[] generatorClass = x.sign();
            out.write(keySet.getSigBlockTemplate());
            out.write(generatorClass);
            if (getLogger().isDebugEnabled()) {
                MessageDigest generatorMethod = MessageDigest.getInstance("SHA-256");
                generatorMethod.update(signatureFileBytes);
                byte[] sfDigest = generatorMethod.digest();
                getLogger().debug("Sig File  -256: \n" + HexDumpEncoder.encode(sfDigest));
                getLogger().debug("Signature: \n" + HexDumpEncoder.encode(generatorClass));
                Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                cipher.init(2, keySet.getPublicKey());
                byte[] tmpData = cipher.doFinal(generatorClass);
                getLogger().debug("Signature Decrypted: \n" + HexDumpEncoder.encode(tmpData));
            }
        } else {
            try {
                x = null;
                Class generatorClass1 = Class.forName("kellinwood.security.zipsigner.optional.SignatureBlockGenerator");
                Method generatorMethod1 = generatorClass1.getMethod("generate", new Class[]{KeySet.class, (new byte[1]).getClass()});
                byte[] x1 = (byte[]) ((byte[]) generatorMethod1.invoke((Object) null, new Object[]{keySet, signatureFileBytes}));
                out.write(x1);
            } catch (Exception var10) {
                throw new RuntimeException(var10.getMessage(), var10);
            }
        }

    }

    private void copyFiles(Manifest manifest, Map<String, ZioEntry> input, ZipOutput output, long timestamp) throws IOException {
        Map entries = manifest.getEntries();
        ArrayList names = new ArrayList(entries.keySet());
        Collections.sort(names);
        int i = 1;
        Iterator i$ = names.iterator();

        while (i$.hasNext()) {
            String name = (String) i$.next();
            if (this.canceled) {
                break;
            }

            this.progressHelper.progress(0, this.resourceAdapter.getString(ResourceAdapter.Item.COPYING_ZIP_ENTRY, new Object[]{Integer.valueOf(i), Integer.valueOf(names.size())}));
            ++i;
            ZioEntry inEntry = (ZioEntry) input.get(name);
            inEntry.setTime(timestamp);
            output.write(inEntry);
        }

    }

    private void copyFiles(Map<String, ZioEntry> input, ZipOutput output) throws IOException {
        int i = 1;
        Iterator i$ = input.values().iterator();

        while (i$.hasNext()) {
            ZioEntry inEntry = (ZioEntry) i$.next();
            if (this.canceled) {
                break;
            }

            this.progressHelper.progress(0, this.resourceAdapter.getString(ResourceAdapter.Item.COPYING_ZIP_ENTRY, new Object[]{Integer.valueOf(i), Integer.valueOf(input.size())}));
            ++i;
            output.write(inEntry);
        }

    }

    /**
     * @deprecated
     */
    public void signZip(URL keystoreURL, String keystoreType, String keystorePw, String certAlias, String certPw, String inputZipFilename, String outputZipFilename) throws ClassNotFoundException, IllegalAccessException, InstantiationException, IOException, GeneralSecurityException {
        this.signZip(keystoreURL, keystoreType, keystorePw.toCharArray(), certAlias, certPw.toCharArray(), "SHA256withRSA", inputZipFilename, outputZipFilename);
    }

    public void signZip(URL keystoreURL, String keystoreType, char[] keystorePw, String certAlias, char[] certPw, String signatureAlgorithm, String inputZipFilename, String outputZipFilename) throws ClassNotFoundException, IllegalAccessException, InstantiationException, IOException, GeneralSecurityException {
        InputStream keystoreStream = null;

        try {
            KeyStore keystore = null;
            if (keystoreType == null) {
                keystoreType = KeyStore.getDefaultType();
            }

            keystore = KeyStore.getInstance(keystoreType);
            keystoreStream = keystoreURL.openStream();
            keystore.load(keystoreStream, keystorePw);
            Certificate cert = keystore.getCertificate(certAlias);
            X509Certificate publicKey = (X509Certificate) cert;
            Key key = keystore.getKey(certAlias, certPw);
            PrivateKey privateKey = (PrivateKey) key;
            this.setKeys("custom", publicKey, privateKey, signatureAlgorithm, (byte[]) null);
            this.signZip(inputZipFilename, outputZipFilename);
        } finally {
            if (keystoreStream != null) {
                keystoreStream.close();
            }

        }

    }

    public void signZip(Map<String, ZioEntry> zioEntries, String outputZipFilename) throws IOException, GeneralSecurityException {
        this.progressHelper.initProgress();
        this.signZip(zioEntries, new FileOutputStream(outputZipFilename), outputZipFilename);
    }

    public void signZip(String inputZipFilename, String outputZipFilename) throws IOException, GeneralSecurityException {
        File inFile = (new File(inputZipFilename)).getCanonicalFile();
        File outFile = (new File(outputZipFilename)).getCanonicalFile();
        if (inFile.equals(outFile)) {
            throw new IllegalArgumentException(this.resourceAdapter.getString(ResourceAdapter.Item.INPUT_SAME_AS_OUTPUT_ERROR, new Object[0]));
        } else {
            this.progressHelper.initProgress();
            this.progressHelper.progress(1, this.resourceAdapter.getString(ResourceAdapter.Item.PARSING_CENTRAL_DIRECTORY, new Object[0]));
            ZipInput input = ZipInput.read(inputZipFilename);
            this.signZip(input.getEntries(), new FileOutputStream(outputZipFilename), outputZipFilename);
        }
    }

    public void signZip(Map<String, ZioEntry> zioEntries, OutputStream outputStream, String outputZipFilename) throws IOException, GeneralSecurityException {
        boolean debug = getLogger().isDebugEnabled();
        this.progressHelper.initProgress();
        if (this.keySet == null) {
            if (!this.keymode.startsWith("auto")) {
                throw new IllegalStateException("No keys configured for signing the file!");
            }

            String zipOutput = this.autoDetectKey(this.keymode, zioEntries);
            if (zipOutput == null) {
                throw new AutoKeyException(this.resourceAdapter.getString(ResourceAdapter.Item.AUTO_KEY_SELECTION_ERROR, new Object[]{(new File(outputZipFilename)).getName()}));
            }

            this.autoKeyObservable.notifyObservers(zipOutput);
            this.loadKeys(zipOutput);
        }

        ZipOutput var26 = null;

        try {
            var26 = new ZipOutput(outputStream);
            if ("none".equals(this.keySet.getName())) {
                this.progressHelper.setProgressTotalItems(zioEntries.size());
                this.progressHelper.setProgressCurrentItem(0);
                this.copyFiles(zioEntries, var26);
                return;
            }

            int t = 0;
            Iterator timestamp = zioEntries.values().iterator();

            while (timestamp.hasNext()) {
                ZioEntry entry = (ZioEntry) timestamp.next();
                String manifest = entry.getName();
                if (!entry.isDirectory() && !manifest.equals("META-INF/MANIFEST.MF") && !manifest.equals("META-INF/CERT.SF") && !manifest.equals("META-INF/CERT.RSA") && (stripPattern == null || !stripPattern.matcher(manifest).matches())) {
                    t += 3;
                }
            }

            ++t;
            this.progressHelper.setProgressTotalItems(t);
            this.progressHelper.setProgressCurrentItem(0);
            long var27 = this.keySet.getPublicKey().getNotBefore().getTime() + 3600000L;
            Manifest var28 = this.addDigestsToManifest(zioEntries);
            if (this.canceled) {
                return;
            }

            ZioEntry ze = new ZioEntry("META-INF/MANIFEST.MF");
            ze.setTime(var27);
            var28.write(ze.getOutputStream());
            var26.write(ze);
            ze = new ZioEntry("META-INF/CERT.SF");
            ze.setTime(var27);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            this.generateSignatureFile(var28, out);
            if (this.canceled) {
                return;
            }

            byte[] sfBytes = out.toByteArray();
            if (debug) {
                getLogger().debug("Signature File: \n" + new String(sfBytes) + "\n" + HexDumpEncoder.encode(sfBytes));
            }

            ze.getOutputStream().write(sfBytes);
            var26.write(ze);
            this.progressHelper.progress(0, this.resourceAdapter.getString(ResourceAdapter.Item.GENERATING_SIGNATURE_BLOCK, new Object[0]));
            ze = new ZioEntry("META-INF/CERT.RSA");
            ze.setTime(var27);
            this.writeSignatureBlock(this.keySet, sfBytes, ze.getOutputStream());
            var26.write(ze);
            if (this.canceled) {
                return;
            }

            this.copyFiles(var28, zioEntries, var26, var27);
            if (!this.canceled) {
                return;
            }
        } finally {
            var26.close();
            if (this.canceled) {
                try {
                    if (outputZipFilename != null) {
                        (new File(outputZipFilename)).delete();
                    }
                } catch (Throwable var24) {
                    getLogger().warning(var24.getClass().getName() + ":" + var24.getMessage());
                }
            }

        }

    }

    public void addProgressListener(ProgressListener l) {
        this.progressHelper.addProgressListener(l);
    }

    public synchronized void removeProgressListener(ProgressListener l) {
        this.progressHelper.removeProgressListener(l);
    }

    public static class AutoKeyObservable extends Observable {
        public AutoKeyObservable() {
        }

        public void notifyObservers(Object arg) {
            super.setChanged();
            super.notifyObservers(arg);
        }
    }
}

