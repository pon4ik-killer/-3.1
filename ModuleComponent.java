package expensive.display.dropdown.components;

import com.mojang.blaze3d.matrix.MatrixStack;
import expensive.modules.api.Function;
import expensive.modules.api.Setting;
import expensive.modules.api.impl.*;
import expensive.display.dropdown.components.settings.*;
import expensive.display.dropdown.impl.Component;
import expensive.util.client.main.KeyStorage;
import expensive.util.math.main.MathUtil;
import expensive.util.math.main.Vector4i;
import expensive.util.visual.main.color.ColorUtils;
import expensive.util.visual.components.Cursors;
import expensive.util.visual.main.display.DisplayUtils;
import expensive.util.visual.components.Stencil;
import expensive.util.visual.main.fonts.Fonts;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.vector.Vector4f;
import org.lwjgl.glfw.GLFW;
import ru.hogoshi.Animation;
import ru.hogoshi.util.Easings;

@Getter
public class ModuleComponent extends Component {
    private final Vector4f ROUNDING_VECTOR = new Vector4f(7, 7, 7, 7);
    private final Vector4i BORDER_COLOR = new Vector4i(ColorUtils.rgb(45, 46, 53), ColorUtils.rgb(25, 26, 31), ColorUtils.rgb(45, 46, 53), ColorUtils.rgb(25, 26, 31));

    private final Function function;
    public Animation animation = new Animation();
    public boolean open;
    private boolean bind;

    private final ObjectArrayList<Component> components = new ObjectArrayList<>();

    public ModuleComponent(Function function) {
        this.function = function;
        for (Setting<?> setting : function.getSettings()) {
            if (setting instanceof BooleanSetting bool) {
                components.add(new BooleanComponent(bool));
            }
            if (setting instanceof SliderSetting slider) {
                components.add(new SliderComponent(slider));
            }
            if (setting instanceof BindSetting bind) {
                components.add(new BindComponent(bind));
            }
            if (setting instanceof ModeSetting mode) {
                components.add(new ModeComponent(mode));
            }
            if (setting instanceof ModeListSetting mode) {
                components.add(new MultiBoxComponent(mode));
            }
            if (setting instanceof StringSetting string) {
                components.add(new StringComponent(string));
            }

        }
        animation = animation.animate(open ? 1 : 0, 0.3);
    }

    // draw components
    public void drawComponents(MatrixStack stack, float mouseX, float mouseY) {
        if (animation.getValue() > 0) {
            if (animation.getValue() > 0.1 && components.stream().filter(Component::isVisible).count() >= 1) {
                DisplayUtils.drawRectVerticalW(getX() + 5, getY() + 20, getWidth() - 10, 0.5f, ColorUtils.rgb(42, 44, 50), ColorUtils.rgb(28, 28, 33));
            }
            Stencil.initStencilToWrite();
            DisplayUtils.drawRoundedRect(getX() + 0.5f, getY() + 0.5f, getWidth() - 1, getHeight() - 1, ROUNDING_VECTOR, ColorUtils.rgba(23, 23, 23, (int) (255 * 0.33)));
            Stencil.readStencilBuffer(1);
            float y = getY() + 20;
            for (Component component : components) {
                if (component.isVisible()) {
                    component.setX(getX());
                    component.setY(y);
                    component.setWidth(getWidth());
                    component.render(stack, mouseX, mouseY);
                    y += component.getHeight();
                }
            }
            Stencil.uninitStencilBuffer();

        }
    }

    @Override
    public void mouseRelease(float mouseX, float mouseY, int mouse) {
        // TODO Auto-generated method stub

        for (Component component : components) {
            component.mouseRelease(mouseX, mouseY, mouse);
        }

        super.mouseRelease(mouseX, mouseY, mouse);
    }

    private boolean hovered = false;

    @Override
    public void render(MatrixStack stack, float mouseX, float mouseY) {
        int color = ColorUtils.interpolate(-1, ColorUtils.rgb(161, 164, 177), (float) function.getAnimation().getValue());

        function.getAnimation().update();
        super.render(stack, mouseX, mouseY);

        drawOutlinedRect(mouseX, mouseY, color);


        drawText(stack, color);
        drawComponents(stack, mouseX, mouseY);
    }

    @Override
    public void mouseClick(float mouseX, float mouseY, int button) {
        if (isHovered(mouseX, mouseY, 20)) {
            if (button == 0) function.toggle();
            if (button == 1) {
                open = !open;
                animation = animation.animate(open ? 1 : 0, 0.2, Easings.CIRC_OUT);
            }
            if (button == 2) {
                bind = !bind;
            }
        }
        if (isHovered(mouseX, mouseY)) {
            if (open) {
                for (Component component : components) {
                    if (component.isVisible()) component.mouseClick(mouseX, mouseY, button);
                }
            }
        }
        super.mouseClick(mouseX, mouseY, button);
    }

