package com.example.examplemod; // Это соответствует вашей папке на скриншоте

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.DrawHighlightEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.renderer.ActiveRenderInfo;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Matrix4f;
import java.awt.Color;

@Mod.EventBusSubscriber(modid = "examplemod", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CustomOutline {
)
    @SubscribeEvent
    public static void onDrawHighlight(DrawHighlightEvent.HighlightBlock event) {
        event.setCanceled(true); // Отключаем стандартную рамку

        BlockPos pos = event.getTarget().getBlockPos();
        ActiveRenderInfo info = event.getInfo();
        MatrixStack matrixStack = event.getMatrix();
        
        // Создаем радужный эффект
        float hue = (System.currentTimeMillis() % 4000) / 4000f;
        int color = Color.HSBtoRGB(hue, 1.0f, 1.0f);
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;

        double d0 = info.getProjectedView().x;
        double d1 = info.getProjectedView().y;
        double d2 = info.getProjectedView().z;

        IVertexBuilder buffer = event.getBuffers().getBuffer(RenderType.getLines());
        VoxelShape shape = event.getTarget().getWorld().getBlockState(pos).getShape(event.getTarget().getWorld(), pos);

        matrixStack.push();
        matrixStack.translate(pos.getX() - d0, pos.getY() - d1, pos.getZ() - d2);

        Matrix4f matrix4f = matrixStack.getLast().getMatrix();
        shape.forEachEdge((x1, y1, z1, x2, y2, z2) -> {
            buffer.pos(matrix4f, (float)x1, (float)y1, (float)z1).color(r, g, b, 1.0f).endVertex();
            buffer.pos(matrix4f, (float)x2, (float)y2, (float)z2).color(r, g, b, 1.0f).endVertex();
        });

        matrixStack.pop();
    }
}