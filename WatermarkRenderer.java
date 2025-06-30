package expensive.display.display.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import expensive.main.Expensive;
import expensive.events.EventDisplay;
import expensive.display.display.ElementRenderer;
import expensive.display.styles.Style;
import expensive.util.visual.main.color.ColorUtils;
import expensive.util.visual.main.display.DisplayUtils;
import expensive.util.visual.main.fonts.Fonts;
import expensive.util.visual.main.color.fonts.GradientUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class WatermarkRenderer implements ElementRenderer {

    final ResourceLocation logo = new ResourceLocation("expensive/images/hud/logo.png");
    private final ResourceLocation user = new ResourceLocation("expensive/images/hud/user.png");

    @Override
    public void render(EventDisplay eventDisplay) {
        MatrixStack ms = eventDisplay.getMatrixStack();
        float posX = 4;
        float posY = 4;
        float padding = 5;
        float fontSize = 6.5f;
        float iconSize = 10;
        Style style = Expensive.getInstance().getStyleManager().getCurrentStyle();

        DisplayUtils.drawShadow(posX, posY, iconSize + padding * 2, iconSize + padding * 2, 10, style.getFirstColor().getRGB(), style.getSecondColor().getRGB());
        drawStyledRect(posX, posY, iconSize + padding * 2, iconSize + padding * 2, 4);
        DisplayUtils.drawImage(logo, posX + padding, posY + padding, iconSize, iconSize, ColorUtils.rgb(255, 255, 255));

        ITextComponent text = GradientUtil.gradient("SLIV BY BadLuck & fixed by Nikas, nekazuk & Soblazn Paster =) & hwidovskiy.java ");

        float textWidth = Fonts.sfui.getWidth(text, fontSize);

        float localPosX = posX + iconSize + padding * 3;

        DisplayUtils.drawShadow(localPosX, posY, iconSize + padding * 2.5f + textWidth, iconSize + padding * 2, 10, style.getFirstColor().getRGB(), style.getSecondColor().getRGB());
        drawStyledRect(localPosX, posY, iconSize + padding * 2.5f + textWidth, iconSize + padding * 2, 4);
        DisplayUtils.drawImage(user, localPosX + padding, posY + padding, iconSize, iconSize, ColorUtils.rgb(255, 255, 255));

        Fonts.sfui.drawText(ms, text, localPosX + iconSize + padding * 1.5f - 1, posY + iconSize / 2 + 1.5f, fontSize, 255);
    }

    private void drawStyledRect(float x,
                                float y,
                                float width,
                                float height,
                                float radius) {

        DisplayUtils.drawRoundedRect(x - 0.5f, y - 0.5f, width + 1, height + 1, radius + 0.5f, ColorUtils.getColor(0)); // outline
        DisplayUtils.drawRoundedRect(x, y, width, height, radius, ColorUtils.rgba(21, 21, 21, 255));
    }
}
