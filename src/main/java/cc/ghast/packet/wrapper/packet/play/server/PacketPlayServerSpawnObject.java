package cc.ghast.packet.wrapper.packet.play.server;

import cc.ghast.packet.buffer.ProtocolByteBuf;
import cc.ghast.packet.nms.ProtocolVersion;
import cc.ghast.packet.wrapper.packet.Packet;
import cc.ghast.packet.wrapper.packet.ReadableBuffer;
import cc.ghast.packet.wrapper.packet.ServerPacket;
import lombok.Getter;

import java.util.Optional;
import java.util.UUID;

@Getter
public class PacketPlayServerSpawnObject extends Packet<ServerPacket> implements ReadableBuffer {
    public PacketPlayServerSpawnObject(UUID player, ProtocolVersion version) {
        super("PacketPlayOutSpawnObject", player, version);
    }

    public PacketPlayServerSpawnObject(String realName, UUID player, ProtocolVersion version) {
        super(realName, player, version);
    }

    private int entityId;
    private UUID objectUUID;
    private int type;
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;
    private int data;
    private Optional<Short> velocityX;
    private Optional<Short> velocityY;
    private Optional<Short> velocityZ;

    @Override
    public void read(ProtocolByteBuf byteBuf) {
        this.entityId = byteBuf.readVarInt();
        this.objectUUID = byteBuf.readUUID();
        this.x = byteBuf.readInt() / 32.0D;
        this.y = byteBuf.readInt() / 32.0D;
        this.z = byteBuf.readInt() / 32.0D;
        this.pitch = byteBuf.readByte() / 256.0F * 360.0F;
        this.yaw = byteBuf.readByte() / 256.0F * 360.0F;
        this.type = byteBuf.readShort();

        if (version.isOrAbove(ProtocolVersion.V1_8)) {
            this.velocityX = Optional.of(byteBuf.readShort());
            this.velocityY = Optional.of(byteBuf.readShort());
            this.velocityZ = Optional.of(byteBuf.readShort());
        } else {
            this.velocityX = velocityY = velocityZ = Optional.empty();
        }
    }

    public Optional<Double> getMotionX() {
        return velocityX.map(e -> e * 8000.0D);
    }

    public Optional<Double> getMotionY() {
        return velocityY.map(e -> e * 8000.0D);
    }

    public Optional<Double> getMotionZ() {
        return velocityZ.map(e -> e * 8000.0D);
    }
}