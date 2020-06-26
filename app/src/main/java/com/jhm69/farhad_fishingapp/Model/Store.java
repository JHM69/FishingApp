package com.jhm69.farhad_fishingapp.Model;

public class Store {
    private  String shopName;
    private String shopDetails;
    private String shopAddress;
    private String shopLogo;
    private String shopNumber;
    private String shopId;

    public Store(String shopName, String shopDetails, String shopAddress, String shopLogo, String shopNumber, String shopId) {
        this.shopName = shopName;
        this.shopDetails = shopDetails;
        this.shopAddress = shopAddress;
        this.shopLogo = shopLogo;
        this.shopNumber = shopNumber;
        this.shopId = shopId;
    }

    public Store() {
    }

    public String getShopNumber() {
        return shopNumber;
    }

    public String getShopId() {
        return shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public String getShopDetails() {
        return shopDetails;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public String getShopLogo() {
        return shopLogo;
    }
}
