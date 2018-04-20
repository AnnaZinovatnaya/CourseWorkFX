package test;

import Models.CompInCharge;
import Models.Manager;
import javafx.collections.ObservableList;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ChargeTest {
    @Test
    public void checkIfTestExampleWorks()
    {
        Manager.createNewCharge(2000, 50, "Марка");
        Manager.setChargeElements(Manager.getChargeElements());
        Manager.setMandatoryComponents(Manager.getAllMandatoryComponentNames());

        ObservableList<CompInCharge> components = Manager.getChargeMandatoryComps();
        for (CompInCharge aComp: components)
        {
            if (aComp.getName().equals("Чушковый чугун 1"))
            {
                aComp.setMinPercent(20.0);
                aComp.setMaxPercent(30.0);
            }
            else if (aComp.getName().equals("Чушковый чугун 2"))
            {
                aComp.setMinPercent(30.0);
                aComp.setMaxPercent(40.0);
            }
            else if (aComp.getName().equals("Возврат СЧ"))
            {
                aComp.setMinPercent(15.0);
                aComp.setMaxPercent(30.0);
            }
            else if (aComp.getName().equals("Возврат ВЧ"))
            {
                aComp.setMinPercent(15.0);
                aComp.setMaxPercent(30.0);
            }
            else if (aComp.getName().equals("Стальной лом"))
            {
                aComp.setMinPercent(5.0);
                aComp.setMaxPercent(10.0);
            }
            else if (aComp.getName().equals("Стальная стружка"))
            {
                aComp.setMinPercent(5.0);
                aComp.setMaxPercent(10.0);
            }
        }

        assertEquals("Charge is not possible", true, Manager.isChargePossible());

        Manager.calculateCheapCharge();

        ObservableList<CompInCharge> resultComponents = Manager.getChargeResultComps();

        assertEquals("Number of components is not correct", 7, resultComponents.size());

        for (CompInCharge aComp: resultComponents)
        {
            if (aComp.getName().equals("Чушковый чугун 1"))
            {
                assertEquals("Mass of Чушкового чугуна 1 is not correct", 641.7, aComp.getCurrentMass());
            }
            else if (aComp.getName().equals("Чушковый чугун 2"))
            {
                assertEquals("Mass of Чушкового чугуна 2 is not correct", 641.7, aComp.getCurrentMass());
            }
            else if (aComp.getName().equals("Возврат СЧ"))
            {
                assertEquals("Mass of Возврат СЧ is not correct", 320.9, aComp.getCurrentMass());
            }
            else if (aComp.getName().equals("Возврат ВЧ"))
            {
                assertEquals("Mass of Возврат ВЧ is not correct", 320.9, aComp.getCurrentMass());
            }
            else if (aComp.getName().equals("Стальной лом"))
            {
                assertEquals("Mass of Стальной лом is not correct", 107.0, aComp.getCurrentMass());
            }
            else if (aComp.getName().equals("Стальная стружка"))
            {
                assertEquals("Mass of Стальная стружка is not correct", 107.0, aComp.getCurrentMass());
            }
            else if (aComp.getName().equals("Графитизатор 1"))
            {
                assertEquals("Mass of Графитизатор 1 is not correct", 1.96, aComp.getCurrentMass());
            }
        }
    }
}
