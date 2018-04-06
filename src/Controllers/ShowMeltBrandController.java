package Controllers;

import Models.MeltBrand;

public class ShowMeltBrandController {
    private ShowMeltBrandsController showMeltBrandsController;
    private MeltBrand                meltBrand;

    public void init(ShowMeltBrandsController showMeltBrandsController, String name)
    {
        this.showMeltBrandsController = showMeltBrandsController;
        this.meltBrand = MeltBrand.readMeltBrandFromDB(name);
    }
}
