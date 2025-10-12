package crx.skyblock.listener;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.player.PlayerChatEvent;
import crx.skyblock.service.GlobalChatService;

public class ChatListener extends MainListener {

    private final GlobalChatService chatService;

    public ChatListener(GlobalChatService chatService) {
        this.chatService = chatService;
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        String playerName = event.getPlayer().getName();
        String message = event.getMessage();

        this.chatService.sendChatMessage(playerName, message);
        event.setCancelled(true);
    }
}
