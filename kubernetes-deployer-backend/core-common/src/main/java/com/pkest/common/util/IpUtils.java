package com.pkest.common.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * IP工具类
 *
 * @author Jaward
 */
public class IpUtils {

    /**
     * 获取请求IP
     *
     * @param request 请求
     * @return IPV4
     */
    public static String getRequestIp(HttpServletRequest request) {

        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isEmpty(ip)) {
            return request.getRemoteAddr();
        }
        ip = ip.split(", ")[0].trim();
        if ("127.0.0.1".equals(ip)) {
            return request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 获取内网IP
     *
     * @return IP地址
     */
    public static String getInnerIP() {

        boolean isLinuxOS = !System.getProperty("os.name").toLowerCase().startsWith("windows");

        Enumeration<NetworkInterface> netInterfaces = null;
        String ip = "127.0.0.1";
        try {
            netInterfaces = NetworkInterface.getNetworkInterfaces();
            Map<String, String> map = new HashMap();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces.nextElement();
                String name = ni.getName();
                if (!(name.startsWith("eth") || name.startsWith("enp4s"))) {
                    continue;
                }

                List<InterfaceAddress> list = ni.getInterfaceAddresses();
                for (InterfaceAddress ia : list) {
                    InetAddress address = ia.getAddress();
                    if (address instanceof Inet4Address) {
                        map.put(name, address.getHostAddress());
                    }
                }
            }
            if (map.isEmpty()) {
                return ip;
            }
            ip = map.get("eth2");
            if (!isLinuxOS && StringUtils.isNotEmpty(ip)) {
                return ip;
            }
            ip = map.get("eth1");
            if (StringUtils.isEmpty(ip)) {
                ip = map.get("eth0");
            }
            if (StringUtils.isEmpty(ip)) {
                ip = map.get("enp4s1");
            }
            if (StringUtils.isEmpty(ip)) {
                ip = map.get("enp4s0");
            }
            if (StringUtils.isEmpty(ip)) {
                ip = "127.0.0.1";
            }
        } catch (Exception e) {
            ip = "127.0.0.1";
        }
        return ip;
    }
}
