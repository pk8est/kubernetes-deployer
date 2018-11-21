package com.pkest.libs.kubernetes.util;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.internal.SSLUtils;
import io.fabric8.kubernetes.client.utils.BackwardsCompatibilityInterceptor;
import io.fabric8.kubernetes.client.utils.ImpersonatorInterceptor;
import io.fabric8.kubernetes.client.utils.IpAddressMatcher;
import io.fabric8.kubernetes.client.utils.Utils;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by wuzhonggui on 2018/11/19.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
public class HttpClientUtils {

    private static Pattern VALID_IPV4_PATTERN = null;
    public static final String ipv4Pattern = "(http:\\/\\/|https:\\/\\/)?(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])(\\/[0-9]\\d|1[0-9]\\d|2[0-9]\\d|3[0-2]\\d)?";
    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    public HttpClientUtils() {
    }

    public static OkHttpClient createHttpClient(final Config config) {
        try {
            OkHttpClient.Builder e = new OkHttpClient.Builder();
            e.followRedirects(true);
            e.followSslRedirects(true);
            if(config.isTrustCerts() || config.isDisableHostnameVerification()) {
                e.hostnameVerifier(new HostnameVerifier() {
                    public boolean verify(String s, SSLSession sslSession) {
                        return true;
                    }
                });
            }

            TrustManager[] trustManagers = SSLUtils.trustManagers(config);
            KeyManager[] keyManagers = SSLUtils.keyManagers(config);
            if(keyManagers == null && trustManagers == null && !config.isTrustCerts()) {
                SSLContext reqLogger1 = SSLContext.getInstance("TLSv1.2");
                reqLogger1.init(keyManagers, trustManagers, (SecureRandom)null);
                e.sslSocketFactory(reqLogger1.getSocketFactory(), (X509TrustManager)trustManagers[0]);
            } else {
                X509TrustManager reqLogger = null;
                if(trustManagers != null && trustManagers.length == 1) {
                    reqLogger = (X509TrustManager)trustManagers[0];
                }

                try {
                    SSLContext spec = SSLUtils.sslContext(keyManagers, trustManagers, config.isTrustCerts());
                    e.sslSocketFactory(spec.getSocketFactory(), reqLogger);
                } catch (GeneralSecurityException var7) {
                    throw new AssertionError();
                }
            }

            e.addInterceptor(new Interceptor() {
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    Request authReq;
                    if(Utils.isNotNullOrEmpty(config.getUsername()) && Utils.isNotNullOrEmpty(config.getPassword())) {
                        authReq = chain.request().newBuilder().addHeader("Authorization", Credentials.basic(config.getUsername(), config.getPassword())).build();
                        return chain.proceed(authReq);
                    } else if(Utils.isNotNullOrEmpty(config.getOauthToken())) {
                        authReq = chain.request().newBuilder().addHeader("Authorization", "Bearer " + config.getOauthToken()).build();
                        return chain.proceed(authReq);
                    } else {
                        return chain.proceed(request);
                    }
                }
            }).addInterceptor(new ImpersonatorInterceptor(config)).addInterceptor(new BackwardsCompatibilityInterceptor());
            //Logger reqLogger2 = LoggerFactory.getLogger(HttpLoggingInterceptor.class);
            if(logger.isDebugEnabled()) {
                HttpLoggingInterceptor spec1 = new HttpLoggingInterceptor(new HttpLogger());
                spec1.setLevel(HttpLoggingInterceptor.Level.BODY);
                e.addNetworkInterceptor(spec1);
            }

            if(config.getConnectionTimeout() > 0) {
                e.connectTimeout((long)config.getConnectionTimeout(), TimeUnit.MILLISECONDS);
            }

            if(config.getRequestTimeout() > 0) {
                e.readTimeout((long)config.getRequestTimeout(), TimeUnit.MILLISECONDS);
            }

            if(config.getWebsocketPingInterval() > 0L) {
                e.pingInterval(config.getWebsocketPingInterval(), TimeUnit.MILLISECONDS);
            }

            if(config.getMaxConcurrentRequestsPerHost() > 0) {
                Dispatcher spec2 = new Dispatcher();
                spec2.setMaxRequests(config.getMaxConcurrentRequests());
                spec2.setMaxRequestsPerHost(config.getMaxConcurrentRequestsPerHost());
                e.dispatcher(spec2);
            }

            if(config.getMasterUrl().toLowerCase().startsWith("http://") || config.getMasterUrl().startsWith("https://")) {
                try {
                    URL spec3 = getProxyUrl(config);
                    if(spec3 != null) {
                        e.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(spec3.getHost(), spec3.getPort())));
                        if(config.getProxyUsername() != null) {
                            e.proxyAuthenticator(new Authenticator() {
                                public Request authenticate(Route route, Response response) throws IOException {
                                    String credential = Credentials.basic(config.getProxyUsername(), config.getProxyPassword());
                                    return response.request().newBuilder().header("Proxy-Authorization", credential).build();
                                }
                            });
                        }
                    }
                } catch (MalformedURLException var6) {
                    throw new KubernetesClientException("Invalid proxy server configuration", var6);
                }
            }

            if(config.getUserAgent() != null && !config.getUserAgent().isEmpty()) {
                e.addNetworkInterceptor(new Interceptor() {
                    public Response intercept(Chain chain) throws IOException {
                        Request agent = chain.request().newBuilder().header("User-Agent", config.getUserAgent()).build();
                        return chain.proceed(agent);
                    }
                });
            }

            if(config.getTlsVersions() != null && config.getTlsVersions().length > 0) {
                ConnectionSpec spec4 = (new okhttp3.ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)).tlsVersions(config.getTlsVersions()).build();
                e.connectionSpecs(Arrays.asList(new ConnectionSpec[]{spec4, ConnectionSpec.CLEARTEXT}));
            }

            return e.build();
        } catch (Exception var8) {
            throw KubernetesClientException.launderThrowable(var8);
        }
    }

    private static URL getProxyUrl(Config config) throws MalformedURLException {
        URL master = new URL(config.getMasterUrl());
        String host = master.getHost();
        if(config.getNoProxy() != null) {
            String[] proxy = config.getNoProxy();
            int var4 = proxy.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String noProxy = proxy[var5];
                if(isIpAddress(noProxy)) {
                    if((new IpAddressMatcher(noProxy)).matches(host)) {
                        return null;
                    }
                } else if(host.contains(noProxy)) {
                    return null;
                }
            }
        }

        String var7 = config.getHttpsProxy();
        if(master.getProtocol().equals("http")) {
            var7 = config.getHttpProxy();
        }

        return var7 != null?new URL(var7):null;
    }

    private static boolean isIpAddress(String ipAddress) {
        Matcher ipMatcher = VALID_IPV4_PATTERN.matcher(ipAddress);
        return ipMatcher.matches();
    }

    static {
        try {
            VALID_IPV4_PATTERN = Pattern.compile("(http:\\/\\/|https:\\/\\/)?(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])(\\/[0-9]\\d|1[0-9]\\d|2[0-9]\\d|3[0-2]\\d)?", 2);
        } catch (PatternSyntaxException var1) {
            throw KubernetesClientException.launderThrowable("Unable to compile ipv4address pattern.", var1);
        }
    }
}
