package com.example.harshaeditor.PHPLanguageClient;

import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.services.LanguageServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class PhpactorLanguageClient {

    // Inject values from docker-compose.yml environment variables
    @Value("${PHPACTOR_HOST}")
    private String host;

    @Value("${PHPACTOR_PORT}")
    private int port;

    private LanguageServer languageServer;

    @PostConstruct
    public void start() {
        try {
            // This is the client that will handle responses from the server
            MyLocalClient localClient = new MyLocalClient();

            // Connect to the server using the host and port from Docker Compose
            System.out.println("Attempting to connect to Phpactor at " + host + ":" + port);
            Socket socket = new Socket(host, port);
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Launcher<LanguageServer> launcher = Launcher.createLauncher(
                    localClient,
                    LanguageServer.class,
                    in,
                    out,
                    executorService,
                    (message) -> {
                    });

            launcher.startListening();
            this.languageServer = launcher.getRemoteProxy();

            System.out.println("✅ Successfully connected to Phpactor Language Server!");

            // You can now use this languageServer object to send LSP requests
            // (e.g., initialize, textDocument/completion, etc.)

        } catch (Exception e) {
            System.err.println("❌ Failed to connect to Phpactor Language Server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public LanguageServer getServer() {
        return this.languageServer;
    }
}

// NOTE: You still need to implement the LanguageClient interface to receive
// messages.
// class MyLocalClient implements org.eclipse.lsp4j.services.LanguageClient {
// ... }