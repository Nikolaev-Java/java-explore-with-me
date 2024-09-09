package ru.practicum.logging;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.util.StreamUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CachedBodyHttpServletsRequest extends HttpServletRequestWrapper {
    private byte[] body;

    public CachedBodyHttpServletsRequest(HttpServletRequest request) throws IOException {
        super(request);
        InputStream requestInputStream = request.getInputStream();
        this.body = StreamUtils.copyToByteArray(requestInputStream);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new CachedBodyServletInputStream(this.body);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.body);
        return new BufferedReader(new InputStreamReader(byteArrayInputStream));
    }
}
