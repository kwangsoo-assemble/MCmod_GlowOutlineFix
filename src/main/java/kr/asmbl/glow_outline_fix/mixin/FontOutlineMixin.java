package kr.asmbl.glow_outline_fix.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.font.GlyphInfo;
import kr.asmbl.glow_outline_fix.GlyphPixelSize;
import net.minecraft.client.gui.Font;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Font.drawInBatch8xOutline 은 글자를 8방향으로 getShadowOffset() 만큼 밀어 그려 테두리를 만든다.
 * 비트맵 글리프는 그 값이 언제나 1.0 이라, 도트가 1칸보다 작은 폰트(한글 갈무리 ⅔칸)는
 * 사본 사이에 틈이 생겨 테두리가 끊겨 보인다.
 * ⇒ 테두리를 그릴 때만 밀어내는 거리를 그 글리프의 도트 1칸으로 줄인다.
 *   (바닐라 unihex 글리프가 getShadowOffset 을 0.5 로 돌려주는 것과 같은 처리.
 *    getShadowOffset 자체는 안 바꾼다 — 채팅·UI 그림자는 그대로다)
 *
 * method_37297 = drawInBatch8xOutline 안의 글자별 람다 (1.21.8 intermediary 이름)
 */
@Mixin(Font.class)
public abstract class FontOutlineMixin {
	@WrapOperation(
		method = "method_37297",
		at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/font/GlyphInfo;getShadowOffset()F"),
		require = 2
	)
	private float glowOutlineFix$outlineOffset(GlyphInfo glyph, Operation<Float> original) {
		float offset = original.call(glyph);
		if (glyph instanceof GlyphPixelSize sized) {
			float pixel = sized.glowOutlineFix$pixelSize();
			if (pixel > 0f && pixel < offset) {
				return pixel;
			}
		}
		return offset;
	}
}
