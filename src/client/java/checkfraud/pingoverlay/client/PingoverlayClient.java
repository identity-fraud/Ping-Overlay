package checkfraud.pingoverlay.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.resources.Identifier;

public class PingoverlayClient implements ClientModInitializer {
    public static final String MODID = "ping-overlay";
    private static final Identifier ELEMENTID = Identifier.fromNamespaceAndPath(MODID, "before_chat");

    public void onInitializeClient() {
        HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT, ELEMENTID, PingoverlayClient::render);
    }

    private static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player == null || minecraft.options.hideGui || minecraft.getDebugOverlay().showDebugScreen()) {
            return;
        }

        int fpsValue = minecraft.getFps();
        
        String fps = "FPS: " + fpsValue;

        String ping = "Ping: 0ms";

        ClientPacketListener connection = minecraft.getConnection();
        if (connection != null) {
            PlayerInfo playerInfo = connection.getPlayerInfo(minecraft.player.getUUID());
            if (playerInfo != null) {
                int pingValue = playerInfo.getLatency();
                ping = "Ping: " + pingValue + "ms";
            }
        }

        Font font = minecraft.font;
        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();
        
        int hotbarRight = (screenWidth >> 1) + 91;
        int textX = hotbarRight + 5;

        int fpsY = screenHeight - 22; 
        int pingY = screenHeight - 12;

        guiGraphics.drawString(font, fps, textX, fpsY, 0xFFFFFFFF, true);
        guiGraphics.drawString(font, ping, textX, pingY, 0xFFFFFFFF, true);
    }
}
