package dev.vatuu.eulla.extensions;

import dev.vatuu.eulla.render.WorldPortalRenderer;

public interface WorldRendererExt {

    WorldPortalRenderer getWorldPortalRenderer();

    void setPlayerRendering(boolean yes);
}