    @Override
    public void charTyped(char codePoint, int modifiers) {
        for (Component component : components) {
            if (component.isVisible()) component.charTyped(codePoint, modifiers);
        }
        super.charTyped(codePoint, modifiers);
    }

    @Override
    public void keyPressed(int key, int scanCode, int modifiers) {
        for (Component component : components) {
            if (component.isVisible()) component.keyPressed(key, scanCode, modifiers);
        }
        if (bind) {
            if (key == GLFW.GLFW_KEY_DELETE) {
                function.setBind(0);
            } else function.setBind(key);
            bind = false;
        }
        super.keyPressed(key, scanCode, modifiers);
    }

    private void drawOutlinedRect(float mouseX, float mouseY, int color) {
        Stencil.initStencilToWrite();
        DisplayUtils.drawRoundedRect(getX() + 0.5f, getY() + 0.5f, getWidth() - 1, getHeight() - 1, ROUNDING_VECTOR, ColorUtils.rgba(23, 23, 23, (int) (255 * 0.33)));

        Stencil.readStencilBuffer(0);
        DisplayUtils.drawRoundedRect(getX(), getY(), getWidth(), getHeight(), ROUNDING_VECTOR, BORDER_COLOR);
        Stencil.uninitStencilBuffer();
        DisplayUtils.drawRoundedRect(getX(), getY(), getWidth(), getHeight(), ROUNDING_VECTOR, new Vector4i(ColorUtils.rgba(13, 14, 19, (int) (255 * 0.33)), ColorUtils.rgba(16, 17, 23, (int) (255 * 0.33)), ColorUtils.rgba(16, 17, 23, (int) (255 * 0.33)), ColorUtils.rgba(16, 17, 23, (int) (255 * 0.33))));
        DisplayUtils.drawRoundedRect(getX(), getY(), getWidth(), getHeight(), ROUNDING_VECTOR, ColorUtils.rgba(17, 17, 17, (int) (255 * 0.33)));

        if (MathUtil.isHovered(mouseX, mouseY, getX(), getY(), getWidth(), 20)) {
            if (!hovered) {
                GLFW.glfwSetCursor(Minecraft.getInstance().getMainWindow().getHandle(), Cursors.HAND);
                hovered = true;
            }
        } else {
            if (hovered) {
                GLFW.glfwSetCursor(Minecraft.getInstance().getMainWindow().getHandle(), Cursors.ARROW);
                hovered = false;
            }
        }

    }

    private void drawText(MatrixStack stack, int color) {
        DisplayUtils.drawShadow(getX() + 6, getY() + 6.5f, Fonts.montserrat.getWidth(function.getName(), 7) + 3, Fonts.montserrat.getHeight(7), 10, ColorUtils.setAlpha(color, (int) (72 * function.getAnimation().getValue())));

        Fonts.montserrat.drawText(stack, function.getName(), getX() + 6, getY() + 6.5f, color, 7, 0.1f);
        if (components.stream().filter(Component::isVisible).count() >= 1) {
            if (bind) {
                Fonts.montserrat.drawText(stack, function.getBind() == 0 ? "..." : KeyStorage.getReverseKey(function.getBind()), getX() + getWidth() - 6 - Fonts.montserrat.getWidth(function.getBind() == 0 ? "..." : KeyStorage.getReverseKey(function.getBind()), 6, 0.1f), getY() + Fonts.icons.getHeight(6) + 1, ColorUtils.rgb(161, 164, 177), 6, 0.1f);
            } else
                Fonts.icons.drawText(stack, !open ? "B" : "C", getX() + getWidth() - 6 - Fonts.icons.getWidth(!open ? "B" : "C", 6), getY() + Fonts.icons.getHeight(6) + 1, ColorUtils.rgb(161, 164, 177), 6);
        } else {
            if (bind) {
                Fonts.montserrat.drawText(stack, function.getBind() == 0 ? "..." : KeyStorage.getReverseKey(function.getBind()), getX() + getWidth() - 6 - Fonts.montserrat.getWidth(function.getBind() == 0 ? "..." : KeyStorage.getReverseKey(function.getBind()), 6, 0.1f), getY() + Fonts.icons.getHeight(6) + 1, ColorUtils.rgb(161, 164, 177), 6, 0.1f);
            }
        }
    }
}
