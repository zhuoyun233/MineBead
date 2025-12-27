package gg.bluechair.minebead.fabric.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import gg.bluechair.minebead.util.toolbox.SelectionPreview;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

public final class FabricSelectionPreviewRenderer {

    public static void init() {
        WorldRenderEvents.LAST.register(FabricSelectionPreviewRenderer::onWorldRenderLast);
    }

    private static void onWorldRenderLast(WorldRenderContext context) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        Level level = mc.level;
        if (player == null || level == null) return;

        var boxOpt = SelectionPreview.getSelectionBox(player, level);
        if (boxOpt.isEmpty()) return;

        renderLineBox(context, boxOpt.get());
    }

    private static void renderLineBox(WorldRenderContext ctx, AABB worldBox) {
        Camera camera = ctx.camera();
        Vec3 camPos = camera.getPosition();

        // 把世界坐标转成相机相对坐标
        AABB shifted = worldBox.move(-camPos.x, -camPos.y, -camPos.z);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        // 保持 depth test：遮挡关系正确，光影一般更稳
        // RenderSystem.disableDepthTest();

        VertexConsumer vc = Objects.requireNonNull(ctx.consumers()).getBuffer(RenderType.lines());

        LevelRenderer.renderLineBox(
                ctx.matrixStack(),
                vc,
                shifted,
                0.0F, 1.0F, 0.0F, 0.8F
        );

        RenderSystem.disableBlend();
        // RenderSystem.enableDepthTest();
    }

    private FabricSelectionPreviewRenderer() {}
}
