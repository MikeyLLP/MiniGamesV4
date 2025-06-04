package de.mikeyllp.miniGamesV4.game.hideandseek.listeners;


import com.github.retrooper.packetevents.event.PacketListener;

public class PacketEventGlowListener implements PacketListener {

    /*private final Set<Integer> glowEntities = new HashSet<>();

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() != PacketType.Play.Server.ENTITY_METADATA) {
            return;
        }
        WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata(event);

        // Nur für unsere Ziel-Entität: hier z.B. alle Entities in glowEntities
        if (!glowEntities.contains(packet.getEntityId())) {
            return;
        }

        // Liste der Metadaten-Felder auslesen
        List<EntityData<?>> meta = packet.getEntityMetadata();
        for (EntityData<?> entry : meta) {
            // Index 0 enthält das Byte mit den Flags (z.B. on fire, crouching, glowing, ...)
            if (entry.getIndex() == 0) {
                byte flags = (Byte) entry.getValue();
                // Glowing-Bit (Bit 6 = 0x40) setzen
                flags |= 0x40;  // 0x40 hex = binär 0100 0000 (Glowing)
                entry.setValue(flags);
                // Neue Metadaten-Liste zurücksetzen und Packet neu kodieren
                packet.setEntityMetadata(meta);
                event.markForReEncode(true);  // Änderungen übernehmen:contentReference[oaicite:2]{index=2}
                break;
            }
        }
    }*/
}
