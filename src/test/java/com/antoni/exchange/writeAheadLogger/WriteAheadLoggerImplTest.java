package com.antoni.exchange.writeAheadLogger;

import com.antoni.exchange.writeAheadLogger.WriteAheadLoggerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static org.springframework.test.util.AssertionErrors.*;

@SpringBootTest
@SpringBootConfiguration
class WriteAheadLoggerImplTest {

    @Value("${app.log.path}")
    private Path path;
    private TestableWriteAheadLoggerImpl writeAheadLogger;

    private class TestableWriteAheadLoggerImpl extends WriteAheadLoggerImpl {
        public Path getPath() {
            return this.logPath;
        }

        public void setPath(Path path) {
            this.logPath = path;
        }
        @Override
        public void openLog(Path file) throws IOException{
            fileChannel = FileChannel.open(file, StandardOpenOption.APPEND);
        }
        @Override
        public void closeLog() throws IOException{
            fileChannel.close();
        }
        public String readLog() {
            try (FileChannel readChannel = FileChannel.open(path, StandardOpenOption.READ)){
                ByteBuffer readBuffer = ByteBuffer.allocate((int) readChannel.size());
                readChannel.read(readBuffer);
                readBuffer.flip();
                Charset charset = StandardCharsets.UTF_8;
                return String.valueOf(charset.decode(readBuffer));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @BeforeEach
    void setUp() {
        writeAheadLogger = new TestableWriteAheadLoggerImpl();
        writeAheadLogger.setPath(path);
    }

    @Test
    void testWrongFilePathWhenOpeningFileThrowsCorrectAssertion() {
        Path wrongPath = Paths.get("./wrongpath/wal.log");
        try {
            writeAheadLogger.openLog(wrongPath);
            fail("Failed to throw exception for wrong file path");
        } catch (IOException e) {
            assertEquals("Exception message doesn't match expected message", wrongPath.toString(), e.getMessage());
        }
    }

    @Test
    void testWritingToFile() {
        String testString = "order";
        try {
            writeAheadLogger.openLog(writeAheadLogger.getPath());
            writeAheadLogger.writeLog(testString);
            writeAheadLogger.closeLog();
        } catch (IOException e) {
            fail("Failed to open file");
        }
        assertEquals("File not equal to testString",(testString + '\n'), writeAheadLogger.readLog());

    }
}