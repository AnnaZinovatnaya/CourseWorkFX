package test;

import Models.Element;
import Models.MeltBrand;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

public class MeltBrandTest {

    @Test
    public void saveAndDeleteNewMeltBrand() {
        MeltBrand newMeltBrand = new MeltBrand(0, "new meltbrand", "new standard", new ArrayList<>());
        newMeltBrand.getElements().add(new Element("C", 0.0, 1.0, 0, 0));
        newMeltBrand.getElements().add(new Element("Si", 1.0, 2.0, 0, 0));
        newMeltBrand.getElements().add(new Element("S", 2.0, 3.0, 0, 0));

        newMeltBrand.saveToDB();

        assertEquals("MeltBrand is not correctly saved", true, MeltBrand.meltBrandExists("new meltbrand"));
        MeltBrand savedMeltBrand = MeltBrand.readMeltBrandFromDB("new meltbrand");

        assertEquals("MeltBrand name is not correctly saved", "new meltbrand", savedMeltBrand.getName());
        assertEquals("MeltBrand standard is not correctly saved", "new standard", savedMeltBrand.getStandard());

        for (Element el : savedMeltBrand.getElements())
        {
            if (el.getName().equals("C"))
            {
                assertEquals("MeltBrand C min percent is not correctly saved", 0.0, el.getMinPercentDouble());
                assertEquals("MeltBrand C max percent is not correctly saved", 1.0, el.getMaxPercentDouble());
            }
            else if (el.getName().equals("Si"))
            {
                assertEquals("MeltBrand Si min percent is not correctly saved", 1.0, el.getMinPercentDouble());
                assertEquals("MeltBrand Si max percent is not correctly saved", 2.0, el.getMaxPercentDouble());
            }
            else if (el.getName().equals("S"))
            {
                assertEquals("MeltBrand S min percent is not correctly saved", 2.0, el.getMinPercentDouble());
                assertEquals("MeltBrand S max percent is not correctly saved", 3.0, el.getMaxPercentDouble());
            }
        }

        savedMeltBrand.deleteFromDB();
        assertEquals("MeltBrand is not deleted", false, MeltBrand.meltBrandExists("new meltbrand"));
    }

    @Test
    public void saveReadUpdateAndDeleteNewMeltBrand() {
        MeltBrand newMeltBrand = new MeltBrand(0, "new meltbrand", "new standard", new ArrayList<>());
        newMeltBrand.getElements().add(new Element("C", 0.0, 1.0, 0, 0));
        newMeltBrand.getElements().add(new Element("Si", 1.0, 2.0, 0, 0));
        newMeltBrand.getElements().add(new Element("S", 2.0, 3.0, 0, 0));

        newMeltBrand.saveToDB();

        assertEquals("MeltBrand is not correctly saved", true, MeltBrand.meltBrandExists("new meltbrand"));
        MeltBrand savedMeltBrand = MeltBrand.readMeltBrandFromDB("new meltbrand");

        assertEquals("MeltBrand name is not correctly saved", "new meltbrand", savedMeltBrand.getName());
        assertEquals("MeltBrand standard is not correctly saved", "new standard", savedMeltBrand.getStandard());

        for (Element el : savedMeltBrand.getElements())
        {
            if (el.getName().equals("C"))
            {
                assertEquals("MeltBrand C min percent is not correctly saved", 0.0, el.getMinPercentDouble());
                assertEquals("MeltBrand C max percent is not correctly saved", 1.0, el.getMaxPercentDouble());
            }
            else if (el.getName().equals("Si"))
            {
                assertEquals("MeltBrand Si min percent is not correctly saved", 1.0, el.getMinPercentDouble());
                assertEquals("MeltBrand Si max percent is not correctly saved", 2.0, el.getMaxPercentDouble());
            }
            else if (el.getName().equals("S"))
            {
                assertEquals("MeltBrand S min percent is not correctly saved", 2.0, el.getMinPercentDouble());
                assertEquals("MeltBrand S max percent is not correctly saved", 3.0, el.getMaxPercentDouble());
            }
        }

        savedMeltBrand.setStandard("other standard");

        //TODO update elements
        savedMeltBrand.update();

        assertEquals("Updated MeltBrand is not correctly saved", true, MeltBrand.meltBrandExists("new meltbrand"));
        MeltBrand updatedMeltBrand = MeltBrand.readMeltBrandFromDB("new meltbrand");

        assertEquals("MeltBrand standard is not correctly updated", "other standard", updatedMeltBrand.getStandard());

        updatedMeltBrand.deleteFromDB();
        assertEquals("Updated MeltBrand is not deleted", false, MeltBrand.meltBrandExists("new meltbrand"));
    }
}
