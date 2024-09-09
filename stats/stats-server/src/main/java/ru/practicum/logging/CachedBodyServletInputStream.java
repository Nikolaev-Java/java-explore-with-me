package ru.practicum.logging;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CachedBodyServletInputStream extends ServletInputStream {
    private InputStream inputStream;

    public CachedBodyServletInputStream(byte[] bytes) {
        this.inputStream = new ByteArrayInputStream(bytes);
    }

    @Override
    public boolean isFinished() {
        try {
            return inputStream.available() == 0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(ReadListener readListener) {

    }

    @Override
    public int read() throws IOException {
        return inputStream.read();
    }
}
