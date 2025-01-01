package fr.arinonia.launcher.minecraft;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class MinecraftServerPing {
    private static final Gson GSON = new Gson();

    public static class ServerStatus {
        private final String version;
        private final int onlinePlayers;
        private final int maxPlayers;
        private final String description;
        private final boolean online;

        public ServerStatus(final String version, final int onlinePlayers, final int maxPlayers,
                            final String description, final boolean online) {
            this.version = version;
            this.onlinePlayers = onlinePlayers;
            this.maxPlayers = maxPlayers;
            this.description = description;
            this.online = online;
        }

        public String getVersion() {
            return this.version;
        }

        public int getOnlinePlayers() {
            return this.onlinePlayers;
        }

        public int getMaxPlayers() {
            return this.maxPlayers;
        }

        public String getDescription() {
            return this.description;
        }

        public boolean isOnline() {
            return this.online;
        }
    }

    public static CompletableFuture<ServerStatus> pingServer(final String address, final int port, final int timeout) {
        return CompletableFuture.supplyAsync(() -> {
            try (final Socket socket = new Socket()) {
                socket.setSoTimeout(timeout);
                socket.connect(new InetSocketAddress(address, port), timeout);

                try (final DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                     final DataInputStream in = new DataInputStream(socket.getInputStream())) {

                    final ByteArrayOutputStream handshakeBytes = new ByteArrayOutputStream();
                    final DataOutputStream handshake = new DataOutputStream(handshakeBytes);

                    writeVarInt(handshake, 0x00);
                    writeVarInt(handshake, 47);
                    writeString(handshake, address);
                    handshake.writeShort(port);
                    writeVarInt(handshake, 1);

                    writeVarInt(out, handshakeBytes.size());
                    out.write(handshakeBytes.toByteArray());

                    out.writeByte(0x01);
                    out.writeByte(0x00);

                    final int length = readVarInt(in);
                    final int packetId = readVarInt(in);

                    if (packetId == 0x00) {
                        final String json = readString(in);
                        final JsonObject response = GSON.fromJson(json, JsonObject.class);

                        final JsonObject version = response.getAsJsonObject("version");
                        final JsonObject players = response.getAsJsonObject("players");
                        final String description = extractDescription(response.get("description"));

                        return new ServerStatus(
                                version.get("name").getAsString(),
                                players.get("online").getAsInt(),
                                players.get("max").getAsInt(),
                                description,
                                true
                        );
                    }

                    throw new IOException("Invalid packet ID received");
                }
            } catch (final Exception e) {
                return new ServerStatus("Unknown", 0, 0, "Server offline", false);
            }
        });
    }

    private static void writeVarInt(final DataOutputStream out, int value) throws IOException {
        while (true) {
            if ((value & 0xFFFFFF80) == 0) {
                out.writeByte(value);
                return;
            }
            out.writeByte(value & 0x7F | 0x80);
            value >>>= 7;
        }
    }

    private static int readVarInt(final DataInputStream in) throws IOException {
        int numRead = 0;
        int result = 0;
        byte read;
        do {
            read = in.readByte();
            int value = (read & 0b01111111);
            result |= (value << (7 * numRead));
            numRead++;
            if (numRead > 5) {
                throw new RuntimeException("VarInt is too big");
            }
        } while ((read & 0b10000000) != 0);
        return result;
    }

    private static void writeString(final DataOutputStream out, final String string) throws IOException {
        final byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        writeVarInt(out, bytes.length);
        out.write(bytes);
    }

    private static String readString(final DataInputStream in) throws IOException {
        final int length = readVarInt(in);
        final byte[] bytes = new byte[length];
        in.readFully(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private static String extractDescription(final JsonElement element) {
        if (element.isJsonObject()) {
            final JsonObject desc = element.getAsJsonObject();
            if (desc.has("text")) {
                return desc.get("text").getAsString();
            }
        }
        return element.getAsString();
    }
}