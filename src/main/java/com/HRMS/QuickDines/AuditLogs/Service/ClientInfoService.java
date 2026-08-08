package com.HRMS.QuickDines.AuditLogs.Service;

import com.HRMS.QuickDines.AuditLogs.Service.AuditLogsService;
import com.HRMS.QuickDines.AuditLogs.model.ClientInfoDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientInfoService {


    private final HttpServletRequest request;


    // =====================================================
    // GET CLIENT IP ADDRESS
    // =====================================================

    private String getClientIpAddress() {

        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // X-Forwarded-For can contain multiple IPs
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        return ip;
    }


    // =====================================================
    // GET USER AGENT
    // =====================================================

    private String getUserAgent() {

        String userAgent =
                request.getHeader("User-Agent");

        if (userAgent == null) {
            return "UNKNOWN";
        }

        return userAgent;
    }


    // =====================================================
    // GET BROWSER NAME
    // =====================================================

    private String getBrowserName() {

        String userAgent =
                getUserAgent().toLowerCase();

        if (userAgent.contains("edg")) {
            return "Microsoft Edge";
        }

        if (userAgent.contains("chrome")
                && !userAgent.contains("edg")) {
            return "Google Chrome";
        }

        if (userAgent.contains("firefox")) {
            return "Mozilla Firefox";
        }

        if (userAgent.contains("safari")
                && !userAgent.contains("chrome")) {
            return "Safari";
        }

        if (userAgent.contains("opr")
                || userAgent.contains("opera")) {
            return "Opera";
        }

        return "Unknown Browser";
    }


    // =====================================================
    // GET OPERATING SYSTEM
    // =====================================================

    private String getOperatingSystem() {

        String userAgent =
                getUserAgent().toLowerCase();

        if (userAgent.contains("windows")) {
            return "Windows";
        }

        if (userAgent.contains("mac os")
                || userAgent.contains("macintosh")) {
            return "macOS";
        }

        if (userAgent.contains("android")) {
            return "Android";
        }

        if (userAgent.contains("iphone")
                || userAgent.contains("ipad")
                || userAgent.contains("ios")) {
            return "iOS";
        }

        if (userAgent.contains("linux")) {
            return "Linux";
        }

        return "Unknown OS";
    }

    public ClientInfoDTO getClientInfo()
    {
        ClientInfoDTO clientInfo = new ClientInfoDTO();
        clientInfo.setIpAddress(getClientIpAddress());
        clientInfo.setBrowser(getBrowserName());
        clientInfo.setOperatingSystem(getOperatingSystem());
        return clientInfo;
    }
}
