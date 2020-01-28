package dev.vatuu.eulla.extensions.mixin;

import dev.vatuu.eulla.extensions.Matrix4fExt;
import net.minecraft.client.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Matrix4f.class)
public abstract class Matrix4fMixin implements Matrix4fExt {

    @Shadow protected float a00;
    @Shadow protected float a02;
    @Shadow protected float a01;
    @Shadow protected float a03;
    @Shadow protected float a10;
    @Shadow protected float a11;
    @Shadow protected float a12;
    @Shadow protected float a13;
    @Shadow protected float a20;
    @Shadow protected float a30;
    @Shadow protected float a21;
    @Shadow protected float a31;
    @Shadow protected float a22;
    @Shadow protected float a32;
    @Shadow protected float a23;
    @Shadow protected float a33;

    @Override
    @Unique
    public double[] multiply4D(double[] vec4d) {
        double[] ret = new double[4];
        ret[0] = vec4d[0] * this.a00 + vec4d[1] * this.a01 + vec4d[2] * this.a02 + vec4d[3] * this.a03;
        ret[1] = vec4d[0] * this.a10 + vec4d[1] * this.a11 + vec4d[2] * this.a12 + vec4d[3] * this.a13;
        ret[2] = vec4d[0] * this.a20 + vec4d[1] * this.a21 + vec4d[2] * this.a22 + vec4d[3] * this.a23;
        ret[3] = vec4d[0] * this.a30 + vec4d[1] * this.a31 + vec4d[2] * this.a32 + vec4d[3] * this.a33;
        return ret;
    }
}
