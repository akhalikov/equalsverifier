package nl.jqno.equalsverifier.internal.prefabvalues.factories.external;

import nl.jqno.equalsverifier.internal.prefabvalues.FactoryCache;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;

import static nl.jqno.equalsverifier.internal.prefabvalues.factories.Factories.values;

public final class AwtFactory {

    private static final int CS_RED = ColorSpace.CS_sRGB;
    private static final int CS_BLACK = ColorSpace.CS_LINEAR_RGB;

    private AwtFactory() {
        // Do not instantiate
    }

    public static FactoryCache getFactoryCache() {
        FactoryCache cache = new FactoryCache();

        cache.put(
            Color.class,
            values(Color.RED, Color.BLACK, Color.RED));
        cache.put(
            ColorSpace.class,
            values(ColorSpace.getInstance(CS_RED), ColorSpace.getInstance(CS_BLACK), ColorSpace.getInstance(CS_RED)));
        cache.put(
            ICC_ColorSpace.class,
            values(ICC_ColorSpace.getInstance(CS_RED), ICC_ColorSpace.getInstance(CS_BLACK), ICC_ColorSpace.getInstance(CS_RED)));
        cache.put(
            ICC_Profile.class,
            values(ICC_Profile.getInstance(CS_RED), ICC_Profile.getInstance(CS_BLACK), ICC_Profile.getInstance(CS_RED)));

        return cache;
    }
}
