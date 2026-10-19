package com.antoni.exchange.writeAheadLogger;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public abstract class WriteAheadLogger {

    //Protected variable to store log
    protected FileChannel fileChannel;

    protected void openLog(Path file) throws IOException{
        //code to open the log
        fileChannel = FileChannel.open(file, StandardOpenOption.APPEND);
    }

    public void writeLog(String orderString) throws IOException {
        orderString = orderString + '\n';
        Charset charset = StandardCharsets.UTF_8;
        ByteBuffer byteBuffer = charset.encode(orderString);
        fileChannel.write(byteBuffer);
        fileChannel.force(false);
    }

    protected void closeLog() throws IOException {
        //code to close the log
        fileChannel.close();
    }
}
