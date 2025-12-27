package gg.bluechair.minebead.forge.Client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import gg.bluechair.minebead.util.toolbox.SelectionPreview;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ForgeSelectionPreviewRenderer {

    private ForgeSelectionPreviewRenderer() {}

    // 选择一个合适的渲染阶段：
    // AFTER_SOLID_BLOCKS 通常比较稳（线框会被方块正确遮挡），也较“光影友好”
    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_SOLID_BLOCKS) return;

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        Level level = mc.level;
        if (player == null || level == null) return;

        var boxOpt = SelectionPreview.getSelectionBox(player, level);
        if (boxOpt.isEmpty()) return;

        // ✅ Forge 47.4.x 推荐：自己拿全局 BufferSource
        var bufferSource = mc.renderBuffers().bufferSource();

        renderLineBox(
                event.getPoseStack(),
                mc.gameRenderer.getMainCamera(),
                bufferSource,
                boxOpt.get()
        );

        // ✅ 提交 lines（否则可能不显示或延迟）
        bufferSource.endBatch(RenderType.lines());
    }


    private static void renderLineBox(PoseStack poseStack,
                                      Camera camera,
                                      MultiBufferSource bufferSource,
                                      AABB worldBox) {

        Vec3 camPos = camera.getPosition();

        // 转成相机相对坐标
        AABB shifted = worldBox.move(-camPos.x, -camPos.y, -camPos.z);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        // 保持 depth test：遮挡关系正确，光影一般更稳
        // RenderSystem.disableDepthTest();

        VertexConsumer vc = bufferSource.getBuffer(RenderType.lines());

        LevelRenderer.renderLineBox(
                poseStack,
                vc,
                shifted,
                0.0F, 1.0F, 0.0F, 0.8F
        );

        RenderSystem.disableBlend();
        // RenderSystem.enableDepthTest();
    }

    public static void init() {
        MinecraftForge.EVENT_BUS.addListener(ForgeSelectionPreviewRenderer::onRenderLevelStage);
    }
}
