package de.mikeyllp.miniGamesV4.game.hideandseek.listeners;


import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

import static de.mikeyllp.miniGamesV4.game.hideandseek.storage.HideAndSeekGameGroups.glowGroup;
import static de.mikeyllp.miniGamesV4.game.hideandseek.storage.HideAndSeekGameGroups.seekerGroup;


public class PacketEventGlowListener implements PacketListener {
    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (glowGroup.isEmpty()) return;
        if (event.getPacketType() != PacketType.Play.Server.ENTITY_METADATA) return;

        WrapperPlayServerEntityMetadata wrapper = new WrapperPlayServerEntityMetadata(event);
        int entityID = wrapper.getEntityId();
        Player receiver = event.getPlayer();

        System.out.println("Packet an " + receiver.getName() + " für EntityID: " + entityID);

        for (Map.Entry<String, List<Player>> entry : glowGroup.entrySet()) {
            String groupName = entry.getKey();
            List<Player> hiders = entry.getValue();
            List<Player> seekers = seekerGroup.get(groupName);
            if (!seekers.contains(receiver)) {
                for (Player hider : hiders) {
                    if (hider.getEntityId() == entityID) {
                        // Set the Glow effect for the hider
                        wrapper.setEntityMetadata(List.of(
                                new EntityData(0, EntityDataTypes.BYTE, (byte) 0x40)
                        ));
                        return;
                    }
                }
            }
        }
    }
}
