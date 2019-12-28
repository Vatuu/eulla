package dev.vatuu.eulla.extensions;

import dev.vatuu.eulla.PortalViewEntity;
import dev.vatuu.eulla.render.WorldPortalRenderer;

public interface WorldRendererExt {

    WorldPortalRenderer getWorldPortalRenderer();

    void setRenderingPortal(PortalViewEntity yes);
}
