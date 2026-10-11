# MCmod_GlowOutlineFix

**[한국어](#한국어)** 설명은 아래에 있습니다.

> A Fabric client mod (Minecraft 1.21.8) that fixes **broken outlines on glowing text** — glow ink signs and the like —
> when the font is a bitmap font with fine pixels, such as Korean.

> This mod was written with an AI coding assistant (Claude Code) and checked by the maintainer in real use.

## At a glance

| Item | Value |
|---|---|
| Version | `1.0.0` |
| Target | Minecraft 1.21.8 · Fabric · **client only** |
| Requires | Fabric Loader 0.19.3+ · Java 21 (Fabric API is not needed) |

## The problem

Vanilla draws the outline of glowing text by **drawing the glyph 8 times, nudged in 8 directions**
(`Font.drawInBatch8xOutline`). The nudge distance is the glyph's `getShadowOffset()`, which is always `1.0`
(one vanilla font pixel) for bitmap glyphs.

For fonts whose pixel is smaller than that — e.g. a Korean bitmap font added with height 12 (pixel ≈ ⅔) — gaps open
between the nudged copies and the outline looks broken.

## The fix

- **Only while drawing the outline**, the nudge distance is reduced to that glyph's pixel size — the same thing vanilla
  unihex glyphs do by returning `0.5`
- `getShadowOffset()` itself is untouched — chat and UI shadows are unchanged
- Fonts whose pixel is one unit or larger (vanilla ascii, etc.) are unchanged

| File | What |
|---|---|
| `mixin/FontOutlineMixin.java` | wraps the `getShadowOffset()` call in the per-glyph lambda inside `drawInBatch8xOutline` (1.21.8 intermediary `method_37297`) with `@WrapOperation` |
| `mixin/BitmapGlyphMixin.java` | attaches `GlyphPixelSize` to the package-private `BitmapProvider$Glyph` to read its pixel size (`scale`) |
| `GlyphPixelSize.java` | that interface |

⚠ `method_37297` is the 1.21.8 name — it will not match other versions (`require = 2`, so the game fails at startup if it is not found).

## Building

```bash
# JDK 21
./gradlew build
# → build/libs/glow_outline_fix-1.0.0.jar
```

## License

**MIT** (`LICENSE`).

---

# 한국어

> 발광 잉크 표지판 같은 **빛나는 글자의 테두리**가, 한글처럼 도트가 잘은 비트맵 폰트에서 끊겨 보이는 문제를 고치는
> Fabric 클라이언트 모드다 (Minecraft 1.21.8).

> 이 모드는 AI 코딩 도구(Claude Code)로 작성했고 유지보수자가 실제 사용으로 확인했다.

## 한눈에

| 항목 | 값 |
|---|---|
| 버전 | `1.0.0` |
| 타깃 | Minecraft 1.21.8 · Fabric · **클라이언트 전용** |
| 의존 | Fabric Loader 0.19.3+ · Java 21 (Fabric API 는 필요 없다) |

## 무엇이 문제였나

바닐라는 빛나는 글자의 테두리를 **글자를 8방향으로 조금씩 밀어 8번 그려서** 만든다(`Font.drawInBatch8xOutline`).
미는 거리는 글리프의 `getShadowOffset()` 인데, 비트맵 글리프는 이 값이 언제나 `1.0`(바닐라 도트 1칸)이다.

도트 1칸이 그보다 작은 폰트 — 예: 높이 12 로 넣은 한글 비트맵(도트 ≈ ⅔칸) — 는 밀어 그린 사본 사이에 틈이 생겨
테두리가 끊겨 보인다.

## 고친 것

- **테두리를 그릴 때만** 미는 거리를 그 글리프의 도트 1칸 크기로 줄인다 — 바닐라 unihex 글리프가 `0.5` 를 돌려주는 것과 같은 처리
- `getShadowOffset()` 자체는 안 바꾼다 — 채팅 · UI 의 그림자는 그대로다
- 도트가 1칸 이상인 폰트(바닐라 ascii 등)는 그대로다

| 파일 | 무엇 |
|---|---|
| `mixin/FontOutlineMixin.java` | `drawInBatch8xOutline` 안 글자별 람다(1.21.8 intermediary `method_37297`)의 `getShadowOffset()` 호출을 `@WrapOperation` 으로 감싼다 |
| `mixin/BitmapGlyphMixin.java` | 패키지 전용 `BitmapProvider$Glyph` 에 `GlyphPixelSize` 를 붙여 도트 1칸 크기(`scale`)를 읽는다 |
| `GlyphPixelSize.java` | 그 인터페이스 |

⚠ `method_37297` 은 1.21.8 의 이름이다 — 다른 버전에서는 맞지 않는다(`require = 2` 라 못 찾으면 게임 시작 때 실패한다).

## 빌드

```bash
# JDK 21
./gradlew build
# → build/libs/glow_outline_fix-1.0.0.jar
```

## 라이선스

**MIT** (`LICENSE`).
