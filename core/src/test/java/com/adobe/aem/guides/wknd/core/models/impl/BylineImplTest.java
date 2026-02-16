package com.adobe.aem.guides.wknd.core.models.impl;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.factory.ModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.adobe.aem.guides.wknd.core.models.Byline;
import com.adobe.cq.wcm.core.components.models.Image;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.util.List;
import com.google.common.collect.ImmutableList;

@ExtendWith({ AemContextExtension.class, MockitoExtension.class })
public class BylineImplTest {

    private final AemContext ctx = new AemContext();

    @Mock
    private Image image;

    @Mock
    private ModelFactory modelFactory;

    @BeforeEach
    public void setUp() throws Exception {
        // Registra el modelo Sling BylineImpl en el contexto simulado
        ctx.addModelsForClasses(BylineImpl.class);

        // Carga la estructura de recursos JSON en el repositorio JCR simulado
        ctx.load().json("/com/adobe/aem/guides/wknd/core/models/impl/BylineImplTest.json", "/content");

        // Configura el comportamiento simulado de ModelFactory para evitar el NullPointerException
        // Cuando se pida un modelo de Imagen, devolveremos nuestro objeto @Mock de imagen
        lenient().when(modelFactory.getModelFromWrappedRequest(eq(ctx.request()), any(Resource.class), eq(Image.class)))
                .thenReturn(image);

        // Registra el servicio simulado en el contexto de OSGi con la máxima prioridad
        ctx.registerService(ModelFactory.class, modelFactory, org.osgi.framework.Constants.SERVICE_RANKING,
                Integer.MAX_VALUE);
    }

    @Test
    void testGetName() {
        final String expected = "Jane Doe";

        // Define el recurso actual sobre el que estamos trabajando
        ctx.currentResource("/content/byline");
        
        // Adapta la solicitud simulada a la interfaz Byline
        Byline byline = ctx.request().adaptTo(Byline.class);

        // Ejecuta el método y compara resultados
        String actual = byline.getName();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetOccupations() {
        List<String> expected = new ImmutableList.Builder<String>()
                                .add("Blogger")
                                .add("Photographer")
                                .add("YouTuber")
                                .build();

        ctx.currentResource("/content/byline");
        Byline byline = ctx.request().adaptTo(Byline.class);

        List<String> actual = byline.getOccupations();

        assertEquals(expected, actual);
    }

    @Test
    public void testIsEmpty() {
        ctx.currentResource("/content/empty");

        Byline byline = ctx.request().adaptTo(Byline.class);

        assertTrue(byline.isEmpty());
    }

    @Test
    public void testIsEmpty_WithoutName() {
        ctx.currentResource("/content/without-name");

        Byline byline = ctx.request().adaptTo(Byline.class);

        assertTrue(byline.isEmpty());
    }

    @Test
    public void testIsEmpty_WithoutOccupations() {
        ctx.currentResource("/content/without-occupations");

        Byline byline = ctx.request().adaptTo(Byline.class);

        assertTrue(byline.isEmpty());
    }

    @Test
    public void testIsEmpty_WithoutImage() {
        ctx.currentResource("/content/byline");

        lenient().when(modelFactory.getModelFromWrappedRequest(eq(ctx.request()),
            any(Resource.class),
            eq(Image.class))).thenReturn(null);

        Byline byline = ctx.request().adaptTo(Byline.class);

        assertTrue(byline.isEmpty());
    }

    @Test
    public void testIsEmpty_WithoutImageSrc() {
        ctx.currentResource("/content/byline");

        when(image.getSrc()).thenReturn("");

        Byline byline = ctx.request().adaptTo(Byline.class);

        assertTrue(byline.isEmpty());
    }

    @Test
    public void testIsNotEmpty() {
        ctx.currentResource("/content/byline");
        when(image.getSrc()).thenReturn("/content/bio.png");

        Byline byline = ctx.request().adaptTo(Byline.class);

        assertFalse(byline.isEmpty());
    }

}